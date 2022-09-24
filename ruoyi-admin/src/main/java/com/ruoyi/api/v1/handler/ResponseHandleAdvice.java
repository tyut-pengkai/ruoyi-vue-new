package com.ruoyi.api.v1.handler;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.SwaggerVo;
import com.ruoyi.api.v1.encrypt.IEncryptType;
import com.ruoyi.api.v1.encrypt.impl.EncryptAesCbcPKCS5P;
import com.ruoyi.api.v1.encrypt.impl.EncryptAesCbcZeroP;
import com.ruoyi.api.v1.encrypt.impl.EncryptBase64;
import com.ruoyi.api.v1.utils.SignUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.enums.EncrypType;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.service.ISysAppService;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZwGu
 */
@RestControllerAdvice(basePackages = {"com.ruoyi.api.v1"}) // 注意哦，这里要加上需要扫描的包
public class ResponseHandleAdvice implements ResponseBodyAdvice<Object> {

	@Resource
	private ISysAppService appService;

	@Override
	public boolean supports(MethodParameter returnType, @Nullable Class<? extends HttpMessageConverter<?>> aClass) {
		// 如果接口返回的类型本身就是RestResponse那就没有必要进行额外的操作，返回false
		return !returnType.getGenericParameterType().equals(AjaxResult.class) && !returnType.getGenericParameterType().equals(SwaggerVo.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object beforeBodyWrite(@Nullable Object data, @Nullable MethodParameter returnType, @Nullable MediaType mediaType,
								  @Nullable Class<? extends HttpMessageConverter<?>> aClass, @Nullable ServerHttpRequest request, @Nullable ServerHttpResponse response) {
		try {
			// 获取vstr
			Object vstr = null;
			// 获取appkey
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			assert servletRequestAttributes != null;
			HttpServletRequest req = servletRequestAttributes.getRequest();
			Map<String, String> map = (Map) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
			String appkey = map.get("appkey");
			SysApp app = appService.selectSysAppByAppKey(appkey);
			assert request != null;
			String inputData = IOUtils.toString(request.getBody(), StandardCharsets.UTF_8);
			// 是否解密
			EncrypType encrypTypeIn = app.getDataInEnc();
			if (encrypTypeIn != null && encrypTypeIn != EncrypType.NONE) {
				// 开始解密
				IEncryptType encryptType = null;
//				if (encrypTypeIn == EncrypType.AES_CBC_NoPadding) {
//					encryptType = new EncryptAesCbcNoP();
//				} else
				if (encrypTypeIn == EncrypType.AES_CBC_PKCS5Padding) {
					encryptType = new EncryptAesCbcPKCS5P();
				} else if (encrypTypeIn == EncrypType.AES_CBC_ZeroPadding) {
					encryptType = new EncryptAesCbcZeroP();
				} else if (encrypTypeIn == EncrypType.BASE64) {
					encryptType = new EncryptBase64();
				}
				assert encryptType != null;
				inputData = encryptType.decrypt(inputData, app.getDataInPwd());
			}
			// 转化为MAP
			HashMap paramMap = JSON.parseObject(inputData, HashMap.class);
			vstr = paramMap.get("vstr");
			String v = "";
			if (vstr != null) {
				v = String.valueOf(vstr);
			}
			// 计算sign
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("data", handleResult(data));
			resultMap.put("timestamp", String.valueOf(DateUtils.getNowDate().getTime()));
			resultMap.put("vstr", v);
			resultMap.put("sign", getResultSign(resultMap, app));
//			String sign = SignUtil.sign(JSON.toJSONString(resultMap), app.getAppSecret());
//			resultMap.put("sign", sign);
			// 将原本的数据包装在RestResponse里
			AjaxResult result = AjaxResult.success().put(resultMap);
			// String类型不能直接包装，所以要进行些特别的处理
			assert returnType != null;
			if (returnType.getGenericParameterType().equals(String.class) || (data instanceof String && data.toString().startsWith(Constants.PREFIX_TYPE))) {
				// 将数据包装在RestResponse里后，再转换为json字符串响应给前端
				// return JSON.toJSONString(result);
				return new ObjectMapper().writeValueAsString(result);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApiException("处理结果数据出错");
		}
	}

	private String handleResult(Object data) {
		if (data != null) {
			if (data.toString().startsWith(Constants.PREFIX_TYPE)) {
				return data.toString().replaceFirst(Constants.PREFIX_TYPE, "");
			}
			// return data;
			if (data instanceof String) {
				return data.toString();
			}
			// return JSON.toJSONString(data);
			try {
				return new ObjectMapper().writeValueAsString(data);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	private String getResultSign(Map<String, Object> resultMap, SysApp app) {
		String s = String.valueOf(resultMap.get("data")) + resultMap.get("timestamp") + resultMap.get("vstr");
		return SignUtil.sign(s, app.getAppSecret());
	}
}
