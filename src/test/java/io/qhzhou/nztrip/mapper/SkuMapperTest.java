package io.qhzhou.nztrip.mapper;

import io.qhzhou.nztrip.model.Sku;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
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
        assertEquals(sku.hasAdultTicket(), other.hasAdultTicket());
        assertEquals(sku.hasChildTicket(), other.hasChildTicket());
        assertEquals(sku.hasBabyTicket(), other.hasBabyTicket());
        assertEquals(sku.hasElderTicket(), other.hasElderTicket());
        assertEquals(sku.hasFamilyTicket(), other.hasFamilyTicket());
        assertEquals(sku.getAdultTicketRemark(), other.getAdultTicketRemark());
        assertEquals(sku.getChildTicketRemark(), other.getChildTicketRemark());
        assertEquals(sku.getBabyTicketRemark(), other.getBabyTicketRemark());
        assertEquals(sku.getElderTicketRemark(), other.getElderTicketRemark());
        assertEquals(sku.getFamilyTicketRemark(), other.getFamilyTicketRemark());
        assertEquals(sku.getVendorId(), other.getVendorId());
    }

    @Test
    public void testCreate() {
        Sku sku = new Sku();
        sku.setDescription("description");
        sku.setName("name");
        sku.setUuid("uuid");
        sku.setAdultTicket(true);
        sku.setAdultTicketRemark("adult > 18");
        sku.setChildTicket(true);
        sku.setChildTicketRemark("child > 5");
        sku.setBabyTicket(true);
        sku.setBabyTicketRemark("baby > 1");
        sku.setElderTicket(true);
        sku.setElderTicketRemark("elder < 90");
        sku.setFamilyTicket(true);
        sku.setFamilyTicketRemark("2 + 1");
        sku.setCityId(100);
        sku.setCategoryId(200);
        sku.setVendorId(1024);
        assertTrue(skuMapper.create(sku) == 1);
        assertTrue(sku.getId() > 0);
        testEquals(sku, skuMapper.findById(sku.getId()));
    }
}
