package com.ruoyi.api.v1.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.ruoyi.api.v1.support.BaseAutoAware;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Data
public abstract class Function extends BaseAutoAware {

    @Autowired(required = false)
    private Api api;
    @Autowired(required = false)
    private SysApp app;
    @Autowired(required = false)
    private Map<String, String> params;
    @Autowired(required = false)
    @JSONField(serialize = false)
    private HttpServletRequest request;
    @Autowired(required = false)
    @JSONField(serialize = false)
    private HttpServletResponse response;

    public Function() {
        super();
        init();
    }

    /**
     * 获取用户缓存信息
     */
    public LoginUser getLoginUser() {
        return SecurityUtils.getLoginUser();
    }

    public abstract void init();

    public abstract Object handle();

}
