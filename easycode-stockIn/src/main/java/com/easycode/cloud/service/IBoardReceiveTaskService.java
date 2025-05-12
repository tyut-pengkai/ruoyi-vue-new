package com.easycode.cloud.service;

import com.easycode.cloud.domain.dto.BoardReceiveTaskDto;

import java.util.List;

/**
 * 看板领料任务Service接口
 *
 * @author bcp
 * @date 2024-10-17
 */


public interface IBoardReceiveTaskService {


    /**
     * pda看板生产领用提交
     * @param boardCode
     */
    void pdaProdReceiveSubmit(String boardCode) throws Exception;

    /**
     * pda看板生产取货提交
     * @param taskNo
     */
    void pdaProdPickUpSubmit(String taskNo);

    /**
     * pda看板看板物流确认
     * @param taskNo
     */
    void pdaBoardSubmit(String taskNo) throws Exception;

    /**
     * 看板领用列表
     * @param boardReceiveTaskDto
     * @return
     */
    List<BoardReceiveTaskDto> selectBoardReceiveTaskList(BoardReceiveTaskDto boardReceiveTaskDto);

    BoardReceiveTaskDto getBoardReceiveTask(String taskNo);


    /**
     *  批量确认
     * @param ids
     * @return
     */

    int selectionChangeIds(Long[] ids);

    /**
     * 打印任务
     * @param boardReceiveTaskDto
     * @return
     */
    int printTask(BoardReceiveTaskDto boardReceiveTaskDto);
}
