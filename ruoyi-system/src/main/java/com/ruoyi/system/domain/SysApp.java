package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.enums.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 软件管理表
 *
 * @author zwgu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysApp extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 参数主键
     */
    @Excel(name = "参数主键", cellType = Excel.ColumnType.NUMERIC)
    private Long appId;
    /**
     * 软件名称
     */
    private String appName;
    /**
     * 软件描述
     */
    private String description;
    /**
     * API接口地址
     */
    private String apiUrl;
    /**
     * 软件状态（0正常 1停用）
     */
    @Excel(name = "软件状态", readConverterExp = "0=正常,1=停用")
    private String status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 绑定模式
     */
    private BindType bindType;
    /**
     * 是否登录时自动绑定机器码
     */
    private boolean isAutoBind;
    /**
     * 是否开启计费
     */
    private boolean isCharge;
    /**
     * 软件主页
     */
    private String idxUrl;
    /**
     * 首次登录赠送免费时间或点数，单位秒或点
     */
    private Long freeQuotaReg;
    /**
     * 换绑设备扣减时间或点数，单位秒或点
     */
    private Long reduceQuotaUnbind;
    /**
     * 认证类型
     */
    private AuthType authType;
    /**
     * 计费类型
     */
    private BillType billType;
    /**
     * 数据输入加密方式
     */
    private EncrypType dataInEnc;
    /**
     * 数据输入加密密码
     */
    private String dataInPwd;
    /**
     * 数据输出加密方式
     */
    private EncrypType dataOutEnc;
    /**
     * 数据输出加密密码
     */
    private String dataOutPwd;
    /**
     * 数据包过期时间，单位秒，-1为不限制，默认为-1
     */
    private Long dataExpireTime;
    /**
     * 登录用户数量限制，整数，-1为不限制，默认为-1
     */
    private Integer loginLimitU;
    /**
     * 登录机器数量限制，整数，-1为不限制，默认为-1
     */
    private Integer loginLimitM;
    /**
     * 达到上限后的操作，默认为TIPS
     */
    private LimitOper limitOper;
    /**
     * 心跳包时间，单位秒，客户端若在此时间范围内无任何操作将自动下线，默认为300秒
     */
    private Long heartBeatTime;
    /**
     * APP ID
     */
    private String appid;
    /**
     * APP KEY
     */
    private String appkey;
    /**
     * API匿名密码
     */
    private String apiPwd;
    /**
     * 登录码前缀
     */
    private String loginCodePrefix;
    /**
     * 登录码后缀
     */
    private String loginCodeSuffix;
    /**
     * 登录码长度，默认为32
     */
    private Integer loginCodeLen;
    /**
     * 登录码生成规则，默认为大小写字母+数字
     */
    private GenRule loginCodeGenRule;
    /**
     * 登录码生成规则为正则时生效
     */
    private String loginCodeRegex;
    /**
     * 软件图标地址
     */
    private String icon;

}
