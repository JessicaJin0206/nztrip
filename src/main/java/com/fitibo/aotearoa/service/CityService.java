package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.model.City;

import java.util.List;
import java.util.Map;

/**
 * Created by qianhao.zhou on 9/3/16.
 */
public interface CityService {
    List<City> findAll();

    City findById(int cityId);

    Map<Integer, City> findByIds(List<Integer> ids);
}
