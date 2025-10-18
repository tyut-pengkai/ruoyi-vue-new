package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.web.controller.xkt.vo.userNotice.UserNoticeAppListResVO;
import com.ruoyi.web.controller.xkt.vo.userNotice.UserNoticeAppTypePageVO;
import com.ruoyi.web.controller.xkt.vo.userNotice.UserNoticePageVO;
import com.ruoyi.xkt.dto.userNotice.UserNoticeAppResDTO;
import com.ruoyi.xkt.dto.userNotice.UserNoticeAppTypePageDTO;
import com.ruoyi.xkt.dto.userNotice.UserNoticePageDTO;
import com.ruoyi.xkt.dto.userNotice.UserNoticeResDTO;
import com.ruoyi.xkt.service.IUserNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户所有通知Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "用户消息通知")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/user-notices")
public class UserNoticeController extends XktBaseController {

    final IUserNoticeService userNoticeService;

    @ApiOperation(value = "PC - 电商卖家消息列表", httpMethod = "POST", response = R.class)
    @PostMapping("/pc/page")
    public R<Page<UserNoticeResDTO>> pcPage(@Validated @RequestBody UserNoticePageVO pageVO) {
        return R.ok(userNoticeService.pcPage(BeanUtil.toBean(pageVO, UserNoticePageDTO.class)));
    }

    @ApiOperation(value = "APP - 电商卖家消息列表", httpMethod = "GET", response = R.class)
    @GetMapping("/app/list")
    public R<List<UserNoticeAppListResVO>> appList() {
        return R.ok(BeanUtil.copyToList(userNoticeService.appList(), UserNoticeAppListResVO.class));
    }

    @ApiOperation(value = "APP - 获取某一个类型消息列表", httpMethod = "POST", response = R.class)
    @PostMapping("/app/type/page")
    public R<Page<UserNoticeAppResDTO>> appTypePage(@Validated @RequestBody UserNoticeAppTypePageVO pageVO) {
        return R.ok(userNoticeService.appTypePage(BeanUtil.toBean(pageVO, UserNoticeAppTypePageDTO.class)));
    }

    @ApiOperation(value = "APP - 全部已读", httpMethod = "PUT", response = R.class)
    @PutMapping("/app/read/all")
    public R<Integer> appAllRead() {
        return R.ok(userNoticeService.appBatchRead());
    }


}
