package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.Feedback;
import com.ruoyi.xkt.dto.feedback.FeedbackPageDTO;
import com.ruoyi.xkt.dto.feedback.FeedbackResDTO;

import java.util.List;

/**
 * 意见反馈 数据层
 *
 * @author ruoyi
 */
public interface FeedbackMapper extends BaseMapper<Feedback> {

    /**
     * PC查询意见反馈列表
     *
     * @param pageDTO 查询入参
     * @return List<FeedbackResDTO>
     */
    List<FeedbackResDTO> selectPageList(FeedbackPageDTO pageDTO);

}
