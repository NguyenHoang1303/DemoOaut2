package com.example.oauth2core.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

public class HandlerDate {

    public static Long convertLocalDateTimeToLong(LocalDateTime time){
        ZonedDateTime zdt = ZonedDateTime.of(time, ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }

    public static Long getTimeBetween(LocalDateTime timeOne, LocalDateTime timeTwo){
        long result = convertLocalDateTimeToLong(timeOne) - convertLocalDateTimeToLong(timeTwo);
        return result > 0 ? result : -result;
    }

    public static Long convertLongToSecond(Long number){
        return TimeUnit.MILLISECONDS.toSeconds(number);
    }
}
