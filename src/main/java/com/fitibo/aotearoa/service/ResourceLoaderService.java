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

import java.util.List;

import javax.annotation.PostConstruct;

@Service
public class ResourceLoaderService {

    Multimap<Integer, Pair<String, Resource>> confirmationAttachments;

    Multimap<Integer, Pair<String, Resource>> voucherTemplates;

    @Autowired
    ResourcePatternResolver resourcePatternResolver;

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoaderService.class);

    @PostConstruct
    public void init() {
        confirmationAttachments = loadFiles("classpath:confirmation_attachments/*");
        voucherTemplates = loadFiles("classpath:voucher/*");
        logger.info("confirmation_attachments: " + confirmationAttachments);
        logger.info("voucher templates: " + voucherTemplates);
    }

    private Multimap<Integer, Pair<String, Resource>> loadFiles(String path) {
        try {
            Resource[] resources = resourcePatternResolver.getResources(path);
            ArrayListMultimap<Integer, Pair<String, Resource>> result = ArrayListMultimap.create();
            for (Resource resource : resources) {
                logger.info("load resource from: " + path + resource.getFilename());
                String[] strings = resource.getFilename().split("#");
                int id = Integer.parseInt(strings[0]);
                String name = strings[1];
                result.put(id, Pair.of(name, resource));
            }
            return result;
        } catch (Exception e) {
            logger.error("error loading:" + path, e);
            throw new RuntimeException(e);
        }

    }

    public List<Pair<String, Resource>> getConfirmationLetterAttachments(int vendorId) {
        return Lists.newArrayList(confirmationAttachments.get(vendorId));
    }

    public Pair<String, Resource> getVoucher(int agentId) {
        if (voucherTemplates.containsKey(agentId)) {
            return voucherTemplates.get(agentId).stream().findFirst().orElseThrow(() -> new RuntimeException("this should not happen"));
        } else {
            return voucherTemplates.get(0).stream().findFirst().orElseThrow(() -> new RuntimeException("this should not happen"));
        }
    }
}
