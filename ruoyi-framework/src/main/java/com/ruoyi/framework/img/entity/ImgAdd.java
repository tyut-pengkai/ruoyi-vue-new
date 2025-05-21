package com.ruoyi.framework.img.entity;

import lombok.Data;

import java.io.InputStream;

/**
 * @author liangyq
 * @date 2025-04-25 20:08
 */
@Data
public class ImgAdd {
    /**
     * 必填，商品id，最多支持256个字符
     */
    private String productId;
    /**
     * 必填，图片名称，最多支持256个字符
     * <p>
     * 1. productId + picName唯一确定一张图片。
     * 2. 如果多次添加图片具有相同的productId + picName，以最后一次添加为准，前面添加的图片将被覆盖。
     */
    private String picName;
    /**
     * 选填，图片类目
     * <p>
     * [0:上衣 1:裙装 2:下装 3:包 4:鞋 5:配饰 6:零食 7:美妆 8:瓶饮 9:家具 20:玩具 21:内衣 22:数码硬件 88888888:其他]
     */
    private Integer categoryId;
    /**
     * 选填，用户自定义的内容，最多支持4096个字符
     * <p>
     * 查询时会返回该字段。例如可添加图片的描述等文本。
     */
    private String customContent;
    /**
     * 图片输入流
     */
    private InputStream picInputStream;

}
