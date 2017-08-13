package com.fitibo.aotearoa.service;

/**
 * Created by qianhao.zhou on 22/02/2017.
 */
public interface DiscountRateService {

    int getDiscountByAgent(int agentId, int skuId);

    int getDiscountByAdmin(int adminId);

    int getDiscountByOrder(int orderId);

    int getDiscountByVendor(int id, int skuId);
}
