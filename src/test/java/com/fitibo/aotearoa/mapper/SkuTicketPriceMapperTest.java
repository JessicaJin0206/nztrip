package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.SkuTicketPrice;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
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

    @Test
    public void test() {
        SkuTicketPrice price1 = new SkuTicketPrice();
        price1.setSkuId(100);
        price1.setSkuTicketId(1023);
        price1.setDate(DateTime.now().minusDays(1).toDate());
        price1.setTime("上午");
        price1.setCostPrice(100);
        price1.setSalePrice(200);
        price1.setDescription("hello");

        SkuTicketPrice price2 = new SkuTicketPrice();
        price2.setSkuId(100);
        price2.setSkuTicketId(1023);
        price2.setDate(DateTime.now().toDate());
        price2.setTime("晚上5点");
        price2.setDescription("hello 2");
        price2.setSalePrice(2000);
        price2.setCostPrice(500);

        assertEquals(skuTicketPriceMapper.batchCreate(Arrays.asList(price1, price2)), 2);
        assertEquals(skuTicketPriceMapper.findBySkuIdAndStartTime(0, new Date(), new Date()).size(), 0);
        assertEquals(skuTicketPriceMapper.findBySkuIdAndStartTime(100, new Date(), new Date()).size(), 0);
        assertEquals(skuTicketPriceMapper.findBySkuIdAndStartTime(100, DateTime.now().minusDays(2).toDate(), DateTime.now().minusDays(1).toDate()).size(), 1);
        List<SkuTicketPrice> result = skuTicketPriceMapper.findBySkuIdAndStartTime(100, DateTime.now().minusDays(2).toDate(), DateTime.now().plusDays(1).toDate());
        assertEquals(result.size(), 2);
        testEquals(result.get(0), price1);
        testEquals(result.get(1), price2);

    }
}
