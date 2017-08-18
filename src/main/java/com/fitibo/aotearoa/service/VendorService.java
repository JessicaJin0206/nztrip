package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.model.Vendor;

import java.util.List;
import java.util.Map;

/**
 * Created by qianhao.zhou on 9/3/16.
 */
public interface VendorService {
    List<Vendor> findAll();

    Map<Integer, Vendor> findByIds(List<Integer> ids);

    int createVendor(Vendor vendor);

    int update(Vendor vendor);

    Vendor findById(int id);

    Vendor findByEmail(String email);

    List<Vendor> findByKeyword(String keyword);
}
