package com.hsbc.web.controller;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.model.Message;
import com.hsbc.constants.CodeEnum;
import com.hsbc.exception.AntifraudException;
import com.hsbc.response.AntifraudResponse;
import com.hsbc.service.AntifraudService;
import com.hsbc.service.impl.FeatureComputeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * it is a message receiver that takes messaging as payload, which will be detected and giving antifraud feedback
 * it receives message and giving process in a continuous manner, will suspend if message is vacant for a period of time
 */
@RestController
@RequestMapping("/antifraud")
@Slf4j
public class MessagingController {

    @Autowired
    private AntifraudService antifraudService;

    /**
     * overall switch which controls the up or down running of consumers
     */
    private final AtomicBoolean isReceiving = new AtomicBoolean(false);

    @Value("${antifraud.endpoint}")
    private String endpoint;

    @Value("${antifraud.queueName}")
    private String queueName;

    @Value("${antifraud.accessKey}")
    private String accessKey;

    @Value("${antifraud.accessId}")
    private String accessId;

    @Value("${antifraud.consumerMax}")
    private int consumerMax;

    @Value("${antifraud.consumers}")
    private int consumers;

    /**
     * controls the maximum period that each consumer thread shall suspend/sleep when there is no message
     */
    @Value("${antifraud.periodThreshold}")
    private int periodThreshold;

    private final int defaultCoreConsumers = 2;

    private final int period = 2;

    /**
     * keeping global threadpool for maximum the ability of parallel computing
     */
    private ExecutorService basicThreadPool = null;

    @PostConstruct
    public void init() {
        /**
         * shall be initiated only once
         */
        if (basicThreadPool == null) {
            synchronized (this.getClass()) {
                basicThreadPool = new ThreadPoolExecutor(defaultCoreConsumers, consumerMax, 800L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(16), new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "MessagingController-" + r.hashCode());
                    }
                });
            }
        }

        antifraudService.init();

        /**
         * kicking off the receiving
         */
        this.kickoff();
    }

    @GetMapping("/stop")
    public void stop(HttpServletRequest request) {
        this.isReceiving.set(false);

        /**
         * refresh threadpool and the threads regarding
         */
        synchronized (this.getClass()) {
            basicThreadPool = new ThreadPoolExecutor(defaultCoreConsumers, consumerMax, 800L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(16), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "MessagingController-" + r.hashCode());
                }
            });
        }

        log.error("[messaging] consuming stopped");
    }


    @GetMapping("/start")
    public void start(HttpServletRequest request) {
        this.isReceiving.set(true);

        this.kickoff();
    }

    private void fraudRecognizing(AntifraudResponse antifraudResponse) {
        /**
         * fraud detected, giving feedback and warnings accordingly
         */
        if (CodeEnum.typeOf(antifraudResponse.getCode()) == CodeEnum.REJECTED) {
            log.warn("[antifraud] fraud detected regarding account {} - hitting rule {}", antifraudResponse.getAccountId(), antifraudResponse.getDetails());
        }
    }

    private void kickoff() {
        log.info("[messaging] starting... consumers {}", consumers);

        for (int i = 0; i < this.consumers; i++) {
            basicThreadPool.submit(new MessageReceiveRunnable());
        }
    }

    private class MessageReceiveRunnable implements Runnable {

        @Override
        public void run() {
            CloudAccount account = new CloudAccount(accessId, accessKey, endpoint);
            MNSClient mnsClient = account.getMNSClient();

            int currentPeriod = period;
            try {
                CloudQueue cloudQueue = mnsClient.getQueueRef(queueName);
                while (isReceiving.get()) {
                    Message message = null;
                    try {
                        message = cloudQueue.popMessage(period);
                    }
                    /**
                     * all messages been consumed already
                     */ catch (ClientException clientEx) {
                        /**
                         * in a tier up way to add suspending time
                         */
                        currentPeriod *= period;

                        if (currentPeriod > periodThreshold) {
                            currentPeriod = periodThreshold;
                        }

                        try {
                            Thread.sleep(currentPeriod * 1000);
                        } catch (Exception ex2) {
                            /**
                             * intentionally ignore
                             */
                        }
                    } catch (Exception ex) {
                        log.error("[messaging] system failure ", ex);
                    }

                    if (message != null) {
                        try {
                            AntifraudResponse antifraudResponse = antifraudService.process(message);
                            fraudRecognizing(antifraudResponse);
                        } catch (AntifraudException bizEx) {
                            log.error("[messaging] failed to process a message {} - details {}", bizEx.getCode(), bizEx.getMessage());
                        }

                        /**
                         * restore the period to suspend
                         */
                        currentPeriod = period;
                    }
                }
            } catch (Exception ex) {
                log.error("[messaging] failed");
            }

            mnsClient.close();

            log.error("[messaging] thread shutting down");
        }
    }
}
