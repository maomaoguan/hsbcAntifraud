package com.hsbc.service;

import com.googlecode.aviator.runtime.function.AbstractFunction;

import java.util.List;
import java.util.Map;

public interface AviatorService {
    /**
     * registers customized functions
     */
    public void init();

    public List<String> findVariables(String elExpression);

    public Map<String, AbstractFunction> getAvailableFunctions();

    public Object evaluate(Map<String, Object> requestObject, String elExpression) throws Exception;
}
