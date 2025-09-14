package com.ruoyi.web.controller.xkt.vo.storeProductStorageDemandDeduct;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("入库单获取入库单据抵扣需求明细")
@Data
@Builder
public class StoreProdStorageDemandDeductVO {

    @ApiModelProperty(name = "入库code")
    private String storageCode;
    @ApiModelProperty(name = "入库类型 1 PROD_STORAGE  其它入库 2 OTHER_STORAGE  维修入库 3 REPAIR_STORAGE")
    private Integer storageType;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(name = "入库总数量")
    private Integer quantity;
    @ApiModelProperty(value = "工厂名称")
    private String facName;
    @ApiModelProperty(name = "生产成本金额")
    private BigDecimal produceAmount;
    @ApiModelProperty(name = "抵扣明细列表")
    List<SPSDDDemandDetailVO> detailList;

    @Data
    @Accessors(chain = true)
    @ApiModel
    public static class SPSDDDemandDetailVO {
        @ApiModelProperty("档口入库需求抵扣ID")
        private Long spsddId;
        @ApiModelProperty(value = "货号")
        private String prodArtNum;
        @ApiModelProperty(value = "颜色")
        private String colorName;
        @ApiModelProperty(value = "需求code列表")
        List<String> demandCodeList;
        @ApiModelProperty(value = "数量对比列")
        private List<String> compareStrList;
        @ApiModelProperty(value = "尺码为30的数量")
        private List<String> size30List;
        @ApiModelProperty(value = "尺码为31的数量")
        private List<String> size31List;
        @ApiModelProperty(value = "尺码为32的数量")
        private List<String> size32List;
        @ApiModelProperty(value = "尺码为33的数量")
        private List<String> size33List;
        @ApiModelProperty(value = "尺码为34的数量")
        private List<String> size34List;
        @ApiModelProperty(value = "尺码为35的数量")
        private List<String> size35List;
        @ApiModelProperty(value = "尺码为36的数量")
        private List<String> size36List;
        @ApiModelProperty(value = "尺码为37的数量")
        private List<String> size37List;
        @ApiModelProperty(value = "尺码为38的数量")
        private List<String> size38List;
        @ApiModelProperty(value = "尺码为39的数量")
        private List<String> size39List;
        @ApiModelProperty(value = "尺码为40的数量")
        private List<String> size40List;
        @ApiModelProperty(value = "尺码为41的数量")
        private List<String> size41List;
        @ApiModelProperty(value = "尺码为42的数量")
        private List<String> size42List;
        @ApiModelProperty(value = "尺码为43的数量")
        private List<String> size43List;
    }

}
