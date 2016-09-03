package com.fitibo.aotearoa.mapper;

import com.google.common.collect.Lists;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Created by qianhao.zhou on 9/3/16.
 */
public class CityMapperTest extends BaseTest {

    @Autowired
    private CityMapper cityMapper;

    @Test
    public void test() {
        assertEquals(cityMapper.findByIds(Lists.newArrayList(1)).size(), 1);
        assertEquals(cityMapper.findByIds(Lists.newArrayList(1, 2, 3)).size(), 3);
        assertEquals(cityMapper.findByIds(Lists.newArrayList(-1)).size(), 0);
        assertEquals(cityMapper.findByIds(Lists.newArrayList(1, 100, 200)).size(), 1);
        assertEquals(cityMapper.findByIds(Collections.emptyList()).size(), 0);
    }
}
