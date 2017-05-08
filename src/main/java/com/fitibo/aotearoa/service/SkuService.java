package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.mapper.SkuMapper;
import com.fitibo.aotearoa.mapper.SkuTicketMapper;
import com.fitibo.aotearoa.mapper.SkuTicketPriceMapper;
import com.fitibo.aotearoa.model.Sku;
import com.fitibo.aotearoa.model.SkuTicket;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qianhao.zhou on 7/28/16.
 */
@Service("skuService")
public class SkuService {

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SkuTicketMapper skuTicketMapper;

    @Autowired
    private SkuTicketPriceMapper skuTicketPriceMapper;

    public Sku findById(int skuId) {
        Sku sku = skuMapper.findById(skuId);
        if (sku == null) {
            return null;
        }
        sku.setTickets(findOnlineSkuTicketsBySkuId(skuId));
        return sku;
    }

    public List<SkuTicket> findOnlineSkuTicketsBySkuId(int skuId) {
        List<SkuTicket> tickets = skuTicketMapper.findOnlineBySkuId(skuId);
        for (SkuTicket ticket : tickets) {
            int skuTicketId = ticket.getId();
            ticket.setTicketPrices(skuTicketPriceMapper.findAvailableBySkuTicketId(skuId, skuTicketId, new RowBounds()));
        }
        return tickets;
    }


    public Sku findByUuid(String uuid) {
        Sku sku = skuMapper.findByUuid(uuid);
        sku.setTickets(findOnlineSkuTicketsBySkuId(sku.getId()));
        return sku;
    }
}
