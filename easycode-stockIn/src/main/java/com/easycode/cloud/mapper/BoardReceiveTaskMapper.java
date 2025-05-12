package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.dto.BoardReceiveTaskDto;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardReceiveTaskMapper {
    List<BoardReceiveTaskDto> selectBoardReceiveTaskList(BoardReceiveTaskDto boardReceiveTaskDto);

    /**
     * 查询看板领用任务详情
     * @param taskNo
     * @return
     */
    BoardReceiveTaskDto getBoardReceiveTask(@Param("taskNo") String taskNo);
}
