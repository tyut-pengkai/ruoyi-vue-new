package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户代发认证对象 user_authentication
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAuthentication extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 代发认证ID
     */
    @TableId
    private Long id;

    /**
     * sys_user.id
     */
    @Excel(name = "sys_user.id")
    private Long userId;

    /**
     * 真实名称
     */
    private String realName;

    /**
     * 电话号码
     */
    @Excel(name = "电话号码")
    private String phone;

    /**
     * 身份证号码
     */
    @Excel(name = "身份证号码")
    private String idCard;

    /**
     * 身份证头像面文件ID
     */
    @Excel(name = "身份证头像面文件ID")
    private Long idCardFrontFileId;

    /**
     * 身份证国徽面文件ID
     */
    @Excel(name = "身份证国徽面文件ID")
    private Long idCardBackFileId;

    /**
     * 代发认证状态
     */
    @Excel(name = "代发认证状态")
    private String authStatus;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("realName", getRealName())
                .append("phone", getPhone())
                .append("idCard", getIdCard())
                .append("idCardFrontFileId", getIdCardFrontFileId())
                .append("idCardBackFileId", getIdCardBackFileId())
                .append("authStatus", getAuthStatus())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
