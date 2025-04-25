package com.ruoyi.framework.img.entity;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-25 20:50
 */
@Data
public class ImgSearchReq {
    /**
     * 图片路径
     * <p>
     * 本地图片：E:/test/123.jpg
     * URL：https://www.example.com/123.jpg
     */
    private String picPath;
    /**
     * 是否本地图片
     */
    private Boolean picLocalFlag;
    /**
     * 选填，图片类目
     * <p>
     * [0:上衣 1:裙装 2:下装 3:包 4:鞋 5:配饰 6:零食 7:美妆 8:瓶饮 9:家具 20:玩具 21:内衣 22:数码硬件 88888888:其他]
     */
    private Integer categoryId;
    /**
     * 选填，返回结果的数目。取值范围：1-100。默认值：10。
     */
    private Integer num;
    /**
     * 选填，返回结果的起始位置。取值范围：0-499。默认值：0。
     */
    private Integer start;
    /**
     * 选填，若为true则响应数据根据ProductId进行返回
     */
    private Boolean distinctProductId;
}
