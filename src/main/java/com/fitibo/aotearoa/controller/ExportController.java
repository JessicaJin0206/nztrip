package com.fitibo.aotearoa.controller;

import com.fitibo.aotearoa.annotation.Authentication;
import com.fitibo.aotearoa.dto.Role;
import com.fitibo.aotearoa.exception.AuthenticationFailureException;
import com.fitibo.aotearoa.mapper.OrderMapper;
import com.fitibo.aotearoa.model.Order;
import com.fitibo.aotearoa.service.ArchiveService;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by qianhao.zhou on 08/12/2016.
 */
@Controller
public class ExportController extends AuthenticationRequiredController {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ArchiveService archiveService;

    @ExceptionHandler
    public ResponseEntity handleException(AuthenticationFailureException ex) {
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/vouchers/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Authentication
    public ResponseEntity<byte[]> downLoadVoucher(@PathVariable("id") int orderId,
                                                  HttpServletResponse response) throws IOException, InvalidFormatException {
        Order order = orderMapper.findById(orderId);
        if (getToken().getRole() == Role.Agent) {
            if (order.getAgentId() != getToken().getId()) {
                throw new AuthenticationFailureException();
            }
        } else {

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

}
