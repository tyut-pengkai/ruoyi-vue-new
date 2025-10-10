package com.common.plugins;

import cn.hutool.core.util.StrUtil;
import com.common.domain.HybaseBean;
import com.common.domain.OmDataBean;

/**
 * hybase 视频图片插件
 */
public class HybasePluginInfotype11 implements HybasePlugin {

    @Override
    public <T extends HybaseBean> void plugin(T record) {
        OmDataBean omDataBean = (OmDataBean) record;
        if (StrUtil.equals(omDataBean.getInfotype(), "11") && StrUtil.isNotEmpty(omDataBean.getIrPextag2())) {
            omDataBean.setImageUrl(new String[]{omDataBean.getIrPextag2()});
        }
    }
}
