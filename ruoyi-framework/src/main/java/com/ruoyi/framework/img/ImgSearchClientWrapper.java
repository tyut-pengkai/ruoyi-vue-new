package com.ruoyi.framework.img;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.BooleanUtil;
import com.aliyun.imagesearch20201214.Client;
import com.aliyun.imagesearch20201214.models.*;
import com.aliyun.teautil.models.RuntimeOptions;
import com.ruoyi.framework.img.entity.ImgAdd;
import com.ruoyi.framework.img.entity.ImgSearchReq;
import com.ruoyi.framework.img.entity.ImgSearchResult;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-25 19:51
 */
@Slf4j
public class ImgSearchClientWrapper {

    private final Client client;

    private final String instanceName;

    public ImgSearchClientWrapper(Client client, String instanceName) {
        this.client = client;
        this.instanceName = instanceName;
    }

    /**
     * 添加图片到图库
     *
     * @param imgAdd
     * @return
     */
    public boolean addImg(ImgAdd imgAdd) {
        try (InputStream inputStream = imgAdd.getPicInputStream()) {
            AddImageAdvanceRequest request = new AddImageAdvanceRequest();
            request.instanceName = instanceName;
            request.productId = imgAdd.getProductId();
            request.picName = imgAdd.getPicName();
            request.categoryId = imgAdd.getCategoryId();
            request.customContent = imgAdd.getCustomContent();
            request.crop = true;
            request.picContentObject = inputStream;
            AddImageResponse response = client.addImageAdvance(request, new RuntimeOptions());
            if (response.getBody().success) {
                return true;
            }
            log.warn("图片搜索-添加图片失败: {} - {}", imgAdd, response.getBody().toMap());
        } catch (Exception e) {
            log.error("图片搜索服务添加图片异常", e);
        }
        return false;
    }

    /**
     * 删除图库中指定商品的图片
     *
     * @param productId
     * @return
     */
    public boolean deleteImg(String productId) {
        DeleteImageRequest request = new DeleteImageRequest();
        request.instanceName = instanceName;
        request.productId = productId;
        try {
            DeleteImageResponse response = client.deleteImage(request);
            if (response.getBody().success) {
                return true;
            }
            log.warn("图片搜索-删除图片失败: {} - {}", productId, response.getBody().toMap());
        } catch (Exception e) {
            log.error("图片搜索服务删除图片异常", e);
        }
        return false;
    }

    /**
     * 以图搜图
     *
     * @param imgSearchReq
     * @return
     */
    public List<ImgSearchResult> searchByPic(ImgSearchReq imgSearchReq) {
        try (InputStream inputStream = imgSearchReq.getPicInputStream()) {
            SearchImageByPicAdvanceRequest request = new SearchImageByPicAdvanceRequest();
            request.instanceName = instanceName;
            request.categoryId = imgSearchReq.getCategoryId();
            request.num = imgSearchReq.getNum();
            request.start = imgSearchReq.getStart();
            request.crop = true;
            request.distinctProductId = BooleanUtil.isTrue(imgSearchReq.getDistinctProductId());
            request.picContentObject = inputStream;
            SearchImageByPicResponse response = client.searchImageByPicAdvance(request, new RuntimeOptions());
            List<SearchImageByPicResponseBody.SearchImageByPicResponseBodyAuctions> auctions = response.getBody()
                    .getAuctions();
            return BeanUtil.copyToList(auctions, ImgSearchResult.class);
        } catch (Exception e) {
            log.error("图片搜索服务搜索异常", e);
        }
        return ListUtil.empty();
    }


}
