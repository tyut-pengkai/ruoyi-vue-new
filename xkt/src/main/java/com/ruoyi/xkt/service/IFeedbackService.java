package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.feedback.FeedbackDTO;
import com.ruoyi.xkt.dto.feedback.FeedbackPageDTO;
import com.ruoyi.xkt.dto.feedback.FeedbackResDTO;

/**
 * 意见反馈Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IFeedbackService {

    /**
     * 新增意见反馈
     *
     * @param feedbackDTO 意见反馈入参
     * @return Integer
     */
    Integer create(FeedbackDTO feedbackDTO);

    /**
     * 意见反馈分页查询
     *
     * @param pageDTO 分页查询入参
     * @return
     */
    Page<FeedbackResDTO> page(FeedbackPageDTO pageDTO);

    /**
     * 获取意见反馈详情
     *
     * @param id 意见反馈ID
     * @return FeedbackResDTO
     */
    FeedbackResDTO getInfo(Long id);
}
