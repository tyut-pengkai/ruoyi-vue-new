package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.xkt.service.IUserNoticeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户通知接收设置Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/user-notice-settings")
public class UserNoticeSettingController extends XktBaseController {
    @Autowired
    private IUserNoticeSettingService userNoticeSettingService;

}
