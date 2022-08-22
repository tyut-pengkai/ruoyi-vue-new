package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.api.v1.domain.RespItem;
import com.ruoyi.api.v1.domain.vo.SysAppVo;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.system.service.ISysAppService;

import javax.annotation.Resource;

public class AppInfo extends Function {

    @Resource
    private ISysAppService appService;

    @Override
    public void init() {
        this.setApi(new Api("appInfo.ng", "获取软件配置信息", false, Constants.API_TAG_GENERAL,
                "获取软件配置信息", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL, null,
                new Resp(Resp.DataType.object, "软件配置信息", new RespItem[]{
                        new RespItem(Resp.DataType.string, "appName", "软件名称"),
                        new RespItem(Resp.DataType.string, "description", "软件描述"),
                        new RespItem(Resp.DataType.string, "status", "软件状态（0正常 1停用）"),
                        new RespItem(Resp.DataType.string, "bindType", "绑定模式"),
                        new RespItem(Resp.DataType.string, "isCharge", "是否开启计费"),
                        new RespItem(Resp.DataType.string, "idxUrl", "软件主页"),
                        new RespItem(Resp.DataType.integer, "freeQuotaReg", "首次登录赠送免费时间或点数，单位秒或点"),
                        new RespItem(Resp.DataType.integer, "reduceQuotaUnbind", "换绑设备扣减时间或点数，单位秒或点"),
                        new RespItem(Resp.DataType.string, "authType", "认证类型"),
                        new RespItem(Resp.DataType.string, "billType", "计费类型"),
                        new RespItem(Resp.DataType.integer, "dataExpireTime", "数据包过期时间，单位秒，-1为不限制，默认为-1"),
                        new RespItem(Resp.DataType.integer, "loginLimitU", "登录用户数量限制，整数，-1为不限制，默认为-1"),
                        new RespItem(Resp.DataType.integer, "loginLimitM", "登录机器数量限制，整数，-1为不限制，默认为-"),
                        new RespItem(Resp.DataType.string, "limitOper", "达到上限后的操作"),
                        new RespItem(Resp.DataType.integer, "heartBeatTime", "心跳包时间，单位秒，客户端若在此时间范围内无任何操作将自动下线，默认为300秒"),
                        new RespItem(Resp.DataType.string, "welcomeNotice", "启动公告"),
                        new RespItem(Resp.DataType.string, "offNotice", "停机公告"),
                        new RespItem(Resp.DataType.string, "icon", "软件图标地址，当前无实际作用"),
                        new RespItem(Resp.DataType.string, "enableTrial", "是否开启试用"),
                        new RespItem(Resp.DataType.integer, "trialTimesPerIp", "每个ip可试用设备数，-1为不限制"),
                        new RespItem(Resp.DataType.integer, "trialCycle", "每个设备试用时间周期，单位秒，0只能试用一次"),
                        new RespItem(Resp.DataType.integer, "trialTimes", "每个设备每周期试用次数"),
                        new RespItem(Resp.DataType.integer, "trialTime", "每个设备每次试用时长，单位秒"),
                        new RespItem(Resp.DataType.string, "enableTrialByTimeQuantum", "是否开启按时间试用"),
                        new RespItem(Resp.DataType.string, "enableTrialByTimes", "是否开启按次数试用"),
                        new RespItem(Resp.DataType.string, "trialTimeQuantum", "试用时间段"),
                        new RespItem(Resp.DataType.string, "remark", "备注信息"),
                })));
    }

    @Override
    public Object handle() {
        SysApp sysApp = appService.selectSysAppByAppId(getApp().getAppId());
        return new SysAppVo(sysApp);
    }
}
