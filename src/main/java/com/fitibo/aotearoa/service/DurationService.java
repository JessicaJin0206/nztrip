package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.mapper.DurationMapper;
import com.fitibo.aotearoa.model.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/19/16.
 */
@Service
public class DurationService {

    @Autowired
    private DurationMapper durationMapper;

    public List<Duration> findAll() {
        return durationMapper.findAll();
    }

    public Duration findById(int durationId) {
        return durationMapper.findById(durationId);
    }
}
