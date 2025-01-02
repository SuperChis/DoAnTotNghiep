package org.example.ezyshop.config.vnpay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "vnpay")
@Data
public class VNPayConfig {
    private String merchantId;
    private String secretKey;
    private String url;
    private String returnUrl;
    private String apiUrl;
}
