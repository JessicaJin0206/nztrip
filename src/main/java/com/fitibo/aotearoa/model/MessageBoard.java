package com.fitibo.aotearoa.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageBoard extends ModelObject {
    private int adminId;
    private Date createTime;
    private String content;
}
