package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.web.controller.xkt.vo.userBrowsingHistory.UserBrowHisPageVO;
import com.ruoyi.xkt.dto.userBrowsingHistory.UserBrowHisPageDTO;
import com.ruoyi.xkt.dto.userBrowsingHistory.UserBrowsingHisPageResDTO;
import com.ruoyi.xkt.service.IUserBrowsingHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户浏览足迹Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "用户浏览足迹")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/user-brow-his")
public class UserBrowsingHistoryController extends XktBaseController {

    final IUserBrowsingHistoryService userBrowHisService;

    @ApiOperation(value = "获取用户浏览足迹", httpMethod = "POST", response = R.class)
    @PostMapping(value = "/page")
    public R<Page<UserBrowsingHisPageResDTO>> page(@Validated @RequestBody UserBrowHisPageVO pageVO) {
        return R.ok(userBrowHisService.page(BeanUtil.toBean(pageVO, UserBrowHisPageDTO.class)));
    }

}
