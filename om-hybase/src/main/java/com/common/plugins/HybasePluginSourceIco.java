package com.common.plugins;

import cn.hutool.core.net.url.UrlBuilder;
import com.common.domain.HybaseBean;
import com.common.domain.OmDataBean;

/**
 * hybase 文章来源图标插件
 */
public class HybasePluginSourceIco implements HybasePlugin {

    @Override
    public <T extends HybaseBean> void plugin(T record) {
        OmDataBean omDataBean = (OmDataBean) record;
        UrlBuilder urlBuilder = UrlBuilder.of(omDataBean.getUrl());
        omDataBean.setSourceIco(urlBuilder.getScheme() + "://" + urlBuilder.getHost() + "/favicon.ico");
    }
}
