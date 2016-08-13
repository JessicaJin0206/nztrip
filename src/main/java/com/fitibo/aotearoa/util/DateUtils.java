package com.fitibo.aotearoa.util;

import com.fitibo.aotearoa.constants.DateFormatConstants;
import com.fitibo.aotearoa.exception.InvalidDateFormatException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qianhao.zhou on 8/12/16.
 */
public final class DateUtils {
    private DateUtils(){}

    private static ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal<>();

    private static SimpleDateFormat getDateFormat() {
        if (DATE_FORMAT.get() == null) {
            DATE_FORMAT.set(new SimpleDateFormat(DateFormatConstants.DATE_FORMAT));
        }
        return DATE_FORMAT.get();
    }

    public static final String formatDate(Date date) {
        return getDateFormat().format(date);
    }

    public static final Date parseDate(String dateString) {
        try {
            return getDateFormat().parse(dateString);
        } catch (ParseException e) {
            throw new InvalidDateFormatException();
        }
    }

}
