package com.fitibo.aotearoa.service.impl;

import com.fitibo.aotearoa.mapper.VendorMapper;
import com.fitibo.aotearoa.model.Vendor;
import com.fitibo.aotearoa.service.VendorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by qianhao.zhou on 8/4/16.
 */
@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorMapper vendorMapper;

    @Override
    public List<Vendor> findAll() {
        return vendorMapper.findAll();
    }

    @Override
    public Map<Integer, Vendor> findByIds(List<Integer> ids) {
        return null;
    }

    @Override
    public int createVendor(Vendor vendor) {
        vendorMapper.create(vendor);
        return vendor.getId();
    }

    @Override
    public int update(Vendor vendor) {
        int update = vendorMapper.update(vendor);
        return update;
    }

    @Override
    public Vendor findById(int id) {
        return vendorMapper.findById(id);
    }
}
