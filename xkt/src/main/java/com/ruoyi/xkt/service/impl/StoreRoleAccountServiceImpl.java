package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.StoreRole;
import com.ruoyi.xkt.domain.StoreRoleAccount;
import com.ruoyi.xkt.dto.storeRoleAccount.*;
import com.ruoyi.xkt.mapper.StoreRoleAccountMapper;
import com.ruoyi.xkt.mapper.StoreRoleMapper;
import com.ruoyi.xkt.service.IStoreRoleAccountService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 档口子角色账号Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreRoleAccountServiceImpl implements IStoreRoleAccountService {

    final StoreRoleAccountMapper storeRoleAccMapper;
    final StoreRoleMapper storeRoleMapper;

    /**
     * 新增档口子账号
     *
     * @param accDTO 子账号入参
     * @return
     */
    @Override
    @Transactional
    public Integer insert(StoreRoleAccDTO accDTO) {
        // 当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        StoreRoleAccount account = BeanUtil.toBean(accDTO, StoreRoleAccount.class);
        account.setOperatorId(loginUser.getUserId());
        account.setOperatorName(loginUser.getUsername());
        // 若为系统未注册用户，则重新走注册电商卖家角色流程
        if (ObjectUtils.isEmpty(accDTO.getUserId())) {

            // TODO 走注册电商卖家角色流程
            // TODO 走注册电商卖家角色流程
            // TODO 走注册电商卖家角色流程
            // TODO 走注册电商卖家角色流程

            account.setUserId(1000L);
        }
        return this.storeRoleAccMapper.insert(account);
    }

    /**
     * 编辑档口子账号
     *
     * @param accUpdateDTO 档口子账号
     * @return
     */
    @Override
    @Transactional
    public Integer update(StoreRoleAccUpdateDTO accUpdateDTO) {
        StoreRoleAccount account = Optional.ofNullable(this.storeRoleAccMapper.selectOne(new LambdaQueryWrapper<StoreRoleAccount>()
                        .eq(StoreRoleAccount::getId, accUpdateDTO.getStoreRoleAccId()).eq(StoreRoleAccount::getDelFlag, Constants.UNDELETED)
                        .eq(StoreRoleAccount::getStoreId, accUpdateDTO.getStoreId())))
                .orElseThrow(() -> new RuntimeException("未找到该子账号信息"));
        account.setAccountName(accUpdateDTO.getAccountName());
        account.setStoreRoleId(accUpdateDTO.getStoreRoleId());
        return this.storeRoleAccMapper.updateById(account);
    }

    /**
     * 档口子账号列表
     *
     * @param listDTO
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreRoleAccResDTO> list(StoreRoleAccListDTO listDTO) {
        List<StoreRoleAccount> accountList = this.storeRoleAccMapper.selectList(new LambdaQueryWrapper<StoreRoleAccount>()
                .eq(StoreRoleAccount::getStoreId, listDTO.getStoreId()).eq(StoreRoleAccount::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(accountList)) {
            return new ArrayList<>();
        }
        // 找到所有的子账号角色
        List<StoreRole> roleList = Optional.ofNullable(this.storeRoleMapper.selectList(new LambdaQueryWrapper<StoreRole>()
                        .in(StoreRole::getId, accountList.stream().map(StoreRoleAccount::getStoreRoleId).collect(Collectors.toList()))
                        .eq(StoreRole::getDelFlag, Constants.UNDELETED).eq(StoreRole::getStoreId, listDTO.getStoreId())))
                .orElseThrow(() -> new RuntimeException("未找到子账号角色信息"));
        Map<Long, StoreRole> roleMap = roleList.stream().collect(Collectors.toMap(StoreRole::getId, storeRole -> storeRole));
        return accountList.stream().map(account -> {
            StoreRoleAccResDTO accResDTO = BeanUtil.toBean(account, StoreRoleAccResDTO.class);
            accResDTO.setStoreRoleAccId(account.getId());
            accResDTO.setStoreRoleName(ObjectUtils.isNotEmpty(roleMap.get(account.getStoreRoleId()))
                    ? roleMap.get(account.getStoreRoleId()).getRoleName() : "");
            return accResDTO;
        }).collect(Collectors.toList());
    }

    /**
     * 获取档口子账号详情
     *
     * @param storeRoleAccId 档口子账号ID
     * @return StoreRoleAccDetailResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreRoleAccDetailResDTO selectByStoreRoleAccId(Long storeRoleAccId) {
        StoreRoleAccount account = Optional.ofNullable(this.storeRoleAccMapper.selectOne(new LambdaQueryWrapper<StoreRoleAccount>()
                        .eq(StoreRoleAccount::getId, storeRoleAccId).eq(StoreRoleAccount::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new RuntimeException("未找到该子账号信息"));
        // 找打当前档口所有的子角色
        List<StoreRole> roleList = this.storeRoleMapper.selectList(new LambdaQueryWrapper<StoreRole>()
                .eq(StoreRole::getStoreId, account.getStoreId()).eq(StoreRole::getDelFlag, Constants.UNDELETED));
        return BeanUtil.toBean(account, StoreRoleAccDetailResDTO.class)
                .setStoreRoleAccId(account.getId())
                .setRoleList(roleList.stream().map(x -> new StoreRoleAccDetailResDTO.StoreRoleDTO() {{
                    setStoreRoleId(x.getId());
                    setStoreRoleName(x.getRoleName());
                }}).collect(Collectors.toList()));
    }

    /**
     * 停用/启用档口子账号
     *
     * @param updateStatusDTO 停用/启用账号子账号入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateAccountStatus(StoreRoleAccUpdateStatusDTO updateStatusDTO) {
        StoreRoleAccount account = Optional.ofNullable(this.storeRoleAccMapper.selectOne(new LambdaQueryWrapper<StoreRoleAccount>()
                        .eq(StoreRoleAccount::getId, updateStatusDTO.getStoreRoleAccId()).eq(StoreRoleAccount::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new RuntimeException("未找到该子账号信息"));
        account.setDelFlag(updateStatusDTO.getDelFlag());
        return this.storeRoleAccMapper.updateById(account);
    }

}
