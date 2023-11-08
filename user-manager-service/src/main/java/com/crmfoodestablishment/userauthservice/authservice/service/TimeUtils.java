package com.crmfoodestablishment.userauthservice.authservice.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TimeUtils {

    public static Date convertLocalDateTimeToDate(LocalDateTime dateTime) {
        return Date.from(
                dateTime.atZone(ZoneId.systemDefault()).toInstant()
        );
    }

    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
