package com.fitibo.aotearoa.service;

import com.google.common.collect.Maps;

import com.fitibo.aotearoa.mapper.VendorMapper;
import com.fitibo.aotearoa.model.Vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

/**
 * Created by qianhao.zhou on 8/4/16.
 */
@Service
public class VendorService {

    @Autowired
    private VendorMapper vendorMapper;

    @Autowired
    private UtilityService utilityService;

    private volatile LinkedHashMap<Integer, Vendor> vendorMap = null;

    @PostConstruct
    public void init() {
        vendorMap = convert();
        utilityService.getScheduledExecutorService().scheduleAtFixedRate(() ->
                vendorMap = convert(), 5, 5, TimeUnit.MINUTES);

    }

    private LinkedHashMap<Integer, Vendor> convert() {
        List<Vendor> vendors = vendorMapper.findAll();
        LinkedHashMap<Integer, Vendor> result = Maps.newLinkedHashMap();
        for (Vendor vendor : vendors) {
            result.put(vendor.getId(), vendor);
        }
        return result;
    }

    public Map<Integer, Vendor> findAll() {
        return vendorMap;
    }

    public int createVendor(Vendor vendor) {
        vendorMapper.create(vendor);
        vendorMap = convert();
        return vendor.getId();
    }

    public Vendor findById(int id) {
        return vendorMapper.findById(id);
    }
}
