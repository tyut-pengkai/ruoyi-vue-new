package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    private Long userId;
    /**
     * 真实名称
     */
    private String realName;
    /**
     * 身份证号码
     */
    private String idCard;
    /**
     * 身份证头像面文件ID
     */
    private Long idCardFaceFileId;
    /**
     * 身份证国徽面文件ID
     */
    private Long idCardEmblemFileId;
    /**
     * 代发认证状态
     */
    private Integer authStatus;
    /**
     * 拒绝理由
     */
    private String rejectReason;

}
