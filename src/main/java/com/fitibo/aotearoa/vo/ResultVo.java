package com.fitibo.aotearoa.vo;

import lombok.Data;

/**
 * Created by qianhao.zhou on 08/02/2017.
 */
@Data
public class ResultVo {

    public static final ResultVo SUCCESS = new ResultVo(0, "succeed");
    public static final ResultVo FAIL = new ResultVo(-1, "failed");

    private int code;
    private String msg;
    private Object data;

    public ResultVo(int code, String msg) {
        this(code, msg, null);
    }

    public ResultVo(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
