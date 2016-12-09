package com.fitibo.aotearoa.controller;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import com.fitibo.aotearoa.annotation.Authentication;
import com.fitibo.aotearoa.dto.Role;
import com.fitibo.aotearoa.exception.AuthenticationFailureException;
import com.fitibo.aotearoa.mapper.AgentMapper;
import com.fitibo.aotearoa.mapper.OrderMapper;
import com.fitibo.aotearoa.mapper.OrderTicketMapper;
import com.fitibo.aotearoa.mapper.SkuMapper;
import com.fitibo.aotearoa.mapper.SkuTicketMapper;
import com.fitibo.aotearoa.mapper.VendorMapper;
import com.fitibo.aotearoa.model.Order;
import com.fitibo.aotearoa.model.OrderTicket;
import com.fitibo.aotearoa.model.Sku;
import com.fitibo.aotearoa.model.SkuTicket;
import com.fitibo.aotearoa.model.Vendor;
import com.fitibo.aotearoa.util.DateUtils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by qianhao.zhou on 08/12/2016.
 */
@Controller
public class VoucherController extends AuthenticationRequiredController {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AgentMapper agentMapper;

    @Autowired
    private VendorMapper vendorMapper;

    @Autowired
    private OrderTicketMapper orderTicketMapper;

    @Autowired
    private SkuTicketMapper skuTicketMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Value(value = "classpath:voucher_template.xlsx")
    private Resource template;

    @ExceptionHandler
    public ResponseEntity handleException(AuthenticationFailureException ex) {
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/vouchers/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Authentication
    public ResponseEntity<byte[]> downLoad(@PathVariable("id") int orderId, HttpServletResponse response) throws IOException, InvalidFormatException {
        Order order = orderMapper.findById(orderId);
        if (getToken().getRole() == Role.Agent) {
            if (order.getAgentId() != getToken().getId()) {
                throw new AuthenticationFailureException();
            }
        } else {

        }
        Sku sku = skuMapper.findById(order.getSkuId());
        Preconditions.checkNotNull(sku, "invalid sku id:" + order.getSkuId());

        Vendor vendor = vendorMapper.findById(sku.getVendorId());
        Preconditions.checkNotNull(vendor, "invalid vendor id:" + sku.getVendorId());

        List<OrderTicket> orderTickets = orderTicketMapper.findByOrderId(orderId);
        Preconditions.checkArgument(orderTickets.size() > 0, "no available tickets, order id:" + orderId);
        OrderTicket orderTicket = orderTickets.get(0);

        SkuTicket skuTicket = skuTicketMapper.findById(orderTicket.getSkuTicketId());
        Preconditions.checkNotNull(skuTicket, "invalid sku ticket id:" + orderTicket.getSkuTicketId());


        try (InputStream is = template.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            //E5 reference number
            Cell refNumber = sheet.getRow(4).getCell(4);
            refNumber.setCellType(Cell.CELL_TYPE_STRING);
            refNumber.setCellValue(Optional.ofNullable(order.getReferenceNumber()).orElse(""));

            //E6 gathering time
            Cell gatheringTime = sheet.getRow(5).getCell(4);
            gatheringTime.setCellType(Cell.CELL_TYPE_STRING);
            gatheringTime.setCellValue(orderTicket.getGatheringTime());

            //E7 gathering place
            Cell gatheringPlace = sheet.getRow(6).getCell(4);
            gatheringPlace.setCellType(Cell.CELL_TYPE_STRING);
            gatheringPlace.setCellValue(orderTicket.getGatheringPlace());

            //E8 vendor name
            Cell vendorName = sheet.getRow(7).getCell(4);
            vendorName.setCellType(Cell.CELL_TYPE_STRING);
            vendorName.setCellValue(vendor.getName());

            //E9 vendor contact No
            Cell vendorContact = sheet.getRow(8).getCell(4);
            vendorContact.setCellType(Cell.CELL_TYPE_STRING);
            vendorContact.setCellValue(vendor.getPhone());

            //E10 sku name
            Cell skuName = sheet.getRow(9).getCell(4);
            skuName.setCellType(Cell.CELL_TYPE_STRING);
            skuName.setCellValue(sku.getName());

            //E11 Date
            Cell date = sheet.getRow(10).getCell(4);
            date.setCellType(Cell.CELL_TYPE_STRING);
            date.setCellValue(DateUtils.formatDateWithFormat(orderTicket.getTicketDate()));

            //E12 time
            Cell time = sheet.getRow(11).getCell(4);
            time.setCellType(Cell.CELL_TYPE_STRING);
            time.setCellValue(orderTicket.getTicketTime());

            //E13 name
            Cell name = sheet.getRow(12).getCell(4);
            name.setCellType(Cell.CELL_TYPE_STRING);
            name.setCellValue(order.getPrimaryContact());

            //E14 user total
            Cell users = sheet.getRow(13).getCell(4);
            users.setCellType(Cell.CELL_TYPE_STRING);
            Multimap<String, OrderTicket> multimap = ArrayListMultimap.create();
            for (OrderTicket ticket : orderTickets) {
                multimap.put(ticket.getSkuTicket(), ticket);
            }
            ArrayList<String> items = Lists.newArrayList();
            multimap.keySet().forEach(key -> items.add(multimap.get(key).size() + " " + key));
            users.setCellValue(Joiner.on(" & ").join(items));

            //E15 remarks
            Cell remark = sheet.getRow(14).getCell(4);
            remark.setCellType(Cell.CELL_TYPE_STRING);
            remark.setCellValue(order.getRemark());
            workbook.write(baos);
            response.setHeader("Content-Disposition", "attachment; filename=\"voucher.xlsx\"");
            return new ResponseEntity<>(baos.toByteArray(), HttpStatus.CREATED);
        }

    }

}
