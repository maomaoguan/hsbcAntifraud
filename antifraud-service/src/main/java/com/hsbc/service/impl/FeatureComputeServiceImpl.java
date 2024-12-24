package com.hsbc.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.hsbc.constants.CodeEnum;
import com.hsbc.constants.FuncEnum;
import com.hsbc.exception.ComputeException;
import com.hsbc.service.AviatorService;
import com.hsbc.service.DataService;
import com.hsbc.service.FeatureComputeService;
import com.hsbc.vo.FeatureResultVo;
import com.hsbc.vo.FeatureVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
public class FeatureComputeServiceImpl implements FeatureComputeService {
    @Autowired
    private DataService dataService;

    @Autowired
    private AviatorService aviatorService;
    @Value("${antifraud.coreSize}")
    private int coreSize = 16;
    @Value("${antifraud.maxSize}")
    private int maxSize = 128;

    /**
     * keeping global threadpool for maximum the ability of parallel computing
     */
    private final ExecutorService basicThreadPool = new ThreadPoolExecutor(coreSize, maxSize, 800L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(256), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "FEATURESERVICE-" + r.hashCode());
        }
    }, new FeatureRejectedExecutionHandler());


    @Override
    public FeatureResultVo compute(FeatureVo featureVo, JSONObject parameters) throws ComputeException {
        String accountId = parameters.getString("fAccountId");
        List<Map<String, Object>> accountData = dataService.findDataByAccount(accountId);

        if (accountData == null) {
            throw new ComputeException(String.format("no data found for accountId %s", accountId), CodeEnum.SYSTEM_ERROR.getCode());
        }

        int result = this.execute(featureVo, accountData);

        FeatureResultVo featureResultVo = new FeatureResultVo();
        featureResultVo.setFeatureName(featureVo.getName());
        featureResultVo.setStatus(0);
        featureResultVo.setValue(result);

        return featureResultVo;
    }

    @Override
    public List<FeatureResultVo> compute(List<FeatureVo> featureVos, JSONObject parameters) throws ComputeException {
        CompletionService completionService = new ExecutorCompletionService(this.basicThreadPool);
        List<FeatureResultVo> featureResultVos = new ArrayList<>();
        for (FeatureVo featureVo : featureVos) {
            completionService.submit(new Callable<FeatureResultVo>() {
                @Override
                public FeatureResultVo call() throws Exception {
                    return compute(featureVo, parameters);
                }
            });
        }

        for (int i = 0; i < featureVos.size(); i++) {
            try {
                featureResultVos.add((FeatureResultVo) completionService.take().get());
            } catch (Exception ex) {
                /**
                 * intentionally ignore here, since get no responding thread
                 */
            }
        }

        return featureResultVos;
    }

    private int execute(FeatureVo featureVo, List<Map<String, Object>> accountData) {
        FuncEnum funcEnum = FuncEnum.typeOf(featureVo.getFuncType());
        String fieldName = featureVo.getFieldName();

        int cnt = 0;
        int sum = 0;

        for (int i = 0; i < accountData.size(); i++) {
            Map<String, Object> dataRow = accountData.get(i);

            if (StringUtils.isNotBlank(featureVo.getFilter())) {
                Boolean isFiltered = aviatorService.evaluate(dataRow, featureVo.getFilter());

                if (!isFiltered) {
                    continue;
                }
            }

            if (funcEnum == FuncEnum.COUNT) {
                cnt++;
            } else if (funcEnum == FuncEnum.SUM) {
                try {
                    Long fieldVal = (Long) dataRow.get(fieldName);
                    sum += fieldVal;
                } catch (Exception ex) {
                    /**
                     * intentionally ignore here,
                     * TODO: in real system, should give a type module to handle and format various of types
                     */
                }
            }
        }

        if (funcEnum == FuncEnum.COUNT) {
            return cnt;
        } else if (funcEnum == FuncEnum.SUM) {
            return sum;
        } else {
            return 0;
        }
    }


    private class FeatureRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.warn("[featureservice.queryFeatures.reject2] fatal, thread {} is rejected ", r.toString());
        }
    }
}
