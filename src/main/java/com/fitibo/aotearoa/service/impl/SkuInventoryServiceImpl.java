package com.fitibo.aotearoa.service.impl;

import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.dto.SkuInventoryDto;
import com.fitibo.aotearoa.mapper.OrderMapper;
import com.fitibo.aotearoa.mapper.OrderTicketMapper;
import com.fitibo.aotearoa.mapper.OrderTicketUserMapper;
import com.fitibo.aotearoa.mapper.SkuInventoryMapper;
import com.fitibo.aotearoa.mapper.SkuMapper;
import com.fitibo.aotearoa.mapper.SkuTicketMapper;
import com.fitibo.aotearoa.mapper.SkuTicketPriceMapper;
import com.fitibo.aotearoa.model.SkuInventory;
import com.fitibo.aotearoa.service.SkuInventoryService;
import com.fitibo.aotearoa.util.DateUtils;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("skuInventoryService")
public class SkuInventoryServiceImpl implements SkuInventoryService {

    private static final Logger logger = LoggerFactory.getLogger(SkuInventoryService.class);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderTicketMapper orderTicketMapper;

    @Autowired
    private OrderTicketUserMapper orderTicketUserMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SkuTicketMapper skuTicketMapper;

    @Autowired
    private SkuTicketPriceMapper skuTicketPriceMapper;

    @Autowired
    private SkuInventoryMapper skuInventoryMapper;

    @Override
    public SkuInventoryDto getSkuInventory(int skuId, Date date, String time) {
        List<Integer> orderTicketIds = orderTicketMapper.findIdsByDateTimeAndOrderStatus(skuId, date, time, OrderStatus.CONFIRMED.getValue());
        int currentCount = orderTicketUserMapper.countUsersByOrderTicketIds(orderTicketIds);
        SkuInventory skuInventory = skuInventoryMapper.findBySkuIdAndDateTime(skuId, date, time);
        SkuInventoryDto result = new SkuInventoryDto();
        result.setCurrentCount(currentCount);
        result.setTotalCount(Optional.ofNullable(skuInventory).isPresent()?skuInventory.getCount():Integer.MAX_VALUE);
        result.setSkuId(skuId);
        result.setDate(DateUtils.formatDate(date));
        result.setTime(time);
        return result;
    }

    @Override
    public List<SkuInventoryDto> getSkuInventory(int skuId, Date date) {
        List<String> sessions = skuTicketPriceMapper.getSessionsBySkuIdAndDate(skuId, date, date);
        return sessions.stream().map(input -> getSkuInventory(skuId, date, input)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean addSkuInventory(int skuId, Date startDate, Date endDate, List<String> sessions, int totalCount) {
        skuInventoryMapper.delete(skuId, startDate, endDate, sessions);
        LocalDateTime start = LocalDateTime.fromDateFields(startDate);
        LocalDateTime end = LocalDateTime.fromDateFields(endDate);
        for (LocalDateTime date = start; date.compareTo(end) <= 0; date = date.plusDays(1)) {
            skuInventoryMapper.add(skuId, date.toDate(), sessions, totalCount);
        }
        return true;
    }

    @Override
    public boolean updateSkuInventory(int skuId, Date date, String time, int totalCount) {
        return skuInventoryMapper.updateBySkuIdAndDateTime(skuId, date, time, totalCount) == 1;
    }

}
