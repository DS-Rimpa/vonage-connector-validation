package com.company.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final int FAILURE_STATUS_CODE = 400;
    public static final String CHANNEL = "whatsapp";
    public class ServiceProviders {
        public static final String VONAGE = "vonage";
    }


    public class Status {
        public static final String QUEUED = "queued";
        public static final String FAILED = "failed";
    }
}


