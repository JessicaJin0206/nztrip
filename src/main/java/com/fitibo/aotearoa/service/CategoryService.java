package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.model.Category;

import java.util.List;
import java.util.Map;

/**
 * Created by qianhao.zhou on 9/3/16.
 */
public interface CategoryService {
    Category findById(int id);

    Map<Integer, Category> findByIds(List<Integer> ids);

    List<Category> findAll();
}
