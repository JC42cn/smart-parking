package com.smart.smartparking.controller.domain;

import lombok.Data;

//配置类
@Data
public class AliPay {
    private String subject;
    private String traceNo;
    private String totalAmount;
}
