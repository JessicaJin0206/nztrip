package com.fitibo.aotearoa.service.impl;

import com.google.common.collect.Maps;

import com.fitibo.aotearoa.mapper.DurationMapper;
import com.fitibo.aotearoa.model.Duration;
import com.fitibo.aotearoa.service.DurationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qianhao.zhou on 8/19/16.
 */
@Service("durationService")
public class DurationServiceImpl implements DurationService {

    @Autowired
    private DurationMapper durationMapper;

    @Override
    public List<Duration> findAll() {
        return durationMapper.findAll();
    }

    @Override
    public Duration findById(int durationId) {
        return durationMapper.findById(durationId);
    }

    @Override
    public Map<Integer, Duration> findByIds(List<Integer> ids) {
        List<Duration> durations = durationMapper.findByIds(ids);
        HashMap<Integer, Duration> result = Maps.newHashMapWithExpectedSize(durations.size());
        for (Duration duration : durations) {
            result.put(duration.getId(), duration);
        }
        return Collections.unmodifiableMap(result);
    }
}
