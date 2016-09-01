package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.OrderTicket;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by xiaozou on 8/12/16.
 */
public class OrderTicketMapperTest extends BaseTest{

	@Autowired
	private OrderTicketMapper orderTicketMapper;

	@Test
	public void testCreate() {
		OrderTicket orderTicket = new OrderTicket();
		orderTicket.setSkuId(100);
		orderTicket.setOrderId(1001);
		orderTicket.setSkuTicketId(11);
		orderTicket.setSkuTicket("child");
		orderTicket.setCountConstraint("1");
		orderTicket.setAgeConstraint("20-50");
		orderTicket.setWeightConstraint("50-100");
		orderTicket.setTicketDescription("ticketDesc");
		orderTicket.setTicketPriceId(15);
		orderTicket.setTicketDate(new Date());
		orderTicket.setTicketTime("5点");
		orderTicket.setSalePrice(BigDecimal.valueOf(20).setScale(2));
		orderTicket.setCostPrice(BigDecimal.valueOf(100).setScale(2));
		orderTicket.setPriceDescription("priceDesc");
		assertTrue(orderTicketMapper.create(orderTicket) == 1);
		assertTrue(orderTicket.getId() > 0);
		testEquals(orderTicket, orderTicketMapper.findByOrderId(1001).get(0));
	}

	public static void testEquals(OrderTicket ticket, OrderTicket to) {
		assertEquals(ticket.getId(), to.getId());
		assertEquals(ticket.getSkuId(), to.getSkuId());
		assertEquals(ticket.getOrderId(), to.getOrderId());
		assertEquals(ticket.getSkuTicketId(), to.getSkuTicketId());
		assertEquals(ticket.getSkuTicket(), to.getSkuTicket());
		assertEquals(ticket.getCountConstraint(), to.getCountConstraint());
		assertEquals(ticket.getAgeConstraint(), to.getAgeConstraint());
		assertEquals(ticket.getWeightConstraint(), to.getWeightConstraint());
		assertEquals(ticket.getTicketDescription(), to.getTicketDescription());
		assertEquals(ticket.getTicketPriceId(), to.getTicketPriceId());
		assertEquals(ticket.getTicketDate(), to.getTicketDate());
		assertEquals(ticket.getTicketTime(), to.getTicketTime());
		assertEquals(ticket.getSalePrice(), to.getSalePrice());
		assertEquals(ticket.getCostPrice(), to.getCostPrice());
	}
}
