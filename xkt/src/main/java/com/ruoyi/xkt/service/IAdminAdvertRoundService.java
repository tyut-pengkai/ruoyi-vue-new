package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.adminAdvertRound.*;
import com.ruoyi.xkt.dto.advertRound.AdRoundUploadPicDTO;

/**
 * 推广营销Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IAdminAdvertRoundService {

    /**
     * 管理员查询推广营销分页
     *
     * @param pageDTO 分页入参
     * @return Page<AdminAdRoundPageResDTO>
     */
    Page<AdminAdRoundPageResDTO> page(AdminAdRoundPageDTO pageDTO);

    /**
     * 管理员审核推广图
     *
     * @param auditDTO 审核推广图入参
     * @return Integer
     */
    Integer auditPic(AdminAdRoundAuditDTO auditDTO);

    /**
     * 管理员退订
     *
     * @param unsubscribeDTO 退订入参
     * @return
     */
    Integer unsubscribe(AdminAdRoundUnsubscribeDTO unsubscribeDTO);

    /**
     * 上传档口图
     *
     * @param picDTO 上传推广图入参
     * @return Integer
     */
    Integer uploadAdvertPic(AdRoundUploadPicDTO picDTO);

    /**
     * 管理员拦截推广营销
     *
     * @param interceptDTO 拦截推广营销入参
     * @return Integer
     */
    Integer sysIntercept(AdminAdRoundSysInterceptDTO interceptDTO);

    /**
     * 取消拦截广告位
     *
     * @param advertRoundId 推广轮次ID
     * @return Integer
     */
    Integer cancelIntercept(Long advertRoundId);

}
