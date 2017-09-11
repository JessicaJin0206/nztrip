package com.fitibo.aotearoa.controller;

import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.dto.Role;
import com.fitibo.aotearoa.dto.Token;
import com.fitibo.aotearoa.exception.AuthenticationFailureException;
import com.fitibo.aotearoa.model.Order;

/**
 * Created by qianhao.zhou on 10/5/16.
 */
final class AuthenticationHelper {

    private AuthenticationHelper() {
    }

    static void checkAgentAuthentication(Order order, Token token) {
        if (token.getRole() == Role.Agent) {
            switch (OrderStatus.valueOf(order.getStatus())) {
                case NEW:
                case FULL:
                case RESUBMIT:
                    break;
                default:
                    throw new AuthenticationFailureException("agent can not edit this page");
            }
            if (order.getAgentId() != token.getId()) {
                throw new AuthenticationFailureException("invalid agent id:" + token.getId());
            }
        }
    }
}
