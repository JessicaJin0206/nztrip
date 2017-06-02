package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.model.Order;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.Date;
import java.util.List;

/**
 * Created by zhouqianhao on 11/03/2017.
 */
public interface ArchiveService {

    Workbook createVoucher(Order order);

    Workbook createOrderStats();

    Workbook createSkuDetail(Date date, int skuId);
}
