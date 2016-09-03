package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.model.Duration;

import java.util.List;
import java.util.Map;

/**
 * Created by qianhao.zhou on 9/3/16.
 */
public interface DurationService {
    List<Duration> findAll();

    Duration findById(int durationId);

    Map<Integer, Duration> findByIds(List<Integer> ids);
}
