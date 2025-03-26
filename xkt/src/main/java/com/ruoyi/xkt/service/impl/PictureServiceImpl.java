package com.ruoyi.xkt.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.ruoyi.common.core.text.CharsetKit;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.ImgExtUtil;
import com.ruoyi.framework.config.properties.OSSProperties;
import com.ruoyi.framework.oss.OSSClientWrapper;
import com.ruoyi.xkt.dto.picture.DownloadResultDTO;
import com.ruoyi.xkt.dto.picture.PicZipDTO;
import com.ruoyi.xkt.service.IPictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-03-26 15:43
 */
@Slf4j
@Service
public class PictureServiceImpl implements IPictureService {

    @Autowired
    private OSSClientWrapper ossClient;
    @Autowired
    private OSSProperties ossProperties;

    @Override
    public PicZipDTO processPicZip(String key) {
        checkProcessAccess(key);
        //下载至系统临时文件夹
        DownloadResultDTO downloadResult = downloadFile2TempDir(key);
        String tempDirPath = downloadResult.getTempDirPath();
        try {
            String midDirName = "unzip_" + downloadResult.getTempDirName();
            //解压
            File unzipFile = unzip(downloadResult.getFilePath(), tempDirPath + midDirName);
            List<File> allFiles = FileUtil.loopFiles(unzipFile);
            for (File file : allFiles) {
                if (file == null || !file.exists() || file.isDirectory()) {
                    continue;
                }
                //处理图片
                String new450Path = file.getPath().replace(midDirName, "450");
                String new750Path = file.getPath().replace(midDirName, "750");
                if (ImgExtUtil.isImageByMagicNumber(file)) {
                    FileUtil.mkParentDirs(new450Path);
                    FileUtil.mkParentDirs(new750Path);
                    int height;
                    int weight;
                    //450p图片
                    try (InputStream is = new FileInputStream(file);
                         FileOutputStream fos = new FileOutputStream(new450Path)) {
                        BufferedImage in = ImageIO.read(is);
                        height = in.getHeight();
                        weight = in.getWidth();
                        int neww = 450;
                        int newh = (int) (NumberUtil.div(height, weight, 5) * neww);
                        ImgExtUtil.scale(in, fos, neww, newh, null);
                    }
                    //750p图片
                    try (InputStream is = new FileInputStream(file);
                         FileOutputStream fos = new FileOutputStream(new750Path)) {
                        BufferedImage in = ImageIO.read(is);
                        int neww = 750;
                        int newh = (int) (NumberUtil.div(height, weight, 5) * neww);
                        ImgExtUtil.scale(in, fos, neww, newh, null);
                    }
                } else {
                    //非图片
                    FileUtil.copy(file, new File(new450Path), true);
                    FileUtil.copy(file, new File(new750Path), true);
                }
            }
            //打包
            String nameOf450 = FileNameUtil.getPrefix(downloadResult.getFileName()) + "_450.zip";
            String pathOf450 = tempDirPath + nameOf450;
            String nameOf750 = FileNameUtil.getPrefix(downloadResult.getFileName()) + "_750.zip";
            String pathOf750 = tempDirPath + nameOf750;
            ZipUtil.zip(tempDirPath + "450", pathOf450, CharsetKit.CHARSET_GBK, false);
            ZipUtil.zip(tempDirPath + "750", pathOf750, CharsetKit.CHARSET_GBK, false);
            //上传
            String keyOf450 = StrUtil.replaceLast(key, downloadResult.getFileName(), nameOf450);
            String keyOf750 = StrUtil.replaceLast(key, downloadResult.getFileName(), nameOf750);
            try (InputStream is = new FileInputStream(pathOf450)) {
                ossClient.upload(keyOf450, is);
            }
            try (InputStream is = new FileInputStream(pathOf750)) {
                ossClient.upload(keyOf750, is);
            }
            return new PicZipDTO(key, keyOf450, keyOf750);
        } catch (Exception e) {
            log.error("图片处理异常", e);
            throw new ServiceException("图片处理失败");
        } finally {
            //删除临时文件夹
            FileUtil.del(tempDirPath);
        }
    }

    /**
     * 解压文件
     * TODO 仅支持zip
     *
     * @param source
     * @param target
     * @return
     */
    private File unzip(String source, String target) {
        Assert.notEmpty(source);
        Assert.notEmpty(target);
        File file = null;
        try {
            file = ZipUtil.unzip(source, target, CharsetKit.CHARSET_GBK);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("MALFORMED")) {
                //尝试用UTF8编码
                file = ZipUtil.unzip(source, target, CharsetKit.CHARSET_UTF_8);
            }
        }
        return file;
    }

    /**
     * OSS下载文件到系统临时文件夹
     *
     * @param ossKey
     * @return
     */
    private DownloadResultDTO downloadFile2TempDir(String ossKey) {
        Assert.notEmpty("ossKey不能为空");
        try {
            String baseTempDir = ossProperties.getTempDir();
            String tempDirName = IdUtil.fastSimpleUUID();
            String tempDirPath = baseTempDir + tempDirName + "/";
            //创建临时文件夹
            FileUtil.mkdir(tempDirPath);
            String fileName = FileNameUtil.getName(ossKey);
            String filePath = tempDirPath + fileName;
            ossClient.download(ossKey, filePath);
            return DownloadResultDTO
                    .builder()
                    .fileName(fileName)
                    .filePath(filePath)
                    .tempDirName(tempDirName)
                    .tempDirPath(tempDirPath)
                    .build();
        } catch (Exception e) {
            log.error("文件下载异常", e);
            throw new ServiceException("文件下载失败");
        }
    }

    private void checkProcessAccess(String key) {
        if (!"zip".equals(FileNameUtil.getSuffix(key))) {
            throw new IllegalArgumentException("非zip格式图包");
        }
    }

}
