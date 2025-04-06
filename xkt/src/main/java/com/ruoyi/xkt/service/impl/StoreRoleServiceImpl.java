package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.StoreRole;
import com.ruoyi.xkt.domain.StoreRoleAccount;
import com.ruoyi.xkt.domain.StoreRoleMenu;
import com.ruoyi.xkt.dto.storeRole.StoreRoleDTO;
import com.ruoyi.xkt.dto.storeRole.StoreRoleListDTO;
import com.ruoyi.xkt.dto.storeRole.StoreRoleResDTO;
import com.ruoyi.xkt.dto.storeRole.StoreRoleUpdateStatusDTO;
import com.ruoyi.xkt.mapper.StoreRoleAccountMapper;
import com.ruoyi.xkt.mapper.StoreRoleMapper;
import com.ruoyi.xkt.mapper.StoreRoleMenuMapper;
import com.ruoyi.xkt.service.IStoreRoleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 档口子角色Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreRoleServiceImpl implements IStoreRoleService {

    final StoreRoleMapper storeRoleMapper;
    final StoreRoleMenuMapper storeRoleMenuMapper;
    final StoreRoleAccountMapper storeRoleAccMapper;


    /**
     * 新增档口子角色
     *
     * @param storeRoleDTO 档口子角色
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreRole(StoreRoleDTO storeRoleDTO) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        StoreRole storeRole = BeanUtil.toBean(storeRoleDTO, StoreRole.class);

        // TODO 暂时列为-1
        // TODO 暂时列为-1
        // TODO 暂时列为-1
        storeRole.setRoleId(1000L);


        storeRole.setOperatorId(loginUser.getUserId());
        storeRole.setOperatorName(loginUser.getUsername());
        int count = this.storeRoleMapper.insert(storeRole);
        // 新增档口子角色菜单
        List<StoreRoleMenu> roleMenuList = storeRoleDTO.getMenuList().stream().map(menuName -> {
            StoreRoleMenu storeRoleMenu = new StoreRoleMenu();
            return storeRoleMenu.setStoreId(storeRoleDTO.getStoreId())
                    .setStoreRoleId(storeRole.getId()).setMenuName(menuName);
        }).collect(Collectors.toList());
        this.storeRoleMenuMapper.insert(roleMenuList);
        return count;
    }

    /**
     * 更新档口子角色
     *
     * @param storeRoleDTO 档口子角色信息
     * @return
     */
    @Override
    @Transactional
    public Integer update(StoreRoleDTO storeRoleDTO) {
        Optional.ofNullable(storeRoleDTO.getStoreRoleId()).orElseThrow(() -> new ServiceException("档口角色ID不能为空!", HttpStatus.ERROR));
        StoreRole storeRole = Optional.ofNullable(this.storeRoleMapper.selectOne(new LambdaQueryWrapper<StoreRole>()
                        .eq(StoreRole::getId, storeRoleDTO.getStoreRoleId()).eq(StoreRole::getDelFlag, Constants.UNDELETED)
                        .eq(StoreRole::getStoreId, storeRoleDTO.getStoreId())))
                .orElseThrow(() -> new ServiceException("档口角色不存在!", HttpStatus.ERROR));
        BeanUtil.copyProperties(storeRoleDTO, storeRole);
        int count = this.storeRoleMapper.updateById(storeRole);
        // 将旧的子角色菜单置为无效
        List<StoreRoleMenu> menuList = this.storeRoleMenuMapper.selectList(new LambdaQueryWrapper<StoreRoleMenu>()
                .eq(StoreRoleMenu::getStoreRoleId, storeRoleDTO.getStoreRoleId()).eq(StoreRoleMenu::getDelFlag, Constants.UNDELETED)
                .eq(StoreRoleMenu::getStoreId, storeRoleDTO.getStoreId()));
        if (CollectionUtils.isNotEmpty(menuList)) {
            menuList.forEach(x -> x.setDelFlag(Constants.DELETED));
            this.storeRoleMenuMapper.updateById(menuList);
        }
        // 新增档口子角色菜单
        List<StoreRoleMenu> roleMenuList = storeRoleDTO.getMenuList().stream().map(menuName -> {
            StoreRoleMenu storeRoleMenu = new StoreRoleMenu();
            return storeRoleMenu.setStoreId(storeRoleDTO.getStoreId())
                    .setStoreRoleId(storeRole.getId()).setMenuName(menuName);
        }).collect(Collectors.toList());
        this.storeRoleMenuMapper.insert(roleMenuList);
        return count;
    }

    /**
     * 查询档口子角色菜单详情
     *
     * @param storeRoleId 档口子角色ID
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public StoreRoleDTO selectByStoreRoleId(Long storeRoleId) {
        StoreRole storeRole = Optional.ofNullable(this.storeRoleMapper.selectOne(new LambdaQueryWrapper<StoreRole>()
                        .eq(StoreRole::getId, storeRoleId).eq(StoreRole::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口子角色不存在!", HttpStatus.ERROR));
        // 找到档口子角色菜单
        List<StoreRoleMenu> menuList = this.storeRoleMenuMapper.selectList(new LambdaQueryWrapper<StoreRoleMenu>()
                .eq(StoreRoleMenu::getStoreRoleId, storeRoleId).eq(StoreRoleMenu::getDelFlag, Constants.UNDELETED)
                .eq(StoreRoleMenu::getStoreId, storeRole.getStoreId()));

        // TODO 还要返回档口角色所有的菜单
        // TODO 还要返回档口角色所有的菜单
        // TODO 还要返回档口角色所有的菜单
        // TODO 还要返回档口角色所有的菜单


        return BeanUtil.toBean(storeRole, StoreRoleDTO.class)
                .setMenuList(menuList.stream().map(StoreRoleMenu::getMenuName).collect(Collectors.toList()));
    }

    /**
     * 档口子角色分页查询
     *
     * @param pageDTO 档口子角色分页查询入参
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreRoleResDTO> list(StoreRoleListDTO pageDTO) {
        List<StoreRole> storeRoleList = this.storeRoleMapper.selectList(new LambdaQueryWrapper<StoreRole>()
                .eq(StoreRole::getStoreId, pageDTO.getStoreId()).eq(StoreRole::getDelFlag, Constants.UNDELETED));
        return storeRoleList.stream().map(x -> BeanUtil.toBean(x, StoreRoleResDTO.class).setStoreRoleId(x.getId()))
                .collect(Collectors.toList());
    }

    /**
     * 更新档口子角色状态
     *
     * @param updateStatusDTO 子角色状态更新入参
     * @return
     */
    @Override
    @Transactional
    public Integer updateRoleStatus(StoreRoleUpdateStatusDTO updateStatusDTO) {
        StoreRole storeRole = Optional.ofNullable(this.storeRoleMapper.selectOne(new LambdaQueryWrapper<StoreRole>()
                        .eq(StoreRole::getId, updateStatusDTO.getStoreRoleId()).eq(StoreRole::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口角色不存在!", HttpStatus.ERROR));
        storeRole.setDelFlag(updateStatusDTO.getDelFlag());
        // 如果是启用变为停用，则还需将子角色菜单置为无效，子角色账号置为无效
        if (Objects.equals(updateStatusDTO.getDelFlag(), Constants.DELETED)) {
            List<StoreRoleMenu> menuList = this.storeRoleMenuMapper.selectList(new LambdaQueryWrapper<StoreRoleMenu>()
                    .eq(StoreRoleMenu::getStoreRoleId, updateStatusDTO.getStoreRoleId()).eq(StoreRoleMenu::getDelFlag, Constants.UNDELETED)
                    .eq(StoreRoleMenu::getStoreId, storeRole.getStoreId()));
            if (CollectionUtils.isNotEmpty(menuList)) {
                menuList.forEach(x -> x.setDelFlag(Constants.DELETED));
                this.storeRoleMenuMapper.updateById(menuList);
            }
            // 找到子角色账号，并置为无效
            List<StoreRoleAccount> accountList = this.storeRoleAccMapper.selectList(new LambdaQueryWrapper<StoreRoleAccount>()
                    .eq(StoreRoleAccount::getStoreRoleId, updateStatusDTO.getStoreRoleId()).eq(StoreRoleAccount::getDelFlag, Constants.UNDELETED)
                    .eq(StoreRoleAccount::getStoreId, storeRole.getStoreId()));
            if (CollectionUtils.isNotEmpty(accountList)) {
                accountList.forEach(x -> x.setDelFlag(Constants.DELETED));
                this.storeRoleAccMapper.updateById(accountList);
            }
        }
        return this.storeRoleMapper.updateById(storeRole);
    }

}
