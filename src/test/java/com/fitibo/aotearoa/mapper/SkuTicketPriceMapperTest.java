package com.fitibo.aotearoa.mapper;

import com.google.common.collect.Lists;

import com.fitibo.aotearoa.model.SkuTicketPrice;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public class SkuTicketPriceMapperTest extends BaseTest {

    @Autowired
    private SkuTicketPriceMapper skuTicketPriceMapper;

    private static void testEquals(SkuTicketPrice price, SkuTicketPrice other) {
        assertEquals(price.getSkuId(), other.getSkuId());
        assertEquals(price.getSkuTicketId(), other.getSkuTicketId());
        assertEquals(price.getDate(), other.getDate());
        assertEquals(price.getTime(), other.getTime());
        assertEquals(price.getSalePrice(), other.getSalePrice());
        assertEquals(price.getCostPrice(), other.getCostPrice());
        assertEquals(price.getDescription(), other.getDescription());
    }

    static List<SkuTicketPrice> INITIAL_LIST;

    static {
        INITIAL_LIST = Lists.newArrayList();
        SkuTicketPrice price1 = new SkuTicketPrice();
        price1.setSkuId(100);
        price1.setSkuTicketId(1023);
        price1.setDate(DateTime.now().dayOfYear().roundFloorCopy().toDate());
        price1.setTime("上午");
        price1.setCostPrice(BigDecimal.valueOf(100).setScale(2));
        price1.setSalePrice(BigDecimal.valueOf(200).setScale(2));
        price1.setDescription("hello");


        SkuTicketPrice price2 = new SkuTicketPrice();
        price2.setSkuId(100);
        price2.setSkuTicketId(1023);
        price2.setDate(DateTime.now().dayOfYear().roundFloorCopy().toDate());
        price2.setTime("晚上5点");
        price2.setCostPrice(BigDecimal.valueOf(200).setScale(2));
        price2.setSalePrice(BigDecimal.valueOf(400).setScale(2));
        price2.setDescription("hello");

        SkuTicketPrice price4 = new SkuTicketPrice();
        price4.setSkuId(100);
        price4.setSkuTicketId(1023);
        price4.setDate(DateTime.now().plusDays(1).dayOfYear().roundFloorCopy().toDate());
        price4.setTime("晚上5点");
        price4.setDescription("hello 2");
        price4.setSalePrice(BigDecimal.valueOf(100).setScale(2));
        price4.setCostPrice(BigDecimal.valueOf(200).setScale(2));

        SkuTicketPrice price3 = new SkuTicketPrice();
        price3.setSkuId(100);
        price3.setSkuTicketId(1023);
        price3.setDate(DateTime.now().plusDays(1).dayOfYear().roundFloorCopy().toDate());
        price3.setTime("上午");
        price3.setDescription("hello 2");
        price3.setSalePrice(BigDecimal.valueOf(200).setScale(2));
        price3.setCostPrice(BigDecimal.valueOf(400).setScale(2));

        INITIAL_LIST.add(price1);
        INITIAL_LIST.add(price2);
        INITIAL_LIST.add(price3);
        INITIAL_LIST.add(price4);
    }

    @Before
    public void before() {
        assertEquals(skuTicketPriceMapper.batchCreate(INITIAL_LIST), 4);
    }

    @After
    public void after() {
        skuTicketPriceMapper.batchDelete(100, 1023, DateTime.now().plusDays(1).dayOfYear().roundFloorCopy().toDate(), Lists.newArrayList("上午", "晚上5点"));
        skuTicketPriceMapper.batchDelete(100, 1023, DateTime.now().dayOfYear().roundFloorCopy().toDate(), Lists.newArrayList("上午", "晚上5点"));
    }

    @Test
    public void test() {
        assertEquals(skuTicketPriceMapper.findBySkuIdAndStartTime(0, new Date(), new Date()).size(), 0);
        assertEquals(skuTicketPriceMapper.findBySkuIdAndStartTime(100, new Date(), new Date()).size(), 0);
        assertEquals(skuTicketPriceMapper.findBySkuIdAndStartTime(100, DateTime.now().minusDays(1).toDate(), DateTime.now().toDate()).size(), 2);
        assertEquals(skuTicketPriceMapper.findBySkuIdAndStartTime(100, DateTime.now().toDate(), DateTime.now().plusDays(1).toDate()).size(), 2);
        List<SkuTicketPrice> result = skuTicketPriceMapper.findBySkuIdAndStartTime(100, DateTime.now().minusDays(2).toDate(), DateTime.now().plusDays(1).toDate());
        assertEquals(result.size(), 4);
        testEquals(result.get(0), INITIAL_LIST.get(0));
        testEquals(result.get(1), INITIAL_LIST.get(1));
        testEquals(result.get(2), INITIAL_LIST.get(2));
        testEquals(result.get(3), INITIAL_LIST.get(3));

    }

    @Test
    public void testBatchCreateAndDelete() {
        List<SkuTicketPrice> list = Lists.newArrayList();
        assertEquals(0, skuTicketPriceMapper.batchCreate(list));
        assertEquals(skuTicketPriceMapper.batchCreate(INITIAL_LIST), 4);
        assertEquals(4, skuTicketPriceMapper.batchDelete(100, 1023, DateTime.now().plusDays(1).dayOfYear().roundFloorCopy().toDate(), Collections.emptyList()));
    }

}
