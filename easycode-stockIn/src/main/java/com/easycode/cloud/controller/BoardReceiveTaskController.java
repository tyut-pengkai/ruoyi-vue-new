package com.easycode.cloud.controller;

import com.easycode.cloud.domain.dto.BoardReceiveTaskDto;
import com.easycode.cloud.service.IBoardReceiveTaskService;
import com.easycode.common.annotation.Log;

import com.easycode.common.core.controller.BaseController;
import com.easycode.common.core.domain.AjaxResult;
import com.easycode.common.core.page.TableDataInfo;
import com.easycode.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 看板领料任务管理
 * @author liushuo
 */
@RestController
@RequestMapping("/boardReceiveTask")
public class BoardReceiveTaskController extends BaseController {

    @Autowired
    private IBoardReceiveTaskService boardReceiveTaskService;

    /**
     * pda看板生产领用提交
     * @return
     */

    @Log(title = "看板领用提交")
    @PostMapping("pdaProdReceiveSubmit/{boardCode}")
    public AjaxResult pdaProdReceiveSubmit(@PathVariable String boardCode) throws Exception {
        boardReceiveTaskService.pdaProdReceiveSubmit(boardCode);
        return AjaxResult.success();
    }

    /**
     * pda看板生产取货提交
     * @return
     */
    @PostMapping("pdaProdPickUpSubmit/{taskNo}")
    public AjaxResult pdaProdPickUpSubmit(@PathVariable String taskNo) {
        boardReceiveTaskService.pdaProdPickUpSubmit(taskNo);
        return AjaxResult.success();
    }

    /**
     * pda看板看板物流确认
     * @return
     */
    @PostMapping("pdaBoardSubmit/{taskNo}")
    public AjaxResult pdaBoardSubmit(@PathVariable String taskNo) throws Exception {
        boardReceiveTaskService.pdaBoardSubmit(taskNo);
        return AjaxResult.success();
    }

    /**
     * 看板领用列表
     * @param boardReceiveTaskDto
     * @return
     */
    @GetMapping("/list")
    public AjaxResult list(BoardReceiveTaskDto boardReceiveTaskDto) {
        List<BoardReceiveTaskDto> list = boardReceiveTaskService.selectBoardReceiveTaskList(boardReceiveTaskDto);
        return AjaxResult.success(list);
    }


    /**
     * 看板领用列表
     * @param boardReceiveTaskDto
     * @return
     */
    @GetMapping("/allList")
    public TableDataInfo allList(BoardReceiveTaskDto boardReceiveTaskDto) {
        startPage();
        List<BoardReceiveTaskDto> list = boardReceiveTaskService.selectBoardReceiveTaskList(boardReceiveTaskDto);
        return getDataTable(list);
    }

    /**
     * 看板领用列表
     * @param boardReceiveTaskDto
     * @return
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, BoardReceiveTaskDto boardReceiveTaskDto) {
        List<BoardReceiveTaskDto> list = boardReceiveTaskService.selectBoardReceiveTaskList(boardReceiveTaskDto);
        ExcelUtil<BoardReceiveTaskDto> util = new ExcelUtil<BoardReceiveTaskDto>(BoardReceiveTaskDto.class);
        util.exportExcel(response, list, "看板水位翻包记录");
    }

    /**
     * 批量确认
     */
    @GetMapping(value = "/selectionChange/{ids}")
    public AjaxResult close(@PathVariable("ids") Long[] ids)
    {
        return toAjax(boardReceiveTaskService.selectionChangeIds(ids));
    }

    /**
     * pda翻包任务详情
     * @return
     */
    @Log(title = "翻包任务详情")
    @GetMapping("/{taskNo}")
    public AjaxResult getBoardReceiveTask(@PathVariable String taskNo) {
        BoardReceiveTaskDto boardReceiveTask = boardReceiveTaskService.getBoardReceiveTask(taskNo);
        return AjaxResult.success(boardReceiveTask);
    }

    /**
     * 打印任务
     * @return
     */
    @Log(title = "打印任务")
    @PostMapping("/printTask")
    public AjaxResult printTask(@RequestBody BoardReceiveTaskDto boardReceiveTaskDto) {
        return AjaxResult.success(boardReceiveTaskService.printTask(boardReceiveTaskDto));
    }
}
