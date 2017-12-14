package com.fitibo.aotearoa.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by qianhao.zhou on 3/23/16.
 */
public final class GuidGenerator {

    private GuidGenerator() {
    }

    private static ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<>();
    private static final String DATE_FORMAT = "yyyyMMddHHmm";

    private static SimpleDateFormat getSimpleDateFormat() {
        if (dateFormat.get() == null) {
            dateFormat.set(new SimpleDateFormat(DATE_FORMAT));
        }
        return dateFormat.get();
    }

    public static String generate(int length) {
        String date = getSimpleDateFormat().format(new Date());
        return date + randomNumber(length - DATE_FORMAT.length());
    }

    private static String randomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(10));
        }
        return sb.toString();
    }
}
