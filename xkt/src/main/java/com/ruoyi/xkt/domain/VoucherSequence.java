package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统单据编号规则，每天凌晨定时任务清空编号，又重新从1开始
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class VoucherSequence extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 单据编号id
     */
    @TableId
    private Long id;
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 单据类型
     */
    private String type;
    /**
     * 将date根据单据类型格式化，不同单据的时间格式可能有差异，比如"2023-04" 或者 "2023-04-22"
     */
    private String dateFormat;
    /**
     * 单据类型决定，比如"SD"
     */
    private String prefix;
    /**
     * 下一个序列号 若跨月，则通过每天1点定时任务置为1
     */
    private Integer nextSequence;
    /**
     * 将sequence转换成对应的格式，如"%04d"
     */
    private String sequenceFormat;

}
