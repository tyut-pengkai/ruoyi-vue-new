package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.dto.store.*;
import com.ruoyi.xkt.dto.storeCertificate.StoreCertResDTO;
import com.ruoyi.xkt.enums.StoreStatus;
import com.ruoyi.xkt.mapper.StoreMapper;
import com.ruoyi.xkt.service.IStoreCertificateService;
import com.ruoyi.xkt.service.IStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 档口Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements IStoreService {

    final StoreMapper storeMapper;
    final IStoreCertificateService storeCertService;
    final SysUserMapper userMapper;

    /**
     * 注册时新增档口数据
     *
     * @param createDTO 档口基础数据
     * @return 结果
     */
    @Override
    @Transactional
    public int create(StoreCreateDTO createDTO) {
        Store store = new Store();
        // 初始化注册时只需绑定用户ID即可
        store.setUserId(createDTO.getUserId());
        // 默认档口状态为：已注册
        store.setStoreStatus(StoreStatus.REGISTERED.getValue());
        // 当前时间往后推1年为试用期时间
        Date oneYearAfter = Date.from(LocalDate.now().plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        store.setTrialEndTime(oneYearAfter);
        return this.storeMapper.insert(store);
    }

    /**
     * 档口分页数据
     *
     * @param pageDTO 查询入参
     * @return Page<StorePageResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StorePageResDTO> page(StorePageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<StorePageResDTO> storeList = this.storeMapper.selectStorePage(pageDTO);
        return Page.convert(new PageInfo<>(storeList));
    }

    /**
     * 更新档口启用/停用状态
     *
     * @param delFlagDTO 入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateDelFlag(StoreUpdateDelFlagDTO delFlagDTO) {
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, delFlagDTO.getStoreId())))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        store.setDelFlag(delFlagDTO.getDelFlag() ? Constants.UNDELETED : Constants.DELETED);
        return storeMapper.updateById(store);
    }

    /**
     * 审核档口
     *
     * @param auditDTO 审核入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer approve(StoreAuditDTO auditDTO) {
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, auditDTO.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        if (auditDTO.getApprove()) {
            // 如果档口状态不为 待审核 或 审核驳回 则报错
            if (!Objects.equals(store.getStoreStatus(), StoreStatus.UN_AUDITED.getValue()) ||
                !Objects.equals(store.getStoreStatus(), StoreStatus.AUDIT_REJECTED.getValue())) {
                throw new ServiceException("当前状态不为待审核 或 审核驳回，不可审核!", HttpStatus.ERROR);
            }
            store.setStoreStatus(StoreStatus.TRIAL_PERIOD.getValue());
        } else {
            store.setStoreStatus(StoreStatus.AUDIT_REJECTED.getValue());
            store.setRejectReason(auditDTO.getRejectReason());
        }
        return this.storeMapper.updateById(store);
    }

    /**
     * 获取档口基本信息
     *
     * @param storeId 档口ID
     * @return StoreBasicResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreBasicResDTO getInfo(Long storeId) {
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                .eq(Store::getId, storeId))).orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        StoreBasicResDTO resDTO = BeanUtil.toBean(store, StoreBasicResDTO.class);
        resDTO.setStoreId(storeId);

        // TODO 用户名称
        // TODO 用户名称
        // TODO 用户名称

        // 获取档口绑定的用户
        /*SysUser user = Optional.ofNullable(this.userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserId, store.getUserId()))).orElseThrow(() -> new ServiceException("用户不存在!", HttpStatus.ERROR));
        resDTO.setUserName(user.getUserName());*/
        return resDTO;
    }

    /**
     * 审核时获取档口信息
     *
     * @param storeId 档口ID
     * @return StoreApproveResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreApproveResDTO getApproveInfo(Long storeId) {
        return new StoreApproveResDTO(){{
            setBasic(getInfo(storeId));
            setCertificate(storeCertService.getInfo(storeId));
        }};
    }

    /**
     * 修改档口基本信息
     *
     * @param storeUpdateDTO 档口
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStore(StoreUpdateDTO storeUpdateDTO) {
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, storeUpdateDTO.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        BeanUtil.copyProperties(storeUpdateDTO, store);
        // 如果当前状态为认证营业执照
        if (Objects.equals(store.getStoreStatus(), StoreStatus.AUTH_LICENSE.getValue())) {
            // 将档口状态更改为：认证基本信息
            store.setStoreStatus(StoreStatus.AUTH_BASE_INFO.getValue());
        }
        return storeMapper.updateById(store);
    }

}
