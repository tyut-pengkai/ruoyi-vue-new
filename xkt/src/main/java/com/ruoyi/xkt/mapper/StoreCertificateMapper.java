package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreCertificate;

import java.util.List;

/**
 * 档口认证Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreCertificateMapper extends BaseMapper<StoreCertificate> {
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
     * 删除档口认证
     *
     * @param storeCertId 档口认证主键
     * @return 结果
     */
    public int deleteStoreCertificateByStoreCertId(Long storeCertId);

    /**
     * 批量删除档口认证
     *
     * @param storeCertIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreCertificateByStoreCertIds(Long[] storeCertIds);
}
