package com.zhuangxv.bot.api;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
@Data
public class ApiResult {

    private String status;

    @JSONField(name = "retcode")
    private int retCode;

    private Object data;

    private String echo;

}
