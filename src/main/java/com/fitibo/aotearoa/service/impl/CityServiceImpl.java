package com.fitibo.aotearoa.service.impl;

import com.google.common.collect.Maps;

import com.fitibo.aotearoa.mapper.CityMapper;
import com.fitibo.aotearoa.model.City;
import com.fitibo.aotearoa.service.CityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qianhao.zhou on 8/1/16.
 */
@Service("cityService")
public class CityServiceImpl implements CityService {

    @Autowired
    private CityMapper cityMapper;

    @Override
    public List<City> findAll() {
        return cityMapper.findAll();
    }

    @Override
    public City findById(int cityId) {
        return cityMapper.findById(cityId);
    }

    @Override
    public Map<Integer, City> findByIds(List<Integer> ids) {
        List<City> cities = cityMapper.findByIds(ids);
        HashMap<Integer, City> result = Maps.newHashMapWithExpectedSize(cities.size());
        for (City city : cities) {
            result.put(city.getId(), city);
        }
        return Collections.unmodifiableMap(result);
    }
}
