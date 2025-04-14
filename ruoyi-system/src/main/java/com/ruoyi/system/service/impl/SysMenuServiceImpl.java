package com.ruoyi.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.dto.menu.*;
import com.ruoyi.system.domain.vo.MetaVo;
import com.ruoyi.system.domain.vo.RouterVo;
import com.ruoyi.system.domain.vo.menu.SysMenuDTO;
import com.ruoyi.system.mapper.SysMenuMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysRoleMenuMapper;
import com.ruoyi.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.SecurityUtils.getUserId;
import static com.ruoyi.common.utils.SecurityUtils.getUsername;

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


    /**
     * 新增菜单
     *
     * @param menuDTO 菜单入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(MenuDTO menuDTO) {
        if (ObjectUtils.isNotEmpty(menuDTO.getMenuId())) {
            throw new ServiceException("新增菜单失败，menuId必须为空!", HttpStatus.ERROR);
        }
        if (!this.checkMenuNameUnique(menuDTO.getMenuId(), menuDTO.getMenuName(), menuDTO.getParentId())) {
            throw new ServiceException("新增菜单'" + menuDTO.getMenuName() + "'失败，菜单名称已存在", HttpStatus.ERROR);
        } else if (UserConstants.YES_FRAME.equals(menuDTO.getIsFrame()) && !StringUtils.ishttp(menuDTO.getPath())) {
            throw new ServiceException("新增菜单'" + menuDTO.getMenuName() + "'失败，地址必须以http(s)://开头", HttpStatus.ERROR);
        }
        SysMenu menu = BeanUtil.toBean(menuDTO, SysMenu.class);
        menu.setCreateBy(getUsername());
        return this.menuMapper.insert(menu);
    }

    /**
     * 编辑菜单
     *
     * @param menuDTO 编辑菜单
     * @return Integer
     */
    @Override
    @Transactional
    public Integer update(MenuDTO menuDTO) {
        // 编辑menuId不能为空
        Optional.ofNullable(menuDTO.getMenuId()).orElseThrow(() -> new ServiceException("菜单ID不能为空!", HttpStatus.ERROR));
        if (!this.checkMenuNameUnique(menuDTO.getMenuId(), menuDTO.getMenuName(), menuDTO.getParentId())) {
            throw new ServiceException("修改菜单'" + menuDTO.getMenuName() + "'失败，菜单名称已存在", HttpStatus.ERROR);
        } else if (UserConstants.YES_FRAME.equals(menuDTO.getIsFrame()) && !StringUtils.ishttp(menuDTO.getPath())) {
            throw new ServiceException("修改菜单'" + menuDTO.getMenuName() + "'失败，地址必须以http(s)://开头", HttpStatus.ERROR);
        } else if (menuDTO.getMenuId().equals(menuDTO.getParentId())) {
            throw new ServiceException("修改菜单'" + menuDTO.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        SysMenu menu = Optional.ofNullable(this.menuMapper.selectOne(new LambdaQueryWrapper<SysMenu>()
                        .eq(SysMenu::getMenuId, menuDTO.getMenuId()).eq(SysMenu::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("菜单不存在!", HttpStatus.ERROR));
        menu.setUpdateBy(getUsername());
        menu.setUpdateTime(new Date());
        BeanUtil.copyProperties(menuDTO, menu);
        return this.menuMapper.updateById(menu);
    }

    /**
     * 菜单详情
     *
     * @param menuId 菜单ID
     * @return MenuResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public MenuResDTO getById(Long menuId) {
        SysMenu menu = Optional.ofNullable(this.menuMapper.selectOne(new LambdaQueryWrapper<SysMenu>()
                        .eq(SysMenu::getMenuId, menuId).eq(SysMenu::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("菜单不存在!", HttpStatus.ERROR));
        return BeanUtil.toBean(menu, MenuResDTO.class).setMenuId(menuId);
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     * @return Integer
     */
    @Override
    @Transactional
    public Integer delete(Long menuId) {
        SysMenu menu = Optional.ofNullable(this.menuMapper.selectOne(new LambdaQueryWrapper<SysMenu>()
                        .eq(SysMenu::getMenuId, menuId).eq(SysMenu::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("菜单不存在!", HttpStatus.ERROR));
        if (this.hasChildByMenuId(menuId)) {
            throw new ServiceException("存在子菜单,不允许删除", HttpStatus.ERROR);
        }
        if (this.checkMenuExistRole(menuId)) {
            throw new ServiceException("菜单已分配,不允许删除", HttpStatus.ERROR);
        }
        menu.setDelFlag(Constants.DELETED);
        return this.menuMapper.updateById(menu);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuResDTO> list(MenuListDTO listDTO) {
        Long userId = getUserId();
        List<SysMenu> menuList = null;
        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(userId)) {
            LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getDelFlag, Constants.UNDELETED);
            if (StringUtils.isNotBlank(listDTO.getMenuName())) {
                queryWrapper.like(SysMenu::getMenuName, listDTO.getMenuName());
            }
            if (StringUtils.isNotBlank(listDTO.getStatus())) {
                queryWrapper.eq(SysMenu::getStatus, listDTO.getStatus());
            }
            menuList = this.menuMapper.selectList(queryWrapper);
        } else {
            menuList = this.menuMapper.getMenuListByUserId(listDTO.getMenuName(), listDTO.getStatus(), userId);
        }
        return BeanUtil.copyToList(menuList, MenuResDTO.class);
    }

    /**
     * 获取菜单列表树
     *
     * @param listDTO 菜单查询入参
     * @return List<MenuResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<TreeSelectDTO> treeSelect(MenuListDTO listDTO) {
        List<MenuResDTO> list = this.list(listDTO);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<MenuResDTO> menuTrees = getMenuTree(list);
        return menuTrees.stream().map(x -> new TreeSelectDTO() {{
            setId(x.getMenuId());
            setLabel(x.getMenuName());
            setChildren(x.getChildren().stream().map(x -> new TreeSelectDTO() {{
                setId(x.getMenuId());
                setLabel(x.getMenuName());
            }}).collect(Collectors.toList()));
        }}).collect(Collectors.toList());
    }

    /**
     * 获取用户选中的菜单数据
     *
     * @param roleId 角色ID
     * @return UserRoleTreeSelectDTO
     */
    @Override
    @Transactional(readOnly = true)
    public UserRoleTreeSelectDTO roleMenuTreeSelect(Long roleId) {
        // 当前用户所有的菜单
        List<MenuResDTO> list = this.list(new MenuListDTO());
        // 用户选中的菜单
        List<Long> checkedIdList = this.selectMenuListByRoleId(roleId);
        // 将用户的菜单转化为tree
        List<MenuResDTO> menuTrees = getMenuTree(list);
        List<TreeSelectDTO> tree = menuTrees.stream().map(x -> new TreeSelectDTO() {{
            setId(x.getMenuId());
            setLabel(x.getMenuName());
            setChildren(x.getChildren().stream().map(x -> new TreeSelectDTO() {{
                setId(x.getMenuId());
                setLabel(x.getMenuName());
            }}).collect(Collectors.toList()));
        }}).collect(Collectors.toList());
        return new UserRoleTreeSelectDTO() {{
            setTree(tree);
            setCheckedIdList(checkedIdList);
        }};
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    private List<MenuResDTO> getMenuTree(List<MenuResDTO> menus) {
        List<MenuResDTO> returnList = new ArrayList<>();
        List<Long> tempList = menus.stream().map(MenuResDTO::getMenuId).collect(Collectors.toList());
        for (Iterator<MenuResDTO> iterator = menus.iterator(); iterator.hasNext(); ) {
            MenuResDTO menu =  iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t    子节点
     */
    private void recursionFn(List<MenuResDTO> list, MenuResDTO t) {
        // 得到子节点列表
        List<MenuResDTO> childList = getChildList(list, t);
        t.setChildren(childList);
        for (MenuResDTO tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<MenuResDTO> getChildList(List<MenuResDTO> list, MenuResDTO t) {
        List<MenuResDTO> tlist = new ArrayList<>();
        Iterator<MenuResDTO> it = list.iterator();
        while (it.hasNext()) {
            MenuResDTO n = it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<MenuResDTO> list, MenuResDTO t) {
        return getChildList(list, t).size() > 0;
    }



    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
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
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(Long userId) {
        return selectMenuList(new SysMenu(), userId);
    }

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId) {
        List<SysMenu> menuList = null;
        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(userId)) {
            menuList = menuMapper.selectMenuList(menu);
        } else {
//            menu.getParams().put("userId", userId);
            menuList = menuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = menuMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByRoleId(Long roleId) {
        List<String> perms = menuMapper.selectMenuPermsByRoleId(roleId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名称
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menus = null;
        if (SecurityUtils.isAdmin(userId)) {
            menus = menuMapper.selectMenuTreeAll();
        } else {
            menus = menuMapper.selectMenuTreeByUserId(userId);
        }
        return getChildPerms(menus, 0);
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);
        return menuMapper.selectMenuListByRoleId(roleId, Boolean.TRUE);
//        return menuMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));

            List<SysMenu> cMenus = new ArrayList<>();
//            List<SysMenu> cMenus = menu.getChildren();

            if (StringUtils.isNotEmpty(cMenus) && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(getRouteName(menu.getRouteName(), menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(getRouteName(menu.getRouteName(), routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        List<Long> tempList = menus.stream().map(SysMenu::getMenuId).collect(Collectors.toList());
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext(); ) {
            SysMenu menu = (SysMenu) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public SysMenu selectMenuById(Long menuId) {
        return menuMapper.selectMenuById(menuId);
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
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(SysMenu menu) {
        return menuMapper.insertMenu(menu);
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int updateMenu(SysMenu menu) {
        return menuMapper.updateMenu(menu);
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteMenuById(Long menuId) {
        return menuMapper.deleteMenuById(menuId);
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public boolean checkMenuNameUnique(SysMenu menu) {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu) {
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            return StringUtils.EMPTY;
        }
        return getRouteName(menu.getRouteName(), menu.getPath());
    }

    /**
     * 获取路由名称，如没有配置路由名称则取路由地址
     *
     * @param name 路由名称
     * @param path 路由地址
     * @return 路由名称（驼峰格式）
     */
    public String getRouteName(String name, String path) {
        String routerName = StringUtils.isNotEmpty(name) ? name : path;
        return StringUtils.capitalize(routerName);
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenu menu) {
        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t    子节点
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
//        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
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
