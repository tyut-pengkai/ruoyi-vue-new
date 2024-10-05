package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 人工成本对象 bj_worker_cost
 * 
 * @author ssq
 * @date 2024-10-05
 */
public class BjWorkerCost extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 人工成本单id */
    private Long id;

    /** 关联的报价单id */
    @Excel(name = "关联的报价单id")
    private Long quoteId;

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
    private Long processSawWorker;

    /** 弯 */
    @Excel(name = "弯")
    private Long processBendWorker;

    /** 钻 */
    @Excel(name = "钻")
    private Long processDrillWorker;

    /** 车 */
    @Excel(name = "车")
    private Long processLatheWorker;

    /** 外磨 */
    @Excel(name = "外磨")
    private Long processGrindWorker;

    /** 铣 */
    @Excel(name = "铣")
    private Long processMillWorker;

    /** 校平 */
    @Excel(name = "校平")
    private Long processLevelWorker;

    /** 镗铣 */
    @Excel(name = "镗铣")
    private Long processBorWorker;

    /** 焊 */
    @Excel(name = "焊")
    private Long processWeldWorker;

    /** 装 */
    @Excel(name = "装")
    private Long processPackWorker;

    /** 打磨 */
    @Excel(name = "打磨")
    private Long processPolishWorker;

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
    private Long surfaceGalvanizedWorker;

    /** 调质 */
    @Excel(name = "调质")
    private Long surfaceConditWorker;

    /** 冲砂 */
    @Excel(name = "冲砂")
    private Long surfaceSandwashWorker;

    /** QPQ */
    @Excel(name = "QPQ")
    private Long surfacePqpWorker;

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
    private Long sprayPickWorker;

    /** 喷塑 */
    @Excel(name = "喷塑")
    private Long sprayPlasticWorker;

    /** 电泳 */
    @Excel(name = "电泳")
    private Long sprayElectroWorker;

    /** 底漆 */
    @Excel(name = "底漆")
    private Long sprayPrimerWorker;

    /** 面漆 */
    @Excel(name = "面漆")
    private Long sprayTopcoatWorker;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setQuoteId(Long quoteId) 
    {
        this.quoteId = quoteId;
    }

    public Long getQuoteId() 
    {
        return quoteId;
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
    public void setProcessSawWorker(Long processSawWorker) 
    {
        this.processSawWorker = processSawWorker;
    }

    public Long getProcessSawWorker() 
    {
        return processSawWorker;
    }
    public void setProcessBendWorker(Long processBendWorker) 
    {
        this.processBendWorker = processBendWorker;
    }

    public Long getProcessBendWorker() 
    {
        return processBendWorker;
    }
    public void setProcessDrillWorker(Long processDrillWorker) 
    {
        this.processDrillWorker = processDrillWorker;
    }

    public Long getProcessDrillWorker() 
    {
        return processDrillWorker;
    }
    public void setProcessLatheWorker(Long processLatheWorker) 
    {
        this.processLatheWorker = processLatheWorker;
    }

    public Long getProcessLatheWorker() 
    {
        return processLatheWorker;
    }
    public void setProcessGrindWorker(Long processGrindWorker) 
    {
        this.processGrindWorker = processGrindWorker;
    }

    public Long getProcessGrindWorker() 
    {
        return processGrindWorker;
    }
    public void setProcessMillWorker(Long processMillWorker) 
    {
        this.processMillWorker = processMillWorker;
    }

    public Long getProcessMillWorker() 
    {
        return processMillWorker;
    }
    public void setProcessLevelWorker(Long processLevelWorker) 
    {
        this.processLevelWorker = processLevelWorker;
    }

    public Long getProcessLevelWorker() 
    {
        return processLevelWorker;
    }
    public void setProcessBorWorker(Long processBorWorker) 
    {
        this.processBorWorker = processBorWorker;
    }

    public Long getProcessBorWorker() 
    {
        return processBorWorker;
    }
    public void setProcessWeldWorker(Long processWeldWorker) 
    {
        this.processWeldWorker = processWeldWorker;
    }

    public Long getProcessWeldWorker() 
    {
        return processWeldWorker;
    }
    public void setProcessPackWorker(Long processPackWorker) 
    {
        this.processPackWorker = processPackWorker;
    }

    public Long getProcessPackWorker() 
    {
        return processPackWorker;
    }
    public void setProcessPolishWorker(Long processPolishWorker) 
    {
        this.processPolishWorker = processPolishWorker;
    }

    public Long getProcessPolishWorker() 
    {
        return processPolishWorker;
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
    public void setSurfaceGalvanizedWorker(Long surfaceGalvanizedWorker) 
    {
        this.surfaceGalvanizedWorker = surfaceGalvanizedWorker;
    }

    public Long getSurfaceGalvanizedWorker() 
    {
        return surfaceGalvanizedWorker;
    }
    public void setSurfaceConditWorker(Long surfaceConditWorker) 
    {
        this.surfaceConditWorker = surfaceConditWorker;
    }

    public Long getSurfaceConditWorker() 
    {
        return surfaceConditWorker;
    }
    public void setSurfaceSandwashWorker(Long surfaceSandwashWorker) 
    {
        this.surfaceSandwashWorker = surfaceSandwashWorker;
    }

    public Long getSurfaceSandwashWorker() 
    {
        return surfaceSandwashWorker;
    }
    public void setSurfacePqpWorker(Long surfacePqpWorker) 
    {
        this.surfacePqpWorker = surfacePqpWorker;
    }

    public Long getSurfacePqpWorker() 
    {
        return surfacePqpWorker;
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
    public void setSprayPickWorker(Long sprayPickWorker) 
    {
        this.sprayPickWorker = sprayPickWorker;
    }

    public Long getSprayPickWorker() 
    {
        return sprayPickWorker;
    }
    public void setSprayPlasticWorker(Long sprayPlasticWorker) 
    {
        this.sprayPlasticWorker = sprayPlasticWorker;
    }

    public Long getSprayPlasticWorker() 
    {
        return sprayPlasticWorker;
    }
    public void setSprayElectroWorker(Long sprayElectroWorker) 
    {
        this.sprayElectroWorker = sprayElectroWorker;
    }

    public Long getSprayElectroWorker() 
    {
        return sprayElectroWorker;
    }
    public void setSprayPrimerWorker(Long sprayPrimerWorker) 
    {
        this.sprayPrimerWorker = sprayPrimerWorker;
    }

    public Long getSprayPrimerWorker() 
    {
        return sprayPrimerWorker;
    }
    public void setSprayTopcoatWorker(Long sprayTopcoatWorker) 
    {
        this.sprayTopcoatWorker = sprayTopcoatWorker;
    }

    public Long getSprayTopcoatWorker() 
    {
        return sprayTopcoatWorker;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("quoteId", getQuoteId())
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
            .append("processSawWorker", getProcessSawWorker())
            .append("processBendWorker", getProcessBendWorker())
            .append("processDrillWorker", getProcessDrillWorker())
            .append("processLatheWorker", getProcessLatheWorker())
            .append("processGrindWorker", getProcessGrindWorker())
            .append("processMillWorker", getProcessMillWorker())
            .append("processLevelWorker", getProcessLevelWorker())
            .append("processBorWorker", getProcessBorWorker())
            .append("processWeldWorker", getProcessWeldWorker())
            .append("processPackWorker", getProcessPackWorker())
            .append("processPolishWorker", getProcessPolishWorker())
            .append("surfaceGalvanized", getSurfaceGalvanized())
            .append("surfaceCondit", getSurfaceCondit())
            .append("surfaceSandwash", getSurfaceSandwash())
            .append("surfacePqp", getSurfacePqp())
            .append("surfaceGalvanizedWorker", getSurfaceGalvanizedWorker())
            .append("surfaceConditWorker", getSurfaceConditWorker())
            .append("surfaceSandwashWorker", getSurfaceSandwashWorker())
            .append("surfacePqpWorker", getSurfacePqpWorker())
            .append("sprayWashpickling", getSprayWashpickling())
            .append("sprayPlastic", getSprayPlastic())
            .append("sprayElectro", getSprayElectro())
            .append("sprayPrimer", getSprayPrimer())
            .append("sprayTopcoat", getSprayTopcoat())
            .append("sprayPickWorker", getSprayPickWorker())
            .append("sprayPlasticWorker", getSprayPlasticWorker())
            .append("sprayElectroWorker", getSprayElectroWorker())
            .append("sprayPrimerWorker", getSprayPrimerWorker())
            .append("sprayTopcoatWorker", getSprayTopcoatWorker())
            .toString();
    }
}
