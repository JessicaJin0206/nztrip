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

    Multimap<Integer, File> voucherTemplates;

    @Autowired
    ResourcePatternResolver resourcePatternResolver;

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoaderService.class);

    @PostConstruct
    public void init() {
        confirmationAttachments = loadFiles("classpath:confirmation/vendor/*");
        voucherTemplates = loadFiles("classpath:voucher/agent/*");
        logger.info("confirmation letter attachments: " + confirmationAttachments);
        logger.info("voucher templates: " + voucherTemplates);
    }

    private Multimap<Integer, File> loadFiles(String path) {
        try {
            Resource[] resources = resourcePatternResolver.getResources(path);
            ArrayListMultimap<Integer, File> result = ArrayListMultimap.create();
            for (Resource resource : resources) {
                if (resource.getFile().isDirectory()) {
                    int vendorId = Integer.parseInt(resource.getFile().getName());
                    File[] attachments = resource.getFile().listFiles();
                    result.putAll(vendorId, Arrays.asList(attachments));
                }

            }
            return result;
        } catch (Exception e) {
            logger.error("error init confirmationAttachments");
            throw new RuntimeException(e);
        }

    }

    public List<File> getConfirmationLetterAttachments(int vendorId) {
        return Lists.newArrayList(confirmationAttachments.get(vendorId));
    }

    public File getVoucher(int agentId) {
        if (voucherTemplates.containsKey(agentId)) {
            return voucherTemplates.get(agentId).stream().findFirst().orElseThrow(() -> new RuntimeException("this should not happen"));
        } else {
            return voucherTemplates.get(0).stream().findFirst().orElseThrow(() -> new RuntimeException("this should not happen"));
        }
    }
}
