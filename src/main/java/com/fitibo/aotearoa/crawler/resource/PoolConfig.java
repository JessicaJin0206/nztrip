package com.fitibo.aotearoa.crawler.resource;


import com.fitibo.aotearoa.crawler.driver.CustomizedChromeDriver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import us.codecraft.webmagic.Site;

/**
 * Created by qianhao.zhou on 10/4/16.
 */
@Configuration
public class PoolConfig {

    @Value("${chrome.driver.location}")
    private String chromeDriverLocation;

    @Value("${chrome.driver.poolSize}")
    private int poolSize;

    @Bean
    public Site site() {
        return Site.me().setRetryTimes(3).setSleepTime(1000);
    }

    @Bean
    public SimplePool<CustomizedChromeDriver> chromeDriverPool() {
        DefaultChromeDriverPool result = new DefaultChromeDriverPool(poolSize, chromeDriverLocation);
        result.start();
        return result;
    }
}
