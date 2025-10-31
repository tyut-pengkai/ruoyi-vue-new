package com.ruoyi.system.service;

/**
 * @author liangyq
 * @date 2025-10-31
 */
public interface ISysHtmlService {

    void saveHtml(String title, String content);

    String getHtmlContent(String title);
}
