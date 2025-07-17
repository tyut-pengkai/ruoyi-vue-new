package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.feedback.FeedbackPageVO;
import com.ruoyi.web.controller.xkt.vo.feedback.FeedbackResVO;
import com.ruoyi.web.controller.xkt.vo.feedback.FeedbackVO;
import com.ruoyi.web.controller.xkt.vo.notice.NoticeResVO;
import com.ruoyi.xkt.dto.feedback.FeedbackDTO;
import com.ruoyi.xkt.dto.feedback.FeedbackPageDTO;
import com.ruoyi.xkt.dto.feedback.FeedbackResDTO;
import com.ruoyi.xkt.service.IFeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 意见反馈Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "意见反馈")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/feedback")
public class FeedbackController extends XktBaseController {

    final IFeedbackService feedbackService;

    @ApiOperation(value = "新增意见反馈", httpMethod = "POST", response = R.class)
    @Log(title = "新增意见反馈", businessType = BusinessType.INSERT)
    @PostMapping("")
    public R<Integer> create(@Validated @RequestBody FeedbackVO feedbackVO) {
        return success(feedbackService.create(BeanUtil.toBean(feedbackVO, FeedbackDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "意见反馈列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<FeedbackResDTO>> page(@Validated @RequestBody FeedbackPageVO pageVO) {
        return R.ok(feedbackService.page(BeanUtil.toBean(pageVO, FeedbackPageDTO.class)));
    }

    @ApiOperation(value = "意见反馈详情", httpMethod = "PUT", response = R.class)
    @GetMapping("/{id}")
    public R<FeedbackResVO> getInfo(@PathVariable Long id) {
        return R.ok(BeanUtil.toBean(feedbackService.getInfo(id), FeedbackResVO.class));
    }

}
