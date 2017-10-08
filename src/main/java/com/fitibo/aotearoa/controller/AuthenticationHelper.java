package com.fitibo.aotearoa.controller;

import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.dto.Role;
import com.fitibo.aotearoa.dto.Token;
import com.fitibo.aotearoa.exception.AuthenticationFailureException;
import com.fitibo.aotearoa.model.Order;
import com.fitibo.aotearoa.model.Sku;

/**
 * Created by qianhao.zhou on 10/5/16.
 */
final class AuthenticationHelper {

    private AuthenticationHelper() {
    }

    static void checkOrderAuthentication(Order order, Sku sku, Token token) {
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
        } else if (token.getRole() == Role.Vendor) {
            if (sku.getVendorId() == token.getId() && order.isFromVendor()) {
                //do nothing
            } else {
                throw new AuthenticationFailureException("order: " + order.getId() + " does not belong to vendor: " + token.getId());
            }
        } else if (token.getRole() == Role.Admin) {
            //do nothing
        } else {
            throw new AuthenticationFailureException("invalid role:" + token.getRole());
        }
    }
}
