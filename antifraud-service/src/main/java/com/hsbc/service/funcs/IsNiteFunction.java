package com.hsbc.service.funcs;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.hsbc.util.AntifraudUtil;

import java.util.Map;

public class IsNiteFunction extends AbstractFunction {
    @Override
    public String getName() {
        return "isNite";
    }

    @Override
    public AviatorObject call(Map<String, Object> env,
                              AviatorObject arg1) {
        Number time = FunctionUtils.getNumberValue(arg1, env);

        return AviatorBoolean.valueOf(!AntifraudUtil.isDay(time.longValue()));
    }
}
