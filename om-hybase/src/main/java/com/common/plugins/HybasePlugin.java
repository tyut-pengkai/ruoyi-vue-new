package com.common.plugins;

import com.common.domain.HybaseBean;

/**
 * hybase插件接口
 */
public interface HybasePlugin {

    <T extends HybaseBean> void plugin(T record);
}
