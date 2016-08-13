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

    public static void main(String[] args) {
        System.out.println("<script>" +
                "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
                "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
                "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
                "o.secondary_contact_wechat, o.reference_number, s.name " +
                "from `order` o left join `sku` s on o.sku_id = s.id " +
                "where 1 = 1" +
                "<if test=\"uuid != null && uuid != ''\">and o.uuid = #{uuid} </if>" +
                "<if test=\"keyword != null && keyword != ''\">and s.name = #{keyword} </if>" +
                "<if test=\"referenceNumber != null && referenceNumber != ''\">and o.reference_number = #{referenceNumber} </if>" +
                "</script>");
    }
}
