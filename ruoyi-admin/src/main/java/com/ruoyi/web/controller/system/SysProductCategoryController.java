package com.ruoyi.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.vo.productCategory.AppHomeProdCateListResVO;
import com.ruoyi.common.core.domain.vo.productCategory.ProdCateListResVO;
import com.ruoyi.common.core.domain.vo.productCategory.ProdCateListVO;
import com.ruoyi.common.core.domain.vo.productCategory.ProdCateVO;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.dto.productCategory.ProdCateDTO;
import com.ruoyi.system.domain.dto.productCategory.ProdCateListDTO;
import com.ruoyi.system.service.ISysProductCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类
 *
 * @author ruoyi
 */
@Api(tags = "系统基础数据 - 商品分类")
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/v1/sys/prod-cate")
public class SysProductCategoryController extends XktBaseController {

    final ISysProductCategoryService prodCateService;

    @ApiOperation(value = "新增商品分类", httpMethod = "POST", response = R.class)
    @Log(title = "新增商品分类", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody ProdCateVO prodCateVO) {
        return R.ok(prodCateService.create(BeanUtil.toBean(prodCateVO, ProdCateDTO.class)));
    }

    @ApiOperation(value = "修改商品分类", httpMethod = "PUT", response = R.class)
    @Log(title = "修改商品分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> update(@Validated @RequestBody ProdCateVO prodCateVO) {
        return R.ok(prodCateService.update(BeanUtil.toBean(prodCateVO, ProdCateDTO.class)));
    }

    @ApiOperation(value = "删除商品分类", httpMethod = "DELETE", response = R.class)
    @Log(title = "删除商品分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{prodCateId}")
    public R<Integer> delete(@PathVariable Long prodCateId) {
        return R.ok(prodCateService.delete(prodCateId));
    }


    @ApiOperation(value = "查询商品分类详细", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{prodCateId}")
    public R<ProdCateVO> getInfo(@PathVariable Long prodCateId) {
        return R.ok(BeanUtil.toBean(prodCateService.selectById(prodCateId), ProdCateVO.class));
    }

    @ApiOperation(value = "获取商品分类列表", httpMethod = "POST", response = R.class)
    @PostMapping("/list")
    public R<List<ProdCateListResVO>> list(@RequestBody ProdCateListVO listVO) {
        return R.ok(BeanUtil.copyToList(prodCateService.selectList(BeanUtil.toBean(listVO, ProdCateListDTO.class)), ProdCateListResVO.class));
    }

    @ApiOperation(value = "根据1级分类获取二级分类列表", httpMethod = "GET", response = R.class)
    @GetMapping("/sub/{parCateId}")
    public R<List<ProdCateVO>> getSubListByParCateId(@PathVariable Long parCateId) {
        return R.ok(BeanUtil.copyToList(prodCateService.getSubListByParCateId(parCateId), ProdCateVO.class));
    }

    @ApiOperation(value = "获取所有的二级分类", httpMethod = "GET", response = R.class)
    @GetMapping("/subs")
    public R<List<ProdCateVO>> getAllSubList() {
        return R.ok(BeanUtil.copyToList(prodCateService.getAllSubList(), ProdCateVO.class));
    }

    @ApiOperation(value = "APP首页获取商品分类", httpMethod = "GET", response = R.class)
    @GetMapping("/app/home/list")
    public R<List<AppHomeProdCateListResVO>> appHomeCate() {
        return R.ok(BeanUtil.copyToList(prodCateService.selectAppHomeCate(), AppHomeProdCateListResVO.class));
    }

    @ApiOperation(value = "APP分类页", httpMethod = "GET", response = R.class)
    @GetMapping("/app/list")
    public R<List<AppHomeProdCateListResVO>> appCate() {
        return R.ok(BeanUtil.copyToList(prodCateService.appCate(), AppHomeProdCateListResVO.class));
    }

}
