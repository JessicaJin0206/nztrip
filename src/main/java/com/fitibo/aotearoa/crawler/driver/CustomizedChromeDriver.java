package com.fitibo.aotearoa.crawler.driver;


import com.fitibo.aotearoa.crawler.resource.Resource;

import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qianhao.zhou on 10/4/16.
 */
public class CustomizedChromeDriver extends ChromeDriver implements Resource {

    private static Logger logger = LoggerFactory.getLogger(CustomizedChromeDriver.class);

    @Override
    public void shutdown() {
        try {
            this.quit();
        } catch (Exception e) {
            logger.warn("error shutdown driver", e);
        }
    }
}
