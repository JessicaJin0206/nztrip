package com.fitibo.aotearoa.controller;

import com.fitibo.aotearoa.annotation.Authentication;
import com.fitibo.aotearoa.dto.Role;
import com.fitibo.aotearoa.exception.AuthenticationFailureException;
import com.fitibo.aotearoa.mapper.AgentMapper;
import com.fitibo.aotearoa.mapper.OrderMapper;
import com.fitibo.aotearoa.mapper.SkuMapper;
import com.fitibo.aotearoa.model.Order;
import com.fitibo.aotearoa.model.Sku;
import com.fitibo.aotearoa.service.ArchiveService;
import com.fitibo.aotearoa.util.DateUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by qianhao.zhou on 08/12/2016.
 */
@Controller
public class ExportController extends AuthenticationRequiredController {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private ArchiveService archiveService;

    @Autowired
    private AgentMapper agentMapper;

    @ExceptionHandler
    public ResponseEntity handleException(AuthenticationFailureException ex) {
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/orders/{id}/voucher", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Authentication(value = {Role.Admin, Role.Agent, Role.Vendor})
    public ResponseEntity<byte[]> downloadVoucher(@PathVariable("id") int orderId,
                                                  HttpServletResponse response) throws IOException, InvalidFormatException {
        Order order = orderMapper.findById(orderId);
        if (getToken().getRole() == Role.Agent) {
            if (order.getAgentId() != getToken().getId()) {
                throw new AuthenticationFailureException("order:" + orderId + " does not belong to agent:" + getToken().getId());
            }
        } else if (getToken().getRole() == Role.Vendor) {
            Sku sku = skuMapper.findById(order.getSkuId());
            if (sku.getVendorId() != getToken().getId()) {
                throw new AuthenticationFailureException("order:" + orderId + " does not belong to vendor:" + getToken().getId());
            }
        }
        Workbook voucher = archiveService.createVoucher(order);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            voucher.write(baos);
            response.setHeader("Content-Disposition", "attachment; filename=\"voucher.xlsx\"");
            return new ResponseEntity<>(baos.toByteArray(), HttpStatus.CREATED);
        }
    }


    @RequestMapping(value = "/orders/export", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Authentication(Role.Admin)
    public ResponseEntity<byte[]> downloadOrderStats(HttpServletResponse response)
            throws IOException, InvalidFormatException {
        Workbook orderStats = archiveService.createOrderStats();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            orderStats.write(baos);
            response.setHeader("Content-Disposition", "attachment; filename=\"orders.xlsx\"");
            return new ResponseEntity<>(baos.toByteArray(), HttpStatus.CREATED);

        }
    }

    /**
     * 导出单个sku的价格信息（和并场次的版本）
     */
    @RequestMapping(value = "/sku_tickets/export/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Authentication(Role.Admin)
    public ResponseEntity<byte[]> downloadSkuTickets(HttpServletResponse response,
                                                     @PathVariable("id") int skuId,
                                                     @CookieValue(value = "language", defaultValue = "en") String language)
            throws IOException, InvalidFormatException {
        int id = getToken().getId();//agentId
        Map.Entry<String, Workbook> entry = archiveService.createSkuTickets(skuId, id, language);
        Workbook skuTickets = entry.getValue();
        String fileName = entry.getKey();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            skuTickets.write(baos);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GB2312"), "ISO_8859_1") + ".xlsx\"");
            return new ResponseEntity<>(baos.toByteArray(), HttpStatus.CREATED);

        }
    }

    /**
     * 导出单个sku详情（暂时系统里面没有调用的地方）
     */
    @RequestMapping(value = "/sku/export/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Authentication(Role.Agent)
    public ResponseEntity<byte[]> downloadSku(HttpServletResponse response,
                                              @PathVariable("id") int skuId,
                                              @CookieValue(value = "language", defaultValue = "en") String language)
            throws IOException, InvalidFormatException {
        Workbook skus = archiveService.createSkuDetail(skuId, language);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            skus.write(baos);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + new String("新西兰产品信息表".getBytes("GB2312"), "ISO_8859_1") + ".xlsx\"");
            return new ResponseEntity<>(baos.toByteArray(), HttpStatus.CREATED);

        }
    }

    /**
     * 导出符合搜索条件的sku（所有不带分页），对应skus.ftl里面的导出按钮
     */
    @RequestMapping(value = "/skus/export", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Authentication
    public ResponseEntity<byte[]> downloadSkus(HttpServletResponse response,
                                               @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                               @RequestParam(value = "cityid", defaultValue = "0") int cityId,
                                               @RequestParam(value = "categoryid", defaultValue = "0") int categoryId,
                                               @CookieValue(value = "language", defaultValue = "en") String language)
            throws IOException, InvalidFormatException {
        int vendorId = 0;
        if (getToken().getRole() == Role.Agent) {
            vendorId = agentMapper.findById(getToken().getId()).getVendorId();
        }
        Workbook orderStats = archiveService.createSkusDetail(keyword, cityId, categoryId, vendorId, language);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            orderStats.write(baos);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + new String("新西兰产品信息表".getBytes("GB2312"), "ISO_8859_1") + ".xlsx\"");
            return new ResponseEntity<>(baos.toByteArray(), HttpStatus.CREATED);

        }
    }

    @RequestMapping(value = "/export/skus/{id}/detail", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Authentication({Role.Admin, Role.Vendor})
    public ResponseEntity<byte[]> downloadSkuDetail(HttpServletResponse response,
                                                    @PathVariable("id") int skuId,
                                                    @RequestParam(value = "date", required = false) String dateString) throws IOException {
        Date date;
        if (StringUtils.isEmpty(dateString)) {
            date = DateTime.now().dayOfYear().roundFloorCopy().toDate();
        } else {
            date = DateUtils.parseDate(dateString);
        }
        Workbook skuDetail = archiveService.createSkuDetail(date, skuId);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            skuDetail.write(baos);
            response.setHeader("Content-Disposition", "attachment; filename=\"detail(" + DateUtils.formatDate(date) + ").xlsx\"");
            return new ResponseEntity<>(baos.toByteArray(), HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/export/skus/{id}/overview", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Authentication({Role.Admin, Role.Vendor})
    public ResponseEntity<byte[]> downloadSkuOverview(HttpServletResponse response, @PathVariable("id") int skuId) throws IOException {
        DateTime from = DateTime.now().monthOfYear().roundFloorCopy();
        DateTime to = from.plusMonths(3);

        Workbook result = archiveService.createSkuOverview(skuId, from, to);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            result.write(baos);
            response.setHeader("Content-Disposition", "attachment; filename=\"overview.xlsx\"");
            return new ResponseEntity<>(baos.toByteArray(), HttpStatus.CREATED);
        }
    }

}
