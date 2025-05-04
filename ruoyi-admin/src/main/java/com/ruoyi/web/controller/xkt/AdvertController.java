package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.advert.AdvertCreateVO;
import com.ruoyi.web.controller.xkt.vo.advert.AdvertPageVO;
import com.ruoyi.web.controller.xkt.vo.advert.AdvertResVO;
import com.ruoyi.web.controller.xkt.vo.advert.AdvertUpdateVO;
import com.ruoyi.xkt.dto.advert.AdvertCreateDTO;
import com.ruoyi.xkt.dto.advert.AdvertPageDTO;
import com.ruoyi.xkt.dto.advert.AdvertResDTO;
import com.ruoyi.xkt.dto.advert.AdvertUpdateDTO;
import com.ruoyi.xkt.service.IAdvertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员管理推广营销Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "推广营销")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/adverts")
public class AdvertController extends XktBaseController {

    final IAdvertService advertService;

    /**
     * 新增推广营销。只有超级管理员有权限
     */
    @ApiOperation(value = "新增推广营销", httpMethod = "POST", response = R.class)
    @Log(title = "新增推广营销", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody AdvertCreateVO createVO) {
        return R.ok(advertService.create(BeanUtil.toBean(createVO, AdvertCreateDTO.class)));
    }

    /**
     * 获取推广营销详细信息
     */
    @ApiOperation(value = "获取推广营销详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{advertId}")
    public R<AdvertResVO> getInfo(@PathVariable("advertId") Long advertId) {
        return R.ok(BeanUtil.toBean(advertService.getInfo(advertId), AdvertResVO.class));
    }

    /**
     * 查询推广营销列表
     */
    @ApiOperation(value = "查询推广营销列表 ", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<AdvertResDTO>> page(@Validated @RequestBody AdvertPageVO pageVO) {
        return R.ok(advertService.page(BeanUtil.toBean(pageVO, AdvertPageDTO.class)));
    }

    /**
     * 修改推广营销信息
     */
    @ApiOperation(value = "修改推广营销信息", httpMethod = "PUT", response = R.class)
    @Log(title = "修改推广营销信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@Validated @RequestBody AdvertUpdateVO updateVO) {
        return R.ok(advertService.updateAdvert(BeanUtil.toBean(updateVO, AdvertUpdateDTO.class)));
    }


    /**
     * 推广营销下线
     */
    @ApiOperation(value = "推广营销下线", httpMethod = "PUT", response = R.class)
    @Log(title = "推广营销下线", businessType = BusinessType.UPDATE)
    @PutMapping("/offline/{advertId}")
    public R<Integer> offline(@PathVariable Long advertId) {
        return R.ok(advertService.offline(advertId));
    }


}
