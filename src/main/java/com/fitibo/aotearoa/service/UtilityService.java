package com.fitibo.aotearoa.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by qianhao.zhou on 8/1/16.
 */
@Service
public class UtilityService {

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor((r) -> {
        Thread result = new Thread(r);
        result.setDaemon(true);
        return result;
    });

    public ScheduledExecutorService getScheduledExecutorService() {
        return this.scheduledExecutorService;
    }

}
