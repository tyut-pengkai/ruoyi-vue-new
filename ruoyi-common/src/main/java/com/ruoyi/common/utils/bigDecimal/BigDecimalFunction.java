package com.ruoyi.common.utils.bigDecimal;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2023/11/11 9:08
 */
@FunctionalInterface
public interface BigDecimalFunction<T> {

    BigDecimal applyAsBigDecimal(T value);

}
