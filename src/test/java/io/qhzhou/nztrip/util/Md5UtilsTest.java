package io.qhzhou.nztrip.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by qianhao.zhou on 8/9/16.
 */
public class Md5UtilsTest {

    @Test
    public void test() {
        Assert.assertEquals(Md5Utils.md5("123456"), "e10adc3949ba59abbe56e057f20f883e");
    }
}
