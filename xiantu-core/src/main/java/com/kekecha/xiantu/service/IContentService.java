package com.kekecha.xiantu.service;

public interface IContentService {
    int saveToFile(String type, String content);
    String getContentByType(String type);
}
