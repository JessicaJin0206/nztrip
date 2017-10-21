package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.model.SkuTicketPrice;

import java.math.BigDecimal;

public interface PricingService {

    BigDecimal calculate(SkuTicketPrice skuTicketPrice, int discount);
}
