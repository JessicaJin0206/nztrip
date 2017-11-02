package com.fitibo.aotearoa.util;

import com.fitibo.aotearoa.constants.DateFormatConstants;
import com.fitibo.aotearoa.exception.InvalidDateFormatException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by qianhao.zhou on 8/12/16.
 */
public final class DateUtils {
    private DateUtils() {
    }

    private static ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal<>();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern(DateFormatConstants.DATE_FORMAT);

    private static ThreadLocal<SimpleDateFormat> DATE_TIME_FORMAT = new ThreadLocal<>();

    private static SimpleDateFormat getDateFormat() {
        if (DATE_FORMAT.get() == null) {
            DATE_FORMAT.set(new SimpleDateFormat(DateFormatConstants.DATE_FORMAT));
        }
        return DATE_FORMAT.get();
    }

    private static SimpleDateFormat getDateTimeFormat() {
        if (DATE_TIME_FORMAT.get() == null) {
            DATE_TIME_FORMAT.set(new SimpleDateFormat(DateFormatConstants.DATE_TIME_FORMAT));
        }
        return DATE_TIME_FORMAT.get();
    }

    public static String formatDateTime(Date date) {
        return getDateTimeFormat().format(date);
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

    public static int differentDays(Date date1, Date date2) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant1 = date1.toInstant();
        LocalDate localDate1 = LocalDateTime.ofInstant(instant1, zone).toLocalDate();
        Instant instant2 = date2.toInstant();
        LocalDate localDate2 = LocalDateTime.ofInstant(instant2, zone).toLocalDate();
        return (int) (localDate1.toEpochDay() - localDate2.toEpochDay());
    }
}
