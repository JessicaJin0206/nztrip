package io.qhzhou.nztrip.service;

import com.google.common.collect.Maps;
import io.qhzhou.nztrip.mapper.CityMapper;
import io.qhzhou.nztrip.mapper.VendorMapper;
import io.qhzhou.nztrip.model.City;
import io.qhzhou.nztrip.model.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by qianhao.zhou on 8/4/16.
 */
@Service
public class VendorService {

    @Autowired
    private VendorMapper vendorMapper;

    @Autowired
    private UtilityService utilityService;

    private volatile LinkedHashMap<Integer, Vendor> cityMap = null;

    @PostConstruct
    public void init() {
        cityMap = convert();
        utilityService.getScheduledExecutorService().scheduleAtFixedRate(() -> {
            cityMap = convert();
        }, 5, 5, TimeUnit.MINUTES);

    }

    private LinkedHashMap<Integer, Vendor> convert() {
        List<Vendor> vendors = vendorMapper.findAll();
        LinkedHashMap<Integer, Vendor> result = Maps.newLinkedHashMap();
        for (Vendor vendor : vendors) {
            result.put(vendor.getId(), vendor);
        }
        return result;
    }

    public Map<Integer, Vendor> findAll() {
        return cityMap;
    }
}
