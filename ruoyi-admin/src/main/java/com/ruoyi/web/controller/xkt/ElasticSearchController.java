package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.notice.*;
import com.ruoyi.xkt.dto.notice.*;
import com.ruoyi.xkt.service.IElasticSearchService;
import com.ruoyi.xkt.service.INoticeService;
import com.ruoyi.xkt.service.IStoreProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ES相关处理
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/es")
public class ElasticSearchController extends XktBaseController {

    final IElasticSearchService esService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @Log(title = "往ES中批量新增数据", businessType = BusinessType.INSERT)
    @PutMapping("/batch")
    public R<Integer> batchCreate() {
        return R.ok(esService.batchCreate());
    }

}
