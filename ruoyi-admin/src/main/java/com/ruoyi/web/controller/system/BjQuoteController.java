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
import com.ruoyi.system.domain.BjQuote;
import com.ruoyi.system.service.IBjQuoteService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 对外报价Controller
 * 
 * @author ssq
 * @date 2024-10-05
 */
@RestController
@RequestMapping("/system/easyquote")
public class BjQuoteController extends BaseController
{
    @Autowired
    private IBjQuoteService bjQuoteService;

    /**
     * 查询对外报价列表
     */
    @PreAuthorize("@ss.hasPermi('system:easyquote:list')")
    @GetMapping("/list")
    public TableDataInfo list(BjQuote bjQuote)
    {
        startPage();
        List<BjQuote> list = bjQuoteService.selectBjQuoteList(bjQuote);
        return getDataTable(list);
    }

    /**
     * 导出对外报价列表
     */
    @PreAuthorize("@ss.hasPermi('system:easyquote:export')")
    @Log(title = "对外报价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BjQuote bjQuote)
    {
        List<BjQuote> list = bjQuoteService.selectBjQuoteList(bjQuote);
        ExcelUtil<BjQuote> util = new ExcelUtil<BjQuote>(BjQuote.class);
        util.exportExcel(response, list, "对外报价数据");
    }

    /**
     * 获取对外报价详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:easyquote:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bjQuoteService.selectBjQuoteById(id));
    }

    /**
     * 新增对外报价
     */
    @PreAuthorize("@ss.hasPermi('system:easyquote:add')")
    @Log(title = "对外报价", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BjQuote bjQuote)
    {
        return toAjax(bjQuoteService.insertBjQuote(bjQuote));
    }

    /**
     * 修改对外报价
     */
    @PreAuthorize("@ss.hasPermi('system:easyquote:edit')")
    @Log(title = "对外报价", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BjQuote bjQuote)
    {
        return toAjax(bjQuoteService.updateBjQuote(bjQuote));
    }

    /**
     * 删除对外报价
     */
    @PreAuthorize("@ss.hasPermi('system:easyquote:remove')")
    @Log(title = "对外报价", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bjQuoteService.deleteBjQuoteByIds(ids));
    }
}
