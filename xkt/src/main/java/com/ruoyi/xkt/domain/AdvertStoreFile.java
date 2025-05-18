package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 档口上传的推广营销文件对象 advert_store_file
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AdvertStoreFile extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 推广营销文件ID
     */
    @TableId
    private Long id;
    /**
     * 上传的推广营销ID  做记录用
     */
    private Long advertRoundId;
    /**
     * 展示类型 1推广图、2商品、3推广图及商品、4店铺名称
     */
    private Integer displayType;
    /**
     * 位置
     */
    private String position;
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 系统文件ID
     */
    private Long picId;
    /**
     * 和推广营销中的typeId一致
     */
    private Integer typeId;
    /**
     * 凭证日期
     */
    private Date voucherDate;

}
