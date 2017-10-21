package com.fitibo.aotearoa.service.impl;

import com.fitibo.aotearoa.model.SkuTicketPrice;
import com.fitibo.aotearoa.service.PricingService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("pricingService")
public class PricingServiceImpl implements PricingService {
    @Override
    public BigDecimal calculate(SkuTicketPrice ticketPrice, int discount) {
        BigDecimal cost = ticketPrice.getCostPrice();
        BigDecimal sale = ticketPrice.getSalePrice();
        return cost.add(sale.subtract(cost).multiply(BigDecimal.valueOf(discount))
                .divide(BigDecimal.valueOf(100), BigDecimal.ROUND_CEILING));
    }
}
