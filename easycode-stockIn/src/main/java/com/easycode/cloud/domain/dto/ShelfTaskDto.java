package com.easycode.cloud.domain.dto;

import com.easycode.cloud.domain.ShelfTask;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author bcp
 */
@Alias("ShelfTaskDto")
public class ShelfTaskDto extends ShelfTask {

    private String closeTask;

    /**
     * 任务状态
     */
    private String[] taskStatusArr;

    /**
     * 确认仓位代码
     */
    private String confirmPosition;

    /**
     * 上架数量
     */
    private BigDecimal shelvesQty;

    /**
     * 换算
     */
    private BigDecimal conversDefault;

    /**
     * 上架任务列表
     */
    private List<Long> ids;

    public String getCloseTask() {
        return closeTask;
    }

    public void setCloseTask(String closeTask) {
        this.closeTask = closeTask;
    }

    @Override
    public String[] getTaskStatusArr() {
        return taskStatusArr;
    }

    @Override
    public void setTaskStatusArr(String[] taskStatusArr) {
        this.taskStatusArr = taskStatusArr;
    }

    public String getConfirmPosition() {
        return confirmPosition;
    }

    public void setConfirmPosition(String confirmPosition) {
        this.confirmPosition = confirmPosition;
    }

    public BigDecimal getShelvesQty() {
        return shelvesQty;
    }

    public void setShelvesQty(BigDecimal shelvesQty) {
        this.shelvesQty = shelvesQty;
    }

    public BigDecimal getConversDefault() {
        return conversDefault;
    }

    public void setConversDefault(BigDecimal conversDefault) {
        this.conversDefault = conversDefault;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
