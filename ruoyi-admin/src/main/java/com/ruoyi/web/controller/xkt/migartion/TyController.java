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

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/prod/cache/{userId}")
    public R<Integer> createProdCache(@PathVariable Integer userId, MultipartFile file) throws IOException {
        ExcelUtil<TyProdImportVO> util = new ExcelUtil<>(TyProdImportVO.class);
        List<TyProdImportVO> tyProdVOList = util.importExcel(file.getInputStream());
        // 去掉空格
        tyProdVOList.forEach(x -> x.setProdArtNum(x.getProdArtNum().trim()).setColorName(x.getColorName().trim())
                .setOtherSnPrefix(x.getOtherSnPrefix().trim()));
        Map<String, List<TyProdImportVO>> prodMap = tyProdVOList.stream().collect(Collectors.groupingBy(TyProdImportVO::getProdArtNum));
        prodMap.forEach((k, v) -> {
            System.err.println(k + ":" + v);
        });
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_TY_PROD_KEY + userId, tyProdVOList, 5, TimeUnit.DAYS);
        return R.ok();
    }


    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/cus/cache/{userId}")
    public R<Integer> createCusCache(@PathVariable Integer userId, MultipartFile file) throws IOException {
        ExcelUtil<TyCusImportVO> util = new ExcelUtil<>(TyCusImportVO.class);
        List<TyCusImportVO> tyProdVOList = util.importExcel(file.getInputStream());
        tyProdVOList.forEach(x -> x.setCusName(x.getCusName().trim()));
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_TY_CUS_KEY + userId, tyProdVOList, 5, TimeUnit.DAYS);
        return R.ok();
    }


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
        tyProdVOList.stream()
                // 只设置有优惠的存货及颜色
                .filter(x -> ObjectUtils.isNotEmpty(x.getBasicPrice()) && ObjectUtils.isNotEmpty(x.getCustomerPrice()))
                .forEach(x -> {
                    if (importCusDiscMap.containsKey(x.getProdArtNum() + ":" + x.getColorName())) {
                        System.err.println(x.getProdArtNum() + ":" + x.getColorName());
                    } else {
                        x.setColorName(x.getColorName().trim()).setCusName(cusName).setProdArtNum(x.getProdArtNum().trim());
                        importCusDiscMap.put(x.getProdArtNum() + ":" + x.getColorName(), x);
                        importList.add(x);
                    }
                });
        // 加到总的客户优惠上
        CollectionUtils.addAll(cacheList, importList);
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_TY_CUS_DISCOUNT_KEY + userId, cacheList, 5, TimeUnit.DAYS);
        return R.ok();
    }


}
