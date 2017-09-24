package com.fitibo.aotearoa.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

@Service
public class ResourceLoaderService {

    Multimap<Integer, File> confirmationAttachments;

    @Autowired
    ResourcePatternResolver resourcePatternResolver;

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoaderService.class);

    @PostConstruct
    public void init() {
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:confirmation/vendor/*");
            confirmationAttachments = ArrayListMultimap.create();
            for (Resource resource : resources) {
                if (resource.getFile().isDirectory()) {
                    int vendorId = Integer.parseInt(resource.getFile().getName());
                    File[] attachments = resource.getFile().listFiles();
                    confirmationAttachments.putAll(vendorId, Arrays.asList(attachments));
                }

            }
        } catch (Exception e) {
            logger.error("error init confirmationAttachments");
            throw new RuntimeException(e);
        }
    }



    public List<File> getConfirmationLetterAttachments(int vendorId) {
        return Lists.newArrayList(confirmationAttachments.get(vendorId));
    }
}
