package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 意见反馈
 *
 * @author liujiang
 * @date 2025-05-03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Feedback extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 意见反馈ID
     */
    @TableId
    private Long id;
    /**
     * 反馈内容
     */
    private String content;
    /**
     * 联系方式
     */
    private String contact;

}
