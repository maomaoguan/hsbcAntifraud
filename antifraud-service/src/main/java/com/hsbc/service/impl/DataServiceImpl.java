package com.hsbc.service.impl;

import com.hsbc.service.DataService;
import com.hsbc.service.builder.DataBuilder;
import com.hsbc.util.FileUtil;
import com.hsbc.vo.FeatureVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class DataServiceImpl implements DataService {
    private ConcurrentMap<String, List<Map<String, Object>>> accountData = new ConcurrentHashMap<>();

    private final String dataConfigPrefix = "mockdata/";

    private final String accountPrefix = "account";

    @Value("${antifraud.accounts}")
    private int accountCnts;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private DataBuilder dataBuilder;

    @Override
    public void init() {
        for (int i = 1; i <= accountCnts; i++) {
            String accountName = accountPrefix + i;

            try {
                List<String> lines = fileUtil.loadLines(dataConfigPrefix + accountName);
                accountData.put(accountName, dataBuilder.buildData(lines));
            } catch (Exception ex) {
                log.warn("[dataService] unable to init account data {}", accountName);
            }
        }

        log.info("[dataService] data initiated in total {}", accountData.size());
    }

    @Override
    public List<Map<String, Object>> findDataByAccount(String userId) {
        return this.accountData.get(userId);
    }
}
