package com.easycode.cloud.domain.dto;

import com.easycode.cloud.domain.InspectOrder;
import com.weifu.cloud.domian.vo.InventoryDetailsVo;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * 送检单dto
 * @author bcp
 */
@Alias("InspectOrderDto")
public class InspectOrderDto extends InspectOrder {
    /**
     * 处理单号
     */
    private String disposeOrderNo;

    /** 送检单主键集合 */
    private Long[] ids;
    /** 送检单号集合 */
    private List<String> orderNos;

    /**
     * 任务号
     */
    private String taskNo;

    /**
     * 仓位地点
     */
    private String positionNo;

    //移动类型
    private String moveType;

    //批次
    private String lot;

    private String userName;
    /**
     * 台账集合
     */
    private List<InventoryDetailsVo> inventoryDetails;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getDisposeOrderNo() {
        return disposeOrderNo;
    }

    public void setDisposeOrderNo(String disposeOrderNo) {
        this.disposeOrderNo = disposeOrderNo;
    }

    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }

    public List<String> getOrderNos() {
        return orderNos;
    }

    public void setOrderNos(List<String> orderNos) {
        this.orderNos = orderNos;
    }

    public List<InventoryDetailsVo> getInventoryDetails() {
        return inventoryDetails;
    }

    public void setInventoryDetails(List<InventoryDetailsVo> inventoryDetails) {
        this.inventoryDetails = inventoryDetails;
    }
}
