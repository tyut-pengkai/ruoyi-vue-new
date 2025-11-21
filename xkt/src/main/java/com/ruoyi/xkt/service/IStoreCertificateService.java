package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeCertificate.StoreCertDTO;
import com.ruoyi.xkt.dto.storeCertificate.StoreCertResDTO;
import com.ruoyi.xkt.dto.storeCertificate.StoreCertStepResDTO;

/**
 * 档口认证Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreCertificateService {

    /**
     * 新增档口认证
     *
     * @param certDTO 档口认证入参
     * @return int
     */
    Integer create(StoreCertDTO certDTO);

    /**
     * 根据档口ID获取档口详情信息
     *
     * @param storeId 档口ID
     * @return StoreCertResDTO
     */
    StoreCertResDTO getInfo(Long storeId);

    /**
     * 编辑档口认证信息
     *
     * @param certDTO 档口认证信息
     * @return Integer
     */
    Integer update(StoreCertDTO certDTO);

    /**
     * 新增认证流程 获取认证信息
     *
     * @param storeId 档口ID
     * @return StoreCertStepResDTO
     */
    StoreCertStepResDTO getStepCertInfo(Long storeId);

    /**
     * 发短信验证码（档口认证）
     *
     * @param phoneNumber
     */
    void sendSmsVerificationCode(String phoneNumber);

    /**
     * 验证短信验证码（档口认证）
     *
     * @param phoneNumber
     * @param code
     */
    void validateSmsVerificationCode(String phoneNumber, String code);
}
