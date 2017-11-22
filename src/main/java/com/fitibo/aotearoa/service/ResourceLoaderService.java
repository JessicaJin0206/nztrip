package com.fitibo.aotearoa.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.PostConstruct;

@Service
public class ResourceLoaderService {

    Multimap<Integer, Pair<String, Resource>> confirmationAttachments;

    Multimap<Integer, Pair<String, Resource>> agentVoucherTemplates;

    Multimap<Integer, Pair<String, Resource>> vendorVoucherTemplates;

    Multimap<Integer, Pair<String, Resource>> agentPDFVoucherTemplates;

    Multimap<Integer, Pair<String, Resource>> vendorPDFVoucherTemplates;

    @Value(value = "classpath:voucher_template.xlsx")
    private Resource defaultVoucherTemplate;

    @Value(value = "classpath:voucher_template.pdf")
    private Resource defaultPDFVoucherTemplate;

    @Autowired
    ResourcePatternResolver resourcePatternResolver;

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoaderService.class);

    @PostConstruct
    public void init() {
        confirmationAttachments = loadFiles("classpath:confirmation_attachments/*");
        agentVoucherTemplates = loadFiles("classpath:agent_voucher/*");
        vendorVoucherTemplates = loadFiles("classpath:vendor_voucher/*");
        agentPDFVoucherTemplates = loadFiles("classpath:agent_pdf_voucher/*");
        vendorPDFVoucherTemplates = loadFiles("classpath:vendor_pdf_voucher/*");
        logger.info("confirmation_attachments: " + confirmationAttachments);
        logger.info("agent_voucher templates: " + agentVoucherTemplates);
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

    public Pair<String, Resource> getVoucherByVendorIdOrDefault(int vendorId) {
        return vendorVoucherTemplates.get(vendorId).stream().findFirst().orElse(Pair.of("voucher_template.xlsx", defaultVoucherTemplate));
    }

    public Pair<String, Resource> getVoucherByAgentIdOrDefault(int agentId) {
        return agentVoucherTemplates.get(agentId).stream().findFirst().orElse(Pair.of("voucher_template.xlsx", defaultVoucherTemplate));
    }

    public Pair<String, Resource> getPDFVoucherByVendorIdOrDefault(int vendorId) {
        return vendorPDFVoucherTemplates.get(vendorId).stream().findFirst().orElse(Pair.of("voucher_template.pdf", defaultPDFVoucherTemplate));
    }

    public Pair<String, Resource> getPDFVoucherByAgentIdOrDefault(int agentId) {
        return agentPDFVoucherTemplates.get(agentId).stream().findFirst().orElse(Pair.of("voucher_template.pdf", defaultPDFVoucherTemplate));
    }
}
