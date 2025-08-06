package com.ruoyi.framework.ocr;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.ocr_api20210707.Client;
import com.aliyun.ocr_api20210707.models.RecognizeBusinessLicenseRequest;
import com.aliyun.ocr_api20210707.models.RecognizeBusinessLicenseResponse;
import com.aliyun.ocr_api20210707.models.RecognizeIdcardRequest;
import com.aliyun.ocr_api20210707.models.RecognizeIdcardResponse;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.framework.ocr.ali.BusinessLicenseOcrResponse;
import com.ruoyi.framework.ocr.ali.IdCardOcrResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liangyq
 * @date 2025-06-29
 */
@Slf4j
@Component
public class OcrClientWrapper implements InitializingBean {

    @Value("${ocr.accessKeyId:}")
    private String aliyunOAccessKeyId;

    @Value("${ocr.accessKeySecret:}")
    private String aliyunOAccessKeySecret;

    @Value("${ocr.endpoint:}")
    private String aliyunEndpoint;

    private Client aliyunOcrClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(aliyunOAccessKeyId)
                .setAccessKeySecret(aliyunOAccessKeySecret);
        config.endpoint = aliyunEndpoint;
        aliyunOcrClient = new Client(config);
    }

    /**
     * 身份证识别
     *
     * @param url
     * @return
     */
    public IdCard recognizeIdCard(String url) {
        RecognizeIdcardRequest request = new RecognizeIdcardRequest();
        try {
            RecognizeIdcardResponse response = aliyunOcrClient.recognizeIdcard(request.setUrl(url));
            String dataStr = response.getBody().getData();
            log.info("阿里云身份证OCR结果：{}", dataStr);
            IdCardOcrResponse ocrResp = JSONObject.parseObject(dataStr, IdCardOcrResponse.class);
            IdCard idCard = new IdCard();
            if (ocrResp != null && ocrResp.getData() != null) {
                if (ocrResp.getData().getFace() != null
                        && ocrResp.getData().getFace().getData() != null) {
                    BeanUtil.copyProperties(ocrResp.getData().getFace().getData(), idCard);
                }
                if (ocrResp.getData().getBack() != null
                        && ocrResp.getData().getBack().getData() != null) {
                    BeanUtil.copyProperties(ocrResp.getData().getBack().getData(), idCard);
                }
            }
            return idCard;
        } catch (Exception e) {
            log.error("阿里云身份证OCR异常", e);
        }
        throw new ServiceException("OCR识别失败，请重新上传图片 或 联系专属业务经理：刘江 13548158817");
    }

    public BusinessLicense recognizeBusinessLicense(String url) {
        RecognizeBusinessLicenseRequest request = new RecognizeBusinessLicenseRequest();
        try {
            RecognizeBusinessLicenseResponse response = aliyunOcrClient.recognizeBusinessLicense(request.setUrl(url));
            String dataStr = response.getBody().getData();
            log.info("阿里云营业执照OCR结果：{}", dataStr);
            BusinessLicenseOcrResponse ocrResp = JSONObject.parseObject(dataStr, BusinessLicenseOcrResponse.class);
            if (ocrResp != null && ocrResp.getData() != null) {
                return ocrResp.getData();
            }
            return new BusinessLicense();
        } catch (Exception e) {
            log.error("阿里云营业执照OCR异常", e);
        }
        throw new ServiceException("OCR识别失败，请重新上传图片 或 联系专属业务经理：刘江 13548158817");
    }
}
