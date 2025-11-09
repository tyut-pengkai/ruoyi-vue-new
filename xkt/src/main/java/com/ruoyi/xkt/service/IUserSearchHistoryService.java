package com.ruoyi.xkt.service;

import java.util.List;

/**
 * 用户浏览历史Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IUserSearchHistoryService {

    /**
     * 获取用户的搜索历史
     *
     * @return List<String>
     */
    List<String> recordList();

    /**
     * 清空用户的搜索历史
     *
     * @return Integer
     */
    Integer clearSearchHisRecord();
}
