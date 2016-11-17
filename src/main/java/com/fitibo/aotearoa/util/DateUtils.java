package com.fitibo.aotearoa.util;

import com.fitibo.aotearoa.constants.DateFormatConstants;
import com.fitibo.aotearoa.exception.InvalidDateFormatException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qianhao.zhou on 8/12/16.
 */
public final class DateUtils {
    private DateUtils() {
    }

    private static ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal<>();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern(DateFormatConstants.DATE_FORMAT);

    private static SimpleDateFormat getDateFormat() {
        if (DATE_FORMAT.get() == null) {
            DATE_FORMAT.set(new SimpleDateFormat(DateFormatConstants.DATE_FORMAT));
        }
        return DATE_FORMAT.get();
    }

    public static String formatDate(Date date) {
        return getDateFormat().format(date);
    }

    public static String formatDateWithFormat(Date date) {
        return getDateFormat().format(date) + "(" + getDateFormat().toPattern() + ")";
    }

    public static Date parseDate(String dateString) {
        try {
            return getDateFormat().parse(dateString);
        } catch (ParseException e) {
            throw new InvalidDateFormatException("invalid date string:" + dateString);
        }
    }

    public static DateTime parseDateTime(String dateString) {
        return DateTime.parse(dateString, DATE_FORMATTER);
    }

}
