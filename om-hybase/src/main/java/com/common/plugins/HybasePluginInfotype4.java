package com.common.plugins;

import cn.hutool.core.util.StrUtil;
import com.common.domain.HybaseBean;
import com.common.domain.OmDataBean;

/**
 * hybase 倾向性插件
 */
public class HybasePluginInfotype4 implements HybasePlugin {

    @Override
    public <T extends HybaseBean> void plugin(T record) {
        OmDataBean omDataBean = (OmDataBean) record;
        if (StrUtil.equals(omDataBean.getInfotype(), "4")) {
            omDataBean.setTitle(null);
            if (StrUtil.isNotEmpty(omDataBean.getIrPextag3())) {
                omDataBean.setImageUrl(StrUtil.splitToArray(omDataBean.getIrPextag3(), ";"));
                omDataBean.setIrPextag3(null);
                omDataBean.setIrPextag2(omDataBean.getIrPextag2());
            }
        }
    }
}
