package io.qhzhou.nztrip.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by qianhao.zhou on 7/17/16.
 */
@RestController
public class TestController {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }
}
