package com.hsbc.service.funcs;


import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorLong;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class GetDayFunction extends AbstractFunction {
    @Override
    public String getName() {
        return "getDay";
    }

    @Override
    public AviatorObject call(Map<String, Object> env,
                              AviatorObject arg1) {
        Number time = FunctionUtils.getNumberValue(arg1, env);

        Date dateTime = new Date(time.longValue());
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);

        return AviatorLong.valueOf(cal.get(Calendar.DAY_OF_MONTH));
    }
}

