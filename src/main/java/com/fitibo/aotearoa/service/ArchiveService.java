package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.model.Order;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by zhouqianhao on 11/03/2017.
 */
public interface ArchiveService {

    Workbook createVoucher(Order order);

    Workbook createOrderStats();

    Workbook createSkuDetail(Date date, int skuId);

    Workbook createSkuOverview(int skuId, DateTime from, DateTime to);

    //Workbook createSkuOverview(int skuId, DateTime from, DateTime to);

    Workbook createSkuDetail(int skuId);

    Workbook createSkusDetail(String keyword, int cityId, int categoryId,int vendorId);

    Pair<String,Workbook> createSkuTickets(int skuId,int agentId);

}
