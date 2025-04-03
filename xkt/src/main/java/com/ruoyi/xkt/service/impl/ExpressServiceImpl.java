package com.ruoyi.xkt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.xkt.domain.Express;
import com.ruoyi.xkt.domain.ExpressFeeConfig;
import com.ruoyi.xkt.mapper.ExpressFeeConfigMapper;
import com.ruoyi.xkt.mapper.ExpressMapper;
import com.ruoyi.xkt.service.IExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liangyq
 * @date 2025-04-03 13:37
 */
@Service
public class ExpressServiceImpl implements IExpressService {

    @Autowired
    private ExpressMapper expressMapper;
    @Autowired
    private ExpressFeeConfigMapper expressFeeConfigMapper;

    @Override
    public void checkExpress(Long expressId) {
        Assert.notNull(expressId);
        Express express = expressMapper.selectById(expressId);
        Assert.isTrue(BeanValidators.exists(express), "快递不存在");
        Assert.isTrue(express.getSystemDeliverAccess(), "快递不可用");
    }

    @Override
    public ExpressFeeConfig getExpressFeeConfig(Long expressId, String provinceCode, String cityCode,
                                                String countyCode) {
        Assert.notNull(expressId);
        Assert.notEmpty(provinceCode);
        Assert.notEmpty(cityCode);
        Assert.notEmpty(countyCode);
        Map<String, ExpressFeeConfig> map = expressFeeConfigMapper.selectList(Wrappers.lambdaQuery(ExpressFeeConfig.class)
                .eq(ExpressFeeConfig::getExpressId, expressId)
                .in(ExpressFeeConfig::getRegionCode, Arrays.asList(provinceCode, cityCode, countyCode)))
                .stream()
                //过滤掉已被删除的配置
                .filter(BeanValidators::exists)
                .collect(Collectors.toMap(o -> o.getRegionCode(), o -> o, (n, o) -> n));
        ExpressFeeConfig expressFeeConfig = null;
        if (CollUtil.isNotEmpty(map)) {
            if (map.size() == 1) {
                expressFeeConfig = CollUtil.getFirst(map.values());
            } else {
                expressFeeConfig = map.get(countyCode);
                //按区市省从小到大去匹配
                if (expressFeeConfig == null) {
                    expressFeeConfig = map.get(cityCode);
                    if (expressFeeConfig == null) {
                        expressFeeConfig = map.get(provinceCode);
                    }
                }
            }
        }
        return expressFeeConfig;
    }
}
