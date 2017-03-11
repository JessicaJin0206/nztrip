package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.model.Order;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * Created by zhouqianhao on 11/03/2017.
 */
public interface ArchiveService {

    Workbook createVoucher(Order order);

    Workbook createOrderStats();
}
