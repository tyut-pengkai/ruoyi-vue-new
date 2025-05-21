package com.ruoyi.xkt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.text.CharsetKit;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.ImgExtUtil;
import com.ruoyi.framework.config.properties.OSSProperties;
import com.ruoyi.framework.img.ImgSearchClientWrapper;
import com.ruoyi.framework.img.entity.ImgAdd;
import com.ruoyi.framework.img.entity.ImgSearchReq;
import com.ruoyi.framework.oss.OSSClientWrapper;
import com.ruoyi.xkt.dto.picture.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private ImgSearchClientWrapper imgSearchClient;

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

    @Override
    public ProductPicSyncResultDTO sync2ImgSearchServer(ProductPicSyncDTO productPicSyncDTO) {
        Assert.notNull(productPicSyncDTO);
        String productId = productPicSyncDTO.getStoreProductId().toString();
        //删除商品图片
        boolean allSuccess = imgSearchClient.deleteImg(productId);
        List<String> picKeys = CollUtil.emptyIfNull(productPicSyncDTO.getProductPicKeys());
        List<PicSyncResultDTO> picSyncResultList = new ArrayList<>(picKeys.size());
        for (String picKey : picKeys) {
            ImgAdd imgAdd = new ImgAdd();
            imgAdd.setProductId(productId);
            imgAdd.setPicName(FileUtil.getName(picKey));
            imgAdd.setCategoryId(Constants.IMG_SEARCH_CATEGORY_ID);
            try {
                imgAdd.setPicInputStream(ossClient.getObject(picKey));
            } catch (Exception e) {
                log.error("获取图片流异常: " + picKey, e);
                allSuccess = false;
                picSyncResultList.add(new PicSyncResultDTO(picKey, false));
                continue;
            }
            //添加商品图片
            boolean success = imgSearchClient.addImg(imgAdd);
            if (!success) {
                allSuccess = false;
            }
            picSyncResultList.add(new PicSyncResultDTO(picKey, success));
        }
        return new ProductPicSyncResultDTO(allSuccess, picSyncResultList);
    }

    @Override
    public List<ProductMatchDTO> searchProductByPicKey(String picKey, Integer num) {
        Assert.notEmpty(picKey);
        ImgSearchReq imgSearchReq = new ImgSearchReq();
        imgSearchReq.setCategoryId(Constants.IMG_SEARCH_CATEGORY_ID);
        imgSearchReq.setNum(num);
        imgSearchReq.setStart(0);
        imgSearchReq.setDistinctProductId(true);
        try {
            imgSearchReq.setPicInputStream(ossClient.getObject(picKey));
        } catch (Exception e) {
            log.error("获取图片流异常: " + picKey, e);
            return ListUtil.empty();
        }
        return imgSearchClient.searchByPic(imgSearchReq)
                .stream()
                //过滤搜索评分0.5以下的商品
                .filter(o -> o.getScore() >= Constants.IMG_SEARCH_MATCH_SCORE_THRESHOLD)
                .map(o -> new ProductMatchDTO(Long.parseLong(o.getProductId()), o.getScore()))
                .collect(Collectors.toList());
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
