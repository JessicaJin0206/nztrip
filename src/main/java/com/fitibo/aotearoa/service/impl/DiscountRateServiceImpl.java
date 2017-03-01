package com.fitibo.aotearoa.service.impl;

import com.fitibo.aotearoa.mapper.AdminMapper;
import com.fitibo.aotearoa.mapper.AgentMapper;
import com.fitibo.aotearoa.mapper.OrderMapper;
import com.fitibo.aotearoa.mapper.SkuMapper;
import com.fitibo.aotearoa.mapper.SpecialRateMapper;
import com.fitibo.aotearoa.model.Agent;
import com.fitibo.aotearoa.model.Order;
import com.fitibo.aotearoa.model.Sku;
import com.fitibo.aotearoa.model.SpecialRate;
import com.fitibo.aotearoa.service.DiscountRateService;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qianhao.zhou on 22/02/2017.
 */
@Service("discountRateService")
public class DiscountRateServiceImpl implements DiscountRateService {

  @Autowired
  private AgentMapper agentMapper;

  @Autowired
  private AdminMapper adminMapper;

  @Autowired
  private OrderMapper orderMapper;

  @Autowired
  private SpecialRateMapper specialRateMapper;

  @Autowired
  private SkuMapper skuMapper;

  @Override
  public int getDiscountByAgent(int agentId, int skuId) {
    int defaultRate = agentMapper.findById(agentId).getDiscount();
    Sku sku = skuMapper.findById(skuId);
    if (sku == null) {
      return defaultRate;
    }
    SpecialRate specialRate = specialRateMapper.findBySku(sku.getUuid(), agentId);
    if (specialRate == null) {
      return defaultRate;
    }
    return specialRate.getDiscount();
  }

  @Override
  public int getDiscountByAdmin(int adminId) {
    return adminMapper.findById(adminId).getDiscount();
  }

  @Override
  public int getDiscountByOrder(int orderId) {
    Order order = orderMapper.findById(orderId);
    Preconditions.checkNotNull(order, "invalid order id:" + orderId);
    return getDiscountByAgent(order.getAgentId(), order.getSkuId());
  }
}
