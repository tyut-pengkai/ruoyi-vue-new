package com.ruoyi.web.controller.xkt.migartion.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@Data
@Accessors(chain = true)
public class CusDiscErrorVO {

    // 客户优惠金额小于0
    private List<String> errDiscList;
    // 客户在同一货品，不同颜色优惠金额不一致
    private List<String> errCusDiscUnSameList;

}
