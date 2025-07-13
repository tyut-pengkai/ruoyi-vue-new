package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.notice.*;

import java.util.List;

/**
 * 公告 服务层
 *
 * @author ruoyi
 */
public interface INoticeService {

    /**
     * 新增公告
     *
     * @param createDTO 新增公告参数
     * @return Integer
     */
    Integer create(NoticeCreateDTO createDTO);

    /**
     * 编辑公告
     *
     * @param editDTO 编辑入参
     * @return Integer
     */
    Integer edit(NoticeEditDTO editDTO);

    /**
     * 删除公告
     *
     * @param deleteDTO 删除入参
     * @return Integer
     */
    Integer delete(NoticeDeleteDTO deleteDTO);

    /**
     * 获取公告详情
     *
     * @param noticeId 公告ID
     * @return NoticeResDTO
     */
    NoticeResDTO getInfo(Long noticeId);

    /**
     * 分页查询公告列表
     *
     * @param pageDTO 分页查询入参
     * @return Page<NoticeResDTO>
     */
    Page<NoticeResDTO> page(NoticePageDTO pageDTO);

    /**
     * 创建单个通知
     *
     * @param userId           被通知的userId
     * @param title            标题
     * @param noticeType       NoticeType
     * @param ownerType        NoticeOwnerType
     * @param storeId          档口ID
     * @param targetNoticeType 目标通知类型
     * @param content          通知内容
     * @return 新增成功条数
     */
    Integer createSingleNotice(Long userId, String title, Integer noticeType, Integer ownerType,
                               Long storeId, Integer targetNoticeType, String content);

    /**
     * 获取最新的10条公告
     *
     * @return List<NoticeLatest10ResDTO>
     */
    List<NoticeLatest10ResDTO> latest10();

}
