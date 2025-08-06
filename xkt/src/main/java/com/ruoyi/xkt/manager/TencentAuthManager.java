package com.ruoyi.xkt.manager;

import com.ruoyi.common.utils.ip.IpUtils;
import com.tencentcloudapi.captcha.v20190722.CaptchaClient;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultRequest;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultResponse;
import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liangyq
 * @date 2025-08-06
 */
@Slf4j
@Component
public class TencentAuthManager implements InitializingBean {

    private static final Long SUCCESS_CODE = 1L;

    @Value("${tencent.secretId:}")
    private String secretId;
    @Value("${tencent.secretKey:}")
    private String secretKey;
    @Value("${tencent.captcha.captchaAppId:}")
    private Long captchaAppId;
    @Value("${tencent.captcha.appSecretKey:}")
    private String appSecretKey;

    private CaptchaClient client;

    @Override
    public void afterPropertiesSet() throws Exception {
        Credential cred = new Credential(secretId, secretKey);
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("captcha.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        client = new CaptchaClient(cred, "", clientProfile);
    }

    public boolean validate(String ticket, String randstr) {
        try {
            DescribeCaptchaResultRequest req = new DescribeCaptchaResultRequest();
            req.setCaptchaType(9L);
            req.setTicket(ticket);
            req.setRandstr(randstr);
            req.setUserIp(IpUtils.getIpAddr());
            req.setCaptchaAppId(captchaAppId);
            req.setAppSecretKey(appSecretKey);
            // 返回的resp是一个DescribeCaptchaResultResponse的实例，与请求对象对应
            DescribeCaptchaResultResponse resp = client.DescribeCaptchaResult(req);
            if (SUCCESS_CODE.equals(resp.getCaptchaCode())) {
                return true;
            }
            log.warn("滑动验证未通过: {}", AbstractModel.toJsonString(resp));
        } catch (Exception e) {
            log.error("滑动验证校验异常", e);
        }
        return false;
    }
}
