package com.fitibo.aotearoa.util;

import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.model.Status;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xiaozou on 8/16/16.
 */
public class StatusUtils {

    private StatusUtils() {
    }

    private static List<Status> statusList = new LinkedList<>();

    static {
        statusList.add(new Status(OrderStatus.NEW, "新建"));
        statusList.add(new Status(OrderStatus.PENDING, "待确认"));
        statusList.add(new Status(OrderStatus.FULL, "已满"));
        statusList.add(new Status(OrderStatus.CONFIRMED, "已确认"));
        statusList.add(new Status(OrderStatus.MODIFYING, "修改中"));
        statusList.add(new Status(OrderStatus.CANCELLED, "已取消"));
        statusList.add(new Status(OrderStatus.CLOSED, "已关闭"));
    }

    public static List<Status> getStatusList() {
        return statusList;
    }

    public static boolean containStatus(int status) {
        for (Status statusObject : statusList) {
            if (statusObject.getId() == status) {
                return true;
            }
        }
        return false;
    }
}
