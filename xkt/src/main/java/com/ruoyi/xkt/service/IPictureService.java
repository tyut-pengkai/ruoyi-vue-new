package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.picture.PicZipDTO;
import com.ruoyi.xkt.dto.picture.ProductMatchDTO;
import com.ruoyi.xkt.dto.picture.ProductPicSyncDTO;
import com.ruoyi.xkt.dto.picture.ProductPicSyncResultDTO;

import java.util.List;

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

    /**
     * 同步图片到搜图服务器
     *
     * @param productPicSyncDTO
     * @return
     */
    ProductPicSyncResultDTO sync2ImgSearchServer(ProductPicSyncDTO productPicSyncDTO);

    /**
     * 以图搜商品
     *
     * @param picKey
     * @param num
     * @return
     */
    List<ProductMatchDTO> searchProductByPicKey(String picKey, Integer num);

}
