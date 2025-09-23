package com.ruoyi.web.controller.xkt.migartion;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtAttrVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtCateVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtProdSkuVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtProdVO;
import com.ruoyi.xkt.domain.SysProductCategory;
import com.ruoyi.xkt.mapper.SysProductCategoryMapper;
import com.ruoyi.xkt.service.shipMaster.IShipMasterService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
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

    final IShipMasterService shipMasterService;
    final RedisCache redisCache;
    final SysProductCategoryMapper prodCateMapper;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/sale/cache")
    public R<Integer> createSaleCache(@Validated @RequestBody GtProdVO doubleRunVO) {
        List<GtProdVO.DRIArtNoVO> artNoList = doubleRunVO.getData().getData();
        final Integer userId = doubleRunVO.getData().getData().get(0).getUser_id();
        // 先从redis中获取列表数据
        List<GtProdSkuVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + userId), new ArrayList<>());
        artNoList.forEach(artNoInfo -> {
            artNoInfo.getSkus().forEach(x -> x.setColor(this.decodeUnicode(x.getColor()))
                    .setArticle_number(artNoInfo.getArticle_number()).setProduct_id(artNoInfo.getId()));
            cacheList.addAll(artNoInfo.getSkus());
        });
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + userId, cacheList);
        return R.ok();
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/off-sale/cache")
    public R<Integer> createOffSaleCache(@Validated @RequestBody GtProdVO doubleRunVO) {
        List<GtProdVO.DRIArtNoVO> artNoList = doubleRunVO.getData().getData();
        final Integer userId = doubleRunVO.getData().getData().get(0).getUser_id();
        // 先从redis中获取列表数据
        List<GtProdSkuVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_OFF_SALE_BASIC_KEY + userId), new ArrayList<>());
        artNoList.forEach(artNoInfo -> {
            artNoInfo.getSkus().forEach(x -> x.setColor(this.decodeUnicode(x.getColor()))
                    .setArticle_number(artNoInfo.getArticle_number()).setProduct_id(artNoInfo.getId()));
            cacheList.addAll(artNoInfo.getSkus());
        });
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_GT_OFF_SALE_BASIC_KEY + userId, cacheList);
        return R.ok();
    }

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

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/cate/cache/{user_id}")
    public R<Integer> createCateCache(@PathVariable(value = "user_id") Integer user_id, MultipartFile file) throws IOException {
        ExcelUtil<GtCateVO> util = new ExcelUtil<>(GtCateVO.class);
        List<GtCateVO> artNoCateList = util.importExcel(file.getInputStream());
        // 步橘网获取所有的分类
        List<SysProductCategory> cateList = this.prodCateMapper.selectList(new LambdaQueryWrapper<SysProductCategory>()
                .eq(SysProductCategory::getDelFlag, Constants.UNDELETED).ne(SysProductCategory::getId, Constants.TOPMOST_PRODUCT_CATEGORY_ID));
        Map<String, Long> cateNameMap = cateList.stream().collect(Collectors.toMap(SysProductCategory::getName, SysProductCategory::getId));
        artNoCateList.forEach(x -> {
            // 必须用trim()
            x.setArticle_number(x.getArticle_number().trim()).setCate_name(x.getCate_name().trim());
            if (!cateNameMap.containsKey(x.getCate_name())) {
                throw new ServiceException("分类不存在：" + x.getCate_name(), HttpStatus.ERROR);
            }
            x.setBuju_cate_id(cateNameMap.get(x.getCate_name().trim()));
        });
        // 放到缓存中
        redisCache.setCacheObject(CacheConstants.MIGRATION_GT_SALE_CATE_KEY + user_id, artNoCateList, 5, TimeUnit.DAYS);
        return R.ok();
    }


    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @GetMapping("/cache/{userId}")
    public R<Integer> getCache(@PathVariable Integer userId) {
        // 从redis中获取数据
        List<GtProdSkuVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + userId), new ArrayList<>());
        if (CollectionUtils.isEmpty(cacheList)) {
            return R.fail();
        }
        Map<String, List<GtProdSkuVO>> artNoGroup = cacheList.stream()
                .sorted(Comparator.comparing(GtProdSkuVO::getArticle_number)
                        .thenComparing(GtProdSkuVO::getColor))
                .collect(Collectors
                        .groupingBy(GtProdSkuVO::getArticle_number, LinkedHashMap::new, Collectors.toList()));
        // 货号 颜色 map
        Map<String, List<String>> artNoColorMap = new LinkedHashMap<>();
        artNoGroup.forEach((artNo, colorList) -> {
            artNoColorMap.put(artNo, colorList.stream().map(GtProdSkuVO::getColor).collect(Collectors.toList()));
        });

        artNoColorMap.forEach((k, v) -> {
            System.err.println(k + ":" + v);
        });

        // TODO 如何对比？？

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
