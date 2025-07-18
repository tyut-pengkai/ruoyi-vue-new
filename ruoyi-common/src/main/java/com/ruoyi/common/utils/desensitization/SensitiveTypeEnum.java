package com.ruoyi.common.utils.desensitization;

/**
 * 敏感类型
 *
 * @author liangyuqi
 * @date 2022/4/29 11:22
 */
public enum SensitiveTypeEnum {
    /**
     * 默认：全部替换为“*”
     */
    DEFAULT,
    /**
     * 中文名：两个字,对姓名第一个字进行隐藏，三个字,对姓名第二个字进行隐藏，四个字,对姓名第三个字进行隐藏
     */
    CHINESE_NAME,
    /**
     * 身份证号：只显示身份证前两位和后两位
     */
    ID_CARD_NO,
    /**
     * 电话号码：只显示电话号码前三位和后三位
     */
    PHONE_NO,
    /**
     * 银行卡号：只显示前4位和后4位
     */
    BANK_CARD_NO,
    /**
     * 详细地址：只显示详细地址前9位
     */
    DETAILED_ADDRESS,
    /**
     * 对象：可以是数组、集合、POJO
     */
    OBJECT
}
