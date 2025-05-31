package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.xkt.service.IUserBrowsingHistoryService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
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

}
