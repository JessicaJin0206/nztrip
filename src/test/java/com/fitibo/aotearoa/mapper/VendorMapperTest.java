package com.fitibo.aotearoa.mapper;

import com.google.common.collect.Lists;

import com.fitibo.aotearoa.model.Vendor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by qianhao.zhou on 7/25/16.
 */
public class VendorMapperTest extends BaseTest {

    @Autowired
    private VendorMapper vendorMapper;

    @Test
    public void test() {
        Vendor vendor = new Vendor();
        vendor.setName("test vendor");
        vendor.setEmail("test@abc.com");
        assertEquals(vendorMapper.create(vendor), 1);
        Vendor result = vendorMapper.findById(vendor.getId());
        assertNotNull(result);
        assertEquals(result.getName(), vendor.getName());
        assertEquals(result.getEmail(), vendor.getEmail());

        assertEquals(vendorMapper.findByIds(Lists.newArrayList(1, 2, 3)).size(), 3);
        assertEquals(vendorMapper.findByIds(Lists.newArrayList(1, 3)).size(), 2);
        assertEquals(vendorMapper.findByIds(Lists.newArrayList(1, 2, 3, 100)).size(), 3);
        assertEquals(vendorMapper.findByIds(Collections.emptyList()).size(), 0);
    }

}
