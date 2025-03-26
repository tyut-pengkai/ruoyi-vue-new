package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreCertificate;
import com.ruoyi.xkt.service.IStoreCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口认证Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/store-certs")
public class StoreCertificateController extends BaseController {
    @Autowired
    private IStoreCertificateService storeCertificateService;

    /**
     * 查询档口认证列表
     */
    @PreAuthorize("@ss.hasPermi('system:certificate:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreCertificate storeCertificate) {
        startPage();
        List<StoreCertificate> list = storeCertificateService.selectStoreCertificateList(storeCertificate);
        return getDataTable(list);
    }

    /**
     * 导出档口认证列表
     */
    @PreAuthorize("@ss.hasPermi('system:certificate:export')")
    @Log(title = "档口认证", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreCertificate storeCertificate) {
        List<StoreCertificate> list = storeCertificateService.selectStoreCertificateList(storeCertificate);
        ExcelUtil<StoreCertificate> util = new ExcelUtil<StoreCertificate>(StoreCertificate.class);
        util.exportExcel(response, list, "档口认证数据");
    }

    /**
     * 获取档口认证详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:certificate:query')")
    @GetMapping(value = "/{storeCertId}")
    public AjaxResult getInfo(@PathVariable("storeCertId") Long storeCertId) {
        return success(storeCertificateService.selectStoreCertificateByStoreCertId(storeCertId));
    }

    /**
     * 新增档口认证
     */
    @PreAuthorize("@ss.hasPermi('system:certificate:add')")
    @Log(title = "档口认证", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreCertificate storeCertificate) {
        return toAjax(storeCertificateService.insertStoreCertificate(storeCertificate));
    }

    /**
     * 修改档口认证
     */
    @PreAuthorize("@ss.hasPermi('system:certificate:edit')")
    @Log(title = "档口认证", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreCertificate storeCertificate) {
        return toAjax(storeCertificateService.updateStoreCertificate(storeCertificate));
    }

    /**
     * 删除档口认证
     */
    @PreAuthorize("@ss.hasPermi('system:certificate:remove')")
    @Log(title = "档口认证", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeCertIds}")
    public AjaxResult remove(@PathVariable Long[] storeCertIds) {
        return toAjax(storeCertificateService.deleteStoreCertificateByStoreCertIds(storeCertIds));
    }
}
