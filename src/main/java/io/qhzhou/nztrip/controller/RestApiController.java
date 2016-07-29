package io.qhzhou.nztrip.controller;

import io.qhzhou.nztrip.mapper.SkuMapper;
import io.qhzhou.nztrip.model.Sku;
import io.qhzhou.nztrip.vo.SkuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by qianhao.zhou on 7/29/16.
 */
@RestController
public class RestApiController {

    @Autowired
    private SkuMapper skuMapper;

    @RequestMapping(value = "v1/api/skus", method = RequestMethod.POST)
    public SkuVo createSku(SkuVo skuVo) {
        Sku sku = parse(skuVo);
        skuMapper.create(sku);
        skuVo.setId(sku.getId());
        return skuVo;
    }

    private static Sku parse(SkuVo skuVo) {
        Sku result = new Sku();
        result.setUuid(skuVo.getUuid());
        result.setName(skuVo.getName());
        result.setGatheringPlace(skuVo.getGatheringPlace());
        result.setPickupService(skuVo.hasPickupService());
        result.setDescription(skuVo.getDescription());
        result.setVendorId(skuVo.getVendorId());
        result.setCityId(skuVo.getCityId());
        result.setCategoryId(skuVo.getCategoryId());
        return result;
    }
}
