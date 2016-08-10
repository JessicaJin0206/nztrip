package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.model.City;
import com.google.common.collect.Maps;
import com.fitibo.aotearoa.mapper.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by qianhao.zhou on 8/1/16.
 */
@Service
public class CityService {

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private UtilityService utilityService;

    private volatile LinkedHashMap<Integer, City> cityMap = null;

    @PostConstruct
    public void init() {
        cityMap = convert();
        utilityService.getScheduledExecutorService().scheduleAtFixedRate(() -> {
            cityMap = convert();
        }, 5, 5, TimeUnit.MINUTES);

    }

    private LinkedHashMap<Integer, City> convert() {
        List<City> cities = cityMapper.findAll();
        LinkedHashMap<Integer, City> result = Maps.newLinkedHashMap();
        for (City city : cities) {
            result.put(city.getId(), city);
        }
        return result;
    }

    public Map<Integer, City> findAll() {
        return cityMap;
    }
}
