package com.ruoyi.web.controller.xkt.shipMaster;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.web.controller.xkt.shipMaster.vo.doubRun.DoubleRunProdVO;
import com.ruoyi.web.controller.xkt.shipMaster.vo.shipMaster.ShipMasterProdVO;
import com.ruoyi.xkt.service.shipMaster.IShipMasterService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Compare 相关
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/compare")
public class CompareBizController extends BaseController {

    final IShipMasterService shipMasterService;
    final RedisCache redisCache;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PutMapping("/double/ship/{userId}/{supplierId}")
    public R<Integer> compare(@PathVariable("userId") Integer userId, @PathVariable("supplierId") Integer supplierId) {
        Map<String, List<String>> multiSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiOffSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiSameFMap = new HashMap<>();
        List<DoubleRunProdVO.DRIArtNoSkuVO> doubleRunSaleBasicList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_DOUBLE_RUN_SALE_BASIC_KEY + userId), new ArrayList<>());
        List<String> doubleRunSaleArtNoList = doubleRunSaleBasicList.stream().map(DoubleRunProdVO.DRIArtNoSkuVO::getArticle_number)
                .distinct().collect(Collectors.toList());
        // 查看double_run 在售的商品 这边有多少相似的货号
        doubleRunSaleArtNoList.forEach(article_number -> {
            // 只保留数字，去除其他所有符号
            String cleanArtNo = article_number.replaceAll("[^0-9]", "");
            List<String> existList = multiSaleSameGoMap.containsKey(cleanArtNo) ?  multiSaleSameGoMap.get(cleanArtNo) : new ArrayList<>();
            existList.add(article_number);
            multiSaleSameGoMap.put(cleanArtNo, existList);
        });

        // 查看double_run 下架的商品有多少相似的货号
        List<DoubleRunProdVO.DRIArtNoSkuVO> doubleRunOffSaleBasicList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_DOUBLE_RUN_OFF_SALE_BASIC_KEY + userId), new ArrayList<>());
        List<String> doubleRunOffSaleArtNoList = doubleRunOffSaleBasicList.stream().map(DoubleRunProdVO.DRIArtNoSkuVO::getArticle_number)
                .distinct().collect(Collectors.toList());
        doubleRunOffSaleArtNoList.forEach(article_number -> {
            // 只保留数字，去除其他所有符号
            String cleanArtNo = article_number.replaceAll("[^0-9]", "");
            List<String> existList = multiOffSaleSameGoMap.containsKey(cleanArtNo) ?  multiOffSaleSameGoMap.get(cleanArtNo) : new ArrayList<>();
            existList.add(article_number);
            multiOffSaleSameGoMap.put(cleanArtNo, existList);
        });


        // 查看ShipMaster 这边有多少相似的货号
        List<ShipMasterProdVO.SMIVO> shipMasterProdList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId), new ArrayList<>());
        List<String> shipArtNoList = shipMasterProdList.stream().map(ShipMasterProdVO.SMIVO::getArtNo)
                .distinct().collect(Collectors.toList());
        shipArtNoList.forEach(artNo -> {
            // 只保留数字，去除其他所有符号
            String cleanArtNo = artNo.replaceAll("[^0-9]", "");
            List<String> existList = multiSameFMap.containsKey(cleanArtNo) ?  multiSameFMap.get(cleanArtNo) : new ArrayList<>();
            existList.add(artNo);
            multiSameFMap.put(cleanArtNo, existList);
        });




        multiSaleSameGoMap.forEach((key, value) -> {
            if (value.size() > 1) {
                System.err.println(key + ":" + value + ":" + value.size());
            }
        });

        System.err.println("============");

        multiSameFMap.forEach((key, value) -> {
            if (value.size() > 1) {
                System.err.println(key + ":" + value + ":" + value.size());
            }
        });

        Set<String> commonKeys = new HashSet<>(multiSaleSameGoMap.keySet());
        commonKeys.retainAll(multiSameFMap.keySet());

        // 获取GO2独有的key
        Set<String> onlyInGoMap = new HashSet<>(multiSaleSameGoMap.keySet());
        onlyInGoMap.removeAll(commonKeys);

        // 获取ShipMaster独有的key
        Set<String> onlyInFMap = new HashSet<>(multiSameFMap.keySet());
        onlyInFMap.removeAll(commonKeys);

        // 打印各自独有的key
        System.err.println("============ GO2独有的key ============");
        onlyInGoMap.forEach(key -> {
            System.err.println(key + ":" + multiSaleSameGoMap.get(key));
        });

        System.err.println("============ ShipMaster独有的key ============");
        onlyInFMap.forEach(key -> {
            System.err.println(key + ":" + multiSameFMap.get(key));
        });


        onlyInFMap.removeAll(multiOffSaleSameGoMap.keySet());
        System.err.println("============ ShipMaster 去掉下架的 独有的key ============");
        onlyInFMap.forEach(key -> {
            System.err.println(key + ":" + multiSameFMap.get(key));
        });




      /*  List<String> articleNumberList = doubleRunBasicList.stream().map(DoubleRunVO.DRIArtNoSkuVO::getArticle_number)
                .distinct().collect(Collectors.toList());
        List<ShipMasterVO.SMIVO> shipMasterProdList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId), new ArrayList<>());
        List<String> artNoList = shipMasterProdList.stream().map(ShipMasterVO.SMIVO::getArtNo).distinct().collect(Collectors.toList());
*/








        /*// double_run基础数据
        List<DoubleRunVO.DRIArtNoSkuVO> doubleRunBasicList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_DOUBLE_RUN_BASIC_KEY + userId), new ArrayList<>());
        List<String> articleNumberList = doubleRunBasicList.stream().map(DoubleRunVO.DRIArtNoSkuVO::getArticle_number)
                .map(this::cleanArticleNumber).map(x -> x.toLowerCase(Locale.ROOT)).distinct().collect(Collectors.toList());
        // ship_master基础数据
        List<ShipMasterVO.SMIVO> shipMasterProdList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId), new ArrayList<>());
        List<String> artNoList = shipMasterProdList.stream().map(ShipMasterVO.SMIVO::getArtNo)
                .map(x -> x.toLowerCase(Locale.ROOT)).distinct().collect(Collectors.toList());


        Set<String> common = new HashSet<>(articleNumberList);
        common.retainAll(artNoList);

        // 找出只存在于第一个列表的货号
        Set<String> onlyInList1 = new HashSet<>(articleNumberList);
        onlyInList1.removeAll(artNoList);

        // 找出只存在于第二个列表的货号
        Set<String> onlyInList2 = new HashSet<>(artNoList);
        onlyInList2.removeAll(articleNumberList);

        common.forEach(artNo -> System.err.println("common:" + artNo));
        onlyInList1.stream().sorted(Comparator.naturalOrder()).forEach(artNo -> System.err.println("onlyInList1:" + artNo));
        onlyInList2.stream().sorted(Comparator.naturalOrder()).forEach(artNo -> System.err.println("onlyInList2:" + artNo));*/


        return R.ok();
    }


}
