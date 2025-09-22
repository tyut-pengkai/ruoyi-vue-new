package com.ruoyi.web.controller.xkt.shipMaster;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.web.controller.xkt.shipMaster.vo.DoubleRunImportVO;
import com.ruoyi.xkt.service.shipMaster.IShipMasterService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ShipMaster 相关
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/double-run")
public class DoubleRunController extends BaseController {

    final IShipMasterService shipMasterService;
    final RedisCache redisCache;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/cache")
    public R<Integer> createCache(@Validated @RequestBody DoubleRunImportVO importVO) {
        List<DoubleRunImportVO.DRIArtNoVO> artNoList = importVO.getData().getData();
        final Integer userId = importVO.getData().getData().get(0).getUserId();
        // 先从redis中获取列表数据
        List<DoubleRunImportVO.DRIArtNoSkuVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.DOUBLE_RUN_KEY + userId), new ArrayList<>());
//        Map<String, Map<String, List<DoubleRunImportVO.DRIArtNoSkuVO>>> artNoMap = new LinkedHashMap<>();
        artNoList.forEach(artNoInfo -> {
            artNoInfo.getSkus().forEach(x -> x.setColor(this.decodeUnicode(x.getColor()))
                    .setArticle_number(artNoInfo.getArticle_number()));
            cacheList.addAll(artNoInfo.getSkus());
//            artNoMap.put(artNoInfo.getArticle_number(), artNoInfo.getSkus().stream().collect(Collectors
//                    .groupingBy(DoubleRunImportVO.DRIArtNoSkuVO::getColor)));
        });
//        CollectionUtils.addAll(cacheList, artNoList);
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.DOUBLE_RUN_KEY + userId, cacheList);

//        Map<String, List<String>> artNoColorMap = new LinkedHashMap<>();
//        artNoMap.forEach((artNo, colorSkuMap) -> {
//            artNoColorMap.put(artNo, new ArrayList<>(colorSkuMap.keySet()));
//            colorSkuMap.forEach((color, skuList) -> {
//                skuList.sort(Comparator.comparing(DoubleRunImportVO.DRIArtNoSkuVO::getSize));
//                System.err.println(artNo + ":" + color + ":" + skuList);
//            });
//        });

        return R.ok();
    }


    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @GetMapping("/cache/{userId}")
    public R<Integer> getCache(@PathVariable Integer userId) {
        // 从redis中获取数据
        List<DoubleRunImportVO.DRIArtNoSkuVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.DOUBLE_RUN_KEY + userId), new ArrayList<>());
        if (CollectionUtils.isEmpty(cacheList)) {
            return R.fail();
        }
        Map<String, List<DoubleRunImportVO.DRIArtNoSkuVO>> artNoGroup = cacheList.stream()
                .sorted(Comparator.comparing(DoubleRunImportVO.DRIArtNoSkuVO::getArticle_number)
                        .thenComparing(DoubleRunImportVO.DRIArtNoSkuVO::getColor))
                .collect(Collectors
                        .groupingBy(DoubleRunImportVO.DRIArtNoSkuVO::getArticle_number, LinkedHashMap::new, Collectors.toList()));
        // 货号 颜色 map
        Map<String, List<String>> artNoColorMap = new LinkedHashMap<>();
        artNoGroup.forEach((artNo, colorList) -> {
            artNoColorMap.put(artNo, colorList.stream().map(DoubleRunImportVO.DRIArtNoSkuVO::getColor).collect(Collectors.toList()));
        });

        // TODO 如何对比？？





        return R.ok();
    }



    /**
     * Unicode解码方法
     * @param unicodeStr 包含Unicode编码的字符串
     * @return 解码后的字符串
     */
    private String decodeUnicode(String unicodeStr) {
        if (unicodeStr == null || unicodeStr.isEmpty()) {
            return unicodeStr;
        }
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
