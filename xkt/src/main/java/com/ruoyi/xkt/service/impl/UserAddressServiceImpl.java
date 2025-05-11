package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.XktBaseEntity;
import com.ruoyi.xkt.domain.UserAddress;
import com.ruoyi.xkt.dto.express.ExpressStructAddressDTO;
import com.ruoyi.xkt.dto.express.UserAddressInfoDTO;
import com.ruoyi.xkt.mapper.UserAddressMapper;
import com.ruoyi.xkt.service.IExpressService;
import com.ruoyi.xkt.service.IUserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户收货地址Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class UserAddressServiceImpl implements IUserAddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private IExpressService expressService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserAddressInfoDTO createUserAddress(UserAddressInfoDTO userAddressInfoDTO) {
        Assert.notNull(userAddressInfoDTO.getUserId());
        Assert.notEmpty(userAddressInfoDTO.getReceiveName());
        Assert.notEmpty(userAddressInfoDTO.getReceivePhone());
        Assert.notEmpty(userAddressInfoDTO.getAddress());
        UserAddress userAddress = BeanUtil.toBean(userAddressInfoDTO, UserAddress.class);
        ExpressStructAddressDTO structAddressDTO = expressService.parseNamePhoneAddress(userAddressInfoDTO.getAddress());
        Assert.notEmpty(structAddressDTO.getProvinceCode(), "省编码异常");
        Assert.notEmpty(structAddressDTO.getProvinceName(), "省名称异常");
        Assert.notEmpty(structAddressDTO.getCityCode(), "市编码异常");
        Assert.notEmpty(structAddressDTO.getCityName(), "市名称异常");
        Assert.notEmpty(structAddressDTO.getCountyCode(), "区县编码异常");
        Assert.notEmpty(structAddressDTO.getCountyName(), "区县名称异常");
        Assert.notEmpty(structAddressDTO.getDetailAddress(), "详细地址异常");
        userAddress.setProvinceCode(structAddressDTO.getProvinceCode());
        userAddress.setCityCode(structAddressDTO.getCityCode());
        userAddress.setDistrictCode(structAddressDTO.getCountyCode());
        userAddress.setDetailAddress(structAddressDTO.getDetailAddress());
        userAddressMapper.insert(userAddress);
        return getUserAddress(userAddress.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserAddressInfoDTO copyUserAddress(Long id) {
        UserAddress userAddress = userAddressMapper.selectById(id);
        Assert.notNull(userAddress);
        userAddress.setId(null);
        userAddressMapper.insert(userAddress);
        return getUserAddress(userAddress.getId());
    }

    @Override
    public UserAddressInfoDTO getUserAddress(Long id) {
        Assert.notNull(id);
        UserAddress userAddress = userAddressMapper.selectById(id);
        UserAddressInfoDTO info = BeanUtil.toBean(userAddress, UserAddressInfoDTO.class);
        Map<String, String> regionNameMap = expressService.getRegionNameMapCache();
        info.setProvinceName(regionNameMap.get(info.getProvinceCode()));
        info.setCityName(regionNameMap.get(info.getCityCode()));
        info.setDistrictName(regionNameMap.get(info.getDistrictCode()));
        info.setCountyCode(info.getDistrictCode());
        info.setCountyName(regionNameMap.get(info.getCountyCode()));
        return info;
    }

    @Override
    public List<UserAddressInfoDTO> listByUser(Long userId) {
        Assert.notNull(userId);
        List<UserAddress> userAddressList = userAddressMapper.selectList(Wrappers.lambdaQuery(UserAddress.class)
                .eq(UserAddress::getUserId, userId)
                .eq(XktBaseEntity::getDelFlag, Constants.UNDELETED));
        if (CollUtil.isEmpty(userAddressList)) {
            return ListUtil.empty();
        }
        Map<String, String> regionNameMap = expressService.getRegionNameMapCache();
        return userAddressList.stream().map(o -> {
            UserAddressInfoDTO info = BeanUtil.toBean(o, UserAddressInfoDTO.class);
            info.setProvinceName(regionNameMap.get(info.getProvinceCode()));
            info.setCityName(regionNameMap.get(info.getCityCode()));
            info.setDistrictName(regionNameMap.get(info.getDistrictCode()));
            info.setCountyCode(info.getDistrictCode());
            info.setCountyName(regionNameMap.get(info.getCountyCode()));
            return info;
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserAddressInfoDTO modifyUserAddress(UserAddressInfoDTO userAddressInfoDTO) {
        Assert.notNull(userAddressInfoDTO.getId());
        Assert.notNull(userAddressInfoDTO.getUserId());
        Assert.notEmpty(userAddressInfoDTO.getReceiveName());
        Assert.notEmpty(userAddressInfoDTO.getReceivePhone());
        Assert.notEmpty(userAddressInfoDTO.getAddress());
        UserAddress userAddress = userAddressMapper.selectById(userAddressInfoDTO.getId());
        Assert.notNull(userAddress);
        ExpressStructAddressDTO structAddressDTO = expressService.parseNamePhoneAddress(userAddressInfoDTO.getAddress());
        Assert.notEmpty(structAddressDTO.getProvinceCode(), "省编码异常");
        Assert.notEmpty(structAddressDTO.getProvinceName(), "省名称异常");
        Assert.notEmpty(structAddressDTO.getCityCode(), "市编码异常");
        Assert.notEmpty(structAddressDTO.getCityName(), "市名称异常");
        Assert.notEmpty(structAddressDTO.getCountyCode(), "区县编码异常");
        Assert.notEmpty(structAddressDTO.getCountyName(), "区县名称异常");
        Assert.notEmpty(structAddressDTO.getDetailAddress(), "详细地址异常");
        userAddress.setUserId(userAddressInfoDTO.getUserId());
        userAddress.setReceiveName(userAddressInfoDTO.getReceiveName());
        userAddress.setReceivePhone(userAddressInfoDTO.getReceivePhone());
        userAddress.setProvinceCode(structAddressDTO.getProvinceCode());
        userAddress.setCityCode(structAddressDTO.getCityCode());
        userAddress.setDistrictCode(structAddressDTO.getCountyCode());
        userAddress.setDetailAddress(structAddressDTO.getDetailAddress());
        userAddressMapper.updateById(userAddress);
        return getUserAddress(userAddress.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUserAddress(Long id) {
        Assert.notNull(id);
        UserAddress userAddress = new UserAddress();
        userAddress.setId(id);
        userAddress.setDelFlag(Constants.DELETED);
        userAddressMapper.updateById(userAddress);
    }
}
