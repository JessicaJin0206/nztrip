package com.fitibo.aotearoa.controller;

import com.fitibo.aotearoa.annotation.Authentication;
import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.dto.Role;
import com.fitibo.aotearoa.exception.AuthenticationFailureException;
import com.fitibo.aotearoa.mapper.AgentMapper;
import com.fitibo.aotearoa.mapper.OrderMapper;
import com.fitibo.aotearoa.mapper.OrderStatMapper;
import com.fitibo.aotearoa.mapper.OrderTicketMapper;
import com.fitibo.aotearoa.mapper.SkuMapper;
import com.fitibo.aotearoa.mapper.SkuTicketMapper;
import com.fitibo.aotearoa.mapper.VendorMapper;
import com.fitibo.aotearoa.model.Order;
import com.fitibo.aotearoa.model.OrderStat;
import com.fitibo.aotearoa.model.OrderTicket;
import com.fitibo.aotearoa.model.Sku;
import com.fitibo.aotearoa.model.SkuTicket;
import com.fitibo.aotearoa.model.Vendor;
import com.fitibo.aotearoa.service.SkuService;
import com.fitibo.aotearoa.util.DateUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
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

import rx.Observable;

/**
 * Created by qianhao.zhou on 08/12/2016.
 */
@Controller
public class ExportController extends AuthenticationRequiredController {

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

    @Autowired
    private OrderStatMapper orderStatMapper;

    @Autowired
    private SkuService skuService;

    @Value(value = "classpath:voucher_template.xlsx")
    private Resource voucherTemplate;

    @Value(value = "classpath:voucher_template_28.xlsx")
    private Resource voucherTemplate_28;

    @Value(value = "classpath:order_template.xlsx")
    private Resource orderTemplate;

    @ExceptionHandler
    public ResponseEntity handleException(AuthenticationFailureException ex) {
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    private Resource getVoucherTemplate(Order order) {
        int agentId = order.getAgentId();
        if (agentId == 28) {//hard code here
            return voucherTemplate_28;
        } else {
            return voucherTemplate;
        }
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
        Sku sku = skuService.findById(order.getSkuId());
        Preconditions.checkNotNull(sku, "invalid sku id:" + order.getSkuId());

        Vendor vendor = vendorMapper.findById(sku.getVendorId());
        Preconditions.checkNotNull(vendor, "invalid vendor id:" + sku.getVendorId());

        List<OrderTicket> orderTickets = orderTicketMapper.findByOrderId(orderId);
        Preconditions
                .checkArgument(orderTickets.size() > 0, "no available tickets, order id:" + orderId);
        OrderTicket firstOrderTicket = orderTickets.get(0);

        SkuTicket skuTicket = skuTicketMapper.findById(firstOrderTicket.getSkuTicketId());
        Preconditions.checkNotNull(skuTicket, "invalid sku ticket id:" + firstOrderTicket.getSkuTicketId());

        try (InputStream is = getVoucherTemplate(order).getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            fillRowWithVoucher(sheet, order, firstOrderTicket, orderTickets, vendor, sku);
            sheet.protectSheet("eyounz2016");
            workbook.write(baos);
            response.setHeader("Content-Disposition", "attachment; filename=\"voucher.xlsx\"");
            return new ResponseEntity<>(baos.toByteArray(), HttpStatus.CREATED);
        }
    }

    private void fillRowWithVoucher(Sheet sheet, Order order, OrderTicket firstOrderTicket, List<OrderTicket> orderTickets, Vendor vendor, Sku sku) {
        //E5 reference number
        Cell refNumber = sheet.getRow(4).getCell(4);
        refNumber.setCellType(Cell.CELL_TYPE_STRING);
        refNumber.setCellValue(Optional.ofNullable(order.getReferenceNumber()).orElse(""));

        //E6 gathering time
        Cell gatheringTime = sheet.getRow(5).getCell(4);
        gatheringTime.setCellType(Cell.CELL_TYPE_STRING);
        gatheringTime.setCellValue(firstOrderTicket.getGatheringTime());

        //E7 gathering place
        Cell gatheringPlace = sheet.getRow(6).getCell(4);
        gatheringPlace.setCellType(Cell.CELL_TYPE_STRING);
        gatheringPlace.setCellValue(firstOrderTicket.getGatheringPlace());

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
        date.setCellValue(DateUtils.formatDateWithFormat(firstOrderTicket.getTicketDate()));

        //E12 time
        Cell time = sheet.getRow(11).getCell(4);
        time.setCellType(Cell.CELL_TYPE_STRING);
        time.setCellValue(firstOrderTicket.getTicketTime());

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
    }

    @RequestMapping(value = "/orders/export", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Authentication(Role.Admin)
    public ResponseEntity<byte[]> downloadOrderStats(HttpServletResponse response)
            throws IOException, InvalidFormatException {
        List<OrderStat> orderStats = orderStatMapper.queryAll();
        try (InputStream is = orderTemplate.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, BigDecimal> costPriceMap = Maps.newConcurrentMap();
            Observable.from(orderStats).groupBy(OrderStat::getOrderId)
                    .subscribe(groupedObservable ->
                            groupedObservable.
                                    map(OrderStat::getCostPrice).
                                    reduce(BigDecimal::add).
                                    collect(() -> costPriceMap,
                                            (map, ele) -> map.put(groupedObservable.getKey(), ele)).
                                    subscribe()
                    );

            Map<String, BigDecimal> salePriceMap = Maps.newConcurrentMap();
            Observable.from(orderStats).groupBy(OrderStat::getOrderId)
                    .subscribe(groupedObservable ->
                            groupedObservable.
                                    map(OrderStat::getSalePrice).
                                    reduce(BigDecimal::add).
                                    collect(() -> salePriceMap,
                                            (map, ele) -> map.put(groupedObservable.getKey(), ele)).
                                    subscribe()
                    );

            Observable.zip(Observable.from(orderStats), Observable.range(0, orderStats.size()), Pair::of).
                    subscribe(pair -> {
                        OrderStat orderStat = pair.getLeft();
                        int index = pair.getRight();
                        Row row = sheet.createRow(index + 1);
                        fillRowWithOrderStat(row, orderStat, costPriceMap, salePriceMap);

                    });
            sheet.protectSheet("eyounz2016");
            workbook.write(baos);
            response.setHeader("Content-Disposition", "attachment; filename=\"orders.xlsx\"");
            return new ResponseEntity<>(baos.toByteArray(), HttpStatus.CREATED);
        }
    }

    private void fillRowWithOrderStat(Row row, OrderStat orderStat, Map<String, BigDecimal> costPriceMap, Map<String, BigDecimal> salePriceMap) {
        int col = 0;
        Cell orderId = row.createCell(col++);
        orderId.setCellType(Cell.CELL_TYPE_STRING);
        orderId.setCellValue(orderStat.getOrderId());

        Cell createTime = row.createCell(col++);
        createTime.setCellType(Cell.CELL_TYPE_STRING);
        createTime.setCellValue(DateUtils.formatDate(orderStat.getCreateTime()));

        Cell orderDate = row.createCell(col++);
        orderDate.setCellType(Cell.CELL_TYPE_STRING);
        orderDate.setCellValue(DateUtils.formatDate(orderStat.getTicketDate()));

        Cell contact = row.createCell(col++);
        contact.setCellValue(Cell.CELL_TYPE_STRING);
        contact.setCellValue(orderStat.getPrimaryContact());

        Cell agent = row.createCell(col++);
        agent.setCellValue(Cell.CELL_TYPE_STRING);
        agent.setCellValue(orderStat.getAgent());

        Cell orderStatus = row.createCell(col++);
        orderStatus.setCellType(Cell.CELL_TYPE_STRING);
        orderStatus.setCellValue(OrderStatus.valueOf(orderStat.getStatus()).getDesc());

        Cell referenceNumber = row.createCell(col++);
        referenceNumber.setCellType(Cell.CELL_TYPE_STRING);
        referenceNumber.setCellValue(orderStat.getReferenceNumber());

        Cell skuId = row.createCell(col++);
        skuId.setCellType(Cell.CELL_TYPE_STRING);
        skuId.setCellValue(orderStat.getSkuId());

        Cell skuName = row.createCell(col++);
        skuName.setCellType(Cell.CELL_TYPE_STRING);
        skuName.setCellValue(orderStat.getSkuName());

        Cell vendor = row.createCell(col++);
        vendor.setCellType(Cell.CELL_TYPE_STRING);
        vendor.setCellValue(orderStat.getVendorName());

        Cell ticketTime = row.createCell(col++);
        ticketTime.setCellType(Cell.CELL_TYPE_STRING);
        ticketTime.setCellValue(orderStat.getTicketTime());

        Cell ticket = row.createCell(col++);
        ticket.setCellType(Cell.CELL_TYPE_STRING);
        ticket.setCellValue(orderStat.getTicket());

        Cell count = row.createCell(col++);
        count.setCellType(Cell.CELL_TYPE_NUMERIC);
        count.setCellValue(1);

        Cell salePrice = row.createCell(col++);
        salePrice.setCellType(Cell.CELL_TYPE_NUMERIC);
        salePrice.setCellValue(
                orderStat.getSalePrice().setScale(2, RoundingMode.HALF_EVEN).doubleValue());

        Cell totalSalePrice = row.createCell(col++);
        totalSalePrice.setCellType(Cell.CELL_TYPE_NUMERIC);
        totalSalePrice.setCellValue(
                salePriceMap.get(orderStat.getOrderId()).setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue());

        Cell price = row.createCell(col++);
        price.setCellType(Cell.CELL_TYPE_NUMERIC);
        price.setCellValue(
                orderStat.getPrice().setScale(2, RoundingMode.HALF_EVEN).doubleValue());

        Cell totalPrice = row.createCell(col++);
        totalPrice.setCellType(Cell.CELL_TYPE_NUMERIC);
        totalPrice.setCellValue(
                orderStat.getTotalPrice().setScale(2, RoundingMode.HALF_EVEN).doubleValue());

        Cell costPrice = row.createCell(col++);
        costPrice.setCellType(Cell.CELL_TYPE_NUMERIC);
        costPrice.setCellValue(
                orderStat.getCostPrice().setScale(2, RoundingMode.HALF_EVEN).doubleValue());

        Cell totalCostPrice = row.createCell(col++);
        totalCostPrice.setCellType(Cell.CELL_TYPE_NUMERIC);
        totalCostPrice.setCellValue(
                costPriceMap.get(orderStat.getOrderId()).setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue());
    }

}
