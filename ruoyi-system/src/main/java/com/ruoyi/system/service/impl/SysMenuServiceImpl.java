package com.ruoyi.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.model.*;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.vo.menu.SysMenuDTO;
import com.ruoyi.system.mapper.SysMenuMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysRoleMenuMapper;
import com.ruoyi.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单 业务层处理
 *
 * @author ruoyi
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements ISysMenuService {

    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    final SysMenuMapper menuMapper;
    final SysRoleMapper roleMapper;
    final SysRoleMenuMapper roleMenuMapper;

    @Override
    public MenuInfo getMenuById(Long menuId) {
        return menuMapper.getMenuInfoById(menuId);
    }

    @Override
    public List<MenuListItem> listMenu(MenuQuery query) {
        return menuMapper.listMenu(query);
    }

    @Override
    public List<MenuTreeNode> getMenuTree(MenuQuery query) {
        return getMenuTree(listMenu(query));
    }

    @Override
    public <T extends SysMenu> List<MenuTreeNode> getMenuTree(Collection<T> menus) {
        List<MenuTreeNode> list = BeanUtil.copyToList(menus, MenuTreeNode.class);
        List<MenuTreeNode> treeNodeList = CollUtil.newArrayList();
        // 按parentCode进行分组
        Map<Long, List<MenuTreeNode>> treeNodeMap = list.stream()
                .filter(o -> o.getParentId() != null)
                .collect(Collectors.groupingBy(MenuTreeNode::getParentId));
        for (MenuTreeNode treeNode : list) {
            // 如果没有父级, 设置为根节点
            if (treeNode.getParentId() == null || treeNode.getParentId() == 0) {
                treeNodeList.add(treeNode);
            }
            // 为当前节点添加子节点
            List<MenuTreeNode> subTreeNodeList = treeNodeMap.get(treeNode.getMenuId());
            treeNode.setChildren(CollUtil.emptyIfNull(subTreeNodeList));
        }
        return treeNodeList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createMenu(MenuInfoEdit menuEdit) {
        SysMenu menu = BeanUtil.toBean(menuEdit, SysMenu.class);
        insertMenuBase(menu);
        return menu.getMenuId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public InfluenceScope updateMenu(MenuInfoEdit menuEdit) {
        Assert.notNull(menuEdit.getMenuId());
        SysMenu menu = menuMapper.selectById(menuEdit.getMenuId());
        Assert.notNull(menu);
        menu.setMenuName(menuEdit.getMenuName());
        menu.setParentId(menuEdit.getParentId());
        menu.setOrderNum(menuEdit.getOrderNum());
        menu.setPath(menuEdit.getPath());
        menu.setComponent(menuEdit.getComponent());
        menu.setQuery(menuEdit.getQuery());
        menu.setRouteName(menuEdit.getRouteName());
        menu.setIsFrame(menuEdit.getIsFrame());
        menu.setIsCache(menuEdit.getIsCache());
        menu.setMenuType(menuEdit.getMenuType());
        menu.setVisible(menuEdit.getVisible());
        List<Long> userIds = ListUtil.empty();
        if (!Constants.SYS_NORMAL_STATUS.equals(menuEdit.getStatus())) {
            checkMenuDeleteAccess(menu);
            userIds = roleMenuMapper.listRelUserId(menuEdit.getMenuId());
        }
        menu.setStatus(menuEdit.getStatus());
        menu.setPerms(menuEdit.getPerms());
        menu.setIcon(menuEdit.getIcon());
        menu.setRemark(menuEdit.getRemark());
        updateMenuBase(menu);
        return new InfluenceScope(1, new HashSet<>(userIds));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public InfluenceScope batchUpdateStatus(List<Long> menuIds, String status) {
        int c = 0;
        Set<Long> userIds = new HashSet<>();
        if (CollUtil.isNotEmpty(menuIds) && StrUtil.isNotEmpty(status)) {
            List<SysMenu> menus = menuMapper.selectByIds(menuIds);
            for (SysMenu menu : menus) {
                if (!Constants.SYS_NORMAL_STATUS.equals(status)) {
                    checkMenuDeleteAccess(menu);
                    userIds.addAll(roleMenuMapper.listRelUserId(menu.getMenuId()));
                }
                if (!status.equals(menu.getStatus())) {
                    menu.setStatus(status);
                    updateMenuBase(menu);
                    c++;
                }
            }
        }
        return new InfluenceScope(c, userIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public InfluenceScope batchDelete(List<Long> menuIds) {
        int c = 0;
        Set<Long> userIds = new HashSet<>();
        if (CollUtil.isNotEmpty(menuIds)) {
            List<SysMenu> menus = menuMapper.selectByIds(menuIds);
            for (SysMenu menu : menus) {
                if (Constants.UNDELETED.equals(menu.getDelFlag())) {
                    checkMenuDeleteAccess(menu);
                    userIds.addAll(roleMenuMapper.listRelUserId(menu.getMenuId()));
                    menu.setDelFlag(Constants.DELETED);
                    updateMenuBase(menu);
                    c++;
                }
            }
        }
        return new InfluenceScope(c, userIds);
    }

    /**
     * 新增菜单
     *
     * @param menu
     */
    private void insertMenuBase(SysMenu menu) {
        checkMenuBase(menu);
        Assert.isNull(menu.getMenuId());
        if (menu.getOrderNum() == null) {
            menu.setOrderNum(1);
        }
        menu.setStatus(Constants.SYS_NORMAL_STATUS);
        menu.setDelFlag(Constants.UNDELETED);
        menu.setVersion(0L);
        menuMapper.insert(menu);
    }


    /**
     * 修改菜单
     *
     * @param menu
     */
    private void updateMenuBase(SysMenu menu) {
        checkMenuBase(menu);
        Assert.notNull(menu.getMenuId());
        int c = menuMapper.updateById(menu);
        if (c == 0) {
            throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
        }
    }

    /**
     * 菜单基础信息校验
     *
     * @param menu
     */
    private void checkMenuBase(SysMenu menu) {
        Assert.notNull(menu);
        Assert.notEmpty(menu.getMenuName(), "菜单名称不能为空");
    }

    /**
     * 检查是否可删除/停用
     *
     * @param menu
     */
    private void checkMenuDeleteAccess(SysMenu menu) {
        boolean used = checkMenuExistRole(menu.getMenuId());
        if (used) {
            throw new ServiceException("菜单[" + menu.getMenuName() + "]使用中，无法停用或删除");
        }
        boolean used2 = hasChildByMenuId(menu.getMenuId());
        if (used2) {
            throw new ServiceException("菜单[" + menu.getMenuName() + "]存在子菜单，无法停用或删除");
        }
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @return 结果
     */
    private Boolean checkMenuNameUnique(Long id, String menuName, Long parentId) {
        Long menuId = ObjectUtils.defaultIfNull(id, -1L);
        SysMenu info = menuMapper.checkMenuNameUnique(menuName, parentId);
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    /**
     * 根据角色获取菜单列表
     *
     * @param roleId   角色ID
     * @param menuType 菜单类型
     * @return List<SysMenuDTO>
     */
    @Override
    public List<SysMenuDTO> selectMenuListByRoleIdAndMenuType(Long roleId, String menuType) {
        return this.menuMapper.selectMenuListByRoleIdAndMenuType(roleId, menuType);
    }


    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        int result = menuMapper.hasChildByMenuId(menuId);
        return result > 0;
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean checkMenuExistRole(Long menuId) {
        int result = roleMenuMapper.checkMenuExistRole(menuId);
        return result > 0;
    }


    /**
     * 内链域名特殊字符替换
     *
     * @return 替换后的内链域名
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, ".", ":"},
                new String[]{"", "", "", "/", "/"});
    }
}
