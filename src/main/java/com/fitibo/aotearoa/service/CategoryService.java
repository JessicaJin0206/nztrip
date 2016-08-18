package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.mapper.CategoryMapper;

import com.google.common.collect.Maps;

import com.fitibo.aotearoa.model.Category;

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
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private UtilityService utilityService;

    private volatile LinkedHashMap<Integer, Category> categoryMap = null;

    @PostConstruct
    public void init() {
        categoryMap = convert();
        utilityService.getScheduledExecutorService().scheduleAtFixedRate(() -> {
            categoryMap = convert();
        }, 5, 5, TimeUnit.MINUTES);

    }

    private LinkedHashMap<Integer, Category> convert() {
        List<Category> cities = categoryMapper.findAll();
        LinkedHashMap<Integer, Category> result = Maps.newLinkedHashMap();
        for (Category city : cities) {
            result.put(city.getId(), city);
        }
        return result;
    }

    public Map<Integer, Category> findAll() {
        return categoryMap;
    }
}
