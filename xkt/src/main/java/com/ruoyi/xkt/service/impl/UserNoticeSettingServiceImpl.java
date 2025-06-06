package com.ruoyi.xkt.service.impl;

import com.ruoyi.xkt.mapper.UserNoticeSettingMapper;
import com.ruoyi.xkt.service.IUserNoticeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户通知接收设置Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class UserNoticeSettingServiceImpl implements IUserNoticeSettingService {

    @Autowired
    private UserNoticeSettingMapper userNoticeSettingMapper;


}
