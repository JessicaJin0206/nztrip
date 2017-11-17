package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.constants.CommonConstants;
import com.fitibo.aotearoa.mapper.AgentMapper;
import com.fitibo.aotearoa.mapper.SkuMapper;
import com.fitibo.aotearoa.mapper.SkuTicketMapper;
import com.fitibo.aotearoa.mapper.SkuTicketPriceMapper;
import com.fitibo.aotearoa.model.Agent;
import com.fitibo.aotearoa.model.Sku;
import com.fitibo.aotearoa.model.SkuTicket;
import com.fitibo.aotearoa.model.SkuTicketPrice;
import com.fitibo.aotearoa.util.DateUtils;
import com.fitibo.aotearoa.vo.OrderTicketUserVo;
import com.fitibo.aotearoa.vo.OrderTicketVo;
import com.fitibo.aotearoa.vo.OrderVo;
import com.fitibo.aotearoa.vo.Scan;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("scanService")
public class ScanService {
    private static final Logger logger = LoggerFactory.getLogger(ScanService.class);
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SkuTicketMapper skuTicketMapper;
    @Autowired
    private SkuTicketPriceMapper skuTicketPriceMapper;
    @Autowired
    private AgentMapper agentMapper;
    @Autowired
    private DiscountRateService discountRateService;
    @Autowired
    private PricingService pricingService;

    public OrderVo scanOrder(Scan scan) {
        Map<String, String> map = Maps.newHashMap();
        //懒猫
        map.put("订单编号", "agentOrderId");
        map.put("出发日期", "ticketDate");
        map.put("联系人拼音", "primaryContact");
        map.put("国内手机号(+86)", "primaryContactPhone");
        map.put("邮箱地址", "primaryContactEmail");

        //KLOOK
        map.put("Date Request", "ticketDate");
        map.put("Lead person name", "primaryContact");
        map.put("Lead person number", "primaryContactPhone");
        map.put("Lead person email", "primaryContactEmail");

        Map<String, String> userMap = Maps.newHashMap();
        userMap.put("姓名拼音", "name");
        userMap.put("体重(kg)", "weight");
        userMap.put("成人", "Adult");
        userMap.put("儿童", "Child");

        Sku sku = skuMapper.findById(scan.getSkuId());
        String gatheringPlace = Lists.newArrayList(sku.getGatheringPlace().split(CommonConstants.SEPARATOR)).get(0);

        Agent agent = agentMapper.findById(scan.getAgentId());

        List<SkuTicket> skuTickets = skuTicketMapper.findOnlineBySkuId(scan.getSkuId());
        List<OrderTicketVo> ticketVos = Lists.newArrayList();

        boolean hasTravelInfo = false;

        Map<String, Integer> peopleMap = Maps.newHashMap();
        List<String> nameList = Lists.newArrayList();
        List<Integer> weightList = Lists.newArrayList();

        Multimap<String, OrderTicketUserVo> users = ArrayListMultimap.create();

        OrderVo orderVo = new OrderVo();
        orderVo.setAgentId(agent.getId());
        orderVo.setSkuId(sku.getId());
        orderVo.setAgentName(agent.getName());
        orderVo.setPrimaryContactPhone(agent.getDefaultContactPhone());
        orderVo.setPrimaryContact(agent.getDefaultContact());
        orderVo.setPrimaryContactEmail(agent.getDefaultContactEmail());
        orderVo.setRemark("");

        String skuName = null;
        try {
            Map<String, PropertyDescriptor> propertyDescriptorMap = Stream.of(Introspector.getBeanInfo(OrderVo.class).getPropertyDescriptors()).collect(Collectors.toMap(PropertyDescriptor::getName, Function.identity()));
            BeanInfo beanInfo = Introspector.getBeanInfo(OrderTicketUserVo.class);
            Map<String, PropertyDescriptor> userPropertyDescriptorMap = Stream.of(beanInfo.getPropertyDescriptors()).collect(Collectors.toMap(PropertyDescriptor::getName, Function.identity()));
            List<String> strings = Stream.of(scan.getContent().replace(":", "：").split("\n")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            if (strings.size() > 0 && strings.get(0).startsWith("Klook Order")) {
                List<String> temp = Stream.of(strings.get(0).split("-")).map(String::trim).collect(Collectors.toList());
                orderVo.setAgentOrderId(temp.get(temp.size() - 1));
            }
            for (int i = 0; i < strings.size(); i++) {
                String line = strings.get(i);
                if (line.equals("出行人信息 travel info")) {
                    hasTravelInfo = true;
                    continue;
                }
                if (hasTravelInfo) {
                    if (line.contains("、")) {
                        String temp = line.split("、")[1];
                        if (temp.contains("：")) {
                            String type = temp.substring(0, 2);
                            temp = temp.substring(2);
                            String[] strings1 = temp.split("\\s+");
                            OrderTicketUserVo orderTicketUserVo = new OrderTicketUserVo();
                            for (int j = 0; j < strings1.length; j++) {
                                if (strings1[j].equals("：") && userMap.containsKey(strings1[j - 1])) {
                                    PropertyDescriptor propertyDescriptor = userPropertyDescriptorMap.get(userMap.get(strings1[j - 1]));
                                    Method method = propertyDescriptor.getWriteMethod();
                                    if (propertyDescriptor.getPropertyType().equals(int.class)) {
                                        if (j + 1 < strings1.length) {
                                            method.invoke(orderTicketUserVo, Integer.parseInt(strings1[j + 1]));
                                            j++;
                                        }
                                    } else {
                                        StringBuilder stringBuilder = new StringBuilder();
                                        j++;
                                        while (true) {
                                            stringBuilder.append(strings1[j]).append(" ");
                                            j++;
                                            if (j >= strings1.length) {
                                                break;
                                            }
                                            if (j == strings1.length - 1) {
                                                stringBuilder.append(strings1[j]);
                                                break;
                                            }
                                            if (j < strings1.length - 1 && strings1[j + 1].equals("：")) {
                                                j--;
                                                break;
                                            }
                                        }
                                        method.invoke(orderTicketUserVo, stringBuilder.toString().trim());
                                    }

                                }
                            }
                            users.put(userMap.get(type), orderTicketUserVo);
                        } else {
                            OrderTicketUserVo orderTicketUserVo = new OrderTicketUserVo();
                            do {
                                i++;
                                line = strings.get(i);
                                if (line.contains("：")) {
                                    List<String> temp1 = Stream.of(line.split("：")).map(String::trim).collect(Collectors.toList());
                                    if (userMap.containsKey(temp1.get(0))) {
                                        PropertyDescriptor propertyDescriptor = userPropertyDescriptorMap.get(userMap.get(temp1.get(0)));
                                        Method method = propertyDescriptor.getWriteMethod();
                                        if (propertyDescriptor.getPropertyType().equals(int.class)) {
                                            method.invoke(orderTicketUserVo, Integer.parseInt(temp1.get(1)));
                                        } else {
                                            method.invoke(orderTicketUserVo, temp1.get(1));
                                        }
                                    }
                                }
                            } while (!line.contains("、"));
                            users.put(userMap.get(temp), orderTicketUserVo);
                        }
                    }
                }
                if (line.startsWith("Participant") && line.contains("--")) {
                    if (line.contains("Passport")) {
                        nameList.add(strings.get(i + 1).split("：")[1].trim() + " " + line.split("：")[1].trim());
                        i++;
                        continue;
                    } else if (line.contains("Weight")) {
                        String weight = line.split("：")[1].trim();
                        weightList.add(Integer.parseInt(weight.replace("公斤", "").trim()));
                    }
                }
                if (line.contains("：")) {
                    if (line.startsWith("Preferred") && line.contains("Time")) {
                        orderVo.setRemark(orderVo.getRemark() + line + "  ");
                        continue;
                    }
                    List<String> temp = Stream.of(line.split("：")).map(String::trim).collect(Collectors.toList());
                    if (temp.get(0).equals("用户留言") || temp.get(0).equals("备注")) {
                        if (temp.size() > 1) {
                            orderVo.setRemark(orderVo.getRemark() + Stream.of(line.split("：", 2)).map(String::trim).collect(Collectors.toList()).get(1));
                        }
                    } else if (temp.get(0).equals("Units")) {
                        List<String> list = Stream.of(temp.get(1).split(",")).map(s -> s.split("\\(")[0].trim()).collect(Collectors.toList());
                        for (String s : list) {
                            List<String> t = Stream.of(s.split("x")).map(String::trim).collect(Collectors.toList());
                            if (t.size() == 2) {
                                peopleMap.put(t.get(1).replace("Person", "Adult"), Integer.parseInt(t.get(0)));
                            }
                        }
                    } else if (map.containsKey(temp.get(0))) {
                        Method method = propertyDescriptorMap.get(map.get(temp.get(0))).getWriteMethod();
                        if (temp.size() == 1) {
                            i++;
                            if (i < strings.size()) {
                                method.invoke(orderVo, strings.get(i));
                            }
                        } else {
                            String s = temp.get(1);
                            int index = s.indexOf(")");
                            if (index == -1) {
                                method.invoke(orderVo, temp.get(1));
                            } else {
                                method.invoke(orderVo, s.substring(index + 1).trim());
                            }
                        }
                    } else if (temp.get(0).equals("SKU名称") || temp.get(0).equals("Package booked") || temp.get(0).equals("Activity booked")) {
                        if (temp.size() > 1) {
                            skuName = temp.get(1);
                        }
                    }
                    if (temp.size() > 2) {
                        List<String> temp1 = Stream.of(line.split("：|\\s+")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
                        for (int j = 0; j < temp1.size(); j += 2) {
                            try {
                                switch (temp1.get(j)) {
                                    case "成人":
                                        peopleMap.put("Adult", Integer.parseInt(temp1.get(j + 1)));
                                        break;
                                    case "儿童":
                                        peopleMap.put("Child", Integer.parseInt(temp1.get(j + 1)));
                                        break;
                                    case "婴儿":
                                        peopleMap.put("Infant", Integer.parseInt(temp1.get(j + 1)));
                                        break;
                                    default:
                                        break;
                                }
                            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                                logger.info("ignore exception int auto import", e);
                            }
                        }
                    }
                }
            }
            if (!hasTravelInfo) {
                if (nameList.size() != 0 || weightList.size() != 0) {
                    int k = 0, w = 0;
                    for (Map.Entry<String, Integer> entry : peopleMap.entrySet()) {
                        for (int i = 0; i < entry.getValue(); i++) {
                            String s = entry.getKey();
                            OrderTicketUserVo orderTicketUserVo = new OrderTicketUserVo();
                            if (k < nameList.size()) {
                                orderTicketUserVo.setName(nameList.get(k));
                                k++;
                            } else {
                                orderTicketUserVo.setName(s + (i + 1));
                            }
                            if (w < weightList.size()) {
                                orderTicketUserVo.setWeight(weightList.get(w));
                                w++;
                            }
                            users.put(s, orderTicketUserVo);
                        }
                    }
                } else {
                    for (Map.Entry<String, Integer> entry : peopleMap.entrySet()) {
                        for (int i = 0; i < entry.getValue(); i++) {
                            String s = entry.getKey();
                            OrderTicketUserVo orderTicketUserVo = new OrderTicketUserVo();
                            orderTicketUserVo.setName(s + (i + 1));
                            users.put(s, orderTicketUserVo);
                        }
                    }
                }
            }
            for (Map.Entry<String, Collection<OrderTicketUserVo>> entry : users.asMap().entrySet()) {
                String type = entry.getKey();
                for (OrderTicketUserVo orderTicketUserVo : entry.getValue()) {
                    List<OrderTicketVo> orderTicketVos = parse(skuTickets, orderVo, scan, gatheringPlace);
                    OrderTicketVo vo = null;
                    int max = -1;
                    if (orderTicketVos.size() == 0) {
                        break;
                    } else if (skuName == null || orderTicketVos.size() == 1) {
                        vo = orderTicketVos.get(0);
                    } else {
                        List<OrderTicketVo> orderTicketVoList = orderTicketVos.stream()
                                .filter(orderTicketVo -> orderTicketVo.getSkuTicket().contains(type)).collect(Collectors.toList());
                        switch (orderTicketVoList.size()) {
                            case 1:
                                vo = orderTicketVos.get(0);
                                break;
                            case 0:
                                orderTicketVoList = orderTicketVos;
                            default:
                                for (OrderTicketVo orderTicketVo : orderTicketVoList) {
                                    String ticketName = orderTicketVo.getSkuTicket().replace(type, "").trim().toLowerCase();
                                    int count = 0;
                                    for (String temp : ticketName.split("\\s+|\\+")) {
                                        if (skuName.toLowerCase().contains(temp)) {
                                            count++;
                                        }
                                    }
                                    if (count > max) {
                                        max = count;
                                        vo = orderTicketVo;
                                    }
                                }
                        }
                    }
                    if (vo != null) {
                        if (orderTicketUserVo.getAge() == 0) {
                            orderTicketUserVo.setAge(Integer.parseInt(vo.getAgeConstraint().split("-")[0]));
                        }
                        if (orderTicketUserVo.getWeight() == 0) {
                            orderTicketUserVo.setWeight(Integer.parseInt(vo.getWeightConstraint().split("-")[0]));
                        }
                        vo.setOrderTicketUsers(Lists.newArrayList(orderTicketUserVo));
                        ticketVos.add(vo);
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("auto import error", e);
        }
        orderVo.setOrderTickets(ticketVos);
        orderVo.setPrimaryContact(orderVo.getPrimaryContact().replace("-", " "));
        return orderVo;
    }

    private List<OrderTicketVo> parse(List<SkuTicket> skuTickets, OrderVo orderVo, Scan scan, String gatheringPlace) {
        List<OrderTicketVo> orderTicketVos = Lists.newArrayList();
        for (SkuTicket skuTicket : skuTickets) {
            int discount = discountRateService.getDiscountByAgent(scan.getAgentId(), scan.getSkuId());
            List<SkuTicketPrice> skuTicketPrices = skuTicketPriceMapper.findAvailableBySkuTicketIdAndDate(skuTicket.getSkuId(), skuTicket.getId(), DateUtils.parseDate(orderVo.getTicketDate()), new RowBounds());
            for (SkuTicketPrice skuTicketPrice : skuTicketPrices) {
                if (skuTicketPrice.getTime().equals(scan.getTime())) {
                    OrderTicketVo orderTicketVo = new OrderTicketVo();
                    orderTicketVo.setSkuTicketId(skuTicket.getId());
                    orderTicketVo.setSkuTicket(skuTicket.getName());
                    orderTicketVo.setAgeConstraint(skuTicket.getAgeConstraint());
                    orderTicketVo.setCountConstraint(skuTicket.getCountConstraint());
                    orderTicketVo.setWeightConstraint(skuTicket.getWeightConstraint());
                    orderTicketVo.setPriceDescription(skuTicket.getDescription());
                    orderTicketVo.setGatheringPlace(gatheringPlace);
                    orderTicketVo.setTicketDate(orderVo.getTicketDate());
                    orderTicketVo.setTicketPriceId(skuTicketPrice.getId());
                    orderTicketVo.setTicketTime(scan.getTime());
                    orderTicketVo.setPrice(pricingService.calculate(skuTicketPrice, discount));
                    orderTicketVo.setSalePrice(skuTicketPrice.getSalePrice());
                    orderTicketVos.add(orderTicketVo);
                }
            }
        }
        return orderTicketVos;
    }

}
