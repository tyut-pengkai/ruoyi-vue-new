package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.domain.Feedback;
import com.ruoyi.xkt.dto.feedback.FeedbackDTO;
import com.ruoyi.xkt.dto.feedback.FeedbackPageDTO;
import com.ruoyi.xkt.dto.feedback.FeedbackResDTO;
import com.ruoyi.xkt.mapper.FeedbackMapper;
import com.ruoyi.xkt.service.IFeedbackService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 意见反馈 服务层实现
 *
 * @author ruoyi
 */
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements IFeedbackService {

    final FeedbackMapper feedbackMapper;

    /**
     * 新增意见反馈
     *
     * @param feedbackDTO 意见反馈入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(FeedbackDTO feedbackDTO) {
        Feedback feedback = BeanUtil.toBean(feedbackDTO, Feedback.class);
        return this.feedbackMapper.insert(feedback);
    }

    /**
     * 意见反馈分页查询
     *
     * @param pageDTO 分页查询入参
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackResDTO> page(FeedbackPageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<FeedbackResDTO> feedbackList = this.feedbackMapper.selectPageList(pageDTO);
        return CollectionUtils.isEmpty(feedbackList) ? Page.empty(pageDTO.getPageSize(), pageDTO.getPageNum())
                : Page.convert(new PageInfo<>(feedbackList));
    }

    /**
     * 获取意见反馈详情
     *
     * @param id 意见反馈ID
     * @return FeedbackResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public FeedbackResDTO getInfo(Long id) {
        Feedback feedback = Optional.ofNullable(this.feedbackMapper.selectOne(new LambdaQueryWrapper<Feedback>()
                        .eq(Feedback::getId, id).eq(Feedback::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new RuntimeException("意见反馈不存在!"));
        return BeanUtil.toBean(feedback, FeedbackResDTO.class);
    }


}
