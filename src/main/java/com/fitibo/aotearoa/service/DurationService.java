package com.fitibo.aotearoa.service;

import com.google.common.collect.Maps;

import com.fitibo.aotearoa.mapper.DurationMapper;
import com.fitibo.aotearoa.model.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

/**
 * Created by qianhao.zhou on 8/19/16.
 */
@Service
public class DurationService {

    @Autowired
    private DurationMapper durationMapper;

    @Autowired
    private UtilityService utilityService;

    private volatile LinkedHashMap<Integer, Duration> durationMap = null;

    @PostConstruct
    public void init() {
        durationMap = convert();
        utilityService.getScheduledExecutorService().scheduleAtFixedRate(() -> {
            durationMap = convert();
        }, 5, 5, TimeUnit.MINUTES);

    }

    private LinkedHashMap<Integer, Duration> convert() {
        LinkedHashMap<Integer, Duration> result = Maps.newLinkedHashMap();
        for (Duration duration : durationMapper.findAll()) {
            result.put(duration.getId(), duration);
        }
        return result;
    }

    public Map<Integer, Duration> findAll() {
        return durationMap;
    }
}
