package com.ruoyi.web.controller.xkt.migartion;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyCusDiscImportVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyCusImportVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyProdImportVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyProdStockVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
                    String tySnPrefix = x.getTySnPrefix().trim();
                    // 如果货号包括-R 则表明是 货号为绒里，手动给颜色添加后缀“绒里”
                    if (prodArtNum.contains("R")) {
                        colorName = colorName.contains("绒") ? colorName : (colorName + "绒里");
                    }
                    return x.setProdArtNum(prodArtNum).setColorName(colorName).setTySnPrefix(tySnPrefix);
                })
                .collect(Collectors.toList());
        Map<String, List<TyProdImportVO>> prodMap = tyProdVOList.stream().collect(Collectors.groupingBy(TyProdImportVO::getProdArtNum));
        prodMap.forEach((k, v) -> {
            System.err.println(k + ":" + v);
        });
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_TY_PROD_KEY + userId, tyProdVOList, 5, TimeUnit.DAYS);
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
        redisCache.setCacheObject(CacheConstants.MIGRATION_TY_CUS_KEY + userId, tyCusVOList, 5, TimeUnit.DAYS);
        return R.ok();
    }

    /**
     * step3
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/cus/disc/cache/{cusName}/{userId}")
    public R<Integer> createCusDiscCache(@PathVariable(value = "userId") Integer userId, @PathVariable(value = "cusName") String cusName,
                                         MultipartFile file) throws IOException {
        // 从redis中获取已存在的客户优惠数据
        List<TyCusDiscImportVO> cacheList = redisCache.getCacheObject(CacheConstants.MIGRATION_TY_CUS_DISCOUNT_KEY + userId);
        cacheList = Optional.ofNullable(cacheList).orElse(new ArrayList<>());
        // 判断当前客户是否已导入过，若是则直接报错
        Map<String, List<TyCusDiscImportVO>> cusDiscMap = cacheList.stream().collect(Collectors.groupingBy(TyCusDiscImportVO::getCusName));
        if (cusDiscMap.containsKey(cusName)) {
            throw new ServiceException("客户已导入过优惠数据", HttpStatus.ERROR);
        }
        ExcelUtil<TyCusDiscImportVO> util = new ExcelUtil<>(TyCusDiscImportVO.class);
        List<TyCusDiscImportVO> tyProdVOList = util.importExcel(file.getInputStream());
        // 因为是采用的截图转excel方式，所以每个张图会冗余部分数据长度，导入是需要判断是否已存在
        Map<String, TyCusDiscImportVO> importCusDiscMap = new ConcurrentHashMap<>();
        List<TyCusDiscImportVO> importList = new ArrayList<>();
        List<String> errorList = new ArrayList<>();
        tyProdVOList.stream()
                // 只设置有优惠的存货及颜色
                .filter(x -> ObjectUtils.isNotEmpty(x.getBasicPrice()) && ObjectUtils.isNotEmpty(x.getCustomerPrice()))
                .forEach(x -> {
                    if (importCusDiscMap.containsKey(x.getProdArtNum() + ":" + x.getColorName())) {
                        System.err.println(x.getProdArtNum() + ":" + x.getColorName());
                    } else {
                        final int discount = x.getBasicPrice() - x.getCustomerPrice();
                        if (discount < 0) {
                            errorList.add(cusName + ":" + x.getProdArtNum() + ":" + x.getColorName() + ":优惠金额不能小于0");
                        }
                        String prodArtNum = x.getProdArtNum().trim();
                        String colorName = x.getColorName().trim();
                        // 如果货号包括-R 则表明是 货号为绒里，手动给颜色添加后缀“绒里”
                        if (prodArtNum.contains("R")) {
                            colorName = colorName.contains("绒") ? colorName : (colorName + "绒里");
                        }
                        x.setProdArtNum(prodArtNum).setColorName(colorName).setCusName(cusName).setDiscount(discount);
                        importCusDiscMap.put(x.getProdArtNum() + ":" + x.getColorName(), x);
                        importList.add(x);
                    }
                });
        if (CollectionUtils.isNotEmpty(errorList)) {
            throw new ServiceException(errorList.toString(), HttpStatus.ERROR);
        }
        // 加到总的客户优惠上
        CollectionUtils.addAll(cacheList, importList);
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_TY_CUS_DISCOUNT_KEY + userId, cacheList, 5, TimeUnit.DAYS);
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
            // 如果货号包括R 则表明是 货号为绒里，手动给颜色添加后缀“绒里”
            if (prodArtNum.contains("R")) {
                colorName = colorName.contains("绒") ? colorName : (colorName + "绒里");
            }
            if (importStockMap.containsKey(prodArtNum + ":" + colorName)) {
                System.err.println(prodArtNum + ":" + colorName);
            } else {
                importStockMap.put(prodArtNum + ":" + colorName, x);
                cacheList.add(x.setProdArtNum(prodArtNum).setColorName(colorName));
            }
        });
        redisCache.setCacheObject(CacheConstants.MIGRATION_TY_PROD_STOCK_KEY + userId, cacheList, 5, TimeUnit.DAYS);
        return R.ok();
    }


}
