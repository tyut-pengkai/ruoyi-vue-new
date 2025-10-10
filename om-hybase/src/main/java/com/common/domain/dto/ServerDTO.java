package com.common.domain.dto;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 服务管理
 */
@Data
@NoArgsConstructor
public class ServerDTO implements Serializable {


    /**
     * 配置键值
     */
    private String key;

    /**
     * 库名称
     */
    private String dbName;

    /**
     * 服务ip
     */
    private String ip;

    /**
     * 服务端口
     */
    private String port;

    /**
     * 协议类型
     */
    private String agreement;

    /**
     * 服务的基础路径
     */
    private String baseUrl;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    public String getUrl() {
        // agreement + "://" + ip + ":" + port;
        return agreement + CharUtil.COLON + CharUtil.SLASH + CharUtil.SLASH + ip + CharUtil.COLON + port + StrUtil.nullToEmpty(baseUrl);
    }
}
