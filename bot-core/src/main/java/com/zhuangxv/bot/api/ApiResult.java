package com.zhuangxv.bot.api;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ApiResult {

    private String status;

    @JSONField(name = "retcode")
    private int retCode;

    private JSONObject data;

    private String echo;

}
