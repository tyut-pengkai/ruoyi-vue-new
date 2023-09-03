package com.ruoyi.api.v1.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.api.v1.anno.Encrypt;
import com.ruoyi.api.v1.constants.Constants;
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
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author ZwGu
 */
@Order(0)
@Slf4j
@RestControllerAdvice(basePackages = {"com.ruoyi.api.v1"})
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

	@Resource
	private ISysAppService appService;
	@Resource
	private ValidUtils validUtils;
	@Resource
	private ObjectMapper objectMapper;

	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
		// 这里设置成false 它就不会再走这个类了
		if (methodParameter.getMethod().isAnnotationPresent(Encrypt.class)) {
			// 获取注解配置的包含和去除字段
			Encrypt serializedField = methodParameter.getMethodAnnotation(Encrypt.class);
			return serializedField.out();
		}
		return false;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType,
								  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
								  ServerHttpResponse serverHttpResponse) {
		try {
			log.debug("开始对返回值进行加密操作!");
			// 定义是否解密
			boolean encode = false;
			// 获取注解配置的包含和去除字段
			Encrypt serializedField = methodParameter.getMethodAnnotation(Encrypt.class);
			// 入参是否需要解密
			encode = serializedField.out();
			log.debug("对方法method :【" + methodParameter.getMethod().getName() + "】数据进行加密!");
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = servletRequestAttributes.getRequest();
			Map<String, String> map = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
			String appkey = map.get("appkey");
			return encryptedBody(appkey, body, encode);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("对方法method :【" + methodParameter.getMethod().getName() + "】数据进行加密出现异常：" + e.getMessage());
			throw new ApiException(ErrorCode.ERROR_RESPONSE_ENCRYPT_EXCEPTION);
		}
	}

	public Object encryptedBody(String appkey, Object body, Boolean encode) throws IOException {
		SysApp app = appService.selectSysAppByAppKey(appkey);
		// 检查软件是否可用（软件存在且正常开放）
		validUtils.apiCheckApp(appkey, app);
		// 是否加密
		EncrypType encrypTypeOut = app.getDataOutEnc();
		if (!(encode && encrypTypeOut != null && encrypTypeOut != EncrypType.NONE)) {
			log.debug("没有加密标识不进行加密!");
			return body instanceof String ? Constants.PREFIX_TYPE + body : body;
		}
		log.debug("对字符串开始加密!");
		// String result = JSON.toJSONString(body);
		String result = objectMapper.writeValueAsString(body);
		if (body instanceof String) {
			result = (String) body;
		}
		log.debug("原始内容：" + result);
		IEncryptType encryptType = null;
		try {
//			if (encrypTypeOut == EncrypType.AES_CBC_NoPadding) {
//				encryptType = new EncryptAesCbcNoP();
//			} else
			if (encrypTypeOut == EncrypType.AES_CBC_PKCS5Padding) {
				encryptType = new EncryptAesCbcPKCS5P();
			} else if (encrypTypeOut == EncrypType.AES_CBC_ZeroPadding) {
				encryptType = new EncryptAesCbcZeroP();
			} else if (encrypTypeOut == EncrypType.BASE64) {
				encryptType = new EncryptBase64();
			}
//			String outputData = RSAUtils.encryptedDataOnJava(result, Constants.publicKey);
			String outputData = encryptType.encrypt(result, app.getDataOutPwd());
			log.debug("加密内容：" + outputData);
			log.debug("操作结束!");
			return body instanceof String ? Constants.PREFIX_TYPE + outputData : outputData;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApiException(ErrorCode.ERROR_RESPONSE_ENCRYPT_EXCEPTION);
		}
	}
}
