package com.ruoyi.web.controller.agent;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.domain.vo.AgentInfoVo;
import com.ruoyi.agent.service.ISysAgentUserService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    /**
     * 查询代理管理列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentUser:list')")
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
    @Log(title = "代理管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysAgent sysAgent) {
        if (sysAgent.getParentAgentId() == null || sysAgent.getParentAgentId() <= 0) {
            sysAgent.setParentAgentId(null);
        }
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
            sysAgentService.checkAgent(agent, true);
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
        return AjaxResult.success();
    }

    /**
     * 修改代理管理
     */
    @PreAuthorize("@ss.hasPermi('agent:agentUser:edit')")
    @Log(title = "代理管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAgent sysAgent) {
        if (sysAgent.getParentAgentId() == null || sysAgent.getParentAgentId() <= 0) {
            sysAgent.setParentAgentId(null);
        }
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
            sysAgentService.checkAgent(agent, true);
            if (sysAgent.getParentAgentId() == null) {
                sysAgent.setParentAgentId(agent.getAgentId());
            }
        }
        if (Objects.equals(sysAgent.getParentAgentId(), sysAgent.getAgentId())) {
            throw new ServiceException("代理与上级代理不能为同一个用户");
        }
        checkCircleParentAgentAndFillPath(sysAgent);
        // 更新代理的所有下级代理
        String oldPath = sysAgentService.selectSysAgentByAgentId(sysAgent.getAgentId()).getPath();
        List<Long> subAgentIds = sysAgentService.getSubAgents(sysAgent.getAgentId());
        for (Long subAgentId : subAgentIds) {
            SysAgent agent = sysAgentService.selectSysAgentByAgentId(subAgentId);
            agent.setPath(agent.getPath().replace(oldPath, sysAgent.getPath()));
            sysAgentService.updateSysAgent(agent);
        }
        sysAgent.setUpdateBy(getUsername());
        return toAjax(sysAgentService.updateSysAgent(sysAgent));
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
    @GetMapping("/listNonAgents")
    public TableDataInfo listNonAgents(String username) {
        List<AgentInfoVo> list = sysAgentService.getNonAgents(username);
        return getDataTable(list);
    }

}
