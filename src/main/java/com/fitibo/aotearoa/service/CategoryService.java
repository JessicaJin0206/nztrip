package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.mapper.CategoryMapper;
import com.fitibo.aotearoa.model.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/1/16.
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public Category findById(int id) {
        return categoryMapper.findById(id);
    }

    public List<Category> findAll() {
        return categoryMapper.findAll();
    }

}
