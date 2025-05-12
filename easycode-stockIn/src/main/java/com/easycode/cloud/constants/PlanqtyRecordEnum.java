package com.easycode.cloud.constants;
/**
 * @author zjj
 * @description 采购订单 需求量计算的类型类
 * @date 2024/06/12
 */
public enum PlanqtyRecordEnum {

    //变更类型（1：sap需求量、11：sap变更时、2：sap收货数量、21：sap收货变更时、3：wms建单数量、31：修改建单数量变化值、32：单据作废或删除时数量变化值、4：wms收货数量、41：wms收货作废或者删除时）

    TYPE_SAP_CREATE_PLAN("1","sap创建需求数量"),
    TYPE_SAP_UPDATE_PLAN("11","sap更新需求数量"),
    TYPE_SAP_CREATE_RECEIVING("2","sap创建收货数量"),
    TYPE_SAP_UPDATE_RECEIVING("22","sap更新收获数量"),
    TYPE_WMS_CREATE_RECEIVING("3","wms创建收获数量"),
    TYPE_WMS_UPDATE_RECEIVING("4","wms更新收获数量"),
    TYPE_WMS_FINISH_RECEIVING("5","wms收货完成同步sap数量");

    String key;
    String value;

    PlanqtyRecordEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }
    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
