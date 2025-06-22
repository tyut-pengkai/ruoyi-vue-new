package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storeProdStorage.StoreStorageExportVO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreStorageDetailDownloadDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreStorageExportDTO;
import com.ruoyi.xkt.service.IStoreProductStorageDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

/**
 * 档口商品入库明细Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口商品入库明细")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/storage-details")
public class StoreProductStorageDetailController extends XktBaseController {

    final IStoreProductStorageDetailService storageDetailService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "导出入库记录", httpMethod = "POST", response = R.class)
    @Log(title = "导出入库记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @Validated @RequestBody StoreStorageExportVO exportVO) throws UnsupportedEncodingException {
        List<StoreStorageDetailDownloadDTO> downloadList = storageDetailService.export(BeanUtil.toBean(exportVO, StoreStorageExportDTO.class));
        ExcelUtil<StoreStorageDetailDownloadDTO> util = new ExcelUtil<>(StoreStorageDetailDownloadDTO.class);
        // 设置下载excel名
        String encodedFileName = URLEncoder.encode("入库记录" + DateUtils.getDate(),  "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName + ".xlsx");
        util.exportExcel(response, downloadList, "入库记录");
    }

}
