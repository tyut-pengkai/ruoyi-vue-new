package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.enums.*;

import java.util.List;
import java.util.Map;

public class SysApp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 软件ID */
    @Excel(name = "软件编号")
    private Long appId;

    /** 软件名称 */
    @Excel(name = "软件名称")
    private String appName;

    /** 软件描述 */
    @Excel(name = "软件描述")
    private String description;

    /** API接口地址 */
    private String apiUrl;

    /** 软件状态（0正常 1停用） */
    @Excel(name = "软件状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 绑定模式 */
    @Excel(name = "绑定模式", dictType = "sys_bind_type")
    private BindType bindType;

    /** 是否开启计费 */
    @Excel(name = "是否开启计费", readConverterExp = "Y=是,N=否")
    private String isCharge;

    /** 软件主页 */
    @Excel(name = "软件主页")
    private String idxUrl;

    /**
     * 首次登录赠送免费时间或点数，单位秒或点
     */
    @Excel(name = "首次登录赠送免费时间或点数，单位秒或点")
    private Long freeQuotaReg;

    /**
     * 换绑设备扣减时间或点数，单位秒或点
     */
    @Excel(name = "换绑设备扣减时间或点数，单位秒或点")
    private Long reduceQuotaUnbind;

    /**
     * 是否允许用户过期(计时模式)或余额为负数(计点模式)，默认为否
     */
    @Excel(name = "是否允许用户过期(计时模式)或余额为负数(计点模式)", readConverterExp = "Y=是,N=否")
    private String enableNegative;

    /**
     * 认证类型
     */
    @Excel(name = "认证类型", dictType = "sys_auth_type")
    private AuthType authType;

    /**
     * 计费类型
     */
    @Excel(name = "计费类型", dictType = "sys_bill_type")
    private BillType billType;

    /**
     * 数据输入加密方式
     */
    @Excel(name = "数据输入加密方式", dictType = "sys_encryp_type")
    private EncrypType dataInEnc;

    /** 数据输入加密密码 */
    private String dataInPwd;

    /** 数据输出加密方式 */
    @Excel(name = "数据输出加密方式", dictType = "sys_encryp_type")
    private EncrypType dataOutEnc;

    /** 数据输出加密密码 */
    private String dataOutPwd;

    /** 数据包过期时间，单位秒，-1为不限制，默认为-1 */
    @Excel(name = "数据包过期时间，单位秒，-1为不限制，默认为-1")
    private Long dataExpireTime;

    /** 登录用户数量限制，整数，-1为不限制，默认为-1 */
    @Excel(name = "登录用户数量限制，整数，-1为不限制，默认为1")
    private Integer loginLimitU;

    /** 登录机器数量限制，整数，-1为不限制，默认为-1 */
    @Excel(name = "登录机器数量限制，整数，-1为不限制，默认为1")
    private Integer loginLimitM;

    /** 达到上限后的操作，默认为TIPS */
    @Excel(name = "达到上限后的操作，默认为TIPS", dictType = "sys_limit_oper")
    private LimitOper limitOper;

    /** 达到上限后的操作，如果为注销最早登录，是否优先注销同一台设备上的账号 */
    @Excel(name = "达到上限后的操作，如果为注销最早登录，是否优先注销同一台设备上的账号", readConverterExp = "Y=是,N=否")
    private String enableFirstLogoutLocalMachine;

    /** 心跳包时间，单位秒，客户端若在此时间范围内无任何操作将自动下线，默认为300秒 */
    @Excel(name = "心跳包时间，单位秒，客户端若在此时间范围内无任何操作将自动下线，默认为300秒")
    private Integer heartBeatTime;

    /**
     * APP KEY
     */
    @Excel(name = "APP KEY")
    private String appKey;

    /**
     * APP SECRET
     */
    private String appSecret;

    /**
     * API匿名密码
     */
    private String apiPwd;

    /**
     * 软件启动公告
     */
    private String welcomeNotice;

    /**
     * 软件停机公告
     */
    private String offNotice;

    /**
     * 软件图标地址
     */
    @Excel(name = "软件图标")
    private String icon;

    /**
     * 软件开发者信息
     */
    @Excels({
            @Excel(name = "开发者账号", targetAttr = "userName", type = Excel.Type.EXPORT),
            @Excel(name = "开发者昵称", targetAttr = "nickName", type = Excel.Type.EXPORT)
    })
    private SysUser developer;

    private List<Map<String, String>> enApi;

    /**
     * 是否开启试用
     */
    private String enableTrial;

    /**
     * 每个ip可试用设备数，-1为不限制
     */
    private Integer trialTimesPerIp;

    /**
     * 每个设备试用时间周期，单位秒，0只能试用一次
     */
    private Long trialCycle;

    /**
     * 每个设备每周期试用次数
     */
    private Integer trialTimes;

    /**
     * 每个设备每次试用时长，单位秒
     */
    private Long trialTime;

    /**
     * 开启按时间段试用
     */
    private String enableTrialByTimeQuantum;

    /**
     * 开启按次数使用
     */
    private String enableTrialByTimes;

    /**
     * 试用时间段，格式 00:00:00-00:00:00
     */
    private String trialTimeQuantum;


    private String notAddTrialTimesInTrialTime;

    /**
     * 可解绑次数
     */
    private Integer unbindTimes;

    /**
     * 解绑扣除后最少剩余额度
     */
//    private Integer minQuotaUnbind;

    /**
     * 是否开启解绑
     */
    private String enableUnbind;

    private String enableUnbindByQuota;

    /**
     * 购卡URL
     */
    @Excel(name = "购卡URL")
    private String shopUrl;

    /**
     * 用于承载card的购卡链接，用于筛选要显示的APP列表
     */
    private String cardUrl;

    /**
     * 自定义购卡链接
     */
    @Excel(name = "自定义购卡链接")
    private String customBuyUrl;

    /**
     * 是否开启前台充值
     */
    private String enableFeCharge;

    /**
     * 登录点数扣减策略(计点模式有效）
     */
    @Excel(name = "登录点数扣减策略(计点模式有效）", dictType = "sys_login_reduce_point_strategy")
    private LoginReducePointStrategy loginReducePointStrategy;

    /**
     * 用户解绑时间周期，单位秒，0只能解绑一次
     */
    private Long unbindCycle;

    @Excel(name = "排序")
    private Integer sort;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getUnbindCycle() {
        return unbindCycle;
    }

    public void setUnbindCycle(Long unbindCycle) {
        this.unbindCycle = unbindCycle;
    }

    public LoginReducePointStrategy getLoginReducePointStrategy() {
        return loginReducePointStrategy;
    }

    public void setLoginReducePointStrategy(LoginReducePointStrategy loginReducePointStrategy) {
        this.loginReducePointStrategy = loginReducePointStrategy;
    }

    public String getEnableFeCharge() {
        return enableFeCharge;
    }

    public void setEnableFeCharge(String enableFeCharge) {
        this.enableFeCharge = enableFeCharge;
    }

    public String getCustomBuyUrl() {
        return customBuyUrl;
    }

    public void setCustomBuyUrl(String customBuyUrl) {
        this.customBuyUrl = customBuyUrl;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getEnableUnbindByQuota() {
        return enableUnbindByQuota;
    }

    public void setEnableUnbindByQuota(String enableUnbindByQuota) {
        this.enableUnbindByQuota = enableUnbindByQuota;
    }

    public String getEnableUnbind() {
        return enableUnbind;
    }

    public void setEnableUnbind(String enableUnbind) {
        this.enableUnbind = enableUnbind;
    }

    public Integer getUnbindTimes() {
        return unbindTimes;
    }

    public void setUnbindTimes(Integer unbindTimes) {
        this.unbindTimes = unbindTimes;
    }

    /*public Integer getMinQuotaUnbind() {
        return minQuotaUnbind;
    }

    public void setMinQuotaUnbind(Integer minQuotaUnbind) {
        this.minQuotaUnbind = minQuotaUnbind;
    }*/

    public String getNotAddTrialTimesInTrialTime() {
        return notAddTrialTimesInTrialTime;
    }

    public void setNotAddTrialTimesInTrialTime(String notAddTrialTimesInTrialTime) {
        this.notAddTrialTimesInTrialTime = notAddTrialTimesInTrialTime;
    }

    public String getEnableTrialByTimeQuantum() {
        return enableTrialByTimeQuantum;
    }

    public void setEnableTrialByTimeQuantum(String enableTrialByTimeQuantum) {
        this.enableTrialByTimeQuantum = enableTrialByTimeQuantum;
    }

    public String getEnableTrialByTimes() {
        return enableTrialByTimes;
    }

    public void setEnableTrialByTimes(String enableTrialByTimes) {
        this.enableTrialByTimes = enableTrialByTimes;
    }

    public String getTrialTimeQuantum() {
        return trialTimeQuantum;
    }

    public void setTrialTimeQuantum(String trialTimeQuantum) {
        this.trialTimeQuantum = trialTimeQuantum;
    }

    public String getEnableTrial() {
        return enableTrial;
    }

    public void setEnableTrial(String enableTrial) {
        this.enableTrial = enableTrial;
    }

    public Integer getTrialTimesPerIp() {
        return trialTimesPerIp;
    }

    public void setTrialTimesPerIp(Integer trialTimesPerIp) {
        this.trialTimesPerIp = trialTimesPerIp;
    }

    public Long getTrialCycle() {
        return trialCycle;
    }

    public void setTrialCycle(Long trialCycle) {
        this.trialCycle = trialCycle;
    }

    public Integer getTrialTimes() {
        return trialTimes;
    }

    public void setTrialTimes(Integer trialTimes) {
        this.trialTimes = trialTimes;
    }

    public Long getTrialTime() {
        return trialTime;
    }

    public void setTrialTime(Long trialTime) {
        this.trialTime = trialTime;
    }

    public SysUser getDeveloper() {
        return developer;
    }

    public void setDeveloper(SysUser developer) {
        this.developer = developer;
    }

    public void setAppId(Long appId)
    {
        this.appId = appId;
    }

    public Long getAppId()
    {
        return appId;
    }
    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public String getAppName()
    {
        return appName;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setApiUrl(String apiUrl)
    {
        this.apiUrl = apiUrl;
    }

    public String getApiUrl()
    {
        return apiUrl;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }
    public void setBindType(BindType bindType)
    {
        this.bindType = bindType;
    }

    public BindType getBindType()
    {
        return bindType;
    }

    public void setIsCharge(String isCharge)
    {
        this.isCharge = isCharge;
    }

    public String getIsCharge()
    {
        return isCharge;
    }
    public void setIdxUrl(String idxUrl)
    {
        this.idxUrl = idxUrl;
    }

    public String getIdxUrl()
    {
        return idxUrl;
    }
    public void setFreeQuotaReg(Long freeQuotaReg)
    {
        this.freeQuotaReg = freeQuotaReg;
    }

    public Long getFreeQuotaReg()
    {
        return freeQuotaReg;
    }
    public void setReduceQuotaUnbind(Long reduceQuotaUnbind)
    {
        this.reduceQuotaUnbind = reduceQuotaUnbind;
    }

    public Long getReduceQuotaUnbind()
    {
        return reduceQuotaUnbind;
    }
    public void setAuthType(AuthType authType)
    {
        this.authType = authType;
    }

    public AuthType getAuthType()
    {
        return authType;
    }
    public void setBillType(BillType billType)
    {
        this.billType = billType;
    }

    public BillType getBillType()
    {
        return billType;
    }
    public void setDataInEnc(EncrypType dataInEnc)
    {
        this.dataInEnc = dataInEnc;
    }

    public EncrypType getDataInEnc()
    {
        return dataInEnc;
    }
    public void setDataInPwd(String dataInPwd)
    {
        this.dataInPwd = dataInPwd;
    }

    public String getDataInPwd()
    {
        return dataInPwd;
    }
    public void setDataOutEnc(EncrypType dataOutEnc)
    {
        this.dataOutEnc = dataOutEnc;
    }

    public EncrypType getDataOutEnc()
    {
        return dataOutEnc;
    }
    public void setDataOutPwd(String dataOutPwd)
    {
        this.dataOutPwd = dataOutPwd;
    }

    public String getDataOutPwd()
    {
        return dataOutPwd;
    }
    public void setDataExpireTime(Long dataExpireTime)
    {
        this.dataExpireTime = dataExpireTime;
    }

    public Long getDataExpireTime()
    {
        return dataExpireTime;
    }
    public void setLoginLimitU(Integer loginLimitU)
    {
        this.loginLimitU = loginLimitU;
    }

    public Integer getLoginLimitU()
    {
        return loginLimitU;
    }
    public void setLoginLimitM(Integer loginLimitM)
    {
        this.loginLimitM = loginLimitM;
    }

    public Integer getLoginLimitM()
    {
        return loginLimitM;
    }
    public void setLimitOper(LimitOper limitOper)
    {
        this.limitOper = limitOper;
    }

    public LimitOper getLimitOper()
    {
        return limitOper;
    }
    public void setHeartBeatTime(Integer heartBeatTime)
    {
        this.heartBeatTime = heartBeatTime;
    }

    public Integer getHeartBeatTime()
    {
        return heartBeatTime;
    }
    public void setAppKey(String appKey)
    {
        this.appKey = appKey;
    }

    public String getAppKey()
    {
        return appKey;
    }
    public void setAppSecret(String appSecret)
    {
        this.appSecret = appSecret;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setApiPwd(String apiPwd) {
        this.apiPwd = apiPwd;
    }

    public String getApiPwd() {
        return apiPwd;
    }

    public String getWelcomeNotice() {
        return welcomeNotice;
    }

    public void setWelcomeNotice(String welcomeNotice) {
        this.welcomeNotice = welcomeNotice;
    }

    public String getOffNotice() {
        return offNotice;
    }

    public void setOffNotice(String offNotice) {
        this.offNotice = offNotice;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public List<Map<String, String>> getEnApi() {
        return enApi;
    }

    public void setEnApi(List<Map<String, String>> enApi) {
        this.enApi = enApi;
    }

    public String getEnableNegative() {
        return enableNegative;
    }

    public void setEnableNegative(String enableNegative) {
        this.enableNegative = enableNegative;
    }

    public String getEnableFirstLogoutLocalMachine() {
        return enableFirstLogoutLocalMachine;
    }

    public void setEnableFirstLogoutLocalMachine(String enableFirstLogoutLocalMachine) {
        this.enableFirstLogoutLocalMachine = enableFirstLogoutLocalMachine;
    }

    public String getCardUrl() {
        return cardUrl;
    }

    public void setCardUrl(String cardUrl) {
        this.cardUrl = cardUrl;
    }

    @Override
    public String toString() {
        return "SysApp{" +
                "appId=" + appId +
                ", appName='" + appName + '\'' +
                ", description='" + description + '\'' +
                ", apiUrl='" + apiUrl + '\'' +
                ", status='" + status + '\'' +
                ", delFlag='" + delFlag + '\'' +
                ", bindType=" + bindType +
                ", isCharge='" + isCharge + '\'' +
                ", idxUrl='" + idxUrl + '\'' +
                ", freeQuotaReg=" + freeQuotaReg +
                ", reduceQuotaUnbind=" + reduceQuotaUnbind +
                ", enableNegative='" + enableNegative + '\'' +
                ", authType=" + authType +
                ", billType=" + billType +
                ", dataInEnc=" + dataInEnc +
                ", dataInPwd='" + dataInPwd + '\'' +
                ", dataOutEnc=" + dataOutEnc +
                ", dataOutPwd='" + dataOutPwd + '\'' +
                ", dataExpireTime=" + dataExpireTime +
                ", loginLimitU=" + loginLimitU +
                ", loginLimitM=" + loginLimitM +
                ", limitOper=" + limitOper +
                ", enableFirstLogoutLocalMachine='" + enableFirstLogoutLocalMachine + '\'' +
                ", heartBeatTime=" + heartBeatTime +
                ", appKey='" + appKey + '\'' +
                ", appSecret='" + appSecret + '\'' +
                ", apiPwd='" + apiPwd + '\'' +
                ", welcomeNotice='" + welcomeNotice + '\'' +
                ", offNotice='" + offNotice + '\'' +
                ", icon='" + icon + '\'' +
                ", developer=" + developer +
                ", enApi=" + enApi +
                ", enableTrial='" + enableTrial + '\'' +
                ", trialTimesPerIp=" + trialTimesPerIp +
                ", trialCycle=" + trialCycle +
                ", trialTimes=" + trialTimes +
                ", trialTime=" + trialTime +
                ", enableTrialByTimeQuantum='" + enableTrialByTimeQuantum + '\'' +
                ", enableTrialByTimes='" + enableTrialByTimes + '\'' +
                ", trialTimeQuantum='" + trialTimeQuantum + '\'' +
                ", notAddTrialTimesInTrialTime='" + notAddTrialTimesInTrialTime + '\'' +
                ", unbindTimes=" + unbindTimes +
                ", enableUnbind='" + enableUnbind + '\'' +
                ", enableUnbindByQuota='" + enableUnbindByQuota + '\'' +
                ", shopUrl='" + shopUrl + '\'' +
                ", customBuyUrl='" + customBuyUrl + '\'' +
                ", enableFeCharge='" + enableFeCharge + '\'' +
                '}';
    }
}
