package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.picture.PicZipDTO;

/**
 * @author liangyq
 * @date 2025-03-26 15:42
 */
public interface IPictureService {

    /**
     * 处理图包
     *
     * @param key
     * @return
     */
    PicZipDTO processPicZip(String key);

}
