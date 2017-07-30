package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.dto.SkuInventoryDto;

import java.util.Date;

public interface SkuInventoryService {

    SkuInventoryDto getSkuInventory(int skuId, Date date, String time);
}
