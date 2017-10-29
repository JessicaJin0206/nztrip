package com.fitibo.aotearoa.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupOrder {
    private int groupId;

    private int orderId;

    public GroupOrder(int groupId, int orderId) {
        this.groupId = groupId;
        this.orderId = orderId;
    }
}