package com.kekecha.xiantu.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    String upload(MultipartFile file);
    boolean isValidPath(String file);
    String delete(String file);
}
