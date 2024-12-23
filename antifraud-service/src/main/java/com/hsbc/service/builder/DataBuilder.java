package com.hsbc.service.builder;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j
public class DataBuilder {
    private final Set<String> longTypes = new HashSet<>();

    @PostConstruct
    private void init() {
        /**
         * TODO: in a simple way to handle types,
         * shall have a type module to process type conversions of each fields
         */
        this.longTypes.add("fAmount");
        this.longTypes.add("fEventTime");
        this.longTypes.add("fLoginStatus");
    }

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
                String fieldName = StringUtils.trim(fieldNameTokens[j]);
                if (longTypes.contains(fieldName)) {
                    try {
                        Long valueConverted = Long.parseLong(StringUtils.trim(lineTokens[j]));
                        lineBuilt.put(fieldName, valueConverted);
                    } catch (Exception ex) {
                        /**
                         * illegal data found, skip
                         */
                        continue;
                    }
                } else {
                    lineBuilt.put(fieldName, StringUtils.trim(lineTokens[j]));
                }
            }
        }

        return linesBuilt;
    }

}
