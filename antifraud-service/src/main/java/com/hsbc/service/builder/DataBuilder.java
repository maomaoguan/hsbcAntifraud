package com.hsbc.service.builder;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DataBuilder {
    public List<Map<String, Object>> buildData(List<String> lines) {
        List<Map<String, Object>> linesBuilt = new ArrayList<>();

        if (lines.size() == 0) {
            return linesBuilt;
        }

        String firstLine = lines.get(0);
        String[] fieldNameTokens = StringUtils.split(firstLine, ',');

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] lineTokens = StringUtils.split(line, ',');

            if (lineTokens == null || lineTokens.length != fieldNameTokens.length) {
                continue;
            }

            Map<String, Object> lineBuilt = new HashMap<>();
            linesBuilt.add(lineBuilt);
            for (int j = 0; j < lineTokens.length; j++) {
                lineBuilt.put(fieldNameTokens[j], lineTokens[j]);
            }
        }

        return linesBuilt;
    }

}
