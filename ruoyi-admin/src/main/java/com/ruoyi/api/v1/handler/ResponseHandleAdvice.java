package com.ruoyi.api.v1.handler;

import com.alibaba.fastjson.JSON;
import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.SwaggerVo;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author ZwGu
 */
@RestControllerAdvice(basePackages = {"com.ruoyi.api.v1"}) // 注意哦，这里要加上需要扫描的包
public class ResponseHandleAdvice implements ResponseBodyAdvice<Object> {
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
		// 如果接口返回的类型本身就是RestResponse那就没有必要进行额外的操作，返回false
		return !returnType.getGenericParameterType().equals(AjaxResult.class);
	}

	@Override
	public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType,
								  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
		// String类型不能直接包装，所以要进行些特别的处理
		if (returnType.getGenericParameterType().equals(String.class) || (data instanceof String && data.toString().startsWith(Constants.PREFIX_TYPE))) {
			// 将数据包装在RestResponse里后，再转换为json字符串响应给前端
			return JSON.toJSONString(AjaxResult.success().put("data", handleResult(data)).put("timestamp", DateUtils.getNowDate().getTime()));
		}
		// 将原本的数据包装在RestResponse里
		if (returnType.getGenericParameterType().equals(SwaggerVo.class)) {
			return data;
		}
		return AjaxResult.success().put("data", handleResult(data)).put("timestamp", DateUtils.getNowDate().getTime());
	}

	private Object handleResult(Object data) {
		if (data != null) {
			if (data.toString().startsWith(Constants.PREFIX_TYPE)) {
				return data.toString().replaceFirst(Constants.PREFIX_TYPE, "");
			}
			return data;
		}
		return null;
	}
}
