package com.ruoyi.web.controller.xkt.migartion;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyCusImportVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyProdImportVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_TY_CUS_KEY + userId, tyProdVOList, 5, TimeUnit.DAYS);
        return R.ok();
    }



}
