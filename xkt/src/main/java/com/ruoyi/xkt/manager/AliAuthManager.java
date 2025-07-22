package com.ruoyi.xkt.manager;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liangyq
 * @date 2025-07-22
 */
@Slf4j
@Component
public class AliAuthManager {

    private static final String CAPTCHA_URL = "https://captcha.alicaptcha.com/validate?captcha_id=%s";

    @Value("${aliauth.captcha.appId:}")
    private String captchaId;
    @Value("${aliauth.captcha.appKey:}")
    private String captchaKey;

    public boolean validate(String lotNumber, String captchaOutput, String passToken, String genTime) {
        if (StrUtil.isEmpty(lotNumber)
                || StrUtil.isEmpty(captchaOutput)
                || StrUtil.isEmpty(passToken)
                || StrUtil.isEmpty(genTime)) {
            log.warn("图形认证参数异常: {}, {}, {}, {}", lotNumber, captchaOutput, passToken, genTime);
            return false;
        }
        try {
            // 生成签名使用标准的hmac算法，使用用户当前完成验证的流水号lot_number作为原始消息message，使用客户验证私钥作为key
            // 采用sha256散列算法将message和key进行单向散列生成最终的签名
            String signToken = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, captchaKey).hmacHex(lotNumber);
            // 上传校验参数到验证服务二次验证接口, 校验用户验证状态
            // captcha_id 参数建议放在 url 后面, 方便请求异常时可以在日志中根据id快速定位到异常请求
            String url = String.format(CAPTCHA_URL, captchaId);
            HttpRequest httpRequest = HttpUtil.createPost(url);
            httpRequest.header("Content-Type", "application/x-www-form-urlencoded");
            httpRequest.form("lot_number", lotNumber);
            httpRequest.form("captcha_output", captchaOutput);
            httpRequest.form("pass_token", passToken);
            httpRequest.form("gen_time", genTime);
            httpRequest.form("sign_token", signToken);
            String resBody = httpRequest.execute().body();
            log.info("图形认证结果: {}", resBody);
            return "success".equals(JSON.parseObject(resBody).getString("result"));
        } catch (Exception e) {
            log.error("图形认证失败", e);
        }
        return false;
    }
}
