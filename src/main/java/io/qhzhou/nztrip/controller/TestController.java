package io.qhzhou.nztrip.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by qianhao.zhou on 7/17/16.
 */
@RestController
public class TestController {

    @Value("${my.config}")
    private String address;

    @RequestMapping("test")
    String home() {
        return "Hello World!" + " " + address;
    }
}
