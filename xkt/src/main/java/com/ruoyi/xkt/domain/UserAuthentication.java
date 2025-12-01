package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户代发认证对象 user_authentication
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
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
     * 代发人员手机号
     */
    private String phonenumber;
    /**
     * 身份证国徽面文件ID
     */
    private Long idCardEmblemFileId;
    /**
     * 代发认证状态 1 待审核 2审核驳回  3正式使用
     */
    private Integer authStatus;
    /**
     * 拒绝理由
     */
    private String rejectReason;

}
