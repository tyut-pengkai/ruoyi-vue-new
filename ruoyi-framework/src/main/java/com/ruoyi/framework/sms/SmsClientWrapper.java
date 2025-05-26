package com.ruoyi.framework.sms;

import com.ruoyi.framework.sms.ali.AliSmsServer;
import com.ruoyi.framework.sms.ali.entity.AliSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsClientWrapper {

    @Autowired
    private AliSmsServer aliSmsServer;

    @Value("${sms.send:true}")
    private Boolean doSend;

    public boolean sendSms(String signName, String phoneNumber, String templateCode, String templateParams) {
        boolean sendResult;
        if (doSend) {
            AliSmsResponse response = aliSmsServer.sendSms(signName, phoneNumber, templateCode, templateParams);
            sendResult = response.success();
        }
        // 测试的时候不发短信
        else {
            sendResult = true;
        }
        log.info("发送短信{},{},{},{}:{},doSend:{}", signName, phoneNumber, templateCode, templateParams, sendResult, doSend);
        return sendResult;
    }

}
