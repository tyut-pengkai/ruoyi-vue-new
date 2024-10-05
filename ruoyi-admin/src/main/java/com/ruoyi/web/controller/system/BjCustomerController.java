package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BjCustomer;
import com.ruoyi.system.service.IBjCustomerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 客户管理Controller
 * 
 * @author ssq
 * @date 2024-10-05
 */
@RestController
@RequestMapping("/system/customer")
public class BjCustomerController extends BaseController
{
    @Autowired
    private IBjCustomerService bjCustomerService;

    /**
     * 查询客户管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:customer:list')")
    @GetMapping("/list")
    public TableDataInfo list(BjCustomer bjCustomer)
    {
        startPage();
        List<BjCustomer> list = bjCustomerService.selectBjCustomerList(bjCustomer);
        return getDataTable(list);
    }

    /**
     * 导出客户管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:customer:export')")
    @Log(title = "客户管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BjCustomer bjCustomer)
    {
        List<BjCustomer> list = bjCustomerService.selectBjCustomerList(bjCustomer);
        ExcelUtil<BjCustomer> util = new ExcelUtil<BjCustomer>(BjCustomer.class);
        util.exportExcel(response, list, "客户管理数据");
    }

    /**
     * 获取客户管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:customer:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bjCustomerService.selectBjCustomerById(id));
    }

    /**
     * 新增客户管理
     */
    @PreAuthorize("@ss.hasPermi('system:customer:add')")
    @Log(title = "客户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BjCustomer bjCustomer)
    {
        return toAjax(bjCustomerService.insertBjCustomer(bjCustomer));
    }

    /**
     * 修改客户管理
     */
    @PreAuthorize("@ss.hasPermi('system:customer:edit')")
    @Log(title = "客户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BjCustomer bjCustomer)
    {
        return toAjax(bjCustomerService.updateBjCustomer(bjCustomer));
    }

    /**
     * 删除客户管理
     */
    @PreAuthorize("@ss.hasPermi('system:customer:remove')")
    @Log(title = "客户管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bjCustomerService.deleteBjCustomerByIds(ids));
    }
}
