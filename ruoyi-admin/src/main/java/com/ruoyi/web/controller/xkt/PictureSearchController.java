package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.web.controller.xkt.vo.pictureSearch.PicSearchVO;
import com.ruoyi.web.controller.xkt.vo.storeProd.StoreProdViewVO;
import com.ruoyi.xkt.dto.picture.SearchRequestDTO;
import com.ruoyi.xkt.service.IPictureSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 以图搜款Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "以图搜款")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/pic-search")
public class PictureSearchController extends XktBaseController {

    final IPictureSearchService picSearchService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,seller')")
    @ApiOperation(value = "电商卖家 以图搜款", httpMethod = "POST", response = R.class)
    @PostMapping("")
    public R<List<StoreProdViewVO>> searchProductByPic(@Validated @RequestBody PicSearchVO searchVO) {
        return R.ok(BeanUtil.copyToList(picSearchService
                .searchProductByPic(BeanUtil.toBean(searchVO, SearchRequestDTO.class).setNum(20)), StoreProdViewVO.class));
    }

    @ApiOperation(value = "图搜热款列表", httpMethod = "GET", response = R.class)
    @GetMapping("/hot")
    public R<List<StoreProdViewVO>> searchHotList() {
        return R.ok(BeanUtil.copyToList(picSearchService.listImgSearchTopProduct(), StoreProdViewVO.class));
    }


}
