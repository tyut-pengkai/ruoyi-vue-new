package com.ruoyi.framework.sms;

import com.ruoyi.framework.sms.ali.AliSmsServer;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class SmsConfiguration {

    @Value("${sms.accessKeyId:}")
    private String accessKeyId;

    @Value("${sms.accessKeySecret:}")
    private String accessKeySecret;

    @Value("${sms.regionId:}")
    private String regionId;

    @Bean
    public AliSmsServer aliSmsServer() {
        return new AliSmsServer(accessKeyId, accessKeySecret, regionId);
    }
}
