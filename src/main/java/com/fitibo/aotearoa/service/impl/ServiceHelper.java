package com.fitibo.aotearoa.service.impl;

import com.google.common.collect.Maps;

import com.fitibo.aotearoa.model.ModelObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qianhao.zhou on 9/3/16.
 */
final class ServiceHelper {

    private ServiceHelper() {

    }

    static <T extends ModelObject> Map<Integer, T> convert(List<T> list) {
        HashMap<Integer, T> result = Maps.newHashMapWithExpectedSize(list.size());
        for (T t : list) {
            result.put(t.getId(), t);
        }
        return Collections.unmodifiableMap(result);
    }

}
