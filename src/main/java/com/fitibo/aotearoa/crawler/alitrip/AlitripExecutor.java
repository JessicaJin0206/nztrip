package com.fitibo.aotearoa.crawler.alitrip;


import com.fitibo.aotearoa.crawler.driver.CustomizedChromeDriver;
import com.fitibo.aotearoa.crawler.resource.SimplePool;
import com.fitibo.aotearoa.mapper.PriceRecordMapper;
import com.fitibo.aotearoa.model.PriceRecord;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.PlainText;

/**
 * Created by qianhao.zhou on 18/12/2016.
 */
@Component
public class AlitripExecutor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AlitripConfig config;

    @Autowired
    private Site site;

    @Autowired
    private PriceRecordMapper priceRecordMapper;

    @Autowired
    private SimplePool<CustomizedChromeDriver> pool;

    @Scheduled(cron = "* */1 * * * ?")
    public void execute() {
        try {
            for (Map<String, String> item : config.getItems()) {
                logger.info(this.getClass().getSimpleName() + " started, item:" + item);
                String url = item.get("url");
                String company = item.get("company");
                String category = item.get("category");
                String categoryXpath = item.get("category_xpath");
                String priceXpath = item.get("price_xpath");
                Spider.create(new PageProcessor() {
                    @Override
                    public void process(Page page) {
                        page.putField("url", url);
                        page.putField("category", category);
                        page.putField("price", page.getHtml().xpath(priceXpath).get());
                        page.putField("company", company);
                        page.putField("date", new Date().toString());
                    }

                    @Override
                    public Site getSite() {
                        return site;
                    }
                }).
                        addUrl(url).
                        addPipeline((ResultItems resultItems, Task task) -> {
                            try {
                                PriceRecord priceRecord = new PriceRecord();
                                priceRecord.setUrl(resultItems.get("url"));
                                priceRecord.setCategory(resultItems.get("category"));
                                priceRecord.setCompany(resultItems.get("company"));
                                priceRecord.setPrice(new BigDecimal((String) resultItems.get("price")));
                                priceRecordMapper.create(priceRecord);
                            } catch (NumberFormatException e) {
                                logger.warn("error parsing price from " + resultItems.toString(), e);
                            } catch (Exception e) {
                                logger.error("error insert price record");
                            }
                        }).
                        setDownloader(new Downloader() {
                            @Override
                            public Page download(Request request, Task task) {
                                CustomizedChromeDriver chromeDriver = pool.acquireResource();
                                if (chromeDriver == null) {
                                    logger.warn("cannot get chrome driver, maybe the pool has been shutdown");
                                    return null;
                                }
                                try {
                                    chromeDriver.get(request.getUrl());
                                    int retryTimes = 0;
                                    while (retryTimes++ < 5) {
                                        chromeDriver.findElement(By.xpath(categoryXpath)).click();
                                        Page page = new Page();
                                        page.setRawText(chromeDriver.getPageSource());
                                        page.setUrl(new PlainText(request.getUrl()));
                                        page.setRequest(request);
                                        String price = page.getHtml().xpath(priceXpath).get();
                                        try {
                                            new BigDecimal(price);
                                        } catch (NumberFormatException ex) {
                                            logger.warn("invalid number format price:" + price);
                                            Thread.sleep(10000);
                                            continue;
                                        }
                                        return page;
                                    }
                                } catch (InterruptedException e) {
                                    logger.warn("downloader interrupted by other thread");
                                    return null;
                                } finally {
                                    pool.returnResource(chromeDriver);
                                }
                                return null;
                            }

                            @Override
                            public void setThread(int threadNum) {

                            }
                        }).
                        thread(1).
                        run();
                logger.info(this.getClass().getSimpleName() + " finished");
            }
        } catch (Exception e) {
            logger.error(this.getClass().getSimpleName() + " error", e);
        }
    }
}
