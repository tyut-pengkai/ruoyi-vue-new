package com.ruoyi.web.controller.agent;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.domain.SysAgentItem;
import com.ruoyi.agent.domain.vo.AgentInfoVo;
import com.ruoyi.agent.domain.vo.TemplateInfoVo;
import com.ruoyi.agent.service.ISysAgentItemService;
import com.ruoyi.agent.service.ISysAgentUserService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.agent.anno.AgentPermCheck;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代理管理Controller
 *
 * @author zwgu
 * @date 2022-06-11
 */
@RestController
@RequestMapping("/agent/agentUser")
public class SysAgentUserController extends BaseController {
    @Resource
    private ISysAgentUserService sysAgentService;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private PermissionService permissionService;
    @Autowired
    private ISysAgentItemService sysAgentItemService;
    @Resource
    private ISysConfigService configService;

    /**
     * 查询代理管理列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentUser:list')")
    @AgentPermCheck
    @GetMapping("/list")
    public AjaxResult list(SysAgent sysAgent) {
        List<SysAgent> list = sysAgentService.selectSysAgentList(sysAgent);
        List<SysAgent> filterList = new ArrayList<>();
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            // 获取我的代理ID
            SysAgent agent = sysAgentService.selectSysAgentByUserId(getLoginUser().getUserId());
            List<Long> subAgents = sysAgentService.getSubAgents(agent.getAgentId());
            for (SysAgent item : list) {
                if (subAgents.contains(item.getAgentId())) {
                    filterList.add(item);
                }
            }
        } else {
            filterList.addAll(list);
        }
        for (SysAgent agent : filterList) {
            fill(agent);
        }
        return AjaxResult.success(filterList);
    }

    private void fill(SysAgent agent) {
        // 填充父级代理信息
        fillParentAgent(agent);
        // 填充子代理信息
        SysAgent sAgent = new SysAgent();
        sAgent.setParentAgentId(agent.getAgentId());
        List<SysAgent> subAgents = sysAgentService.selectSysAgentList(sAgent);
        agent.setChildren(subAgents);
        for (SysAgent ag : subAgents) {
            fill(ag);
        }
    }

    /**
     * 导出代理管理列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentUser:export')")
    @AgentPermCheck
    @Log(title = "代理管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysAgent sysAgent) {
        List<SysAgent> list = sysAgentService.selectSysAgentList(sysAgent);
        ExcelUtil<SysAgent> util = new ExcelUtil<SysAgent>(SysAgent.class);
        util.exportExcel(response, list, "代理管理数据");
    }

    /**
     * 获取代理管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('agent:agentUser:query')")
    @AgentPermCheck
    @GetMapping(value = "/{agentId}")
    public AjaxResult getInfo(@PathVariable("agentId") Long agentId) {
        SysAgent agent = sysAgentService.selectSysAgentByAgentId(agentId);
        // 填充父级代理信息
        fillParentAgent(agent);
        return AjaxResult.success(agent);
    }

    /**
     * 新增代理管理
     */
    @PreAuthorize("@ss.hasPermi('agent:agentUser:add')")
    @AgentPermCheck(value = "enableAddSubagent", checkEnableAddSubagent = true)
    @Log(title = "代理管理", businessType = BusinessType.INSERT)
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult add(@Validated @RequestBody SysAgent sysAgent) {
        if (sysAgent.getParentAgentId() == null || sysAgent.getParentAgentId() <= 0) {
            sysAgent.setParentAgentId(null);
        }
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
            if (sysAgent.getParentAgentId() == null) {
                sysAgent.setParentAgentId(agent.getAgentId());
            }
        }
        SysAgent agentNow = sysAgentService.selectSysAgentByUserId(sysAgent.getUserId());
        if (agentNow != null) {
            throw new ServiceException("该用户已经是代理身份");
        }
        // 为新代理添加agent角色
        if (!permissionService.hasRole(sysAgent.getUserId(), "agent")) {
            SysRole sysRole = sysRoleService.selectRoleByKey("agent");
            sysRoleService.insertAuthUsers(sysRole.getRoleId(), new Long[]{sysAgent.getUserId()});
        }
        sysAgent.setCreateBy(getUsername());
        sysAgentService.insertSysAgent(sysAgent);
        checkCircleParentAgentAndFillPath(sysAgent);
        sysAgentService.updateSysAgent(sysAgent);
        // 添加授权
        List<TemplateInfoVo> templateList = sysAgent.getTemplateList();
        for (TemplateInfoVo templateInfoVo : templateList) {
            SysAgentItem agentItem = new SysAgentItem();
            agentItem.setAgentId(sysAgent.getAgentId());
            agentItem.setTemplateType(templateInfoVo.getTemplateType());
            agentItem.setTemplateId(templateInfoVo.getTemplateId());
            agentItem.setCreateBy(getUsername());
            // 校验价格
            checkAgentPrice(templateInfoVo);
            agentItem.setAgentPrice(templateInfoVo.getAgentPrice());
            agentItem.setExpireTime(templateInfoVo.getExpireTime());
            agentItem.setRemark(templateInfoVo.getRemark());
            sysAgentItemService.insertSysAgentItem(agentItem);
        }
        return AjaxResult.success();
    }

    /**
     * 修改代理管理
     */
    @PreAuthorize("@ss.hasPermi('agent:agentUser:edit')")
    @AgentPermCheck(value = "enableAddSubagent", checkEnableAddSubagent = true)
    @Log(title = "代理管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult edit(@RequestBody SysAgent sysAgent) {
        // 检查是否有变更状态权限
        SysAgent oAgent = sysAgentService.selectSysAgentByAgentId(sysAgent.getAgentId());
        if(sysAgent.getStatus() != null && !Objects.equals(sysAgent.getStatus(), oAgent.getStatus()) && !permissionService.hasAnyRoles("sadmin,admin") && !permissionService.hasAgentPermi("enableUpdateSubagentStatus")) {
            throw new ServiceException("您没有该操作的权限（代理系统）");
        }
        if (sysAgent.getParentAgentId() == null || sysAgent.getParentAgentId() <= 0) {
            sysAgent.setParentAgentId(null);
        }
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
            if (sysAgent.getParentAgentId() == null) {
                sysAgent.setParentAgentId(agent.getAgentId());
            }
        }
        if (Objects.equals(sysAgent.getParentAgentId(), sysAgent.getAgentId())) {
            throw new ServiceException("代理与上级代理不能为同一个用户");
        }
        checkCircleParentAgentAndFillPath(sysAgent);
        // 更新代理的所有下级代理
        String oldPath = oAgent.getPath();
        List<Long> subAgentIds = sysAgentService.getSubAgents(sysAgent.getAgentId());
        for (Long subAgentId : subAgentIds) {
            SysAgent agent = sysAgentService.selectSysAgentByAgentId(subAgentId);
            agent.setPath(agent.getPath().replace(oldPath, sysAgent.getPath()));
            sysAgentService.updateSysAgent(agent);
        }
        // 更新代理的代理卡类
        List<TemplateInfoVo> templateList = sysAgent.getTemplateList();
        Map<Long, TemplateInfoVo> templateMap = new HashMap<>();
        templateList.forEach((item->templateMap.put(item.getAgentItemId(), item)));
        // 获取已经具有授权的卡类，为了兼容旧版本，否则不用引入TemplateInfoVo，类型互转比较麻烦
        SysAgentItem qSysAgentItem = new SysAgentItem();
        qSysAgentItem.setAgentId(sysAgent.getAgentId());
        List<SysAgentItem> list = sysAgentItemService.selectSysAgentItemList(qSysAgentItem);
        for (SysAgentItem agentItem : list) {
            Long id = agentItem.getId();
            if(templateMap.containsKey(id)) {
                TemplateInfoVo templateInfoVo = templateMap.get(id);
                templateInfoVo.setHandled(true);
                // 校验价格
                if(templateInfoVo.getAgentPrice() == null || templateInfoVo.getAgentPrice().compareTo(agentItem.getAgentPrice()) != 0) { //避免更高等级代理授权低价之后直接代理无法通过价格校验的问题
                    checkAgentPrice(templateInfoVo);
                }
                agentItem.setAgentPrice(templateInfoVo.getAgentPrice());
                agentItem.setExpireTime(templateInfoVo.getExpireTime());
                agentItem.setRemark(templateInfoVo.getRemark());
                sysAgentItemService.updateSysAgentItem(agentItem);
            } else {
                sysAgentItemService.deleteSysAgentItemById(id);
            }
        }
        // 添加授权
        for (TemplateInfoVo templateInfoVo : templateList) {
            if(!templateInfoVo.isHandled()) {
                SysAgentItem agentItem = new SysAgentItem();
                agentItem.setAgentId(sysAgent.getAgentId());
                agentItem.setTemplateType(templateInfoVo.getTemplateType());
                agentItem.setTemplateId(templateInfoVo.getTemplateId());
                agentItem.setCreateBy(getUsername());
                // 校验价格
                checkAgentPrice(templateInfoVo);
                agentItem.setAgentPrice(templateInfoVo.getAgentPrice());
                agentItem.setExpireTime(templateInfoVo.getExpireTime());
                agentItem.setRemark(templateInfoVo.getRemark());
                sysAgentItemService.insertSysAgentItem(agentItem);
            } else {
                logger.info("处理过{}", templateInfoVo.getReadableName());
            }
        }
        sysAgent.setUpdateBy(getUsername());
        return toAjax(sysAgentService.updateSysAgent(sysAgent));
    }

    private void checkAgentPrice(TemplateInfoVo templateInfoVo) {
        if (templateInfoVo.getAgentPrice() == null) {
            throw new ServiceException(templateInfoVo.getReadableName() + " 代理价格设置有误，子代理价格不能为空");
        }
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
            SysAgentItem item = sysAgentItemService.checkAgentItem(templateInfoVo, agent.getAgentId(), templateInfoVo.getTemplateType(), templateInfoVo.getTemplateId());
            if (templateInfoVo.getAgentPrice().compareTo(item.getAgentPrice()) < 0) {
                throw new ServiceException(templateInfoVo.getReadableName() + " 代理价格设置有误，子代理价格不能低于您的代理价格");
            }
        }
    }

    /**
     * 检查父级代理是否循环代理并填充path值
     */
    private void checkCircleParentAgentAndFillPath(SysAgent sysAgent) {
        String path;
        if (sysAgent.getParentAgentId() != null && sysAgent.getParentAgentId() != 0) {
            SysAgent parentAgent = sysAgentService.selectSysAgentByAgentId(sysAgent.getParentAgentId());
            if (parentAgent != null) {
                // 上级为代理
                if (StringUtils.isNotBlank(parentAgent.getPath()) && parentAgent.getPath().contains("/" + sysAgent.getAgentId() + "/")) {
                    throw new ServiceException("上级代理设置失败，指定的上级代理为该代理的子代理");
                } else {
                    if (StringUtils.isBlank(parentAgent.getPath())) {
                        path = "/" + sysAgent.getAgentId() + "/";
                    } else {
                        if (parentAgent.getPath().endsWith("/")) {
                            path = parentAgent.getPath() + sysAgent.getAgentId() + "/";
                        } else {
                            path = parentAgent.getPath() + "/" + sysAgent.getAgentId() + "/";
                        }
                    }
                }
                int maxSubAgentLevel = 3;
                try {
                    String s = configService.selectConfigByKey("sys.agent.maxSubAgentLevel");
                    if(StringUtils.isNotBlank(s)) {
                        maxSubAgentLevel = Integer.parseInt(s);
                    }
                    List<String> agentIdList = Arrays.stream(path.split("/")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
                    if(agentIdList.size() > maxSubAgentLevel) {
                        throw new ServiceException("系统限制代理层级最高为" + maxSubAgentLevel + "级，该代理所处层级已超过该层级，无法继续添加代理");
                    }
                } catch (NumberFormatException e) {
                    throw new ServiceException("【代理系统-最高代理层级】配置有误，请检查您的【参数设置】，" + e.getMessage() );
                }
            } else {
                throw new ServiceException("上级代理设置失败，指定的上级非代理商");
            }
        } else {
            path = "/" + sysAgent.getAgentId() + "/";
        }
        sysAgent.setPath(path);
    }

    private void fillParentAgent(SysAgent agent) {
        if (agent.getParentAgentId() != null && agent.getParentAgentId() != 0) {
            SysAgent parentAgent = sysAgentService.selectSysAgentByAgentId(agent.getParentAgentId());
            SysUser sysUser = sysUserService.selectUserById(parentAgent.getUserId());
            SysUser user = new SysUser();
            user.setUserId(sysUser.getUserId());
            user.setUserName(sysUser.getUserName());
            user.setNickName(sysUser.getNickName());
            agent.setParentUser(user);
        }
    }

    /**
     * 删除代理管理
     */
    @PreAuthorize("@ss.hasPermi('agent:agentUser:remove')")
    @AgentPermCheck("enableDeleteSubagent")
    @Log(title = "代理管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{agentIds}")
    public AjaxResult remove(@PathVariable Long[] agentIds) {
        // 如果有子代理不允许删除
        for (Long agentId : agentIds) {
            List<Long> subAgents = sysAgentService.getSubAgents(agentId);
            if (subAgents.size() > 0) {
                SysAgent agent = sysAgentService.selectSysAgentByAgentId(agentId);
                throw new ServiceException("代理[" + agent.getUser().getNickName() + "(" + agent.getUser().getUserName() + ")]存在子代理，不允许删除");
            }
        }
        // 移除agent角色
        for (Long agentId : agentIds) {
            SysAgent agent = sysAgentService.selectSysAgentByAgentId(agentId);
            if (!permissionService.hasRole(agent.getUserId(), "agent")) {
                SysRole sysRole = sysRoleService.selectRoleByKey("agent");
                sysRoleService.deleteAuthUsers(sysRole.getRoleId(), new Long[]{agent.getUserId()});
            }
        }
        return toAjax(sysAgentService.deleteSysAgentByAgentIds(agentIds));
    }

    /**
     * 获取所有作者和代理，除去参数的代理
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('agent:agentUser:list')")
    @AgentPermCheck
    @GetMapping("/listAgents/{agentId}")
    public TableDataInfo listDevelopersAndAgents(@PathVariable("agentId") Long agentId) {
        List<SysAgent> list = sysAgentService.selectSysAgentList(new SysAgent());
        List<AgentInfoVo> filterList = new ArrayList<>();
        for (SysAgent agent : list) {
            if (!agentId.equals(agent.getAgentId())) {
                AgentInfoVo info = new AgentInfoVo();
                info.setAgentId(agent.getAgentId());
                info.setUserId(agent.getUserId());
                if (agent.getUser() != null) {
                    info.setUserName(agent.getUser().getUserName());
                    info.setNickName(agent.getUser().getNickName());
                }
                filterList.add(info);
            }
        }
        return getDataTable(filterList);
    }

    /**
     * 获取所有非代理用户
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('agent:agentUser:list')")
    @AgentPermCheck
    @GetMapping("/listNonAgents")
    public TableDataInfo listNonAgents(String username) {
        List<AgentInfoVo> list = sysAgentService.getNonAgents(username);
        return getDataTable(list);
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('agent:agentUser:resetPwd')")
    @AgentPermCheck("enableUpdateSubagentPassword")
    @Log(title = "代理管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        sysUserService.checkUserAllowed(user);
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            SysAgent myAgent = sysAgentService.selectSysAgentByUserId(SecurityUtils.getUserId());
            SysAgent subAgent = sysAgentService.selectSysAgentByUserId(user.getUserId());
            if(subAgent == null) {
                throw new ServiceException("被操作的代理信息缺失", 400);
            }
            List<Long> subAgents = sysAgentService.getSubAgents(myAgent.getAgentId());
            if(!subAgents.contains(subAgent.getAgentId())) {
                throw new ServiceException("操作的代理非您的子代理", 400);
            }
        }
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(sysUserService.resetPwd(user));
    }

}
