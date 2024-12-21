package com.hsbc.service.funcs;


import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorLong;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Date;
import java.util.Map;

public class GetHourFunction extends AbstractFunction {
    @Override
    public String getName() {
        return "getHour";
    }

    @Override
    public AviatorObject call(Map<String, Object> env,
                              AviatorObject arg1) {
        Number time = FunctionUtils.getNumberValue(arg1, env);

        Date dateTime = new Date(time.longValue());

        return AviatorLong.valueOf(dateTime.getHours());
    }
}