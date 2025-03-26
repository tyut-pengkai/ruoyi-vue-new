package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreCertificate;

import java.util.List;

/**
 * 档口认证Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreCertificateService {
    /**
     * 查询档口认证
     *
     * @param storeCertId 档口认证主键
     * @return 档口认证
     */
    public StoreCertificate selectStoreCertificateByStoreCertId(Long storeCertId);

    /**
     * 查询档口认证列表
     *
     * @param storeCertificate 档口认证
     * @return 档口认证集合
     */
    public List<StoreCertificate> selectStoreCertificateList(StoreCertificate storeCertificate);

    /**
     * 新增档口认证
     *
     * @param storeCertificate 档口认证
     * @return 结果
     */
    public int insertStoreCertificate(StoreCertificate storeCertificate);

    /**
     * 修改档口认证
     *
     * @param storeCertificate 档口认证
     * @return 结果
     */
    public int updateStoreCertificate(StoreCertificate storeCertificate);

    /**
     * 批量删除档口认证
     *
     * @param storeCertIds 需要删除的档口认证主键集合
     * @return 结果
     */
    public int deleteStoreCertificateByStoreCertIds(Long[] storeCertIds);

    /**
     * 删除档口认证信息
     *
     * @param storeCertId 档口认证主键
     * @return 结果
     */
    public int deleteStoreCertificateByStoreCertId(Long storeCertId);
}
