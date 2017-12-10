package com.fitibo.aotearoa.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class Transaction {

    private int id;
    private int type;
    private BigDecimal amount;
    private Currency currency;
    private Date createTime;
    private int refId;

}
