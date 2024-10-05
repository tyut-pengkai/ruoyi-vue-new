package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * cost对象 bj_detail_cost
 * 
 * @author ssq
 * @date 2024-10-04
 */
public class BjDetailCost extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 成本单id */
    private Long id;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private Long materialsNo;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String name;

    /** 数量 */
    @Excel(name = "数量")
    private Long num;

    /** 是否外来 */
    @Excel(name = "是否外来")
    private String flag;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal cost;

    /** 材料规格 */
    @Excel(name = "材料规格")
    private String materialSpec;

    /** 单件重量 */
    @Excel(name = "单件重量")
    private Long perWight;

    /** 净重(数量*单件重量) */
    @Excel(name = "净重(数量*单件重量)")
    private Long netWight;

    /** 长 */
    @Excel(name = "长")
    private Long steelLen;

    /** 宽 */
    @Excel(name = "宽")
    private Long steelWid;

    /** 高 */
    @Excel(name = "高")
    private Long steelHei;

    /** 毛坯重量 */
    @Excel(name = "毛坯重量")
    private Long steelWight;

    /** 材料单价 */
    @Excel(name = "材料单价")
    private BigDecimal steelPerPrice;

    /** 毛坯费 */
    @Excel(name = "毛坯费")
    private BigDecimal steelPrice;

    /** 废料重 */
    @Excel(name = "废料重")
    private Long steelScrapWgt;

    /** 废料单价 */
    @Excel(name = "废料单价")
    private Long steelScrapPer;

    /** 废料费 */
    @Excel(name = "废料费")
    private BigDecimal steelScrapPrice;

    /** 原料材料费用小计 */
    @Excel(name = "原料材料费用小计")
    private Long totalSteel;

    /** 数割 */
    @Excel(name = "数割")
    private Long cutNum;

    /** 数割下料费 */
    @Excel(name = "数割下料费")
    private Long cutMaterial;

    /** 数割费用小计 */
    @Excel(name = "数割费用小计")
    private String totalCut;

    /** 锯 */
    @Excel(name = "锯")
    private Long processSaw;

    /** 弯 */
    @Excel(name = "弯")
    private Long processBend;

    /** 钻 */
    @Excel(name = "钻")
    private Long processDrill;

    /** 车 */
    @Excel(name = "车")
    private Long processLathe;

    /** 外磨 */
    @Excel(name = "外磨")
    private Long processGrind;

    /** 铣 */
    @Excel(name = "铣")
    private Long processMill;

    /** 校平 */
    @Excel(name = "校平")
    private Long processLevel;

    /** 镗铣 */
    @Excel(name = "镗铣")
    private Long processBor;

    /** 焊 */
    @Excel(name = "焊")
    private Long processWeld;

    /** 打磨 */
    @Excel(name = "打磨")
    private Long processPolish;

    /** 装 */
    @Excel(name = "装")
    private Long processPack;

    /** 锯 */
    @Excel(name = "锯")
    private Long processSawTime;

    /** 弯 */
    @Excel(name = "弯")
    private Long processBendTime;

    /** 钻 */
    @Excel(name = "钻")
    private Long processDrillTime;

    /** 车 */
    @Excel(name = "车")
    private Long processLatheTime;

    /** 外磨 */
    @Excel(name = "外磨")
    private Long processGrindTime;

    /** 铣 */
    @Excel(name = "铣")
    private Long processMillTime;

    /** 校平 */
    @Excel(name = "校平")
    private Long processLevelTime;

    /** 镗铣 */
    @Excel(name = "镗铣")
    private Long processBorTime;

    /** 焊 */
    @Excel(name = "焊")
    private Long processWeldTime;

    /** 打磨 */
    @Excel(name = "打磨")
    private Long processPolishTime;

    /** 装 */
    @Excel(name = "装")
    private Long processPackTime;

    /** 加工费小计 */
    @Excel(name = "加工费小计")
    private String totalProcess;

    /** 镀锌 */
    @Excel(name = "镀锌")
    private Long surfaceGalvanized;

    /** 调质 */
    @Excel(name = "调质")
    private Long surfaceCondit;

    /** 冲砂 */
    @Excel(name = "冲砂")
    private Long surfaceSandwash;

    /** QPQ */
    @Excel(name = "QPQ")
    private Long surfacePqp;

    /** 镀锌 */
    @Excel(name = "镀锌")
    private Long surfaceGalvanizedWgt;

    /** 调质 */
    @Excel(name = "调质")
    private Long surfaceConditWgt;

    /** 冲砂 */
    @Excel(name = "冲砂")
    private Long surfaceSandwashWgt;

    /** QPQ */
    @Excel(name = "QPQ")
    private Long surfacePqpWgt;

    /** 表面处理费小计 */
    @Excel(name = "表面处理费小计")
    private String totalSurface;

    /** 酸洗 */
    @Excel(name = "酸洗")
    private Long sprayWashpickling;

    /** 喷塑 */
    @Excel(name = "喷塑")
    private Long sprayPlastic;

    /** 电泳 */
    @Excel(name = "电泳")
    private Long sprayElectro;

    /** 底漆 */
    @Excel(name = "底漆")
    private Long sprayPrimer;

    /** 面漆 */
    @Excel(name = "面漆")
    private Long sprayTopcoat;

    /** 酸洗 */
    @Excel(name = "酸洗")
    private Long sprayPickSquare;

    /** 喷塑 */
    @Excel(name = "喷塑")
    private Long sprayPlasticSquare;

    /** 电泳 */
    @Excel(name = "电泳")
    private Long sprayElectroSquare;

    /** 底漆 */
    @Excel(name = "底漆")
    private Long sprayPrimerSquare;

    /** 面漆 */
    @Excel(name = "面漆")
    private Long sprayTopcoatSquare;

    /** 喷涂费用小计 */
    @Excel(name = "喷涂费用小计")
    private String totalSpray;

    /** 裸价(元) */
    @Excel(name = "裸价(元)")
    private BigDecimal nakedPrice;

    /** 利润 */
    @Excel(name = "利润")
    private BigDecimal profit;

    /** 包装运输 */
    @Excel(name = "包装运输")
    private BigDecimal transCost;

    /** 产品合计报价 */
    @Excel(name = "产品合计报价")
    private BigDecimal totalPrice;

    /** 未税 */
    @Excel(name = "未税")
    private BigDecimal noTax;

    /** 公斤价 */
    @Excel(name = "公斤价")
    private BigDecimal perPrice;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMaterialsNo(Long materialsNo) 
    {
        this.materialsNo = materialsNo;
    }

    public Long getMaterialsNo() 
    {
        return materialsNo;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setNum(Long num) 
    {
        this.num = num;
    }

    public Long getNum() 
    {
        return num;
    }
    public void setFlag(String flag) 
    {
        this.flag = flag;
    }

    public String getFlag() 
    {
        return flag;
    }
    public void setCost(BigDecimal cost) 
    {
        this.cost = cost;
    }

    public BigDecimal getCost() 
    {
        return cost;
    }
    public void setMaterialSpec(String materialSpec) 
    {
        this.materialSpec = materialSpec;
    }

    public String getMaterialSpec() 
    {
        return materialSpec;
    }
    public void setPerWight(Long perWight) 
    {
        this.perWight = perWight;
    }

    public Long getPerWight() 
    {
        return perWight;
    }
    public void setNetWight(Long netWight) 
    {
        this.netWight = netWight;
    }

    public Long getNetWight() 
    {
        return netWight;
    }
    public void setSteelLen(Long steelLen) 
    {
        this.steelLen = steelLen;
    }

    public Long getSteelLen() 
    {
        return steelLen;
    }
    public void setSteelWid(Long steelWid) 
    {
        this.steelWid = steelWid;
    }

    public Long getSteelWid() 
    {
        return steelWid;
    }
    public void setSteelHei(Long steelHei) 
    {
        this.steelHei = steelHei;
    }

    public Long getSteelHei() 
    {
        return steelHei;
    }
    public void setSteelWight(Long steelWight) 
    {
        this.steelWight = steelWight;
    }

    public Long getSteelWight() 
    {
        return steelWight;
    }
    public void setSteelPerPrice(BigDecimal steelPerPrice) 
    {
        this.steelPerPrice = steelPerPrice;
    }

    public BigDecimal getSteelPerPrice() 
    {
        return steelPerPrice;
    }
    public void setSteelPrice(BigDecimal steelPrice) 
    {
        this.steelPrice = steelPrice;
    }

    public BigDecimal getSteelPrice() 
    {
        return steelPrice;
    }
    public void setSteelScrapWgt(Long steelScrapWgt) 
    {
        this.steelScrapWgt = steelScrapWgt;
    }

    public Long getSteelScrapWgt() 
    {
        return steelScrapWgt;
    }
    public void setSteelScrapPer(Long steelScrapPer) 
    {
        this.steelScrapPer = steelScrapPer;
    }

    public Long getSteelScrapPer() 
    {
        return steelScrapPer;
    }
    public void setSteelScrapPrice(BigDecimal steelScrapPrice) 
    {
        this.steelScrapPrice = steelScrapPrice;
    }

    public BigDecimal getSteelScrapPrice() 
    {
        return steelScrapPrice;
    }
    public void setTotalSteel(Long totalSteel) 
    {
        this.totalSteel = totalSteel;
    }

    public Long getTotalSteel() 
    {
        return totalSteel;
    }
    public void setCutNum(Long cutNum) 
    {
        this.cutNum = cutNum;
    }

    public Long getCutNum() 
    {
        return cutNum;
    }
    public void setCutMaterial(Long cutMaterial) 
    {
        this.cutMaterial = cutMaterial;
    }

    public Long getCutMaterial() 
    {
        return cutMaterial;
    }
    public void setTotalCut(String totalCut) 
    {
        this.totalCut = totalCut;
    }

    public String getTotalCut() 
    {
        return totalCut;
    }
    public void setProcessSaw(Long processSaw) 
    {
        this.processSaw = processSaw;
    }

    public Long getProcessSaw() 
    {
        return processSaw;
    }
    public void setProcessBend(Long processBend) 
    {
        this.processBend = processBend;
    }

    public Long getProcessBend() 
    {
        return processBend;
    }
    public void setProcessDrill(Long processDrill) 
    {
        this.processDrill = processDrill;
    }

    public Long getProcessDrill() 
    {
        return processDrill;
    }
    public void setProcessLathe(Long processLathe) 
    {
        this.processLathe = processLathe;
    }

    public Long getProcessLathe() 
    {
        return processLathe;
    }
    public void setProcessGrind(Long processGrind) 
    {
        this.processGrind = processGrind;
    }

    public Long getProcessGrind() 
    {
        return processGrind;
    }
    public void setProcessMill(Long processMill) 
    {
        this.processMill = processMill;
    }

    public Long getProcessMill() 
    {
        return processMill;
    }
    public void setProcessLevel(Long processLevel) 
    {
        this.processLevel = processLevel;
    }

    public Long getProcessLevel() 
    {
        return processLevel;
    }
    public void setProcessBor(Long processBor) 
    {
        this.processBor = processBor;
    }

    public Long getProcessBor() 
    {
        return processBor;
    }
    public void setProcessWeld(Long processWeld) 
    {
        this.processWeld = processWeld;
    }

    public Long getProcessWeld() 
    {
        return processWeld;
    }
    public void setProcessPolish(Long processPolish) 
    {
        this.processPolish = processPolish;
    }

    public Long getProcessPolish() 
    {
        return processPolish;
    }
    public void setProcessPack(Long processPack) 
    {
        this.processPack = processPack;
    }

    public Long getProcessPack() 
    {
        return processPack;
    }
    public void setProcessSawTime(Long processSawTime) 
    {
        this.processSawTime = processSawTime;
    }

    public Long getProcessSawTime() 
    {
        return processSawTime;
    }
    public void setProcessBendTime(Long processBendTime) 
    {
        this.processBendTime = processBendTime;
    }

    public Long getProcessBendTime() 
    {
        return processBendTime;
    }
    public void setProcessDrillTime(Long processDrillTime) 
    {
        this.processDrillTime = processDrillTime;
    }

    public Long getProcessDrillTime() 
    {
        return processDrillTime;
    }
    public void setProcessLatheTime(Long processLatheTime) 
    {
        this.processLatheTime = processLatheTime;
    }

    public Long getProcessLatheTime() 
    {
        return processLatheTime;
    }
    public void setProcessGrindTime(Long processGrindTime) 
    {
        this.processGrindTime = processGrindTime;
    }

    public Long getProcessGrindTime() 
    {
        return processGrindTime;
    }
    public void setProcessMillTime(Long processMillTime) 
    {
        this.processMillTime = processMillTime;
    }

    public Long getProcessMillTime() 
    {
        return processMillTime;
    }
    public void setProcessLevelTime(Long processLevelTime) 
    {
        this.processLevelTime = processLevelTime;
    }

    public Long getProcessLevelTime() 
    {
        return processLevelTime;
    }
    public void setProcessBorTime(Long processBorTime) 
    {
        this.processBorTime = processBorTime;
    }

    public Long getProcessBorTime() 
    {
        return processBorTime;
    }
    public void setProcessWeldTime(Long processWeldTime) 
    {
        this.processWeldTime = processWeldTime;
    }

    public Long getProcessWeldTime() 
    {
        return processWeldTime;
    }
    public void setProcessPolishTime(Long processPolishTime) 
    {
        this.processPolishTime = processPolishTime;
    }

    public Long getProcessPolishTime() 
    {
        return processPolishTime;
    }
    public void setProcessPackTime(Long processPackTime) 
    {
        this.processPackTime = processPackTime;
    }

    public Long getProcessPackTime() 
    {
        return processPackTime;
    }
    public void setTotalProcess(String totalProcess) 
    {
        this.totalProcess = totalProcess;
    }

    public String getTotalProcess() 
    {
        return totalProcess;
    }
    public void setSurfaceGalvanized(Long surfaceGalvanized) 
    {
        this.surfaceGalvanized = surfaceGalvanized;
    }

    public Long getSurfaceGalvanized() 
    {
        return surfaceGalvanized;
    }
    public void setSurfaceCondit(Long surfaceCondit) 
    {
        this.surfaceCondit = surfaceCondit;
    }

    public Long getSurfaceCondit() 
    {
        return surfaceCondit;
    }
    public void setSurfaceSandwash(Long surfaceSandwash) 
    {
        this.surfaceSandwash = surfaceSandwash;
    }

    public Long getSurfaceSandwash() 
    {
        return surfaceSandwash;
    }
    public void setSurfacePqp(Long surfacePqp) 
    {
        this.surfacePqp = surfacePqp;
    }

    public Long getSurfacePqp() 
    {
        return surfacePqp;
    }
    public void setSurfaceGalvanizedWgt(Long surfaceGalvanizedWgt) 
    {
        this.surfaceGalvanizedWgt = surfaceGalvanizedWgt;
    }

    public Long getSurfaceGalvanizedWgt() 
    {
        return surfaceGalvanizedWgt;
    }
    public void setSurfaceConditWgt(Long surfaceConditWgt) 
    {
        this.surfaceConditWgt = surfaceConditWgt;
    }

    public Long getSurfaceConditWgt() 
    {
        return surfaceConditWgt;
    }
    public void setSurfaceSandwashWgt(Long surfaceSandwashWgt) 
    {
        this.surfaceSandwashWgt = surfaceSandwashWgt;
    }

    public Long getSurfaceSandwashWgt() 
    {
        return surfaceSandwashWgt;
    }
    public void setSurfacePqpWgt(Long surfacePqpWgt) 
    {
        this.surfacePqpWgt = surfacePqpWgt;
    }

    public Long getSurfacePqpWgt() 
    {
        return surfacePqpWgt;
    }
    public void setTotalSurface(String totalSurface) 
    {
        this.totalSurface = totalSurface;
    }

    public String getTotalSurface() 
    {
        return totalSurface;
    }
    public void setSprayWashpickling(Long sprayWashpickling)
    {
        this.sprayWashpickling = sprayWashpickling;
    }

    public Long getSprayWashpickling()
    {
        return sprayWashpickling;
    }
    public void setSprayPlastic(Long sprayPlastic) 
    {
        this.sprayPlastic = sprayPlastic;
    }

    public Long getSprayPlastic() 
    {
        return sprayPlastic;
    }
    public void setSprayElectro(Long sprayElectro) 
    {
        this.sprayElectro = sprayElectro;
    }

    public Long getSprayElectro() 
    {
        return sprayElectro;
    }
    public void setSprayPrimer(Long sprayPrimer) 
    {
        this.sprayPrimer = sprayPrimer;
    }

    public Long getSprayPrimer() 
    {
        return sprayPrimer;
    }
    public void setSprayTopcoat(Long sprayTopcoat) 
    {
        this.sprayTopcoat = sprayTopcoat;
    }

    public Long getSprayTopcoat() 
    {
        return sprayTopcoat;
    }
    public void setSprayPickSquare(Long sprayPickSquare) 
    {
        this.sprayPickSquare = sprayPickSquare;
    }

    public Long getSprayPickSquare() 
    {
        return sprayPickSquare;
    }
    public void setSprayPlasticSquare(Long sprayPlasticSquare) 
    {
        this.sprayPlasticSquare = sprayPlasticSquare;
    }

    public Long getSprayPlasticSquare() 
    {
        return sprayPlasticSquare;
    }
    public void setSprayElectroSquare(Long sprayElectroSquare) 
    {
        this.sprayElectroSquare = sprayElectroSquare;
    }

    public Long getSprayElectroSquare() 
    {
        return sprayElectroSquare;
    }
    public void setSprayPrimerSquare(Long sprayPrimerSquare) 
    {
        this.sprayPrimerSquare = sprayPrimerSquare;
    }

    public Long getSprayPrimerSquare() 
    {
        return sprayPrimerSquare;
    }
    public void setSprayTopcoatSquare(Long sprayTopcoatSquare) 
    {
        this.sprayTopcoatSquare = sprayTopcoatSquare;
    }

    public Long getSprayTopcoatSquare() 
    {
        return sprayTopcoatSquare;
    }
    public void setTotalSpray(String totalSpray) 
    {
        this.totalSpray = totalSpray;
    }

    public String getTotalSpray() 
    {
        return totalSpray;
    }
    public void setNakedPrice(BigDecimal nakedPrice) 
    {
        this.nakedPrice = nakedPrice;
    }

    public BigDecimal getNakedPrice() 
    {
        return nakedPrice;
    }
    public void setProfit(BigDecimal profit) 
    {
        this.profit = profit;
    }

    public BigDecimal getProfit() 
    {
        return profit;
    }
    public void setTransCost(BigDecimal transCost) 
    {
        this.transCost = transCost;
    }

    public BigDecimal getTransCost() 
    {
        return transCost;
    }
    public void setTotalPrice(BigDecimal totalPrice) 
    {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() 
    {
        return totalPrice;
    }
    public void setNoTax(BigDecimal noTax) 
    {
        this.noTax = noTax;
    }

    public BigDecimal getNoTax() 
    {
        return noTax;
    }
    public void setPerPrice(BigDecimal perPrice) 
    {
        this.perPrice = perPrice;
    }

    public BigDecimal getPerPrice() 
    {
        return perPrice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("materialsNo", getMaterialsNo())
            .append("name", getName())
            .append("num", getNum())
            .append("flag", getFlag())
            .append("cost", getCost())
            .append("materialSpec", getMaterialSpec())
            .append("perWight", getPerWight())
            .append("netWight", getNetWight())
            .append("steelLen", getSteelLen())
            .append("steelWid", getSteelWid())
            .append("steelHei", getSteelHei())
            .append("steelWight", getSteelWight())
            .append("steelPerPrice", getSteelPerPrice())
            .append("steelPrice", getSteelPrice())
            .append("steelScrapWgt", getSteelScrapWgt())
            .append("steelScrapPer", getSteelScrapPer())
            .append("steelScrapPrice", getSteelScrapPrice())
            .append("totalSteel", getTotalSteel())
            .append("cutNum", getCutNum())
            .append("cutMaterial", getCutMaterial())
            .append("totalCut", getTotalCut())
            .append("processSaw", getProcessSaw())
            .append("processBend", getProcessBend())
            .append("processDrill", getProcessDrill())
            .append("processLathe", getProcessLathe())
            .append("processGrind", getProcessGrind())
            .append("processMill", getProcessMill())
            .append("processLevel", getProcessLevel())
            .append("processBor", getProcessBor())
            .append("processWeld", getProcessWeld())
            .append("processPolish", getProcessPolish())
            .append("processPack", getProcessPack())
            .append("processSawTime", getProcessSawTime())
            .append("processBendTime", getProcessBendTime())
            .append("processDrillTime", getProcessDrillTime())
            .append("processLatheTime", getProcessLatheTime())
            .append("processGrindTime", getProcessGrindTime())
            .append("processMillTime", getProcessMillTime())
            .append("processLevelTime", getProcessLevelTime())
            .append("processBorTime", getProcessBorTime())
            .append("processWeldTime", getProcessWeldTime())
            .append("processPolishTime", getProcessPolishTime())
            .append("processPackTime", getProcessPackTime())
            .append("totalProcess", getTotalProcess())
            .append("surfaceGalvanized", getSurfaceGalvanized())
            .append("surfaceCondit", getSurfaceCondit())
            .append("surfaceSandwash", getSurfaceSandwash())
            .append("surfacePqp", getSurfacePqp())
            .append("surfaceGalvanizedWgt", getSurfaceGalvanizedWgt())
            .append("surfaceConditWgt", getSurfaceConditWgt())
            .append("surfaceSandwashWgt", getSurfaceSandwashWgt())
            .append("surfacePqpWgt", getSurfacePqpWgt())
            .append("totalSurface", getTotalSurface())
            .append("sprayWashpickling", getSprayWashpickling())
            .append("sprayPlastic", getSprayPlastic())
            .append("sprayElectro", getSprayElectro())
            .append("sprayPrimer", getSprayPrimer())
            .append("sprayTopcoat", getSprayTopcoat())
            .append("sprayPickSquare", getSprayPickSquare())
            .append("sprayPlasticSquare", getSprayPlasticSquare())
            .append("sprayElectroSquare", getSprayElectroSquare())
            .append("sprayPrimerSquare", getSprayPrimerSquare())
            .append("sprayTopcoatSquare", getSprayTopcoatSquare())
            .append("totalSpray", getTotalSpray())
            .append("nakedPrice", getNakedPrice())
            .append("profit", getProfit())
            .append("transCost", getTransCost())
            .append("totalPrice", getTotalPrice())
            .append("noTax", getNoTax())
            .append("perPrice", getPerPrice())
            .toString();
    }
}
