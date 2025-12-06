package com.ruoyi.xkt.thirdpart.lfv2;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liangyq
 * @date 2025-11-20
 */
@Slf4j
@Component
public class Lfv2Client {

    private static final String ENT4URL = "https://swent4.market.alicloudapi.com/verify/enterprise4";

    private static final String PHONE3URL = "https://swphone3.market.alicloudapi.com/verify/operator3_precision";

    @Value("${lfv2.auth:APPCODE 4dd403cda2694a45a1124b09637612b6}")
    private String authCode;

    @Value("${lfv2.enable}")
    private Boolean enable;

    /**
     * 检查企业四要素
     *
     * @param creditCode      社会统一信用代码
     * @param enterpriseName  企业名
     * @param legalPersonName 法人名称
     * @param legalIdNumber   法人身份证号
     * @return
     */
    public boolean checkEnterprise(String creditCode, String enterpriseName, String legalPersonName,
                                   String legalIdNumber) {
        if (!enable) {
            log.warn("未开启企业四要素检查，直接通过");
            return true;
        }
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", authCode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> body = new HashMap<>();
        body.put("credit_code", creditCode);
        body.put("enterprise_name", enterpriseName);
        body.put("legal_person_name", legalPersonName);
        body.put("legal_id_number", legalIdNumber);
        try {
            String response = httpPost(ENT4URL, headers, body);
            log.info("企业四要素接口: {}, {}", body, response);
            JSONObject result = JSONUtil.parseObj(response).getJSONObject("result");
            Integer r1 = result.getInt("enterprise_name_match");
            Integer r2 = result.getInt("credit_code_match");
            Integer r3 = result.getInt("legal_person_name_match");
            Integer r4 = result.getInt("legal_id_number_match");
            return Integer.valueOf(1).equals(r1)
                    && Integer.valueOf(1).equals(r2)
                    && Integer.valueOf(1).equals(r3)
                    && Integer.valueOf(1).equals(r4);
        } catch (Exception e) {
            log.error("企业四要素接口异常", e);
        }
        return false;
    }

    /**
     * 检查手机号三要素
     *
     * @param phoneNumber 手机号
     * @param personName  姓名
     * @param idNumber    身份证号
     * @return
     */
    public boolean checkPhone(String phoneNumber, String personName, String idNumber) {
        if (!enable) {
            log.warn("未开启手机号三要素校验，直接通过");
            return true;
        }
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", authCode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> body = new HashMap<>();
        body.put("name", personName);
        body.put("id_number", idNumber);
        body.put("phone_number", phoneNumber);
        try {
            String response = httpPost(PHONE3URL, headers, body);
            log.info("手机号三要素接口: {}, {}", body, response);
            Integer checkResult = JSONUtil.parseObj(response).getByPath("result.checkresult", Integer.class);
            return Integer.valueOf(1).equals(checkResult);
        } catch (Exception e) {
            log.error("手机号三要素接口异常", e);
        }
        return false;
    }

    private String httpPost(String url, Map<String, String> headers, Map<String, String> body) {
        HttpRequest httpRequest = HttpUtil.createPost(url);
        if (MapUtil.isNotEmpty(headers)) {
            httpRequest.addHeaders(headers);
        }
        if (MapUtil.isNotEmpty(body)) {
            httpRequest.formStr(body);
        }
        return httpRequest.execute().body();
    }
}
