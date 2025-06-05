package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.notice.*;

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
}
