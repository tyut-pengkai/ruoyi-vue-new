package com.common.plugins;

import cn.hutool.core.util.StrUtil;
import com.common.domain.HybaseBean;
import com.common.domain.OmDataBean;

/**
 * hybase 倾向性插件
 */
public class HybasePluginTendency implements HybasePlugin {

    @Override
    public <T extends HybaseBean> void plugin(T record) {
        OmDataBean omDataBean = (OmDataBean) record;
        if (StrUtil.isEmpty(omDataBean.getTendency())) {
            omDataBean.setTendency("中性");
        }
    }
}
