package com.ruoyi.web.controller.xkt.migartion;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtAttrVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtCateVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtProdSkuVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtProdVO;
import com.ruoyi.xkt.mapper.SysProductCategoryMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ShipMaster 相关
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/gt")
public class GtController extends BaseController {

    final RedisCache redisCache;
    final SysProductCategoryMapper prodCateMapper;

    /**
     * step1
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/cate/cache/{user_id}")
    public R<Integer> createCateCache(@PathVariable(value = "user_id") Integer user_id, @Validated @RequestBody GtCateVO cateInitVO) {
        if (CollectionUtils.isEmpty(cateInitVO.getData())) {
            throw new ServiceException("入参GT分类数据为空!", HttpStatus.ERROR);
        }
        List<GtCateVO.GCIDataVO> cateList = cateInitVO.getData().stream()
                // 只处理二级分类及没有子分类的一级分类，因为我们是存的最后的分类
                .filter(x -> x.getHas_child() == 0)
                .map(x -> Objects.equals(x.getName(), "时尚雪地靴") ? x.setName("雪地靴") : x)
                .collect(Collectors.toList());
        // 先从redis中获取列表数据
        List<GtCateVO.GCIDataVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_CATE_KEY + user_id), new ArrayList<>());
        CollectionUtils.addAll(cacheList, cateList);
        // 放到缓存中
        redisCache.setCacheObject(CacheConstants.MIGRATION_GT_SALE_CATE_KEY + user_id, cacheList);
        return R.ok();
    }

    /**
     * step2
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/sale/cache")
    public R<Integer> createSaleCache(@Validated @RequestBody GtProdVO doubleRunVO) {
        List<GtProdVO.DRIArtNoVO> artNoList = doubleRunVO.getData().getData();
        final Integer userId = doubleRunVO.getData().getData().get(0).getUser_id();
        // 先从redis中获取列表数据
        List<GtProdSkuVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + userId), new ArrayList<>());
        // 已存入的map，增加个校验 如果已导入则不重复导入 避免误操作
        Map<String, String> cacheMap = cacheList.stream().collect(Collectors.toMap(GtProdSkuVO::getArticle_number, GtProdSkuVO::getArticle_number, (v1, v2) -> v2));
        // 三年前的时间
        Date threeYearsBefore = java.sql.Date.valueOf(LocalDate.now().minusYears(3));
        artNoList.stream()
                // 只处理近3年商品
                .filter(x -> x.getCreate_time().after(threeYearsBefore) || x.getUpdate_time().after(threeYearsBefore))
                // 避免误操作
                .filter(x -> !cacheMap.containsKey(x.getArticle_number()))
                .forEach(artNoInfo -> {
                    artNoInfo.getSkus().forEach(x -> x.setColor(StringUtils.isNotBlank(x.getColor()) ? this.decodeUnicode(x.getColor().trim()) : "")
                            .setCharacters(StringUtils.isNotBlank(artNoInfo.getCharacters()) ? artNoInfo.getCharacters().trim() : "")
                            .setArticle_number(StringUtils.isNotBlank(artNoInfo.getArticle_number()) ? artNoInfo.getArticle_number().trim() : "")
                            .setSize(x.getSize().trim().replaceAll("[^0-9.]", ""))
                            .setProduct_id(artNoInfo.getId()).setCategory_nid(artNoInfo.getCategory_nid()));
                    cacheList.addAll(artNoInfo.getSkus());
                    cacheMap.put(artNoInfo.getArticle_number(), artNoInfo.getArticle_number());
                });
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + userId, cacheList);
        return R.ok();
    }

    /**
     * step3
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/off-sale/cache")
    public R<Integer> createOffSaleCache(@Validated @RequestBody GtProdVO doubleRunVO) {
        List<GtProdVO.DRIArtNoVO> artNoList = doubleRunVO.getData().getData();
        final Integer userId = doubleRunVO.getData().getData().get(0).getUser_id();
        // 先从redis中获取列表数据
        List<GtProdSkuVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_OFF_SALE_BASIC_KEY + userId), new ArrayList<>());
        // 已存入的map，增加个校验 如果已导入则不重复导入 避免误操作
        Map<String, String> cacheMap = cacheList.stream().collect(Collectors.toMap(GtProdSkuVO::getArticle_number, GtProdSkuVO::getArticle_number, (v1, v2) -> v2));
        artNoList.stream()
                // 避免误操作
                .filter(x -> !cacheMap.containsKey(x.getArticle_number()))
                .forEach(artNoInfo -> {
                    artNoInfo.getSkus().forEach(x -> x.setColor(StringUtils.isNotBlank(x.getColor()) ? this.decodeUnicode(x.getColor().trim()) : "")
                            .setSize(x.getSize().trim().replaceAll("[^0-9.]", "")).setProduct_id(artNoInfo.getId())
                            .setArticle_number(StringUtils.isNotBlank(artNoInfo.getArticle_number()) ? artNoInfo.getArticle_number().trim() : ""));
                    cacheList.addAll(artNoInfo.getSkus());
                    cacheMap.put(artNoInfo.getArticle_number(), artNoInfo.getArticle_number());
                });
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_GT_OFF_SALE_BASIC_KEY + userId, cacheList);
        return R.ok();
    }

    /**
     * step4
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/attr/cache/{user_id}/{product_id}")
    public R<Integer> createAttrCache(@PathVariable(value = "user_id") Integer user_id, @PathVariable("product_id") Integer product_id,
                                      @Validated @RequestBody GtAttrVO attrVO) {
        // 判断缓存中是否有该product_id
        Map<String, String> existMap = redisCache.getCacheMap(CacheConstants.MIGRATION_GT_SALE_ATTR_KEY + user_id + "_" + product_id);
        if (MapUtils.isNotEmpty(existMap)) {
            throw new ServiceException("该商品已设置过类目属性", HttpStatus.ERROR);
        }
        Map<String, String> attrMap = new HashMap<>();
        attrVO.getData().forEach((itemId, attr) -> {
            // 不处理 multi=1 的属性
            if (attr.getMulti() == 1) {
                return;
            }
            // 有值
            if (attr.getHas_value() == 1) {
                attr.getAttr().stream().filter(x -> x.getChoosed() == 1).forEach(x -> attrMap
                        .put(this.decodeUnicode(x.getProps_name()), this.decodeUnicode(x.getPropsvalue_name())));
            }
        });
        redisCache.setCacheMap(CacheConstants.MIGRATION_GT_SALE_ATTR_KEY + user_id + "_" + product_id, attrMap);
        return R.ok();
    }

    /**
     * step5
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @DeleteMapping("/clear/cache/{user_id}/{product_id}")
    public R<Integer> clearCache(@PathVariable(value = "user_id") Integer userId, @PathVariable("product_id") Integer productId) {

        // 清除GT在售商品缓存
        redisCache.deleteObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + userId);
        // 清除GT下架商品缓存
        redisCache.deleteObject(CacheConstants.MIGRATION_GT_OFF_SALE_BASIC_KEY + userId);
        // 清除GT在售商品属性缓存
        redisCache.deleteObject(CacheConstants.MIGRATION_GT_SALE_ATTR_KEY + userId + "_" + productId);

        return R.ok();
    }


    /**
     * Unicode解码方法
     *
     * @param unicodeStr 包含Unicode编码的字符串
     * @return 解码后的字符串
     */
    private String decodeUnicode(String unicodeStr) {
        if (unicodeStr == null || unicodeStr.isEmpty()) {
            return unicodeStr;
        }
        // 去除首尾的空格
        unicodeStr = unicodeStr.trim();
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < unicodeStr.length()) {
            if (unicodeStr.charAt(i) == '\\' && i + 1 < unicodeStr.length() && unicodeStr.charAt(i + 1) == 'u') {
                // 处理Unicode编码
                if (i + 5 < unicodeStr.length()) {
                    try {
                        String hexCode = unicodeStr.substring(i + 2, i + 6);
                        char ch = (char) Integer.parseInt(hexCode, 16);
                        result.append(ch);
                        i += 6;
                    } catch (NumberFormatException e) {
                        result.append(unicodeStr.charAt(i));
                        i++;
                    }
                } else {
                    result.append(unicodeStr.charAt(i));
                    i++;
                }
            } else {
                result.append(unicodeStr.charAt(i));
                i++;
            }
        }
        return result.toString();
    }


}
