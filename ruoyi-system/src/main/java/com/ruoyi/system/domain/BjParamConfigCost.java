package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 成本参数对象 bj_param_config_cost
 * 
 * @author ssq
 * @date 2024-10-05
 */
public class BjParamConfigCost extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 关联客户id */
    @Excel(name = "关联客户id")
    private Long customerId;

    /** 毛胚重量参数 */
    @Excel(name = "毛胚重量参数")
    private Long blackWight;

    /** 废料重参数 */
    @Excel(name = "废料重参数")
    private Long scrapWight;

    /** 数割下料费 */
    @Excel(name = "数割下料费")
    private Long cutMaterial;

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

    /** 酸洗 */
    @Excel(name = "酸洗")
    private Long sprayPick;

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

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCustomerId(Long customerId) 
    {
        this.customerId = customerId;
    }

    public Long getCustomerId() 
    {
        return customerId;
    }
    public void setBlackWight(Long blackWight) 
    {
        this.blackWight = blackWight;
    }

    public Long getBlackWight() 
    {
        return blackWight;
    }
    public void setScrapWight(Long scrapWight) 
    {
        this.scrapWight = scrapWight;
    }

    public Long getScrapWight() 
    {
        return scrapWight;
    }
    public void setCutMaterial(Long cutMaterial) 
    {
        this.cutMaterial = cutMaterial;
    }

    public Long getCutMaterial() 
    {
        return cutMaterial;
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
    public void setSprayPick(Long sprayPick) 
    {
        this.sprayPick = sprayPick;
    }

    public Long getSprayPick() 
    {
        return sprayPick;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("customerId", getCustomerId())
            .append("blackWight", getBlackWight())
            .append("scrapWight", getScrapWight())
            .append("cutMaterial", getCutMaterial())
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
            .append("surfaceGalvanized", getSurfaceGalvanized())
            .append("surfaceCondit", getSurfaceCondit())
            .append("surfaceSandwash", getSurfaceSandwash())
            .append("surfacePqp", getSurfacePqp())
            .append("sprayPick", getSprayPick())
            .append("sprayPlastic", getSprayPlastic())
            .append("sprayElectro", getSprayElectro())
            .append("sprayPrimer", getSprayPrimer())
            .append("sprayTopcoat", getSprayTopcoat())
            .toString();
    }
}
