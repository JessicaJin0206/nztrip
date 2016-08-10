package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.Sku;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public class SkuMapperTest extends BaseTest {

    @Autowired
    private SkuMapper skuMapper;

    private static void testEquals(Sku sku, Sku other) {
        assertEquals(sku.getId(), other.getId());
        assertEquals(sku.getUuid(), other.getUuid());
        assertEquals(sku.getDescription(), other.getDescription());
        assertEquals(sku.getCityId(), other.getCityId());
        assertEquals(sku.getCategoryId(), other.getCategoryId());
        assertEquals(sku.hasPickupService(), other.hasPickupService());
        assertEquals(sku.getGatheringPlace(), other.getGatheringPlace());
        assertEquals(sku.getVendorId(), other.getVendorId());
    }

    @Test
    public void testCreate() {
        Sku sku = new Sku();
        sku.setDescription("description");
        sku.setName("name");
        sku.setUuid("uuid");
        sku.setPickupService(true);
        sku.setGatheringPlace("a aaaa: bbb");
        sku.setCityId(100);
        sku.setCategoryId(200);
        sku.setVendorId(1024);
        assertTrue(skuMapper.create(sku) == 1);
        assertTrue(sku.getId() > 0);
        testEquals(sku, skuMapper.findById(sku.getId()));
    }

    @Test
    public void testUpdate() {
        Sku sku = skuMapper.findById(1);
        assertNotNull(sku);
        sku.setUuid("abcdefg");
        assertEquals(skuMapper.update(sku), 1);
    }
}
