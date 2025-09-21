package com.ruoyi.web.controller.xkt.shipMaster;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.web.controller.xkt.shipMaster.vo.DoubleRunImportVO;
import com.ruoyi.web.controller.xkt.shipMaster.vo.ShipMasterImportVO;
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
        Map<String, Map<String, List<DoubleRunImportVO.DRIArtNoSkuVO>>> artNoMap = new LinkedHashMap<>();
        artNoList.forEach(artNoInfo -> {
            artNoInfo.getSkus().forEach(x -> x.setColor(this.decodeUnicode(x.getColor())));
            artNoMap.put(artNoInfo.getArticle_number(), artNoInfo.getSkus().stream().collect(Collectors
                    .groupingBy(DoubleRunImportVO.DRIArtNoSkuVO::getColor)));
        });


        artNoMap.forEach((artNo, colorSkuMap) -> {
            colorSkuMap.forEach((color, skuList) -> {
                skuList.sort(Comparator.comparing(DoubleRunImportVO.DRIArtNoSkuVO::getSize));
                System.err.println(artNo + ":" + color + ":" + skuList);
            });
        });


     /*   // 供应商ID
        final Integer supplierId = importVO.getData().getRecords().get(0).getSupplierId();
        // 先从redis中获取列表数据
        List<ShipMasterImportVO.SMIVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.SUPPLIER_KEY + supplierId), new ArrayList<>());
        CollectionUtils.addAll(cacheList, importVO.getData().getRecords());
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.SUPPLIER_KEY + supplierId, cacheList);*/
        return R.ok();
    }


    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @GetMapping("/cache/{supplierId}")
    public R<Integer> getCache(@PathVariable Integer supplierId) {
        /*// 从redis中获取数据
        List<ShipMasterImportVO.SMIVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.SUPPLIER_KEY + supplierId), new ArrayList<>());
        if (CollectionUtils.isEmpty(cacheList)) {
            return R.fail();
        }
        // 按照artNo分组
        Map<String, List<ShipMasterImportVO.SMIVO>> artNoGroup = cacheList.stream()
                .sorted(Comparator.comparing(ShipMasterImportVO.SMIVO::getArtNo)
                        .thenComparing(ShipMasterImportVO.SMIVO::getColor))
                .collect(Collectors
                        .groupingBy(ShipMasterImportVO.SMIVO::getArtNo, LinkedHashMap::new, Collectors.toList()));
        Map<String, Map<String, String>> artSnPrefixMap = new LinkedHashMap<>();
        artNoGroup.forEach((artNo, colorList) -> {
            Map<String, String> snPrefixMap = new LinkedHashMap<>();
            // 按照颜色设置条码前缀
            colorList.forEach(color -> snPrefixMap
                    .put(color.getColor(), color.getSupplierId() + String.format("%05d", color.getSupplierSkuId())));
            artSnPrefixMap.put(artNo, snPrefixMap);
        });
        artSnPrefixMap.forEach((k,v) -> {
            System.err.println(k + ":" + v);
        });*/
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
