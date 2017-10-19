package com.fitibo.aotearoa.vo;

import com.fitibo.aotearoa.model.ModelObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class MessageBoardVo {
    private int adminId;
    private String adminName;
    private String createTime;
    private String content;
}
