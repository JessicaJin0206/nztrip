package com.fitibo.aotearoa.crawler.resource;


import com.fitibo.aotearoa.crawler.driver.CustomizedChromeDriver;

/**
 * Created by qianhao.zhou on 10/3/16.
 */
public class DefaultChromeDriverPool extends SimplePool<CustomizedChromeDriver> {

    private String chromeDriverLocation;

    public DefaultChromeDriverPool(int size, String chromeDriverLocation) {
        super(size);
        this.chromeDriverLocation = chromeDriverLocation;
    }

    @Override
    protected CustomizedChromeDriver create() {
        System.setProperty("webdriver.chrome.driver", chromeDriverLocation);
        return new CustomizedChromeDriver();
    }

}
