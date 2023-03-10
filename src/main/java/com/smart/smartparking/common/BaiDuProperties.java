package com.smart.smartparking.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 百度智能AI
 */
@Data
@ConfigurationProperties(prefix = "bai-du")
public class BaiDuProperties {

    private String appId;
    private String apiKey;
    private String accessKeySecret;

}