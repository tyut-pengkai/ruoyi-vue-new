package com.ruoyi.system.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.WeakCache;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.system.domain.SysHtml;
import com.ruoyi.system.mapper.SysHtmlMapper;
import com.ruoyi.system.service.ISysHtmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liangyq
 * @date 2025-10-31
 */
@Service
public class SysHtmlServiceImpl implements ISysHtmlService {

//    private WeakCache<String, String> htmlCache = CacheUtil.newWeakCache(-1L);
    private Map<String, String> htmlCache = new HashMap<>();
    @Autowired
    private SysHtmlMapper htmlMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveHtml(String title, String content) {
        Assert.notEmpty(title);
        SysHtml html = htmlMapper.selectOne(Wrappers.lambdaQuery(SysHtml.class).eq(SysHtml::getTitle, title));
        if (html == null) {
            //新增
            html = new SysHtml();
            html.setTitle(title);
            html.setContent(content);
            html.setDelFlag(Constants.UNDELETED);
            htmlMapper.insert(html);
        } else {
            //修改
            html.setContent(content);
            html.setDelFlag(Constants.UNDELETED);
            htmlMapper.updateById(html);
        }
        htmlCache.put(title, content);
    }

    @Override
    public String getHtmlContent(String title) {
        Assert.notEmpty(title);
        String content = htmlCache.get(title);
        if (content == null) {
            SysHtml html = htmlMapper.selectOne(Wrappers.lambdaQuery(SysHtml.class).eq(SysHtml::getTitle, title));
            if (html != null) {
                content = html.getContent();
                htmlCache.put(title, content);
            }
        }
        return content;
    }

}
