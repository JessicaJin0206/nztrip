package com.fitibo.aotearoa.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class MessageBoard extends ModelObject {
    private int adminId;
    private Date createTime;
    private String content;
}
