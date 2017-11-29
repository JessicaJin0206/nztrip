package com.fitibo.aotearoa.service;

import com.google.common.collect.Maps;

import com.fitibo.aotearoa.mapper.SkuMapper;
import com.fitibo.aotearoa.mapper.SkuTicketMapper;
import com.fitibo.aotearoa.mapper.SkuTicketPriceMapper;
import com.fitibo.aotearoa.model.Sku;
import com.fitibo.aotearoa.model.SkuTicket;
import com.fitibo.aotearoa.model.SkuTicketPrice;
import com.fitibo.aotearoa.util.DateUtils;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Sku findByUuid(String uuid) {
        Sku sku = skuMapper.findByUuid(uuid);
        sku.setTickets(findOnlineSkuTicketsBySkuId(sku.getId()));
        return sku;
    }

    public Sku findByUuidWithoutTicketPrice(String uuid) {
        Sku sku = skuMapper.findByUuid(uuid);
        sku.setTickets(skuTicketMapper.findOnlineBySkuId(sku.getId()));
        return sku;
    }

    public Sku findByIdWithoutTicketPrice(int skuId) {
        Sku sku = skuMapper.findById(skuId);
        if (sku == null) {
            return null;
        }
        sku.setTickets(skuTicketMapper.findOnlineBySkuId(skuId));
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

    public Map<String, Collection<String>> getAvailableDateMap(int skuId, List<Integer> skuTicketIds) {
        Map<String, Collection<String>> availableDateMap = Maps.newHashMap();
        for (Integer skuTicketId : skuTicketIds) {
            List<SkuTicketPrice> skuTicketPrices = skuTicketPriceMapper.findAvailableBySkuTicketId(skuId, skuTicketId, new RowBounds());
            Set<String> availableDates = skuTicketPrices.stream().map(SkuTicketPrice::getDate).map(DateUtils::formatDate).collect(Collectors.toSet());
            availableDateMap.put(skuTicketId + "", availableDates);
        }
        return Collections.unmodifiableMap(availableDateMap);
    }

    public Map<String, Collection<String>> getAllDateMap(int skuId, List<Integer> skuTicketIds) {
        Map<String, Collection<String>> availableDateMap = Maps.newHashMap();
        for (Integer skuTicketId : skuTicketIds) {
            List<SkuTicketPrice> skuTicketPrices = skuTicketPriceMapper.findAllBySkuTicketId(skuId, skuTicketId, new RowBounds());
            Set<String> availableDates = skuTicketPrices.stream().map(SkuTicketPrice::getDate).map(DateUtils::formatDate).collect(Collectors.toSet());
            availableDateMap.put(skuTicketId + "", availableDates);
        }
        return Collections.unmodifiableMap(availableDateMap);
    }
}
