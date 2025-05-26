package com.ruoyi.framework.sms.ali;

import com.alibaba.fastjson2.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.ruoyi.framework.sms.ali.entity.AliSmsResponse;
import com.ruoyi.framework.sms.ali.entity.Result;
import com.ruoyi.framework.sms.ali.entity.TemplateInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 阿里短信服务
 * 官方文档地址 https://help.aliyun.com/document_detail/121206.html
 *
 * @author liangyq
 */
@Slf4j
public class AliSmsServer {

    private IAcsClient client;

    private String accessKeyId;

    public AliSmsServer(String accessKeyId, String accessKeySecret, String regionId) {
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        client = new DefaultAcsClient(profile);
        this.accessKeyId = accessKeyId;
    }

    /**
     * 发送短信
     *
     * @param signName       短信签名
     * @param phoneNumber    手机号
     * @param templateCode   模版编号
     * @param templateParams 模版参数（json）
     */
    public AliSmsResponse sendSms(String signName, String phoneNumber, String templateCode, String templateParams) {
        try {
            CommonRequest request = newCommonRequest("SendSms");
            request.putQueryParameter("PhoneNumbers", phoneNumber);
            request.putQueryParameter("SignName", signName);
            request.putQueryParameter("TemplateCode", templateCode);
            request.putQueryParameter("TemplateParam", templateParams);
            String params = JSON.toJSONString(request);
            CommonResponse response = client.getCommonResponse(request);
            return new AliSmsResponse(params, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量发短信 最多100个手机号
     *
     * @param signNameList      短信签名
     * @param phoneNumberList   手机号
     * @param templateCode      模版编号
     * @param templateParamList 模版参数 json
     */
    public Result sendBatchSms(List<String> signNameList, List<String> phoneNumberList, String templateCode, List<String> templateParamList) {
        try {
            CommonRequest request = newCommonRequest("SendBatchSms");
            request.putQueryParameter("PhoneNumberJson", JSON.toJSONString(phoneNumberList));
            request.putQueryParameter("SignNameJson", JSON.toJSONString(signNameList));
            request.putQueryParameter("TemplateCode", templateCode);
            request.putQueryParameter("TemplateParamJson", JSON.toJSONString(templateParamList));

            CommonResponse response = client.getCommonResponse(request);
            return handlerResponse(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TemplateInfo querySmsTemplate(String templateCode) {
        CommonRequest request = newCommonRequest("QuerySmsTemplate");
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("AccessKeyId", this.accessKeyId);
        try {
            CommonResponse response = client.getCommonResponse(request);
            if (response.getHttpResponse().isSuccess()) {
                return JSON.parseObject(response.getData(), TemplateInfo.class);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private CommonRequest newCommonRequest(String action) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction(action);
        return request;
    }

    private Result handlerResponse(CommonResponse response) {
        if (!response.getHttpResponse().isSuccess()) {
            return new Result("异常");
        }
        log.info(response.getData());
        return JSON.parseObject(response.getData(), Result.class);
    }

}
