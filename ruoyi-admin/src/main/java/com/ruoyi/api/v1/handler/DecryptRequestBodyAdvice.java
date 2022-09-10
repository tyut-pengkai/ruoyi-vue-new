package com.ruoyi.api.v1.handler;

import com.ruoyi.api.v1.anno.Encrypt;
import com.ruoyi.api.v1.encrypt.IEncryptType;
import com.ruoyi.api.v1.encrypt.impl.EncryptAesCbcPKCS5P;
import com.ruoyi.api.v1.encrypt.impl.EncryptAesCbcZeroP;
import com.ruoyi.api.v1.encrypt.impl.EncryptBase64;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.enums.EncrypType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.framework.api.v1.utils.ValidUtils;
import com.ruoyi.system.service.ISysAppService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author ZwGu
 */
@Slf4j
@ControllerAdvice
@Component
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    @Resource
    private ISysAppService appService;
    @Resource
    private ValidUtils validUtils;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        //这里设置成false 它就不会再走这个类了
        if (methodParameter.getMethod().isAnnotationPresent(Encrypt.class)) {
            //获取注解配置的包含和去除字段
            Encrypt serializedField = methodParameter.getMethodAnnotation(Encrypt.class);
            return serializedField.in();
        }
        return false;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        try {
            log.info("开始对接受值进行解密操作");
            // 定义是否解密
            boolean encode = false;
            //获取注解配置的包含和去除字段
            Encrypt serializedField = methodParameter.getMethodAnnotation(Encrypt.class);
            //入参是否需要解密
            encode = serializedField.in();
            log.info("对方法method :【" + methodParameter.getMethod().getName() + "】数据进行解密!");
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            Map<String, String> map = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            String appkey = map.get("appkey");
            return new MyHttpInputMessage(appkey, httpInputMessage, encode);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("对方法method :【" + methodParameter.getMethod().getName() + "】数据进行解密出现异常：" + e.getMessage());
            throw new ApiException(ErrorCode.ERROR_PARAMETER_DECRYPT_EXCEPTION);
        }
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    /**
     * 对流进行解密操作
     *
     * @param in
     * @return
     */
    public InputStream decryptBody(String appkey, InputStream in, Boolean encode) throws IOException {
        try {
            SysApp app = appService.selectSysAppByAppKey(appkey);
            // 检查软件是否可用（软件存在且正常开放）
            validUtils.apiCheckApp(appkey, app);
            // 获取 json 字符串
            String bodyStr = IOUtils.toString(in, "UTF-8");
            // 检查api参数文本是否为空
            if (StringUtils.isBlank(bodyStr)) {
                throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "请求体内容为空");
            }
            // 获取 params 加密串
//            JSONObject jsonObject = JSONObject.parseObject(bodyStr);
////            Object object = jsonObject.get("params");
////            String inputData = object == null ? "" : object.toString();
//            String inputData = jsonObject.toJSONString();
            String inputData = bodyStr;

            // 验证是否为空
            if (StringUtils.isBlank(inputData)) {
                throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "请求参数为空");
            } else {
                // 是否解密
                EncrypType encrypTypeIn = app.getDataInEnc();
                if (!(encode && encrypTypeIn != null && encrypTypeIn != EncrypType.NONE)) {
                    log.info("没有解密标识不进行解密!");
                    return IOUtils.toInputStream(inputData, "UTF-8");
                }

                // 开始解密
                log.info("对加密串开始解密操作!");
                log.info("原始内容：" + inputData);
                IEncryptType encryptType = null;
                try {
//                    if (encrypTypeIn == EncrypType.AES_CBC_NoPadding) {
//                        encryptType = new EncryptAesCbcNoP();
//                    } else
                    if (encrypTypeIn == EncrypType.AES_CBC_PKCS5Padding) {
                        encryptType = new EncryptAesCbcPKCS5P();
                    } else if (encrypTypeIn == EncrypType.AES_CBC_ZeroPadding) {
                        encryptType = new EncryptAesCbcZeroP();
                    } else if (encrypTypeIn == EncrypType.BASE64) {
                        encryptType = new EncryptBase64();
                    }
//                	String decryptBody = RSAUtils.decryptDataOnJava(inputData, Constants.privateKey);
                    String decryptBody = encryptType.decrypt(inputData, app.getDataInPwd());
                    log.info("解密内容：" + decryptBody);
                    log.info("操作结束!");
                    return IOUtils.toInputStream(decryptBody, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ApiException(ErrorCode.ERROR_PARAMETER_DECRYPT_EXCEPTION);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(ErrorCode.ERROR_PARAMETER_ANALYSE_EXCEPTION);
        }
    }

    //这里实现了HttpInputMessage 封装一个自己的HttpInputMessage
    class MyHttpInputMessage implements HttpInputMessage {
        HttpHeaders headers;
        InputStream body;

        public MyHttpInputMessage(String appkey, HttpInputMessage httpInputMessage, boolean encode) throws IOException {
            this.headers = httpInputMessage.getHeaders();
            // 解密操作
            this.body = decryptBody(appkey, httpInputMessage.getBody(), encode);
        }

        @Override
        public InputStream getBody() {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }
}
