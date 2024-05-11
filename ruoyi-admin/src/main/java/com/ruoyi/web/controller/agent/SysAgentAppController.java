package com.ruoyi.web.controller.agent;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.domain.SysAgentItem;
import com.ruoyi.agent.service.ISysAgentItemService;
import com.ruoyi.agent.service.ISysAgentUserService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.TemplateType;
import com.ruoyi.framework.agent.anno.AgentPermCheck;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysCardTemplateService;
import com.ruoyi.system.service.ISysLoginCodeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * 软件Controller
 *
 * @author zwgu
 * @date 2021-11-05
 */
@RestController
@RequestMapping("/agent/agentApp")
public class SysAgentAppController extends BaseController {
    @Autowired
    private ISysAppService sysAppService;
    @Resource
    private ISysAgentItemService sysAgentItemService;
    @Resource
    private ISysAgentUserService sysAgentService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private ISysCardTemplateService sysCardTemplateService;
    @Resource
    private ISysLoginCodeTemplateService sysLoginCodeTemplateService;

    /**
     * 查询软件列表
     */
    @PreAuthorize("@ss.hasAnyRoles('agent,admin,sadmin')")
    @AgentPermCheck
    @GetMapping("/listAll")
    public TableDataInfo listAll(SysApp sysApp) {
        List<SysApp> list = new ArrayList<>();
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            Set<Long> appIds = new HashSet<>();
            // 获取我的代理ID
            SysAgent agent = sysAgentService.selectSysAgentByUserId(getLoginUser().getUserId());
            // 获取代理的所有卡类
            List<SysAgentItem> agentItems = sysAgentItemService.selectSysAgentItemByAgentId(agent.getAgentId());
            // 获取所有卡类信息
            for (SysAgentItem item : agentItems) {
                if (item.getTemplateType() == TemplateType.CHARGE_CARD) {
                    SysCardTemplate template = sysCardTemplateService.selectSysCardTemplateByTemplateId(item.getTemplateId());
                    SysApp app = template.getApp();
                    if (sysApp == null || sysApp.getAuthType() == null || Objects.equals(sysApp.getAuthType(), app.getAuthType())) {
                        if (!appIds.contains(app.getAppId())) {
                            list.add(app);
                            appIds.add(app.getAppId());
                        }
                    }
                } else if (item.getTemplateType() == TemplateType.LOGIN_CODE) {
                    SysLoginCodeTemplate template = sysLoginCodeTemplateService.selectSysLoginCodeTemplateByTemplateId(item.getTemplateId());
                    SysApp app = template.getApp();
                    if (sysApp == null || sysApp.getAuthType() == null || Objects.equals(sysApp.getAuthType(), app.getAuthType())) {
                        if (!appIds.contains(app.getAppId())) {
                            list.add(app);
                            appIds.add(app.getAppId());
                        }
                    }
                }
            }
        } else {
            list.addAll(sysAppService.selectSysAppList(sysApp));
        }
        return getDataTable(list);
    }

}
