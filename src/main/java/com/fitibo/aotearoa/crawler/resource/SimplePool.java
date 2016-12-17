package com.fitibo.aotearoa.crawler.resource;

import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by qianhao.zhou on 10/3/16.
 */
public abstract class SimplePool<T extends Resource> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ArrayBlockingQueue<T> resourcePool;
    private final int poolSize;
    private volatile boolean started = false;

    public SimplePool(final int size) {
        this.poolSize = size;
        this.resourcePool = new ArrayBlockingQueue<>(size);
    }

    protected abstract T create();

    public T acquireResource() {
        try {
            if (!started) {
                return null;
            }
            return resourcePool.take();
        } catch (InterruptedException e) {
            logger.warn("acquire interrupted, return null");
            return null;
        }
    }

    public boolean returnResource(T resource) {
        Preconditions.checkNotNull(resource, "resource cannot be null");
        try {
            if (!started) {
                resource.shutdown();
                return false;
            }
            resourcePool.put(resource);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void start() {
        for (int i = 0; i < poolSize; i++) {
            try {
                logger.info("creating resource#{} ...", i);
                resourcePool.put(create());
            } catch (InterruptedException e) {
                logger.error("error initialize Pool", e);
                throw new RuntimeException(e);
            }
        }
        started = true;
    }

    public void shutdown() {
        started = false;
        resourcePool.forEach(Resource::shutdown);
    }
}
