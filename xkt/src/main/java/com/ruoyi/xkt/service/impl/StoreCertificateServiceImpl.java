package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreCertificate;
import com.ruoyi.xkt.mapper.StoreCertificateMapper;
import com.ruoyi.xkt.service.IStoreCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口认证Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreCertificateServiceImpl implements IStoreCertificateService {
    @Autowired
    private StoreCertificateMapper storeCertificateMapper;

    /**
     * 查询档口认证
     *
     * @param storeCertId 档口认证主键
     * @return 档口认证
     */
    @Override
    @Transactional(readOnly = true)
    public StoreCertificate selectStoreCertificateByStoreCertId(Long storeCertId) {
        return storeCertificateMapper.selectStoreCertificateByStoreCertId(storeCertId);
    }

    /**
     * 查询档口认证列表
     *
     * @param storeCertificate 档口认证
     * @return 档口认证
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreCertificate> selectStoreCertificateList(StoreCertificate storeCertificate) {
        return storeCertificateMapper.selectStoreCertificateList(storeCertificate);
    }

    /**
     * 新增档口认证
     *
     * @param storeCertificate 档口认证
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreCertificate(StoreCertificate storeCertificate) {
        storeCertificate.setCreateTime(DateUtils.getNowDate());
        return storeCertificateMapper.insertStoreCertificate(storeCertificate);
    }

    /**
     * 修改档口认证
     *
     * @param storeCertificate 档口认证
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreCertificate(StoreCertificate storeCertificate) {
        storeCertificate.setUpdateTime(DateUtils.getNowDate());
        return storeCertificateMapper.updateStoreCertificate(storeCertificate);
    }

    /**
     * 批量删除档口认证
     *
     * @param storeCertIds 需要删除的档口认证主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreCertificateByStoreCertIds(Long[] storeCertIds) {
        return storeCertificateMapper.deleteStoreCertificateByStoreCertIds(storeCertIds);
    }

    /**
     * 删除档口认证信息
     *
     * @param storeCertId 档口认证主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreCertificateByStoreCertId(Long storeCertId) {
        return storeCertificateMapper.deleteStoreCertificateByStoreCertId(storeCertId);
    }
}
