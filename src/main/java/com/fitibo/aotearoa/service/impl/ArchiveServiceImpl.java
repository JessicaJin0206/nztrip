package com.fitibo.aotearoa.service.impl;

import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.constants.SkuConstants;
import com.fitibo.aotearoa.mapper.*;
import com.fitibo.aotearoa.model.*;
import com.fitibo.aotearoa.service.ArchiveService;
import com.fitibo.aotearoa.service.DiscountRateService;
import com.fitibo.aotearoa.service.SkuService;
import com.fitibo.aotearoa.util.DateUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhouqianhao on 11/03/2017.
 */
@Service
public class ArchiveServiceImpl implements ArchiveService {

    private final static Logger logger = LoggerFactory.getLogger(ArchiveService.class);

    @Autowired
    private SkuService skuService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private VendorMapper vendorMapper;

    @Autowired
    private OrderTicketMapper orderTicketMapper;

    @Autowired
    private SkuInventoryMapper skuInventoryMapper;

    @Autowired
    private SkuTicketMapper skuTicketMapper;

    @Autowired
    private SkuTicketPriceMapper skuTicketPriceMapper;

    @Value(value = "classpath:voucher_template.xlsx")
    private Resource voucherTemplate;

    @Value(value = "classpath:voucher_template_28.xlsx")
    private Resource voucherTemplate_28;

    @Value(value = "classpath:voucher_template_59.xlsx")
    private Resource voucherTemplate_59;

    @Value(value = "classpath:voucher_template_54.xlsx")
    private Resource voucherTemplate_54;

    @Value(value = "classpath:order_template.xlsx")
    private Resource orderTemplate;

    @Autowired
    private OrderStatMapper orderStatMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private DiscountRateService discountRateService;

    @Override
    public Workbook createVoucher(Order order) {

        int orderId = order.getId();

        Sku sku = skuService.findById(order.getSkuId());
        Preconditions.checkNotNull(sku, "invalid sku id:" + order.getSkuId());

        Vendor vendor = vendorMapper.findById(sku.getVendorId());
        Preconditions.checkNotNull(vendor, "invalid vendor id:" + sku.getVendorId());

        List<OrderTicket> orderTickets = orderTicketMapper.findByOrderId(orderId);
        Preconditions.checkArgument(orderTickets.size() > 0, "no available tickets, order id:" + orderId);
        OrderTicket firstOrderTicket = orderTickets.get(0);

        SkuTicket skuTicket = skuTicketMapper.findById(firstOrderTicket.getSkuTicketId());
        Preconditions.checkNotNull(skuTicket, "invalid sku ticket id:" + firstOrderTicket.getSkuTicketId());

        try (InputStream is = getVoucherTemplate(order).getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            fillRowWithVoucher(sheet, order, firstOrderTicket, orderTickets, vendor, sku);
            sheet.protectSheet("eyounz2016");

            return workbook;
        } catch (InvalidFormatException e) {
            throw new RuntimeException("voucher template invalid format", e);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private Resource getVoucherTemplate(Order order) {
        int agentId = order.getAgentId();
        if (agentId == 28) {//FIXME: hard code here
            return voucherTemplate_28;
        } else if (agentId == 54) {
            return voucherTemplate_54;
        } else if (agentId == 59) {
            return voucherTemplate_59;
        } else {
            return voucherTemplate;
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

    private static BigDecimal calculateTicketPrice(SkuTicketPriceForExportKey ticketPrice, int discount) {
        BigDecimal cost = ticketPrice.getCostPrice();
        BigDecimal sale = ticketPrice.getSalePrice();
        return cost.add(sale.subtract(cost).multiply(BigDecimal.valueOf(discount))
                .divide(BigDecimal.valueOf(100), BigDecimal.ROUND_CEILING));
    }

    @Override
    public Workbook createOrderStats() {
        List<OrderStat> orderStats = orderStatMapper.queryAll();
        try (InputStream is = orderTemplate.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            Drawing drawing = sheet.createDrawingPatriarch();
            CreationHelper creationHelper = workbook.getCreationHelper();
            ClientAnchor anchor = creationHelper.createClientAnchor();
            Font font = workbook.createFont();
            font.setColor(IndexedColors.BLUE.getIndex());
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);

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
                        fillRowWithOrderStat(row, orderStat, costPriceMap, salePriceMap, creationHelper, drawing, anchor, cellStyle);

                    });
            sheet.protectSheet("eyounz2016");
            return workbook;
        } catch (InvalidFormatException e) {
            throw new RuntimeException("voucher template invalid format", e);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @Override
    public Workbook createSkuDetail(Date date, int skuId) {
        Workbook workbook = new XSSFWorkbook();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 20);
        Sheet sheet = workbook.createSheet();
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setWrapText(true);
        List<OrderTicket> orderTickets = orderTicketMapper.findBySkuIdAndDate(skuId, date);
        List<Order> orders = orderMapper.findByIds(orderTickets.stream().map(OrderTicket::getOrderId).distinct().collect(Collectors.toList()));
        List<String> allTickets = skuTicketPriceMapper.findDistinctTicketBySkuIdAndDate(skuId, date);
        List<SkuTicket> skuTickets = skuTicketMapper.findBySkuId(skuId);
        int rowIndex = 0;

        Row header = sheet.createRow(rowIndex++);
        Cell time = header.createCell(0);
        time.setCellType(Cell.CELL_TYPE_STRING);
        time.setCellValue("TIME");
        header.createCell(1);
        for (int i = 0; i < orders.size(); i++) {
            Cell cell = header.createCell(i + 2);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue("Group" + (i + 1));
        }
        for (String skuTime : allTickets.stream().sorted().collect(Collectors.toList())) {

            Row row = sheet.createRow(rowIndex);
            Cell cell = row.createCell(0);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(skuTime);

            cell = row.createCell(1);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue("NAME:");

            row = sheet.createRow(rowIndex + 1);
            row.createCell(0);
            cell = row.createCell(1);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue("EMAIL:");

            row = sheet.createRow(rowIndex + 2);
            row.createCell(0);
            cell = row.createCell(1);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue("MOBILE:");


            Map<Integer, List<OrderTicket>> orderTicketsMap = orderTickets.stream().filter(orderTicket -> orderTicket.getTicketTime().equalsIgnoreCase(skuTime)).collect(Collectors.groupingBy(OrderTicket::getOrderId));
            for (int i = 0; i < skuTickets.size(); i++) {
                SkuTicket skuTicket = skuTickets.get(i);
                int rowNum = rowIndex + i + 3;
                row = sheet.createRow(rowNum);
                cell = row.createCell(1);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(skuTicket.getName());
            }

            int idx = 0;
            for (Integer orderId : orderTicketsMap.keySet()) {
                Optional<Order> orderOptional = orders.stream().filter(order -> order.getId() == orderId).findFirst();
                Preconditions.checkArgument(orderOptional.isPresent(), "this should not happen");
                Order order = orderOptional.get();
                row = sheet.getRow(rowIndex);
                cell = row.createCell(2 + idx);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(order.getPrimaryContact());

                row = sheet.getRow(rowIndex + 1);
                cell = row.createCell(2 + idx);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(order.getPrimaryContactEmail());

                row = sheet.getRow(rowIndex + 2);
                cell = row.createCell(2 + idx);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(order.getPrimaryContactPhone());

                for (int i = 0; i < skuTickets.size(); i++) {
                    SkuTicket skuTicket = skuTickets.get(i);
                    int rowNum = rowIndex + i + 3;
                    row = sheet.getRow(rowNum);

                    cell = row.createCell(2 + idx);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cell.setCellValue(orderTicketsMap.get(orderId).stream().filter(orderTicket -> orderTicket.getSkuTicket().equalsIgnoreCase(skuTicket.getName())).count());

                }
                idx++;
            }

            rowIndex += skuTickets.size() + 4;


        }
        for (int i = 0; i < 2 + orders.size(); i++) {
            sheet.autoSizeColumn(i);
        }
        return workbook;
    }

    private String formatOrderInfo(int index, Order order, List<OrderTicket> orderTickets) {
        StringBuilder sb = new StringBuilder();
        sb.append("Group").append(index);
        Map<String, Integer> summary = orderTickets.stream().collect(Collectors.groupingBy(OrderTicket::getSkuTicket, Collectors.summingInt(input -> 1)));
        sb.append("(").append(summary.entrySet().stream().map(input -> input.getValue() + " " + input.getKey()).collect(Collectors.joining("|"))).append(")").append("\n");
        sb.append(order.getPrimaryContact()).append(" ").append(order.getPrimaryContactEmail()).append(" ").append(Optional.ofNullable(order.getPrimaryContactPhone()).orElse(""));
        return sb.toString();
    }

    private static final String[] DAY_OF_WEEK = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    @Override
    public Workbook createSkuOverview(int skuId, DateTime from, DateTime to) {
        Date fromDate = from.toDate();
        Date toDate = to.toDate();
        final Map<Date, List<SkuTicketPrice>> skuTicketPriceMap = skuTicketPriceMapper.findBySkuIdAndDuration(skuId, fromDate, toDate).stream().collect(Collectors.groupingBy(SkuTicketPrice::getDate));
        final Map<Date, List<SkuOccupation>> skuOccupationMap = skuInventoryMapper.findSkuOccupationBySkuIdAndDate(skuId, fromDate, toDate).stream().collect(Collectors.groupingBy(SkuOccupation::getTicketDate));
        final Map<Date, List<SkuInventory>> skuInventoryMap = skuInventoryMapper.findBySkuIdAndDate(skuId, fromDate, toDate).stream().collect(Collectors.groupingBy(SkuInventory::getDate));
        Workbook workbook = new XSSFWorkbook();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 14);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setWrapText(true);

        for (DateTime current = from; current.isBefore(to); current = current.plusMonths(1)) {
            Sheet sheet = workbook.createSheet(current.toString("YYYY-MM"));
            int rowIndex = 0;
            Row row = sheet.createRow(rowIndex++);
            //header
            for (int i = 0; i < DAY_OF_WEEK.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(DAY_OF_WEEK[i]);
                cell.setCellStyle(style);
            }

            DateTime day = current;
            Row currentRow = sheet.createRow(rowIndex++);
            for (; day.isBefore(current.plusMonths(1)); day = day.plusDays(1)) {
                final Date date = day.toDate();
                List<SkuTicketPrice> skuTicketPrices = skuTicketPriceMap.containsKey(date) ? skuTicketPriceMap.get(date) : Collections.emptyList();
                String collect = skuTicketPrices.stream().filter(input -> StringUtils.isNoneEmpty(input.getTime())).map(skuTicketPrice -> {
                    Map<String, List<SkuOccupation>> skuOccupationMapByTime = skuOccupationMap.containsKey(date) ? skuOccupationMap.get(date).stream().collect(Collectors.groupingBy(SkuOccupation::getTicketTime)) : Collections.emptyMap();
                    StringBuilder result = new StringBuilder();
                    result.append(skuTicketPrice.getTime()).append(":  ");
                    result.append(Optional.ofNullable(skuOccupationMapByTime.get(skuTicketPrice.getTime())).map(List::size).orElse(0));
                    List<SkuInventory> skuInventories = skuInventoryMap.containsKey(date) ? skuInventoryMap.get(date) : Collections.emptyList();
                    Optional<SkuInventory> first = skuInventories.stream().filter(skuInventory -> skuTicketPrice.getTime().equals(skuTicketPrice.getTime())).findFirst();
                    result.append("/").append(first.map(skuInventory -> skuInventory.getCount() + "").orElse("0"));
                    result.append(" people");
                    return result.toString();
                }).distinct().sorted().collect((Collectors.joining("\n")));
                String cellValue = day.getDayOfMonth() + "\n" + collect;

                Cell cell = currentRow.createCell(day.getDayOfWeek() % 7);
                cell.setCellValue(cellValue);
                cell.setCellStyle(style);
                if (day.getDayOfWeek() == 6) {//here dayOfWeek = 6 means Saturday
                    currentRow = sheet.createRow(rowIndex++);
                }
            }
            for (int i = 0; i < DAY_OF_WEEK.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        return workbook;
    }

    private void fillRowWithOrderStat(Row row, OrderStat orderStat, Map<String, BigDecimal> costPriceMap, Map<String, BigDecimal> salePriceMap, CreationHelper creationHelper, Drawing drawing, ClientAnchor anchor, CellStyle cellStyle) {
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


        //
        Cell totalPrice = row.createCell(col++);
        totalPrice.setCellType(Cell.CELL_TYPE_NUMERIC);
        totalPrice.setCellValue(
                orderStat.getModifiedPrice().setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        if (!orderStat.getTotalPrice().equals(orderStat.getModifiedPrice())) {
            Comment priceComment = drawing.createCellComment(anchor);
            RichTextString str1 = creationHelper.createRichTextString("原价为 " + orderStat.getTotalPrice().setScale(2, RoundingMode.HALF_EVEN).doubleValue() + " 此处为修改后的价格");
            priceComment.setString(str1);
            totalPrice.setCellComment(priceComment);
            totalPrice.setCellStyle(cellStyle);
        } else {

        }

        Cell costPrice = row.createCell(col++);
        costPrice.setCellType(Cell.CELL_TYPE_NUMERIC);
        costPrice.setCellValue(
                orderStat.getCostPrice().setScale(2, RoundingMode.HALF_EVEN).doubleValue());

        Cell totalCostPrice = row.createCell(col++);
        totalCostPrice.setCellType(Cell.CELL_TYPE_NUMERIC);
        totalCostPrice.setCellValue(
                costPriceMap.get(orderStat.getOrderId()).setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue());

        Cell refund = row.createCell(col++);
        refund.setCellType(Cell.CELL_TYPE_NUMERIC);
        refund.setCellValue(orderStat.getRefund().setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        
        Cell agentOrderId = row.createCell(col++);
        agentOrderId.setCellType(Cell.CELL_TYPE_STRING);
        agentOrderId.setCellValue(orderStat.getAgentOrderId());
    }

    @Override
    public Workbook createSkuDetail(int skuId, String language) {
        Workbook workbook = new SXSSFWorkbook();
        Sku sku = skuService.findById(skuId);
        createSkuDetailSheet(workbook, sku, SkuConstants.getSkuField(language));
        return workbook;
    }

    @Override
    public Pair<String, Workbook> createSkuTickets(int skuId, int agentId, String language) {
        Map<String, String> languageMap = SkuConstants.getSkuField(language);
        Workbook workbook = new SXSSFWorkbook();
        int rowIndex = 1, col = 1;
        int maxCol = 0;
        int discount = discountRateService.getDiscountByAgent(agentId, skuId);

        Sku sku = skuMapper.findById(skuId);
        Sheet sheet = workbook.createSheet(sku.getUuid());
        Row nameRow = sheet.createRow(rowIndex++);
        Cell skuNameCell = nameRow.createCell(col);
        skuNameCell.setCellType(Cell.CELL_TYPE_STRING);
        skuNameCell.setCellValue(languageMap.get("名称") + "：");
        Cell nameCell1 = nameRow.createCell(col + 1);
        nameCell1.setCellType(Cell.CELL_TYPE_STRING);
        nameCell1.setCellValue(sku.getName());

        //skuId
        Row skuIdRow = sheet.createRow(rowIndex++);
        Cell skuIdCell = skuIdRow.createCell(col);
        skuIdCell.setCellType(Cell.CELL_TYPE_STRING);
        skuIdCell.setCellValue("SKU ID：");
        Cell skuIdValueCell = skuIdRow.createCell(col + 1);
        skuIdValueCell.setCellType(Cell.CELL_TYPE_STRING);
        skuIdValueCell.setCellValue(sku.getUuid());

        rowIndex++;
        rowIndex++;

        List<SkuTicketPriceForExport> skuTicketPriceForExports = skuTicketPriceMapper.findSkuTicketPriceForExportBySkuId(skuId);
        List<Map.Entry<String, List<SkuTicketPriceForExport>>> entries = skuTicketPriceForExports.stream()
                .collect(Collectors.groupingBy(SkuTicketPriceForExport::getTime))//按照场次分组
                .entrySet().stream().sorted((e1, e2) -> compareTime(e1.getKey(), e2.getKey())).collect(Collectors.toList());//按照场次排序（第一次排序保证合并后同一条里面场次有序）

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //合并具有相同价格区间的场次
        Map<Set<SkuTicketPriceForExportKey>, List<String>> timeMap = new HashMap<>();
        for (Map.Entry<String, List<SkuTicketPriceForExport>> entry : entries) {
            Set<SkuTicketPriceForExportKey> set = entry.getValue().stream().map(SkuTicketPriceForExport::getKey).collect(Collectors.toSet());
            if (timeMap.containsKey(set)) {
                timeMap.get(set).add(entry.getKey());
            } else {
                timeMap.put(set, Lists.newArrayList(entry.getKey()));
            }
        }

        //按照场次排序（第二次排序保证sheet里面场次有序）
        for (Map.Entry<Set<SkuTicketPriceForExportKey>, List<String>> entry : timeMap.entrySet().stream().sorted((e1, e2) -> compareTime(e1.getValue().get(0), e2.getValue().get(0))).collect(Collectors.toList())) {
            //String time = entry.getKey();
            String time = entry.getValue().stream().collect(Collectors.joining(","));
            //场次
            Row timeRow = sheet.createRow(rowIndex++);
            Cell timeCell = timeRow.createCell(col);
            timeCell.setCellValue(languageMap.get("场次"));
            Cell timeValueCell = timeRow.createCell(col + 1);
            timeValueCell.setCellValue(time);
            rowIndex++;

            //Map<String, List<SkuTicketPriceForExportKey>> map = entry.getValue().stream().collect(Collectors.groupingBy(SkuTicketPriceForExportKey::getName));
            //将票分类
            Map<String, List<SkuTicketPriceForExportKey>> map = entry.getKey().stream().collect(Collectors.groupingBy(SkuTicketPriceForExportKey::getName));
            //Set list=map.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).collect(Collectors.to);
            for (Map.Entry<String, List<SkuTicketPriceForExportKey>> stringListEntry : map.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).collect(Collectors.toList())) {
                Row timeRangeRow = sheet.createRow(rowIndex++);
                Row salePriceRow = sheet.createRow(rowIndex++);
                Row costPriceRow = sheet.createRow(rowIndex++);

                String name = stringListEntry.getKey();
                Cell nameCell = salePriceRow.createCell(col);
                nameCell.setCellValue(name);
                Cell salePriceCell = salePriceRow.createCell(col + 1);
                salePriceCell.setCellValue(languageMap.get("零售价"));
                Cell costPriceCell = costPriceRow.createCell(col + 1);
                costPriceCell.setCellValue(languageMap.get("核算价"));

                int colIndex = col + 1;
                if (maxCol < stringListEntry.getValue().size()) {
                    maxCol = stringListEntry.getValue().size();
                }

                //同一张票里面的按照初始日期排序
                for (SkuTicketPriceForExportKey skuTicketPriceForExport : stringListEntry.getValue().stream().sorted(Comparator.comparing(SkuTicketPriceForExportKey::getStartDate)).collect(Collectors.toList())) {
                    colIndex++;

                    Cell timeRangeValueCell = timeRangeRow.createCell(colIndex);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    String startDate = simpleDateFormat.format(skuTicketPriceForExport.getStartDate());
                    String endDate = simpleDateFormat.format(skuTicketPriceForExport.getEndDate());
                    timeRangeValueCell.setCellValue(startDate + " - " + endDate);

                    Cell salePriceValueCell = salePriceRow.createCell(colIndex);
                    salePriceValueCell.setCellValue(skuTicketPriceForExport.getSalePrice().doubleValue());
                    salePriceValueCell.setCellStyle(cellStyle);
                    Cell costPriceValueCell = costPriceRow.createCell(colIndex);
                    costPriceValueCell.setCellValue(calculateTicketPrice(skuTicketPriceForExport, discount).doubleValue());
                    costPriceValueCell.setCellStyle(cellStyle);
                }
                rowIndex++;
            }
        }
        sheet.setColumnWidth(col, 3000);
        sheet.setColumnWidth(col + 1, 3500);
        for (int i = 0; i < maxCol; i++) {
            sheet.setColumnWidth(i + col + 2, 6000);
        }
        //保护sheet
        sheet.protectSheet("eyounz2016");
        return Pair.of(sku.getName(), workbook);
    }

    //排序时间的函数
    private int compareTime(String time1, String time2) {
        if (time1.equals(time2)) {
            return 0;
        }
        String[] s1 = time1.split(":");
        String[] s2 = time2.split(":");
        try {
            if (Integer.parseInt(s1[0]) == Integer.parseInt(s2[0])) {
                return Integer.parseInt(s1[1]) - Integer.parseInt(s2[1]);
            } else {
                return Integer.parseInt(s1[0]) - Integer.parseInt(s2[0]);
            }
        } catch (NumberFormatException e) {
            logger.warn("invalid format", e);
            return -1;
        } catch (ArrayIndexOutOfBoundsException e) {
            //logger.warn("invalid format", e);
            return -1;
        }
    }

    private List<String> getSkuSummaryLanguage(String language) {
        List<String> chinese = Lists.newArrayList("SKU ID", "前台Doc", "名称", "简述", "下单链接", "预定确认时间");
        List<String> english = Lists.newArrayList("SKU ID", "Doc ID", "Name", "Description", "Order Address", "Estimated Confirmation Time");
        Map<String, List<String>> languageMap = Maps.newHashMap();
        languageMap.put("cn", chinese);
        languageMap.put("en", english);
        return languageMap.get(language);
    }

    @Override
    public Workbook createSkusDetail(String keyword, int cityId, int categoryId, int vendorId, String language) {

        List<Sku> skus = skuMapper.findAllByMultiFields(keyword, cityId, categoryId, vendorId, new RowBounds());
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet("Summary");
        Font font = workbook.createFont();
        //font.setFontHeightInPoints((short) 20);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(IndexedColors.WHITE.getIndex());
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        style.setBottomBorderColor(IndexedColors.WHITE.getIndex());
        style.setLeftBorderColor(IndexedColors.WHITE.getIndex());
        style.setTopBorderColor(IndexedColors.WHITE.getIndex());
        style.setRightBorderColor(IndexedColors.WHITE.getIndex());

        style.setWrapText(true);

        /*//基础信息
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,6));
        Cell baseInfoCell=sheet.createRow(0).createCell(0);
        baseInfoCell.setCellStyle(style);
        baseInfoCell.setCellValue("基础信息");

        //预定
        sheet.addMergedRegion(new CellRangeAddress(0,0,7,13));
        Cell reserveCell=sheet.getRow(0).createCell(7);
        reserveCell.setCellStyle(style);
        reserveCell.setCellValue("预定");

        //出行
        sheet.addMergedRegion(new CellRangeAddress(0,0,14,16));
        Cell travelCell=sheet.getRow(0).createCell(14);
        travelCell.setCellStyle(style);
        travelCell.setCellValue("出行");

        Cell noteCell=sheet.getRow(0).createCell(15);
        noteCell.setCellStyle(style);
        noteCell.setCellValue("备注");*/

        List<String> strings = getSkuSummaryLanguage(language);
        List<Integer> widths = Lists.newArrayList(3000, 3000, 12000, 12000, 12000, 5000);
        Row row1 = sheet.createRow(0);
        for (int i = 0; i < strings.size(); i++) {
            Cell cell = row1.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(strings.get(i));
            sheet.setColumnWidth(i, widths.get(i));
        }

        int rowIndex = 1;
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        CellStyle hlinkStyle = workbook.createCellStyle();
        Font hlinkFont = workbook.createFont();
        hlinkFont.setUnderline(Font.U_SINGLE);
        hlinkFont.setColor(IndexedColors.BLUE.getIndex());
        hlinkStyle.setFont(hlinkFont);
        hlinkStyle.setAlignment(CellStyle.ALIGN_CENTER);
        hlinkStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        Map<String, String> languageMap = SkuConstants.getSkuField(language);
        for (Sku sku : skus) {
            createSkuDetailSheet(workbook, sku, languageMap);
            int startCol = 0;
            Sheet skuSheet = workbook.getSheet(sku.getUuid());
            Row row = sheet.createRow(rowIndex++);

            //skuId
            Cell skuIdCell = row.createCell(startCol++);
            skuIdCell.setCellStyle(cellStyle);
            skuIdCell.setCellValue(sku.getUuid());

            //docId
            Cell docIdCell = row.createCell(startCol++);
            docIdCell.setCellStyle(cellStyle);
            docIdCell.setCellValue(sku.getUuid());
            Hyperlink link = workbook.getCreationHelper().createHyperlink(Hyperlink.LINK_DOCUMENT);
            link.setAddress("'" + skuSheet.getSheetName() + "'!A1");
            docIdCell.setHyperlink(link);
            docIdCell.setCellStyle(hlinkStyle);


            //名称
            Cell nameCell = row.createCell(startCol++);
            nameCell.setCellStyle(cellStyle);
            nameCell.setCellValue(sku.getName());

            //简述
            Cell descriptionCell = row.createCell(startCol++);
            descriptionCell.setCellStyle(cellStyle);
            descriptionCell.setCellValue(sku.getDescription());

            //下单链接
            XSSFHyperlink link1 = (XSSFHyperlink) workbook.getCreationHelper().createHyperlink(Hyperlink.LINK_URL);
            String orderURL = "http://order.fitibo.net/create_order?skuId=" + sku.getId();
            link1.setAddress(orderURL);
            link1.setLabel(orderURL);
            Cell addressCell = row.createCell(startCol++);
            addressCell.setCellValue(orderURL);
            addressCell.setCellStyle(hlinkStyle);
            addressCell.setHyperlink(link1);

            //预定确认时间
            Cell onfirmationTimeCell = row.createCell(startCol++);
            onfirmationTimeCell.setCellStyle(cellStyle);
            onfirmationTimeCell.setCellValue(sku.getConfirmationTime());

        }
        sheet.protectSheet("eyounz2016");
        return workbook;
    }

    private void createSkuTicketSheet(Workbook workbook, Sku sku) {
        List<SkuTicket> skuTickets = sku.getTickets();
        Sheet sheet = workbook.createSheet(sku.getUuid());
        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(IndexedColors.WHITE.getIndex());
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        style.setBottomBorderColor(IndexedColors.WHITE.getIndex());
        style.setLeftBorderColor(IndexedColors.WHITE.getIndex());
        style.setTopBorderColor(IndexedColors.WHITE.getIndex());
        style.setRightBorderColor(IndexedColors.WHITE.getIndex());

        int rowIndex = 1;
        //名称
        Row nameRow = sheet.createRow(rowIndex++);
        Cell nameCell = nameRow.createCell(2);
        nameCell.setCellType(Cell.CELL_TYPE_STRING);
        nameCell.setCellValue("名称：");
        Cell nameCell1 = nameRow.createCell(3);
        nameCell1.setCellType(Cell.CELL_TYPE_STRING);
        nameCell1.setCellValue(sku.getName());

        rowIndex++;

        //skuId
        Row skuIdRow = sheet.createRow(rowIndex++);
        Cell skuIdCell = skuIdRow.createCell(2);
        skuIdCell.setCellType(Cell.CELL_TYPE_STRING);
        skuIdCell.setCellValue("Eyounz SKU ID：");
        Cell skuIdValueCell = skuIdRow.createCell(3);
        skuIdValueCell.setCellType(Cell.CELL_TYPE_STRING);
        skuIdValueCell.setCellValue(sku.getUuid());

        CellStyle hlinkstyle = workbook.createCellStyle();
        Font hlinkfont = workbook.createFont();
        hlinkfont.setUnderline(XSSFFont.U_SINGLE);
        hlinkfont.setColor(HSSFColor.BLUE.index);
        hlinkstyle.setFont(hlinkfont);

        //下单地址
        XSSFHyperlink link = (XSSFHyperlink) workbook.getCreationHelper().createHyperlink(Hyperlink.LINK_URL);
        String orderURL = "http://order.fitibo.net/create_order?skuId=" + sku.getId();
        link.setAddress(orderURL);
        link.setLabel(orderURL);
        Row addressRow = sheet.createRow(rowIndex++);
        Cell addressCell = addressRow.createCell(2);
        addressCell.setCellType(Cell.CELL_TYPE_STRING);
        addressCell.setCellValue("Eyounz下单地址：");
        Cell addressValueCell = addressRow.createCell(3);
        addressValueCell.setCellType(Cell.CELL_TYPE_STRING);
        addressValueCell.setCellValue(orderURL);
        addressValueCell.setCellStyle(hlinkstyle);
        addressValueCell.setHyperlink(link);

        rowIndex++;

        int startCol = 1, startRol = rowIndex;

        Cell ticketNameCell = sheet.createRow(rowIndex++).createCell(startCol);
        ticketNameCell.setCellStyle(style);
        ticketNameCell.setCellValue("票种");

        Cell ageCell = sheet.createRow(rowIndex++).createCell(startCol);
        ageCell.setCellStyle(style);
        ageCell.setCellValue("票种范围");

        Cell salePriceCell = sheet.createRow(rowIndex++).createCell(startCol);
        salePriceCell.setCellStyle(style);
        salePriceCell.setCellValue("官网价格NZD");

        Cell costPriceCell = sheet.createRow(rowIndex++).createCell(startCol);
        costPriceCell.setCellStyle(style);
        costPriceCell.setCellValue("核算价格NZD");

        Cell timeCell = sheet.createRow(rowIndex++).createCell(startCol);
        timeCell.setCellStyle(style);
        timeCell.setCellValue("有效期");
        sheet.setColumnWidth(startCol, 4000);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        for (SkuTicket skuTicket : skuTickets) {
            for (SkuTicketPrice skuTicketPrice : skuTicket.getTicketPrices()) {
                int row = startRol;
                startCol++;
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setWrapText(true);
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                Cell ticketNameValueCell = sheet.getRow(row++).createCell(startCol);
                ticketNameValueCell.setCellStyle(cellStyle);
                ticketNameValueCell.setCellValue(skuTicket.getName());

                Cell ageValueCell = sheet.getRow(row++).createCell(startCol);
                ageValueCell.setCellStyle(cellStyle);
                String age = skuTicket.getAgeConstraint();
                if (age.startsWith("-")) {
                    ageValueCell.setCellValue(age.split("-")[0] + "岁以下");
                } else if (age.endsWith("-")) {
                    ageValueCell.setCellValue(age.split("-")[0] + "岁以上");
                } else {
                    ageValueCell.setCellValue(age + "岁");
                }

                Cell salePriceValueCell = sheet.getRow(row++).createCell(startCol);
                salePriceValueCell.setCellStyle(cellStyle);
                salePriceValueCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                salePriceValueCell.setCellValue(skuTicketPrice.getSalePrice().doubleValue());

                Cell costPriceValueCell = sheet.getRow(row++).createCell(startCol);
                costPriceValueCell.setCellStyle(cellStyle);
                costPriceValueCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                costPriceValueCell.setCellValue(skuTicketPrice.getCostPrice().doubleValue());

                Cell timeValueCell = sheet.getRow(row++).createCell(startCol);
                timeValueCell.setCellStyle(cellStyle);
                costPriceValueCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                timeValueCell.setCellValue(simpleDateFormat.format(skuTicketPrice.getDate()));
                sheet.setColumnWidth(startCol, 5000);
            }

        }

    }

    private void createSkuDetailSheet(Workbook workbook, Sku sku, Map<String, String> languageMap) {
        Sheet sheet = workbook.createSheet(sku.getUuid());
        Font font = workbook.createFont();
        //font.setFontHeightInPoints((short) 20);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(IndexedColors.WHITE.getIndex());
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        style.setBottomBorderColor(IndexedColors.WHITE.getIndex());
        style.setLeftBorderColor(IndexedColors.WHITE.getIndex());
        style.setTopBorderColor(IndexedColors.WHITE.getIndex());
        style.setRightBorderColor(IndexedColors.WHITE.getIndex());

        style.setWrapText(true);


        int col1 = 1, col2 = 2;
        //style.setWrapText(true);
        int rowIndex = 1;
        Font boldFont = workbook.createFont();
        //font.setFontHeightInPoints((short) 20);
        boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        //名称
        CellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);
        Row nameRow = sheet.createRow(rowIndex++);
        Cell nameCell = nameRow.createCell(2);
        nameCell.setCellType(Cell.CELL_TYPE_STRING);
        nameCell.setCellValue(languageMap.get("名称") + "：");
        nameCell.setCellStyle(boldStyle);
        Cell nameCell1 = nameRow.createCell(3);
        nameCell1.setCellType(Cell.CELL_TYPE_STRING);
        nameCell1.setCellValue(sku.getName());

        rowIndex++;

        //skuId
        Row skuIdRow = sheet.createRow(rowIndex++);
        Cell skuIdCell = skuIdRow.createCell(2);
        skuIdCell.setCellType(Cell.CELL_TYPE_STRING);
        skuIdCell.setCellValue("Eyounz SKU ID：");
        skuIdCell.setCellStyle(boldStyle);
        Cell skuIdValueCell = skuIdRow.createCell(3);
        skuIdValueCell.setCellType(Cell.CELL_TYPE_STRING);
        skuIdValueCell.setCellValue(sku.getUuid());

        CellStyle hlinkstyle = workbook.createCellStyle();
        Font hlinkfont = workbook.createFont();
        hlinkfont.setUnderline(XSSFFont.U_SINGLE);
        hlinkfont.setColor(HSSFColor.BLUE.index);
        hlinkstyle.setFont(hlinkfont);

        //下单地址
        XSSFHyperlink link = (XSSFHyperlink) workbook.getCreationHelper().createHyperlink(Hyperlink.LINK_URL);
        String orderURL = "http://order.fitibo.net/create_order?skuId=" + sku.getId();
        link.setAddress(orderURL);
        link.setLabel(orderURL);
        Row addressRow = sheet.createRow(rowIndex++);
        Cell addressCell = addressRow.createCell(2);
        addressCell.setCellType(Cell.CELL_TYPE_STRING);
        addressCell.setCellValue(languageMap.get("Eyounz下单地址"));
        addressCell.setCellStyle(boldStyle);
        Cell addressValueCell = addressRow.createCell(3);
        addressValueCell.setCellType(Cell.CELL_TYPE_STRING);
        addressValueCell.setCellValue(orderURL);
        addressValueCell.setCellStyle(hlinkstyle);
        addressValueCell.setHyperlink(link);
        rowIndex++;

        //OfficialWebsite官网查位链接
        Row officialWebsiteRow = sheet.createRow(rowIndex++);
        Cell officialWebsiteCell = officialWebsiteRow.createCell(col1);
        officialWebsiteCell.setCellType(Cell.CELL_TYPE_STRING);
        officialWebsiteCell.setCellValue(languageMap.get("官网查位链接"));
        officialWebsiteCell.setCellStyle(style);

        String url = sku.getOfficialWebsite().trim();
        Cell officialWebsiteValueCell = officialWebsiteRow.createCell(col2);
        officialWebsiteValueCell.setCellType(Cell.CELL_TYPE_STRING);
        officialWebsiteValueCell.setCellValue(url);
        officialWebsiteValueCell.setCellStyle(hlinkstyle);
        Hyperlink officialWebsiteLink = workbook.getCreationHelper().createHyperlink(Hyperlink.LINK_URL);
        try {
            officialWebsiteLink.setAddress(url);
            officialWebsiteValueCell.setHyperlink(officialWebsiteLink);
        } catch (Exception e) {
            logger.warn("invalid URI in sku , skuId is " + sku.getId(), e);
        }

        //description行程概述
        Row descriptionRow = sheet.createRow(rowIndex++);
        Cell descriptionCell = descriptionRow.createCell(col1);
        descriptionCell.setCellType(Cell.CELL_TYPE_STRING);
        descriptionCell.setCellValue(languageMap.get("行程描述"));
        descriptionCell.setCellStyle(style);
        Cell descriptionValueCell = descriptionRow.createCell(col2);
        descriptionValueCell.setCellType(Cell.CELL_TYPE_STRING);
        descriptionValueCell.setCellValue(sku.getDescription());

        //confirmationTime预估确认时长
        Row confirmationTimeRow = sheet.createRow(rowIndex++);
        Cell confirmationTimeCell = confirmationTimeRow.createCell(col1);
        confirmationTimeCell.setCellType(Cell.CELL_TYPE_STRING);
        confirmationTimeCell.setCellValue(languageMap.get("预估确认时长"));
        confirmationTimeCell.setCellStyle(style);
        Cell confirmationTimeValueCell = confirmationTimeRow.createCell(col2);
        confirmationTimeValueCell.setCellType(Cell.CELL_TYPE_STRING);
        confirmationTimeValueCell.setCellValue(sku.getConfirmationTime());

        //rescheduleCancelNotice退改签规定
        Row rescheduleCancelNoticeRow = sheet.createRow(rowIndex++);
        Cell rescheduleCancelNoticeCell = rescheduleCancelNoticeRow.createCell(col1);
        rescheduleCancelNoticeCell.setCellType(Cell.CELL_TYPE_STRING);
        rescheduleCancelNoticeCell.setCellValue(languageMap.get("退改签规定"));
        rescheduleCancelNoticeCell.setCellStyle(style);
        Cell rescheduleCancelNoticeValueCell = rescheduleCancelNoticeRow.createCell(col2);
        rescheduleCancelNoticeValueCell.setCellType(Cell.CELL_TYPE_STRING);
        rescheduleCancelNoticeValueCell.setCellValue(sku.getRescheduleCancelNotice());

        //agendaInfo行程概述
        Row agendaInfoRow = sheet.createRow(rowIndex++);
        Cell agendaInfoCell = agendaInfoRow.createCell(col1);
        agendaInfoCell.setCellType(Cell.CELL_TYPE_STRING);
        agendaInfoCell.setCellValue(languageMap.get("行程概述"));
        agendaInfoCell.setCellStyle(style);
        Cell agendaInfoValueCell = agendaInfoRow.createCell(col2);
        agendaInfoValueCell.setCellType(Cell.CELL_TYPE_STRING);
        agendaInfoValueCell.setCellValue(sku.getAgendaInfo());

        //activityTime活动时间
        Row activityTimeRow = sheet.createRow(rowIndex++);
        Cell activityTimeCell = activityTimeRow.createCell(col1);
        activityTimeCell.setCellType(Cell.CELL_TYPE_STRING);
        activityTimeCell.setCellValue(languageMap.get("活动时间"));
        activityTimeCell.setCellStyle(style);
        Cell activityTimeValueCell = activityTimeRow.createCell(col2);
        activityTimeValueCell.setCellType(Cell.CELL_TYPE_STRING);
        activityTimeValueCell.setCellValue(sku.getActivityTime());

        //openingTime营业时间
        Row openingTimeRow = sheet.createRow(rowIndex++);
        Cell openingTimeCell = openingTimeRow.createCell(col1);
        openingTimeCell.setCellType(Cell.CELL_TYPE_STRING);
        openingTimeCell.setCellValue(languageMap.get("营业时间"));
        openingTimeCell.setCellStyle(style);
        Cell openingTimeValueCell = openingTimeRow.createCell(col2);
        openingTimeValueCell.setCellType(Cell.CELL_TYPE_STRING);
        openingTimeValueCell.setCellValue(sku.getOpeningTime());

        //ticketInfo门票形式
        Row ticketInfoRow = sheet.createRow(rowIndex++);
        Cell ticketInfoCell = ticketInfoRow.createCell(col1);
        ticketInfoCell.setCellType(Cell.CELL_TYPE_STRING);
        ticketInfoCell.setCellValue(languageMap.get("门票形式"));
        ticketInfoCell.setCellStyle(style);
        Cell ticketInfoValueCell = ticketInfoRow.createCell(col2);
        ticketInfoValueCell.setCellType(Cell.CELL_TYPE_STRING);
        ticketInfoValueCell.setCellValue(sku.getTicketInfo());

        //serviceInclude服务包含
        Row serviceIncludeRow = sheet.createRow(rowIndex++);
        Cell serviceIncludeCell = serviceIncludeRow.createCell(col1);
        serviceIncludeCell.setCellType(Cell.CELL_TYPE_STRING);
        serviceIncludeCell.setCellValue(languageMap.get("服务包含"));
        serviceIncludeCell.setCellStyle(style);
        Cell serviceIncludeValueCell = serviceIncludeRow.createCell(col2);
        serviceIncludeValueCell.setCellType(Cell.CELL_TYPE_STRING);
        serviceIncludeValueCell.setCellValue(sku.getServiceInclude());

        //serviceExclude服务未含
        Row serviceExcludeRow = sheet.createRow(rowIndex++);
        Cell serviceExcludeCell = serviceExcludeRow.createCell(col1);
        serviceExcludeCell.setCellType(Cell.CELL_TYPE_STRING);
        serviceExcludeCell.setCellValue(languageMap.get("服务未含"));
        serviceExcludeCell.setCellStyle(style);
        Cell serviceExcludeValueCell = serviceExcludeRow.createCell(col2);
        serviceExcludeValueCell.setCellType(Cell.CELL_TYPE_STRING);
        serviceExcludeValueCell.setCellValue(sku.getServiceExclude());

        //extraItem附加收费项
        Row extraItemRow = sheet.createRow(rowIndex++);
        Cell extraItemCell = extraItemRow.createCell(col1);
        extraItemCell.setCellType(Cell.CELL_TYPE_STRING);
        extraItemCell.setCellValue(languageMap.get("附加收费项"));
        extraItemCell.setCellStyle(style);
        Cell extraItemValueCell = extraItemRow.createCell(col2);
        extraItemValueCell.setCellType(Cell.CELL_TYPE_STRING);
        extraItemValueCell.setCellValue(sku.getExtraItem());

        //priceConstraint限价信息
        Row priceConstraintRow = sheet.createRow(rowIndex++);
        Cell priceConstraintCell = priceConstraintRow.createCell(col1);
        priceConstraintCell.setCellType(Cell.CELL_TYPE_STRING);
        priceConstraintCell.setCellValue(languageMap.get("限价信息"));
        priceConstraintCell.setCellStyle(style);
        Cell priceConstraintValueCell = priceConstraintRow.createCell(col2);
        priceConstraintValueCell.setCellType(Cell.CELL_TYPE_STRING);
        priceConstraintValueCell.setCellValue(sku.getPriceConstraint());

        //otherInfo预订所需其他信息
        Row otherInfoRow = sheet.createRow(rowIndex++);
        Cell otherInfoCell = otherInfoRow.createCell(col1);
        otherInfoCell.setCellType(Cell.CELL_TYPE_STRING);
        otherInfoCell.setCellValue(languageMap.get("预订所需其他信息"));
        otherInfoCell.setCellStyle(style);
        Cell otherInfoValueCell = otherInfoRow.createCell(col2);
        otherInfoValueCell.setCellType(Cell.CELL_TYPE_STRING);
        otherInfoValueCell.setCellValue(sku.getOtherInfo());

        //attention注意事项
        int row1 = rowIndex;
        String[] attentions = sku.getAttention().split("\\n");
        for (String s : attentions) {
            Row row = sheet.createRow(rowIndex++);
            Cell attentionValueCell = row.createCell(col2);
            attentionValueCell.setCellType(Cell.CELL_TYPE_STRING);
            attentionValueCell.setCellValue(s);
        }
        Row attentionRow = sheet.getRow(row1);
        Cell attentionCell = attentionRow.createCell(col1);
        attentionCell.setCellType(Cell.CELL_TYPE_STRING);
        attentionCell.setCellValue(languageMap.get("注意事项"));
        attentionCell.setCellStyle(style);
        /*Cell attentionValueCell = attentionRow.createCell(col2);
        attentionValueCell.setCellType(Cell.CELL_TYPE_STRING);
        attentionValueCell.setCellValue(sku.getAttention());*/


        for (int i = 1; i < 5; i++) {
            sheet.setColumnWidth(i, 4500);
        }
        sheet.protectSheet("eyounz2016");

    }
}
