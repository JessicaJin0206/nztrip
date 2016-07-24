package io.qhzhou.nztrip.mapper;

import io.qhzhou.nztrip.model.SkuPrice;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public class SkuPriceMapperTest extends BaseTest {

    @Autowired
    private SkuPriceMapper skuPriceMapper;

    private static void assertEquals(SkuPrice price, SkuPrice other) {
        Assert.assertEquals(price.getSkuId(), other.getSkuId());
        Assert.assertEquals(price.getStartTime(), other.getStartTime());
        Assert.assertEquals(price.getAdultCostPrice(), other.getAdultCostPrice());
        Assert.assertEquals(price.getAdultSalePrice(), other.getAdultSalePrice());
        Assert.assertEquals(price.getChildCostPrice(), other.getChildCostPrice());
        Assert.assertEquals(price.getChildSalePrice(), other.getChildSalePrice());
        Assert.assertEquals(price.getBabyCostPrice(), other.getBabyCostPrice());
        Assert.assertEquals(price.getBabySalePrice(), other.getBabySalePrice());
        Assert.assertEquals(price.getElderCostPrice(), other.getElderCostPrice());
        Assert.assertEquals(price.getElderSalePrice(), other.getElderSalePrice());
        Assert.assertEquals(price.getFamilyCostPrice(), other.getFamilyCostPrice());
        Assert.assertEquals(price.getFamilySalePrice(), other.getFamilySalePrice());
    }

    @Test
    public void test() {
        SkuPrice price1 = new SkuPrice();
        price1.setSkuId(100);
        price1.setStartTime(DateTime.now().minusDays(1).toDate());
        price1.setAdultCostPrice(100);
        price1.setAdultSalePrice(200);
        price1.setChildCostPrice(50);
        price1.setChildSalePrice(100);
        price1.setBabyCostPrice(30);
        price1.setBabySalePrice(60);
        price1.setElderCostPrice(60);
        price1.setElderSalePrice(120);
        price1.setFamilyCostPrice(200);
        price1.setFamilySalePrice(400);

        SkuPrice price2 = new SkuPrice();
        price2.setSkuId(100);
        price2.setStartTime(DateTime.now().toDate());
        price2.setAdultCostPrice(1000);
        price2.setAdultSalePrice(2000);
        price2.setChildCostPrice(500);
        price2.setChildSalePrice(1000);
        price2.setBabyCostPrice(300);
        price2.setBabySalePrice(600);
        price2.setElderCostPrice(600);
        price2.setElderSalePrice(1200);
        price2.setFamilyCostPrice(2000);
        price2.setFamilySalePrice(4000);

        Assert.assertEquals(skuPriceMapper.batchCreate(Arrays.asList(price1, price2)), 2);
        Assert.assertEquals(skuPriceMapper.findBySkuIdAndStartTime(0, new Date(), new Date()).size(), 0);
        Assert.assertEquals(skuPriceMapper.findBySkuIdAndStartTime(100, new Date(), new Date()).size(), 0);
        Assert.assertEquals(skuPriceMapper.findBySkuIdAndStartTime(100, DateTime.now().minusDays(2).toDate(), DateTime.now().minusDays(1).toDate()).size(), 1);
        List<SkuPrice> result = skuPriceMapper.findBySkuIdAndStartTime(100, DateTime.now().minusDays(2).toDate(), DateTime.now().plusDays(1).toDate());
        Assert.assertEquals(result.size(), 2);
        assertEquals(result.get(0), price1);
        assertEquals(result.get(1), price2);

    }
}
