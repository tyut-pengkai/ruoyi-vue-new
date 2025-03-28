package com.ruoyi.framework.es;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-03-28 10:36
 */
@Data
public class EsConfiguration {

    private String hosts;

    private boolean ssl;

    private String username;

    private String password;

    private int maxConnTotal;

    private int maxConnPerRoute;

}
