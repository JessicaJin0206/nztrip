package com.fitibo.aotearoa.mapper;

import com.google.common.collect.Lists;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Created by qianhao.zhou on 9/3/16.
 */
public class DurationMapperTest extends BaseTest {

    @Autowired
    private DurationMapper durationMapper;

    @Test
    public void test() {
        assertEquals(durationMapper.findByIds(Lists.newArrayList(1, 2, 3)).size(), 3);
        assertEquals(durationMapper.findByIds(Lists.newArrayList(1, 1, 3)).size(), 2);
        assertEquals(durationMapper.findByIds(Lists.newArrayList(1, 2, 3, 100)).size(), 3);
        assertEquals(durationMapper.findByIds(Lists.newArrayList(1)).size(), 1);
        assertEquals(durationMapper.findByIds(Collections.emptyList()).size(), 0);
    }
}
