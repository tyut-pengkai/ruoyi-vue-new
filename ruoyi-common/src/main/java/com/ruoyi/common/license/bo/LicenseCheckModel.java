package com.ruoyi.common.license.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * @author huangchen@deepglint.com
 * @desc 自定义需要校验的License参数
 * @date 2019/3/14 10:00
 */
@Setter
@Getter
@ToString
@Slf4j
public class LicenseCheckModel implements Serializable {

    /**
     * 服务器机器码
     */
    private String serverSn;
    /**
     * 可被允许的IP地址或域名
     */
    private List<String> ipAddress;
    /**
     * 软件位限制
     */
    private Integer appLimit;
    /**
     * 服务器域名
     */
    private List<String> domainName;
    /**
     * 模块名
     */
    private List<String> moduleName;

}
