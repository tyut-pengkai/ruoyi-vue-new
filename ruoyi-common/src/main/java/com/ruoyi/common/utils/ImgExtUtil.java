package com.ruoyi.common.utils;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author liangyq
 * @date 2025-03-26 09:24
 */
@Slf4j
public class ImgExtUtil extends ImgUtil {

    private static final byte[] JPEG_HEADER = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    private static final byte[] PNG_HEADER = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47};
    private static final byte[] GIF_HEADER = new byte[]{0x47, 0x49, 0x46, 0x38};
    private static final byte[] BMP_HEADER = new byte[]{0x42, 0x4D};

    /**
     * 缩放图像（按高度和宽度缩放）<br>
     * 缩放后默认为jpeg格式，此方法并不关闭流
     *
     * @param image      图像
     * @param destStream 缩放后的图像目标流
     * @param width      缩放后的宽度
     * @param height     缩放后的高度
     * @param fixedColor 比例不对时补充的颜色，不补充为{@code null}
     * @throws IORuntimeException IO异常
     */
    public static void scale(BufferedImage image, OutputStream destStream, int width, int height, Color fixedColor) throws IORuntimeException {
        Assert.notNull(image, "图片不能为空");
        try {
            scale(image, getImageOutputStream(destStream), width, height, fixedColor);
        } finally {
            flush(image);
        }
    }

    /**
     * 判断文件是否是图片（jpeg、png、bmp、gif）
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static boolean isImageByMagicNumber(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return false;
        }
        byte[] header = new byte[4];
        try (InputStream is = new FileInputStream(file)) {
            int readBytes = is.read(header);
            if (readBytes == -1) return false;
        } catch (Exception e) {
            log.error("判断文件是否图片失败", e);
            return false;
        }
        // 常见图片格式的魔数
        if (bytesStartWith(header, JPEG_HEADER)) { // JPEG
            return true;
        } else if (bytesStartWith(header, PNG_HEADER)) { // PNG
            return true;
        } else if (bytesStartWith(header, GIF_HEADER)) { // GIF
            return true;
        } else if (bytesStartWith(header, BMP_HEADER)) { // BMP
            return true;
        }
        return false;
    }

    private static boolean bytesStartWith(byte[] source, byte[] target) {
        if (source.length < target.length) return false;
        for (int i = 0; i < target.length; i++) {
            if (source[i] != target[i]) return false;
        }
        return true;
    }


}
