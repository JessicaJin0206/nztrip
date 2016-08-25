package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.mapper.CityMapper;
import com.fitibo.aotearoa.model.City;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/1/16.
 */
@Service
public class CityService {

    @Autowired
    private CityMapper cityMapper;

    public List<City> findAll() {
        return cityMapper.findAll();
    }

    public City findById(int cityId) {
        return cityMapper.findById(cityId);
    }
}
