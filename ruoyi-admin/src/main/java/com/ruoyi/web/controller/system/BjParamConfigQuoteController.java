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
import com.ruoyi.system.domain.BjParamConfigQuote;
import com.ruoyi.system.service.IBjParamConfigQuoteService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 报价参数Controller
 * 
 * @author ssq
 * @date 2024-10-05
 */
@RestController
@RequestMapping("/system/quoteparam")
public class BjParamConfigQuoteController extends BaseController
{
    @Autowired
    private IBjParamConfigQuoteService bjParamConfigQuoteService;

    /**
     * 查询报价参数列表
     */
    @PreAuthorize("@ss.hasPermi('system:quoteparam:list')")
    @GetMapping("/list")
    public TableDataInfo list(BjParamConfigQuote bjParamConfigQuote)
    {
        startPage();
        List<BjParamConfigQuote> list = bjParamConfigQuoteService.selectBjParamConfigQuoteList(bjParamConfigQuote);
        return getDataTable(list);
    }

    /**
     * 导出报价参数列表
     */
    @PreAuthorize("@ss.hasPermi('system:quoteparam:export')")
    @Log(title = "报价参数", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BjParamConfigQuote bjParamConfigQuote)
    {
        List<BjParamConfigQuote> list = bjParamConfigQuoteService.selectBjParamConfigQuoteList(bjParamConfigQuote);
        ExcelUtil<BjParamConfigQuote> util = new ExcelUtil<BjParamConfigQuote>(BjParamConfigQuote.class);
        util.exportExcel(response, list, "报价参数数据");
    }

    /**
     * 获取报价参数详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:quoteparam:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bjParamConfigQuoteService.selectBjParamConfigQuoteById(id));
    }

    /**
     * 新增报价参数
     */
    @PreAuthorize("@ss.hasPermi('system:quoteparam:add')")
    @Log(title = "报价参数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BjParamConfigQuote bjParamConfigQuote)
    {
        return toAjax(bjParamConfigQuoteService.insertBjParamConfigQuote(bjParamConfigQuote));
    }

    /**
     * 修改报价参数
     */
    @PreAuthorize("@ss.hasPermi('system:quoteparam:edit')")
    @Log(title = "报价参数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BjParamConfigQuote bjParamConfigQuote)
    {
        return toAjax(bjParamConfigQuoteService.updateBjParamConfigQuote(bjParamConfigQuote));
    }

    /**
     * 删除报价参数
     */
    @PreAuthorize("@ss.hasPermi('system:quoteparam:remove')")
    @Log(title = "报价参数", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bjParamConfigQuoteService.deleteBjParamConfigQuoteByIds(ids));
    }
}
