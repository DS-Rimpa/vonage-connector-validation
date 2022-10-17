package com.company.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@UtilityClass
public class CommonUtil {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final ObjectMapper objectMapper = new ObjectMapper();
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
    public String getCurrentDateTime() {
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        return format.format(date);
    }
    public boolean isStringNullOrEmpty(String input) {
        if (input == null || input.isEmpty() || input.trim().isEmpty()) return true;
        return false;
    }
}