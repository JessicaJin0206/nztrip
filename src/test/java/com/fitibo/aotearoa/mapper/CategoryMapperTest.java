package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.Category;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public class CategoryMapperTest extends BaseTest {

    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    public void test() {
        List<Category> categories = categoryMapper.findByIds(Lists.newArrayList(1, 2, 3));
        assertEquals(categories.size(), 3);

        assertEquals(categoryMapper.findByIds(Lists.newArrayList(1, 2, 3, 4, 5)).size(), 5);
        assertEquals(categoryMapper.findByIds(Lists.newArrayList(1, 2, 3, 5)).size(), 4);
        assertEquals(categoryMapper.findByIds(Lists.newArrayList(1, 2, 3, 5, 5, 6, 7)).size(), 4);
        assertEquals(categoryMapper.findByIds(Collections.emptyList()).size(), 0);
    }
}
