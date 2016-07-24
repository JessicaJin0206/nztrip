package io.qhzhou.nztrip.mapper;

import io.qhzhou.nztrip.model.Sku;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public class SkuMapperTest extends BaseTest {

    @Autowired
    private SkuMapper skuMapper;

    private static void assertEquals(Sku sku, Sku other) {
        Assert.assertEquals(sku.getId(), other.getId());
        Assert.assertEquals(sku.getUuid(), other.getUuid());
        Assert.assertEquals(sku.getDescription(), other.getDescription());
        Assert.assertEquals(sku.getCityId(), other.getCityId());
        Assert.assertEquals(sku.getCategoryId(), other.getCategoryId());
        Assert.assertEquals(sku.hasAdultTicket(), other.hasAdultTicket());
        Assert.assertEquals(sku.hasChildTicket(), other.hasChildTicket());
        Assert.assertEquals(sku.hasBabyTicket(), other.hasBabyTicket());
        Assert.assertEquals(sku.hasElderTicket(), other.hasElderTicket());
        Assert.assertEquals(sku.hasFamilyTicket(), other.hasFamilyTicket());
        Assert.assertEquals(sku.getAdultTicketRemark(), other.getAdultTicketRemark());
        Assert.assertEquals(sku.getChildTicketRemark(), other.getChildTicketRemark());
        Assert.assertEquals(sku.getBabyTicketRemark(), other.getBabyTicketRemark());
        Assert.assertEquals(sku.getElderTicketRemark(), other.getElderTicketRemark());
        Assert.assertEquals(sku.getFamilyTicketRemark(), other.getFamilyTicketRemark());
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
        assertTrue(skuMapper.create(sku) == 1);
        assertTrue(sku.getId() > 0);
        assertEquals(sku, skuMapper.findById(sku.getId()));
    }
}
