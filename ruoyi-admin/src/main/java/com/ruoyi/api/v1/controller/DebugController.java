package com.ruoyi.api.v1.controller;

import com.alibaba.fastjson.JSON;
import com.ruoyi.api.v1.utils.SignUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysConfigService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/devTool")
@Slf4j
@Api(tags = "调试工具")
@ApiResponses({
		@ApiResponse(code = 200, message = "请求完成", response = AjaxResult.class)
})
public class DebugController extends BaseController {

	@Resource
	private ISysAppService sysAppService;
	@Resource
	private ISysConfigService configService;

	@PostMapping("/{appkey}/calcSign")
	@ApiOperation(value = "计算sign", notes = "计算sign值")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "appkey", value = "AppKey", paramType = "path", required = true, dataType = "String"),
			@ApiImplicitParam(name = "params", value = "接口需要的参数", paramType = "body", required = true, dataType = "Map")
	})
	public AjaxResult getSign(@PathVariable("appkey") String appkey, @RequestBody Map<String, String> params) {
		log.debug("appkey: {}, 请求参数: {}", appkey, JSON.toJSON(params));

		String state = configService.selectConfigByKey("sys.api.devMode");
		if (StringUtils.isEmpty(state) || !Convert.toBool(state)) {
			throw new ApiException("非开发模式下无法调用此方法");
		}

		SysApp app = sysAppService.selectSysAppByAppKey(appkey);
		return AjaxResult.success(SignUtil.sign(params, app.getAppSecret()));
	}
}
