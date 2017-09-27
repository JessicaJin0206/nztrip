package com.fitibo.aotearoa.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by qianhao.zhou on 8/10/16.
 */
@Data
public class OrderTicketVo {

  private int id;
  private int orderId;
  //sku_ticket
  private int skuTicketId;
  private String skuTicket;
  private String countConstraint;
  private String ageConstraint;
  private String weightConstraint;
  private String ticketDescription;
  //sku_ticket_price
  private int ticketPriceId;
  private String ticketDate;
  private String ticketTime;
  private BigDecimal price;
  private BigDecimal salePrice;
  private String priceDescription;
  //order_ticket_user
  private List<OrderTicketUserVo> orderTicketUsers;

  private String gatheringPlace;
  private String gatheringTime;

}
