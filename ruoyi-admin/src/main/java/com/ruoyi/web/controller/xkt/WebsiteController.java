package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.storeCustomer.StoreCusGeneralSaleVO;
import com.ruoyi.web.controller.xkt.vo.storeSale.StoreSalePageVO;
import com.ruoyi.web.controller.xkt.vo.storeSale.StoreSalePayStatusVO;
import com.ruoyi.web.controller.xkt.vo.storeSale.StoreSaleVO;
import com.ruoyi.web.controller.xkt.website.IndexSearchVO;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSaleDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePageDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePageResDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePayStatusDTO;
import com.ruoyi.xkt.dto.website.IndexSearchDTO;
import com.ruoyi.xkt.service.IIndexSearchService;
import com.ruoyi.xkt.service.IStoreSaleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 网站首页Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "网站首页")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/website")
public class WebsiteController extends XktBaseController {

    final IIndexSearchService searchService;

    /**
     * 网站首页搜索
     */
    @ApiOperation(value = "网站首页搜索", httpMethod = "POST", response = R.class)
    @PostMapping("/index/search")
    public R<Page<ESProductDTO>> page(@Validated @RequestBody IndexSearchVO searchVO) throws IOException {
        return R.ok(searchService.search(BeanUtil.toBean(searchVO, IndexSearchDTO.class)));
    }



}
