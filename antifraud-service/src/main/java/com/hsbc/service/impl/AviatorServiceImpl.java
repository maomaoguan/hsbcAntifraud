package com.hsbc.service.impl;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.hsbc.service.AviatorService;
import com.hsbc.service.funcs.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AviatorServiceImpl implements AviatorService {
    private Map<String, AbstractFunction> availableFunctions = new HashMap<>();

    public void init() {
        availableFunctions.put("getDay", new GetDayFunction());
        availableFunctions.put("getHour", new GetHourFunction());
        availableFunctions.put("isWeekend", new IsWeekendFunction());
        availableFunctions.put("isDay", new IsDayFunction());
        availableFunctions.put("isNite", new IsNiteFunction());
    }

    public Map<String, AbstractFunction> getAvailableFunctions() {
        return availableFunctions;
    }

    public List<String> findVariables(String elExpression) {
        Expression expression = AviatorEvaluator.compile(elExpression, true);
        return expression.getVariableNames();
    }

    public Object evaluate(Map<String, Object> requestObject, String elExpression) throws Exception {
        Expression expression = AviatorEvaluator.compile(elExpression, true);

        /**
         * checking parameters which are in demands
         */
        List<String> variableNames = expression.getVariableNames();
        for (String variableName : variableNames) {
            if (!requestObject.containsKey(variableName)) {
                throw new Exception(String.format("variable required in expression not exist, variable %s, expression %s", variableName, elExpression));
            }
        }

        Object result = expression.execute(requestObject);

        return result;
    }
}
