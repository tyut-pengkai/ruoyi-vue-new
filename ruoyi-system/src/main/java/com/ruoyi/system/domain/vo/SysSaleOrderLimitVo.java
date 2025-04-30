package com.ruoyi.system.domain.vo;

import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.sale.domain.SysSaleOrder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SysSaleOrderLimitVo extends SysSaleOrder {

    private int limit;

    public SysSaleOrderLimitVo(SysSaleOrder order, int limit) {
        BeanUtils.copyBeanProp(this, order);
        this.limit = limit;
    }

}
