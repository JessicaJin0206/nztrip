package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.mapper.VendorMapper;
import com.fitibo.aotearoa.model.Vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/4/16.
 */
@Service
public class VendorService {

    @Autowired
    private VendorMapper vendorMapper;

    public List<Vendor> findAll() {
        return vendorMapper.findAll();
    }

    public int createVendor(Vendor vendor) {
        vendorMapper.create(vendor);
        return vendor.getId();
    }

    public int update(Vendor vendor) {
        int update = vendorMapper.update(vendor);
        return update;
    }

    public Vendor findById(int id) {
        return vendorMapper.findById(id);
    }
}
