package com.fitibo.aotearoa.crawler.alitrip;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * Created by qianhao.zhou on 18/12/2016.
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "alitrip")
public class AlitripConfig implements InitializingBean {

    private List<Map<String, String>> items;

    public List<Map<String, String>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, String>> items) {
        this.items = items;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (Map<String, String> item : items) {
            System.out.println(item);
        }
    }
}
