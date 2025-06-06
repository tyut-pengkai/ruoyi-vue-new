package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.UserNotice;
import com.ruoyi.xkt.dto.userNotice.UserNoticeAppListResDTO;
import com.ruoyi.xkt.dto.userNotice.UserNoticeAppResDTO;
import com.ruoyi.xkt.dto.userNotice.UserNoticeResDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户所有通知Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface UserNoticeMapper extends BaseMapper<UserNotice> {

    /**
     * 查询用户所有通知列表
     *
     * @param userId      用户ID
     * @param noticeTitle 公告标题
     * @return 列表
     */
    List<UserNoticeResDTO> selectUserNoticeList(@Param("userId") Long userId, @Param("noticeTitle") String noticeTitle);

    /**
     * 查询APP用户所有通知列表
     *
     * @param userId 用户ID
     * @return 列表
     */
    List<UserNoticeAppListResDTO> appList(Long userId);

    /**
     * 查询APP用户指定类型通知列表
     * @param userId 用户ID
     * @param targetNoticeType 消息接收类型
     * @return 列表
     */
    List<UserNoticeAppResDTO> selectAppTypePage(@Param("userId") Long userId, @Param("targetNoticeType") Integer targetNoticeType);
}
