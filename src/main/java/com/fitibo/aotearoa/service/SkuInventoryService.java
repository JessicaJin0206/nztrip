package com.fitibo.aotearoa.service;

import com.google.common.collect.Multiset;

import com.fitibo.aotearoa.dto.SkuInventoryDto;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;
import java.util.List;

public interface SkuInventoryService {

    SkuInventoryDto getSkuInventory(int skuId, Date date, String time);

    List<SkuInventoryDto> getSkuInventory(int skuId, Date date);

    boolean addSkuInventory(int skuId, Date startDate, Date endDate, List<String> sessions, int totalCount);

    boolean updateSkuInventory(int skuId, Date date, String time, int totalCount);

    boolean checkAvailability(int skuId, Date date, String time, int count);
}
