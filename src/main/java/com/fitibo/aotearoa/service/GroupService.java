package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.constants.CommonConstants;
import com.fitibo.aotearoa.constants.GroupType;
import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.exception.InvalidParamException;
import com.fitibo.aotearoa.exception.ResourceNotFoundException;
import com.fitibo.aotearoa.mapper.*;
import com.fitibo.aotearoa.model.*;
import com.fitibo.aotearoa.util.DateUtils;
import com.fitibo.aotearoa.util.GuidGenerator;
import com.fitibo.aotearoa.util.ObjectParser;
import com.fitibo.aotearoa.vo.GroupMemberVo;
import com.fitibo.aotearoa.vo.GroupVo;
import com.fitibo.aotearoa.vo.OrderVo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("groupService")
public class GroupService {
    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Autowired
    private GroupOrderMapper groupOrderMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderTicketMapper orderTicketMapper;

    @Autowired
    private SkuMapper skuMapper;

    /**
     * 寻找符合条件的group订单（暂时只有multisaver支持）
     *
     * @param groupType
     * @return
     */
    public List<GroupVo> classifyNewOrders(GroupType groupType) {
        List<Order> orders = orderMapper.findUnclassifyOrders();
        if (groupType == GroupType.MULTI_SAVER) {
            orders = orders.stream().filter(order -> SkuService.getVendorId(order.getSkuId()) == 2).collect(Collectors.toList());
        }
        return Lists.newArrayList(parse(orders).collect(Collectors.groupingBy(o -> getClassifyOrderString(groupType, o)))
                .values()).stream().filter(list -> list.size() > 1).map(orderVos -> {
            GroupVo groupVo = new GroupVo();
            groupVo.setType(groupType.getValue());
            groupVo.setRemark(StringUtils.join(orderVos.stream().map(OrderVo::getId).collect(Collectors.toList()), "-"));
            groupVo.setOrderVos(orderVos);
            return groupVo;
        }).collect(Collectors.toList());
    }

    /**
     * 通过orderId来创建group（用于扫描之后快速创建group）
     *
     * @param orderIds
     * @param groupType
     * @return
     */
    public GroupVo createGroup(List<Integer> orderIds, GroupType groupType) {
        List<Order> orders = orderMapper.findByIds(orderIds);
        return createGroup(groupType, parse(orders).collect(Collectors.toList()));
    }

    /**
     * 通过order来创建group（用于扫描之后快速创建group）
     *
     * @param orderVos
     * @param groupType
     * @return
     */
    public GroupVo createGroup(GroupType groupType, List<OrderVo> orderVos) {
        GroupVo groupVo = new GroupVo();
        OrderVo orderVo = orderVos.get(0);
        groupVo.setPrimaryContact(orderVo.getPrimaryContact());
        groupVo.setPrimaryContactEmail(orderVo.getPrimaryContactEmail());
        groupVo.setPrimaryContactPhone(orderVo.getPrimaryContactPhone());
        groupVo.setPrimaryContactWechat(orderVo.getPrimaryContactWechat());
        groupVo.setGroupMemberVos(Lists.newArrayList());
        groupVo.setType(groupType.getValue());
        groupVo.setRemark("");
        groupVo.setAgentId(orderVo.getAgentId());
        groupVo = createGroup(groupVo);
        for (OrderVo orderVo1 : orderVos) {
            addOrderToGroup(groupVo.getId(), orderVo1);
        }
        return groupVo;
    }

    /**
     * 普通直接创建group，对应create_group的提交之后的操作
     *
     * @param groupVo
     * @return
     */
    public GroupVo createGroup(GroupVo groupVo) {
        groupVo.setTotalCostPrice(BigDecimal.ZERO);
        groupVo.setTotalPrice(BigDecimal.ZERO);
        groupVo.setUuid(GuidGenerator.generate(14));
        Date now = new Date();
        groupVo.setCreateTime(now);
        groupVo.setUpdateTime(now);
        groupVo.setStatus(OrderStatus.NEW.getValue());
        Group group = parse(groupVo);
        groupMapper.insert(group);
        groupVo.setId(group.getId());
        //小包团需要成员（用来快速加票）
        switch (GroupType.valueOf(groupVo.getType())) {
            case MULTI_SAVER:
            case NORMAL:
                break;
            case TEAM:
                if (groupVo.getGroupMemberVos().size() == 0) {
                    throw new InvalidParamException("member list is empty");
                }
                groupMemberMapper.batchCreate(groupVo.getGroupMemberVos()
                        .stream().map(groupMemberVo -> {
                            groupMemberVo.setGroupId(group.getId());
                            return parse(groupMemberVo);
                        }).collect(Collectors.toList()));
        }
        return groupVo;
    }

    /**
     * update group信息
     *
     * @param groupVo
     * @return
     */
    public GroupVo updateGroup(GroupVo groupVo) {
        Group group = parseUpdate(groupVo);
        groupMapper.update(group);
        group = groupMapper.findById(group.getId());
        if (group.getType() == GroupType.TEAM.getValue() && groupVo.getGroupMemberVos().size() == 0) {
            throw new InvalidParamException("member list is empty");
        }
        //取出旧成员
        Map<Integer, GroupMember> orderGroupMembers = groupMemberMapper.findByGroupId(group.getId())
                .stream().collect(Collectors.toMap(GroupMember::getId, Function.identity()));
        List<GroupMember> groupMembers = Lists.transform(groupVo.getGroupMemberVos(), this::parse);
        //新建
        List<GroupMember> newGroupMembers = groupMembers.stream().filter(groupMember -> groupMember.getId() == 0).collect(Collectors.toList());
        if (newGroupMembers.size() > 0) {
            groupMemberMapper.batchCreate(newGroupMembers);
        }
        //更新
        groupMembers.stream().filter(groupMember -> groupMember.getId() > 0).forEach(groupMember -> {
            if (orderGroupMembers.containsKey(groupMember.getId())) {
                groupMemberMapper.update(orderGroupMembers.get(groupMember.getId()));
                orderGroupMembers.remove(groupMember.getId());
            }
        });
        //删除
        orderGroupMembers.values().forEach(groupMember -> groupMemberMapper.delete(groupMember));
        //resetGroupTicketDate(group);
        return groupVo;
    }

    /**
     * 分页获取group（暂时没有条件搜索）
     *
     * @param pageSize
     * @param pageNumber
     * @return
     */
    public List<GroupVo> getGroupsByPage(int pageSize, int pageNumber) {
        return groupMapper.findByPage(new RowBounds(pageNumber * pageSize, pageSize)).stream().map(this::parse).collect(Collectors.toList());
    }

    /**
     * 向group中添加订单
     *
     * @param groupId
     * @param uuid
     * @return
     */
    public boolean addOrderToGroup(int groupId, String uuid) {
        Order order = orderMapper.findByUuid(uuid);
        if (order == null) {
            throw new ResourceNotFoundException("invalid order uuid:" + uuid);
        }
        return addOrderToGroup(groupId, parse(Lists.newArrayList(order)).collect(Collectors.toList()).get(0));
    }

    /**
     * 向group中添加订单
     *
     * @param groupId
     * @param orderVo
     * @return
     */
    public boolean addOrderToGroup(int groupId, OrderVo orderVo) {
        Group group = groupMapper.findById(groupId);
        if (group == null) {
            throw new ResourceNotFoundException("invalid group id:" + groupId);
        }
        if (groupOrderMapper.findByOrderId(orderVo.getId()).size() != 0) {
            throw new InvalidParamException("order already belong to group");
        }
        if (orderVo.getOrderTickets() == null || orderVo.getOrderTickets().size() == 0) {
            orderVo.setOrderTickets(Lists.transform(orderTicketMapper.findByOrderId(orderVo.getId()), ObjectParser::parse));
            orderVo.setTicketDate(orderVo.getOrderTickets().get(0).getTicketDate());
        }
        List<OrderVo> orders = parse(orderMapper.findByGroupId(groupId)).collect(Collectors.toList());
        //检查订单是否符合条件
        switch (GroupType.valueOf(group.getType())) {
            case TOGETHER:
                if (!checkTogetherOrder(orderVo, orders)) {
                    return false;
                }
                break;
            case MULTI_SAVER:
                if (!checkMultiSaverOrder(orderVo, orders)) {
                    return false;
                }
                break;
            case TEAM://小包团直接break
                break;
        }
        int result = groupOrderMapper.insert(new GroupOrder(groupId, orderVo.getId()));
        orderVo.setGroupType(group.getType());
        //更新order的groupType状态
        orderMapper.updateGroupType(orderVo.getId(), group.getType());
        resetGroupTicketDate(group);
        return result == 1;
    }

    /**
     * 删除订单
     *
     * @param groupOrder
     * @return
     */
    public boolean deleteOrderFromGroup(GroupOrder groupOrder) {
        Group group = groupMapper.findById(groupOrder.getGroupId());
        if (group == null) {
            throw new ResourceNotFoundException("invalid group id:" + groupOrder.getGroupId());
        }
        Order order = orderMapper.findById(groupOrder.getOrderId());
        if (order == null) {
            throw new ResourceNotFoundException("invalid order id:" + groupOrder.getOrderId());
        }
        if (groupOrderMapper.find(groupOrder).size() == 0) {
            throw new InvalidParamException("order does not belong to group");
        }
        int result = groupOrderMapper.deleteByPrimaryKey(groupOrder);
        orderMapper.updateGroupType(group.getId(), GroupType.NORMAL.getValue());
        resetGroupTicketDate(group);
        return result == 1;
    }

    public int getGroupIdByOrderId(int orderId) {
        List<GroupOrder> groupOrders = groupOrderMapper.findByOrderId(orderId);
        if (groupOrders.size() == 0) {
            return 0;
        } else {
            return groupOrders.get(0).getGroupId();
        }
    }

    private boolean checkMultiSaverOrder(OrderVo order1, OrderVo order2) {
        return order1.getSkuId() != order2.getSkuId()
                && checkPrimaryContact(order1, order2)
                && SkuService.isSameVendor(order1.getSkuId(), order2.getSkuId()) && order1.getOrderTickets().size() == order2.getOrderTickets().size();
    }

    private boolean checkTogetherOrder(OrderVo order1, OrderVo order2) {
        return order1.getSkuId() == order2.getSkuId()
                && order1.getAgentId() == order2.getAgentId()
                && order1.getTicketDate().equals(order2.getTicketDate())
                && checkPrimaryContact(order1, order2);
    }

    /**
     * 获取分类比对所需要的字符串
     *
     * @param groupType
     * @param orderVo
     * @return
     */
    private String getClassifyOrderString(GroupType groupType, OrderVo orderVo) {
        switch (groupType) {
            case MULTI_SAVER:
                return getSimplePrimaryContact(orderVo) + CommonConstants.SEPARATOR + SkuService.getVendorId(orderVo.getSkuId()) + orderVo.getOrderTickets().size();
            default:
                throw new InvalidParamException(groupType.getDesc() + " is not support now");
        }
    }

    /**
     * 为分类做比对的联系人信息
     *
     * @param orderVo
     * @return
     */
    private String getSimplePrimaryContact(OrderVo orderVo) {
        String phone = orderVo.getPrimaryContactPhone().replaceAll("\\s+|\\+", "");
        if (phone.startsWith("86")) {
            phone = phone.substring(2);
        }
        return orderVo.getPrimaryContact().replaceAll("\\s+", "").toLowerCase() + CommonConstants.SEPARATOR + phone;
    }

    private boolean checkPrimaryContact(OrderVo order1, OrderVo order2) {
        String phone1 = order1.getPrimaryContactPhone().replaceAll("\\s+|\\+", "");
        if (phone1.startsWith("86")) {
            phone1 = phone1.substring(2);
        }
        String phone2 = order2.getPrimaryContactPhone().replaceAll("\\s+|\\+", "");
        if (phone2.startsWith("86")) {
            phone2 = phone2.substring(2);
        }
        return order1.getPrimaryContact().replaceAll("\\s+", "")
                .equalsIgnoreCase(order2.getPrimaryContact().replaceAll("\\s+", ""))
                || phone1.equalsIgnoreCase(phone2);
    }

    private boolean checkMultiSaverOrder(OrderVo order, List<OrderVo> orderVos) {
        for (OrderVo orderVo : orderVos) {
            if (!checkMultiSaverOrder(order, orderVo)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkTogetherOrder(OrderVo order, List<OrderVo> orderVos) {
        for (OrderVo orderVo : orderVos) {
            if (!checkTogetherOrder(order, orderVo)) {
                return false;
            }
        }
        return true;
    }

    public GroupVo getGroupById(int id) {
        Group group = groupMapper.findById(id);
        GroupVo groupVo = parse(group);
        groupVo.setGroupMemberVos(groupMemberMapper.findByGroupId(group.getId()).stream().map(this::parse).collect(Collectors.toList()));
        groupVo.setOrderVos(parse(orderMapper.findByGroupId(group.getId())).collect(Collectors.toList()));
        return groupVo;
    }

    /**
     * update group status的函数
     *
     * @param groupId
     * @param toStatus
     * @return
     */
    public boolean updateGroupStatus(int groupId, int toStatus) {
        int result = groupMapper.updateGroupStatus(groupId, toStatus);
        return result == 1;
    }

    /**
     * 重新计算group起始时间
     *
     * @param group
     * @return
     */
    private Group resetGroupTicketDate(Group group) {
        List<Order> orders = orderMapper.findByGroupId(group.getId());
        if (orders.size() == 0) {
            throw new ResourceNotFoundException("group " + group.getId() + " has no orders");
        }
        List<Date> orderTicketDates = orderTicketMapper.findOrderTicketDate(orders.stream().map(Order::getId).collect(Collectors.toList()))
                .stream().map(OrderTicket::getTicketDate).sorted().collect(Collectors.toList());
        group.setTicketDateStart(orderTicketDates.get(0));
        group.setTicketDateEnd(orderTicketDates.get(orderTicketDates.size() - 1));
        groupMapper.updateTicketDate(group);
        return group;
    }

    private Group parse(GroupVo groupVo) {
        Group result = new Group();
        BeanUtils.copyProperties(groupVo, result);
        return result;
    }

    private Group parseUpdate(GroupVo groupVo) {
        Group result = new Group();
        BeanUtils.copyProperties(groupVo, result);
        return result;
    }

    private GroupMember parse(GroupMemberVo groupMemberVo) {
        GroupMember result = new GroupMember();
        BeanUtils.copyProperties(groupMemberVo, result);
        return result;
    }

    private GroupVo parse(Group group) {
        GroupVo result = new GroupVo();
        BeanUtils.copyProperties(group, result);
        result.setTicketDateStart(DateUtils.formatDate(group.getTicketDateStart()));
        result.setTicketDateEnd(DateUtils.formatDate(group.getTicketDateEnd()));
        return result;
    }

    private GroupMemberVo parse(GroupMember groupMember) {
        GroupMemberVo result = new GroupMemberVo();
        BeanUtils.copyProperties(groupMember, result);
        return result;
    }

    private OrderVo parse(Order order) {
        OrderVo orderVo = ObjectParser.parse(order);
        orderVo.setOrderTickets(Lists.transform(orderTicketMapper.findByOrderId(orderVo.getId()), ObjectParser::parse));
        orderVo.setTicketDate(orderVo.getOrderTickets().get(0).getTicketDate());
        return orderVo;
    }

    private Stream<OrderVo> parse(List<Order> orders) {
        return orders.stream().map(this::parse);
    }
}
