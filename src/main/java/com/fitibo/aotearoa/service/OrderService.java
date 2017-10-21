package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.dto.Token;
import com.fitibo.aotearoa.dto.Transition;
import com.fitibo.aotearoa.model.OrderTicket;
import com.fitibo.aotearoa.model.OrderTicketUser;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/25/16.
 */

public interface OrderService {

    List<Transition> getAvailableTransitions(int status);

    boolean replaceAllTickets(Token token, int orderId, int skuTicketId, int skuTicketPriceId, String gatheringPlace);

    Pair<Boolean, String> validateTicketUser(String ageConstraint, String weightConstraint, List<OrderTicketUser> users);
}
