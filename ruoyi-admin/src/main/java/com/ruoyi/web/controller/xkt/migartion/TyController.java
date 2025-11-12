package com.ruoyi.web.controller.xkt.migartion;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.migartion.vo.CusDiscErrorVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtProdSkuVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyCusDiscImportVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyCusImportVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyProdImportVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyProdStockVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TY 相关
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/ty")
public class TyController extends BaseController {

    final RedisCache redisCache;

    /**
     * step0
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/filter/{userId}")
    public void filterUnValidProd(HttpServletResponse response, @PathVariable("userId") Integer userId, MultipartFile file) throws IOException {
        ExcelUtil<TyProdImportVO> util = new ExcelUtil<>(TyProdImportVO.class);
        List<TyProdImportVO> tyProdVOList = util.importExcel(file.getInputStream());
        // 获取GT无效的存货并过滤
        List<GtProdSkuVO> gtOffSaleList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_OFF_SALE_BASIC_KEY + userId), new ArrayList<>());
        List<String> gtOffSaleArtNumList = gtOffSaleList.stream().map(GtProdSkuVO::getArticle_number).distinct().collect(Collectors.toList());
        List<String> tyProdArtNumList = tyProdVOList.stream().map(TyProdImportVO::getProdArtNum).distinct().collect(Collectors.toList());

        // 找出两个列表的交集
        List<String> commonArtNumList = tyProdArtNumList.stream().filter(gtOffSaleArtNumList::contains).collect(Collectors.toList());
        List<String> allMatchArtNumList = new ArrayList<>(commonArtNumList);
        tyProdArtNumList.stream().filter(x -> x.endsWith("R")).forEach(x -> {
            for (String comArtNum : commonArtNumList) {
                if (Objects.equals(x, comArtNum + "R") || Objects.equals(x, comArtNum + "-R")) {
                    System.err.println(x + " : " + comArtNum);
                    allMatchArtNumList.add(x);
                    break;
                }
            }
        });

        List<TyProdImportVO> downloadList = tyProdVOList.stream()
                .filter(x -> ObjectUtils.isNotEmpty(x.getPrice()))
                .filter(x -> CollectionUtils.isEmpty(allMatchArtNumList) || !allMatchArtNumList.contains(x.getProdArtNum()))
                .collect(Collectors.toList());
        ExcelUtil<TyProdImportVO> downloadUtil = new ExcelUtil<>(TyProdImportVO.class);
        // 设置下载excel名
        String encodedFileName = URLEncoder.encode("TY过滤GT" + DateUtils.getDate(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName + ".xlsx");
        downloadUtil.exportExcel(response, downloadList, "TY过滤GT");
    }



    /**
     * step1
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/prod/cache/{userId}")
    public R<Integer> createProdCache(@PathVariable Integer userId, MultipartFile file) throws IOException {
        ExcelUtil<TyProdImportVO> util = new ExcelUtil<>(TyProdImportVO.class);
        List<TyProdImportVO> tyProdVOList = util.importExcel(file.getInputStream());
        // 去掉空格
        tyProdVOList = tyProdVOList.stream()
                // 只导入价格不能为空的存货
                .filter(x -> ObjectUtils.isNotEmpty(x.getPrice()))
                .map(x -> {
                    String prodArtNum = x.getProdArtNum().trim();
                    String colorName = x.getColorName().trim();
                    // 如果有sn则取sn 否则取hpbm
                    String tySnPrefix = StringUtils.isNotBlank(x.getSn()) ? x.getSn().trim() : x.getHpbm().trim();
                    // 如果货号结尾包括R 则表明是 货号为绒里，手动给颜色添加后缀“绒里”
                    if (prodArtNum.endsWith("R")) {
                        colorName = colorName.contains("绒") ? colorName : (colorName + "绒里");
                    }
                    return x.setProdArtNum(prodArtNum).setColorName(colorName).setSn(tySnPrefix);
                })
                .collect(Collectors.toList());
        Map<String, List<TyProdImportVO>> prodMap = tyProdVOList.stream().collect(Collectors.groupingBy(TyProdImportVO::getProdArtNum));
        prodMap.forEach((k, v) -> {
            System.err.println(k + ":" + v);
        });
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_TY_PROD_KEY + userId, tyProdVOList);
        return R.ok();
    }


    /**
     * step2
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/cus/cache/{userId}")
    public R<Integer> createCusCache(@PathVariable Integer userId, MultipartFile file) throws IOException {
        ExcelUtil<TyCusImportVO> util = new ExcelUtil<>(TyCusImportVO.class);
        List<TyCusImportVO> tyCusVOList = util.importExcel(file.getInputStream());
        tyCusVOList.forEach(x -> x.setCusName(x.getCusName().trim()));
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_TY_CUS_KEY + userId, tyCusVOList);
        return R.ok();
    }

    /**
     * step3
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/cus/disc/cache")
    public R<Integer> createCusDiscCache(@RequestParam(value = "userId") Long userId, @RequestParam("compareStr") String compareStr,
                                         MultipartFile file) throws IOException {
        // 导入的excel名，只取客户名称
        final String cusName = file.getOriginalFilename().replaceAll("\\..*$", "").replaceAll("[^\\u4e00-\\u9fa50-9]", "").trim();
        ExcelUtil<TyCusDiscImportVO> util = new ExcelUtil<>(TyCusDiscImportVO.class);
        List<TyCusDiscImportVO> tyProdVOList = util.importExcel(file.getInputStream());
        // 从redis中获取已存在的客户优惠数据
        List<TyCusDiscImportVO> cacheList = redisCache.getCacheObject(CacheConstants.MIGRATION_TY_CUS_DISCOUNT_KEY + userId);
        cacheList = Optional.ofNullable(cacheList).orElse(new ArrayList<>());
        // 前置校验
        this.cusDiscPrefixFilter(userId, cusName, cacheList);
        // 获取GT所有下架的存货，将该部分存货排除
        // 先从redis中获取列表数据
        List<GtProdSkuVO> gtOffSaleList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_OFF_SALE_BASIC_KEY + userId), new ArrayList<>());
        // 同时添加 单里 和 绒里 过滤
        List<String> gtOffSaleArtNumList = gtOffSaleList.stream()
                .map(x -> x.getArticle_number().trim())
                .flatMap(articleNumber -> Stream.of(articleNumber, articleNumber + compareStr))
                .collect(Collectors.toList());

        // 因为是采用的截图转excel方式，所以每个张图会冗余部分数据长度，导入是需要判断是否已存在
        Map<String, TyCusDiscImportVO> importCusDiscMap = new HashMap<>();
        List<TyCusDiscImportVO> importList = new ArrayList<>();
        tyProdVOList.stream()
                // 只设置有优惠的存货及颜色
                .filter(x -> StringUtils.isNotBlank(x.getProdArtNum()) && ObjectUtils.isNotEmpty(x.getBasicPrice()) && ObjectUtils.isNotEmpty(x.getCustomerPrice()))
                // 排除掉GT已下架部分的存货
                .filter(x -> !gtOffSaleArtNumList.contains(x.getProdArtNum()))
                .forEach(x -> {
                    if (importCusDiscMap.containsKey(x.getProdArtNum() + ":" + x.getColorName())) {
                        System.err.println(x.getProdArtNum() + ":" + x.getColorName());
                    } else {
                        final int discount = ObjectUtils.defaultIfNull(x.getBasicPrice(), 0) - ObjectUtils.defaultIfNull(x.getCustomerPrice(), 0);
                        String prodArtNum = x.getProdArtNum().trim();
                        String colorName = x.getColorName().trim();
                        // 如果货号结尾包括-R 则表明是 货号为绒里，手动给颜色添加后缀“绒里”
                        if (prodArtNum.endsWith("R")) {
                            colorName = colorName.contains("绒") ? colorName : (colorName + "绒里");
                        }
                        x.setProdArtNum(prodArtNum).setColorName(colorName).setCusName(cusName).setDiscount(discount);
                        importCusDiscMap.put(x.getProdArtNum() + ":" + x.getColorName(), x);
                        importList.add(x);
                    }
                });
        // 如果同一货号 + 颜色 存在多个优惠，则只取其中之一
        Map<String, List<TyCusDiscImportVO>> distinctDiscMap = importList.stream().distinct().collect(Collectors.groupingBy(x -> x.getProdArtNum() + ":" + x.getColorName()));
        List<TyCusDiscImportVO> distinctDiscList = new ArrayList<>();
        distinctDiscMap.forEach((k, colorDiscList) -> {
            // 存在这种错误数据，同一客户：相同颜色及货号 优惠相同 但初始价格及优惠价格不一样 比如：228 213；218 203 但优惠都是15。所以统一处理，取最大优惠
            if (colorDiscList.size() > 1) {
                // 获取最大优惠
                TyCusDiscImportVO maxDisc = colorDiscList.stream().max(Comparator.comparingInt(TyCusDiscImportVO::getDiscount)).get();
                distinctDiscList.add(maxDisc);
            } else {
                distinctDiscList.addAll(colorDiscList);
            }
        });
        // 加到总的客户优惠上
        CollectionUtils.addAll(cacheList, distinctDiscList);
        // TODO 过滤优惠大于0 是在比较插入数据的时候做的
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_TY_CUS_DISCOUNT_KEY + userId, cacheList);
        return R.ok();
    }


    /**
     * step4
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/stock/cache/{userId}")
    public R<Integer> createTyProdStockCache(@PathVariable Integer userId, MultipartFile file) throws IOException {
        ExcelUtil<TyProdStockVO> util = new ExcelUtil<>(TyProdStockVO.class);
        List<TyProdStockVO> tyStockList = util.importExcel(file.getInputStream());
        // 因为是采用的截图转excel方式，所以每个张图会冗余部分数据长度，导入是需要判断是否已存在
        Map<String, TyProdStockVO> importStockMap = new ConcurrentHashMap<>();
        List<TyProdStockVO> cacheList = new ArrayList<>();
        tyStockList.forEach(x -> {
            String prodArtNum = x.getProdArtNum().trim();
            String colorName = x.getColorName().trim();
            // 如果货号结尾包括R 则表明是 货号为绒里，手动给颜色添加后缀“绒里”
            if (prodArtNum.endsWith("R")) {
                colorName = colorName.contains("绒") ? colorName : (colorName + "绒里");
            }
            if (importStockMap.containsKey(prodArtNum + ":" + colorName)) {
                System.err.println(prodArtNum + ":" + colorName);
            } else {
                importStockMap.put(prodArtNum + ":" + colorName, x);
                cacheList.add(x.setProdArtNum(prodArtNum).setColorName(colorName));
            }
        });
        redisCache.setCacheObject(CacheConstants.MIGRATION_TY_PROD_STOCK_KEY + userId, cacheList);
        return R.ok();
    }

    /**
     * step5
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @GetMapping("/error/cus/price/{userId}")
    public void getErrorCusDisc(HttpServletResponse response, @PathVariable Integer userId) throws UnsupportedEncodingException {
        List<TyCusDiscImportVO> cacheList = redisCache.getCacheObject(CacheConstants.MIGRATION_TY_CUS_DISCOUNT_KEY + userId);
        List<CusDiscErrorVO> errList = new ArrayList<>();
        // 1. 有哪些是优惠价大于销售价的
        cacheList.forEach(record -> {
            final Integer basicPrice = ObjectUtils.defaultIfNull(record.getBasicPrice(), 0);
            final Integer customerPrice = ObjectUtils.defaultIfNull(record.getCustomerPrice(), 0);
            if (basicPrice - customerPrice < 0) {
                errList.add(new CusDiscErrorVO().setCusName(record.getCusName()).setProdArtNum(record.getProdArtNum())
                        .setColorName(record.getColorName()).setErrMsg("优惠价大于原售价").setDetail("销售价:" + basicPrice + " 优惠价:" + customerPrice));
            }
        });
        /*// 2. 有哪些优惠是同一货号不同颜色优惠金额不一致
        Map<String, Map<String, List<TyCusDiscImportVO>>> artNoCusDiscMap = cacheList.stream().collect(Collectors
                .groupingBy(TyCusDiscImportVO::getProdArtNum, Collectors.groupingBy(TyCusDiscImportVO::getCusName)));
        // 货号下客户优惠的map
        artNoCusDiscMap.forEach((artNo, cusDiscMap) -> cusDiscMap.forEach((cusName, cusDiscList) -> {
            // 不同颜色优惠的map
            Map<String, Integer> colorDiscMap = cusDiscList.stream().collect(Collectors
                    .toMap(TyCusDiscImportVO::getColorName, x -> ObjectUtils.defaultIfNull(x.getDiscount(), 0), (v1, v2) -> v2));
            // 判断所有颜色的优惠金额是否一致
            Set<Integer> discValueSet = new HashSet<>(colorDiscMap.values());
            if (discValueSet.size() > 1) {
                StringBuilder errSB = new StringBuilder();
                colorDiscMap.forEach((colorName, discount) -> errSB.append(colorName).append(":").append(discount).append(";"));
                errList.add(new CusDiscErrorVO().setCusName(cusName).setProdArtNum(artNo).setColorName(colorDiscMap.keySet().toString())
                        .setDetail(errSB.toString()).setErrMsg("客户优惠金额不一致"));
            }
        }));*/
        for (int i = 0; i < errList.size(); i++) {
            errList.get(i).setOrderNum(i + 1);
        }
        ExcelUtil<CusDiscErrorVO> util = new ExcelUtil<>(CusDiscErrorVO.class);
        String encodedFileName = URLEncoder.encode("TY客户优惠问题" + DateUtils.getDate(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName + ".xlsx");
        util.exportExcel(response, errList, "TY客户优惠问题");
    }


    /**
     * 客户优惠前置校验
     */
    private void cusDiscPrefixFilter(Long userId, String cusName, List<TyCusDiscImportVO> cacheList) {
        // 判断客户是否存在
        List<TyCusImportVO> tyCusVOList = redisCache.getCacheObject(CacheConstants.MIGRATION_TY_CUS_KEY + userId);
        Map<String, TyCusImportVO> tyCusMap = tyCusVOList.stream().collect(Collectors.toMap(TyCusImportVO::getCusName, x -> x));
        if (!tyCusMap.containsKey(cusName)) {
            throw new ServiceException(cusName + " : 客户不存在", HttpStatus.ERROR);
        }
        // 判断当前客户是否已导入过，若是则直接报错
        Map<String, List<TyCusDiscImportVO>> cusDiscMap = cacheList.stream().collect(Collectors.groupingBy(TyCusDiscImportVO::getCusName));
        if (cusDiscMap.containsKey(cusName)) {
            throw new ServiceException(cusName + " : 客户已导入过优惠数据", HttpStatus.ERROR);
        }
    }

}
