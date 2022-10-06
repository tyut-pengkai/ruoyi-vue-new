package com.ruoyi.web.controller.system;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.NoticeType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页
 *
 * @author ruoyi
 */
@RestController
public class SysIndexController {
    /**
     * 系统基础配置
     */
    @Autowired
    private RuoYiConfig ruoyiConfig;
    @Resource
    private ISysNoticeService sysNoticeService;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index() {
        return StringUtils.format("欢迎使用{}，当前版本：{}，请通过入口地址访问本系统。", ruoyiConfig.getName(), ruoyiConfig.getVersion());
    }

    /**
     * 获取公告
     *
     * @param type 公告类型
     * @return
     */
    @GetMapping("/system/getNotice")
    public AjaxResult getNotice(@RequestParam("type") NoticeType type) {
        Map<String, Object> map = new HashMap<>();
        // 公告
        SysNotice sysNotice = new SysNotice();
        sysNotice.setNoticeType(NoticeType.valueOf(type.name()).getCode());
        sysNotice.setStatus(UserConstants.NORMAL);
        SysNotice latestNotice = sysNoticeService.selectLatestNotice(sysNotice);
        if (latestNotice != null) {
            map.put("content", latestNotice.getNoticeContent());
            map.put("title", latestNotice.getNoticeTitle());
        }
        return AjaxResult.success(map);
    }
}
