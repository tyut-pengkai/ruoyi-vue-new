package com.ruoyi.web.controller.xkt.migartion;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.migartion.vo.GtAndFHBCompareDownloadVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbProdVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtProdSkuVO;
import com.ruoyi.xkt.service.shipMaster.IShipMasterService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    // TODO 档口注册的时候，会创建现金客户，在插入客户时，需要注意
    // TODO 档口注册的时候，会创建现金客户，在插入客户时，需要注意
    // TODO 档口注册的时候，会创建现金客户，在插入客户时，需要注意


    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PutMapping("/double/ship/{userId}/{supplierId}")
    public void compare(HttpServletResponse response, @PathVariable("userId") Integer userId, @PathVariable("supplierId") Integer supplierId) throws UnsupportedEncodingException {
        Map<String, List<String>> multiSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiOffSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiSameFMap = new HashMap<>();
        List<GtProdSkuVO> doubleRunSaleBasicList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + userId), new ArrayList<>());
        Map<String, String> articleNoColorMap = doubleRunSaleBasicList.stream()
                .collect(Collectors.groupingBy(
                        GtProdSkuVO::getArticle_number,
                        Collectors.collectingAndThen(Collectors.mapping(GtProdSkuVO::getColor, Collectors.toList()),
                                list -> "(" + list.stream().distinct().collect(Collectors.joining(",")) + ")")));
        List<String> doubleRunSaleArtNoList = doubleRunSaleBasicList.stream().map(GtProdSkuVO::getArticle_number)
                .distinct().collect(Collectors.toList());
        // 查看double_run 在售的商品 这边有多少相似的货号
        doubleRunSaleArtNoList.forEach(article_number -> {
            // 只保留核心连续的数字，去除其他所有符号
            String cleanArtNo = this.extractCoreArticleNumber(article_number);
            List<String> existList = multiSaleSameGoMap.containsKey(cleanArtNo) ? multiSaleSameGoMap.get(cleanArtNo) : new ArrayList<>();
            existList.add(article_number + articleNoColorMap.get(article_number));
            multiSaleSameGoMap.put(cleanArtNo, existList);
        });

        // 查看double_run 下架的商品有多少相似的货号
        List<GtProdSkuVO> doubleRunOffSaleBasicList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_OFF_SALE_BASIC_KEY + userId), new ArrayList<>());
        List<String> doubleRunOffSaleArtNoList = doubleRunOffSaleBasicList.stream().map(GtProdSkuVO::getArticle_number)
                .distinct().collect(Collectors.toList());
        doubleRunOffSaleArtNoList.forEach(article_number -> {
            // 只保留核心连续的数字，去除其他所有符号
            String cleanArtNo = this.extractCoreArticleNumber(article_number);
            List<String> existList = multiOffSaleSameGoMap.containsKey(cleanArtNo) ? multiOffSaleSameGoMap.get(cleanArtNo) : new ArrayList<>();
            existList.add(article_number);
            multiOffSaleSameGoMap.put(cleanArtNo, existList);
        });

        // 查看ShipMaster 这边有多少相似的货号
        List<FhbProdVO.SMIVO> shipMasterProdList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId), new ArrayList<>());
        Map<String, String> shipMasterArticleNoColorMap = shipMasterProdList.stream()
                .collect(Collectors.groupingBy(
                        FhbProdVO.SMIVO::getArtNo,
                        Collectors.collectingAndThen(Collectors.mapping(FhbProdVO.SMIVO::getColor, Collectors.toList()),
                                list -> "(" + list.stream().distinct().collect(Collectors.joining(",")) + ")")));
        List<String> shipArtNoList = shipMasterProdList.stream().map(FhbProdVO.SMIVO::getArtNo)
                .distinct().collect(Collectors.toList());
        shipArtNoList.forEach(artNo -> {
            // 只保留核心连续的数字，去除其他所有符号
            String cleanArtNo = this.extractCoreArticleNumber(artNo);
            List<String> existList = multiSameFMap.containsKey(cleanArtNo) ? multiSameFMap.get(cleanArtNo) : new ArrayList<>();
            existList.add(artNo + shipMasterArticleNoColorMap.get(artNo));
            multiSameFMap.put(cleanArtNo, existList);
        });

        // 清洗数据之后，GO平台和FHB平台 货号一致的，按照这种来展示: GT => [Z1110(黑色,黑色绒里,棕色,棕色绒里)] <= 清洗后的货号 => [Z1110(黑色,黑色绒里,棕色,棕色绒里)] <= FHB

        System.err.println("============ 两边系统“一致”的货号 ============");
        // 清洗后，相同货号映射
        List<String> matchArtNoList = new ArrayList<>();
        Set<String> commonArtNos = new HashSet<>(multiSaleSameGoMap.keySet());
        commonArtNos.retainAll(multiSameFMap.keySet());
        commonArtNos.forEach(artNo -> {
            final String sameArtNo = "GT => " + multiSaleSameGoMap.get(artNo) + " <= " + artNo + " => " + multiSameFMap.get(artNo) + " <= FHB";
            matchArtNoList.add(sameArtNo);
        });
        // 输出货号清洗后相同的货号
        matchArtNoList.forEach(System.out::println);

        matchArtNoList.add("============ GT独有的货号 ============");
        matchArtNoList.add("============ GT独有的货号 ============");

        System.err.println("============ GT独有的key ============");
        // 获取GO2独有的key
        Set<String> onlyInGoMap = new HashSet<>(multiSaleSameGoMap.keySet());
        onlyInGoMap.removeAll(commonArtNos);
        if (CollectionUtils.isNotEmpty(onlyInGoMap)) {
            onlyInGoMap.forEach(x -> {
                matchArtNoList.addAll(multiSaleSameGoMap.get(x));
                System.out.println(multiSaleSameGoMap.get(x));
            });
        }

        matchArtNoList.add("============ FHB独有的货号 ============");
        matchArtNoList.add("============ FHB独有的货号 ============");

        System.err.println("============ ShipMaster 去掉公共的、下架的 独有的key ============");
        // 获取ShipMaster独有的key  去掉公共的、去掉下架的商品
        Set<String> onlyInFMap = new HashSet<>(multiSameFMap.keySet());
        onlyInFMap.removeAll(commonArtNos);
        onlyInFMap.removeAll(multiOffSaleSameGoMap.keySet());
        if (CollectionUtils.isNotEmpty(onlyInFMap)) {
            onlyInFMap.forEach(x -> {
                matchArtNoList.addAll(multiSameFMap.get(x));
                System.out.println(multiSameFMap.get(x));
            });
        }

        List<GtAndFHBCompareDownloadVO> downloadList = new ArrayList<>();
        for (int i = 0; i < matchArtNoList.size(); i++) {
            downloadList.add(new GtAndFHBCompareDownloadVO().setOrderNum(i + 1).setCode(matchArtNoList.get(i)));
        }
        ExcelUtil<GtAndFHBCompareDownloadVO> util = new ExcelUtil<>(GtAndFHBCompareDownloadVO.class);
        String encodedFileName = URLEncoder.encode("GT与FHB差异" + DateUtils.getDate(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName + ".xlsx");
        util.exportExcel(response, downloadList, "差异");

    }

    /**
     * 提取货号中的核心数字部分
     * 例如: z1104 -> 1104, z1087高 -> 1087, z1003-1 -> 1003, 922- -> 922, -8072 -> 8072
     *
     * @param articleNumber 货号
     * @return 核心数字部分
     */
    private String extractCoreArticleNumber(String articleNumber) {
        if (articleNumber == null || articleNumber.isEmpty()) {
            return "";
        }
        // 使用正则表达式匹配第一组连续的数字
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\d+");
        java.util.regex.Matcher matcher = pattern.matcher(articleNumber);
        // 返回第一组匹配到的数字
        if (matcher.find()) {
            return matcher.group();
        }
        // 如果没有找到数字，返回空字符串
        throw new ServiceException("货号格式错误", HttpStatus.ERROR);
    }


}
