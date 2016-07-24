package io.qhzhou.nztrip.mapper;

import com.google.common.collect.Lists;
import io.qhzhou.nztrip.model.SkuRemark;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public class SkuRemarkMapperTest extends BaseTest {

    @Autowired
    private SkuRemarkMapper skuRemarkMapper;

    private static void assertEquals(SkuRemark skuRemark, SkuRemark other) {
        Assert.assertEquals(skuRemark.getName(), other.getName());
        Assert.assertEquals(skuRemark.getType(), other.getType());
        Assert.assertEquals(skuRemark.isRequired(), other.isRequired());
        Assert.assertEquals(skuRemark.getSkuId(), other.getSkuId());
    }

    @Test
    public void test() {
        final int skuId = 101;
        SkuRemark remark1 = new SkuRemark();
        remark1.setName("remark 1");
        remark1.setRequired(true);
        remark1.setSkuId(skuId);
        remark1.setType(1);

        SkuRemark remark2 = new SkuRemark();
        remark2.setName("remark 2");
        remark2.setRequired(false);
        remark2.setSkuId(skuId);
        remark2.setType(2);

        Assert.assertEquals(skuRemarkMapper.batchCreate(Arrays.asList(remark1, remark2)), 2);

        List<SkuRemark> remarks = skuRemarkMapper.findBySkuId(skuId);
        Assert.assertEquals(remarks.size(), 2);
        assertEquals(remark1, remarks.get(0));
        assertEquals(remark2, remarks.get(1));

    }
}
