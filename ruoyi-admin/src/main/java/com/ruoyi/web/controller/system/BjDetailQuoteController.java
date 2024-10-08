package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.Anonymous;
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
import com.ruoyi.system.domain.BjDetailQuote;
import com.ruoyi.system.service.IBjDetailQuoteService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 详细报价Controller
 *
 * @author ssq
 * @date 2024-10-08
 */
@RestController
@RequestMapping("/system/quote")
public class BjDetailQuoteController extends BaseController
{
    @Autowired
    private IBjDetailQuoteService bjDetailQuoteService;

    /**
     * 查询详细报价列表
     */
    @PreAuthorize("@ss.hasPermi('system:quote:list')")
    @GetMapping("/list")
    public TableDataInfo list(BjDetailQuote bjDetailQuote)
    {
        startPage();
        List<BjDetailQuote> list = bjDetailQuoteService.selectBjDetailQuoteList(bjDetailQuote);
        return getDataTable(list);
    }

    /**
     * 导出详细报价列表
     */
    @PreAuthorize("@ss.hasPermi('system:quote:export')")
    @Log(title = "详细报价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BjDetailQuote bjDetailQuote)
    {
        List<BjDetailQuote> list = bjDetailQuoteService.selectBjDetailQuoteList(bjDetailQuote);
        ExcelUtil<BjDetailQuote> util = new ExcelUtil<BjDetailQuote>(BjDetailQuote.class);
        util.exportExcel(response, list, "详细报价数据");
    }

    /**
     * 获取详细报价详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:quote:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bjDetailQuoteService.selectBjDetailQuoteById(id));
    }

    /**
     * 新增详细报价
     */
//    @PreAuthorize("@ss.hasPermi('system:quote:add')")
    @Anonymous
    @Log(title = "详细报价", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BjDetailQuote bjDetailQuote)
    {
        return toAjax(bjDetailQuoteService.insertBjDetailQuote(bjDetailQuote));
    }

    /**
     * 修改详细报价
     */
    @PreAuthorize("@ss.hasPermi('system:quote:edit')")
    @Log(title = "详细报价", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BjDetailQuote bjDetailQuote)
    {
        return toAjax(bjDetailQuoteService.updateBjDetailQuote(bjDetailQuote));
    }

    /**
     * 删除详细报价
     */
    @PreAuthorize("@ss.hasPermi('system:quote:remove')")
    @Log(title = "详细报价", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bjDetailQuoteService.deleteBjDetailQuoteByIds(ids));
    }
}
