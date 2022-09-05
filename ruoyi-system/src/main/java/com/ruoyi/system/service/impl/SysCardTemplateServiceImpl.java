package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.enums.GenRule;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.mapper.SysCardTemplateMapper;
import com.ruoyi.system.service.ISysCardService;
import com.ruoyi.system.service.ISysCardTemplateService;
import nl.flotsam.xeger.Xeger;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 卡密模板Service业务层处理
 *
 * @author zwgu
 * @date 2021-11-28
 */
@Service
public class SysCardTemplateServiceImpl implements ISysCardTemplateService
{
    @Autowired
    private SysCardTemplateMapper sysCardTemplateMapper;
    @Autowired
    private ISysCardService sysCardService;

    /**
     * 查询卡密模板
     *
     * @param templateId 卡密模板主键
     * @return 卡密模板
     */
    @Override
    public SysCardTemplate selectSysCardTemplateByTemplateId(Long templateId)
    {
        return sysCardTemplateMapper.selectSysCardTemplateByTemplateId(templateId);
    }

    /**
     * 查询卡密模板列表
     *
     * @param sysCardTemplate 卡密模板
     * @return 卡密模板
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysCardTemplate> selectSysCardTemplateList(SysCardTemplate sysCardTemplate)
    {
        return sysCardTemplateMapper.selectSysCardTemplateList(sysCardTemplate);
    }

    /**
     * 新增卡密模板
     *
     * @param sysCardTemplate 卡密模板
     * @return 结果
     */
    @Override
    public int insertSysCardTemplate(SysCardTemplate sysCardTemplate)
    {
        sysCardTemplate.setCreateTime(DateUtils.getNowDate());
        return sysCardTemplateMapper.insertSysCardTemplate(sysCardTemplate);
    }

    /**
     * 修改卡密模板
     *
     * @param sysCardTemplate 卡密模板
     * @return 结果
     */
    @Override
    public int updateSysCardTemplate(SysCardTemplate sysCardTemplate)
    {
        sysCardTemplate.setUpdateTime(DateUtils.getNowDate());
        return sysCardTemplateMapper.updateSysCardTemplate(sysCardTemplate);
    }

    /**
     * 批量删除卡密模板
     *
     * @param templateIds 需要删除的卡密模板主键
     * @return 结果
     */
    @Override
    public int deleteSysCardTemplateByTemplateIds(Long[] templateIds)
    {
        return sysCardTemplateMapper.deleteSysCardTemplateByTemplateIds(templateIds);
    }

    /**
     * 删除卡密模板信息
     *
     * @param templateId 卡密模板主键
     * @return 结果
     */
    @Override
    public int deleteSysCardTemplateByTemplateId(Long templateId)
    {
        return sysCardTemplateMapper.deleteSysCardTemplateByTemplateId(templateId);
    }

    /**
     * 批量新增卡密
     *
     * @param cardTpl
     * @param quantity
     * @param remark
     * @return
     */
    @Override
    public List<SysCard> genSysCardBatch(SysCardTemplate cardTpl, Integer quantity, String onSale, String isAgent, String remark) {
        List<SysCard> sysCardList = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            SysCard sysCard = new SysCard();
            sysCard.setCardName(cardTpl.getCardName());
            sysCard.setCardNo(genNo(cardTpl.getCardNoPrefix(), cardTpl.getCardNoSuffix(), cardTpl.getCardNoLen(), cardTpl.getCardNoGenRule(), cardTpl.getCardNoRegex()));
            sysCard.setCardPass(genPass(cardTpl.getCardPassLen(), cardTpl.getCardPassGenRule(), cardTpl.getCardPassRegex()));
            sysCard.setApp(cardTpl.getApp());
            sysCard.setTemplateId(cardTpl.getTemplateId());
            sysCard.setAppId(cardTpl.getAppId());
            sysCard.setChargeRule(cardTpl.getChargeRule());
            if (cardTpl.getEffectiveDuration() == -1) {
                sysCard.setExpireTime(DateUtils.parseDate(UserConstants.MAX_DATE));
            } else {
//                sysCard.setExpireTime(DateUtils.addSeconds(new Date(), cardTpl.getEffectiveDuration().intValue()));
                LocalDateTime ldt = DateUtils.toLocalDateTime(new Date());
                ldt = ldt.plus(cardTpl.getEffectiveDuration(), ChronoUnit.SECONDS);
                sysCard.setExpireTime(DateUtils.toDate(ldt));
            }
            sysCard.setIsCharged(UserConstants.NO);
            sysCard.setIsSold(UserConstants.NO);
            sysCard.setOnSale(onSale);
            sysCard.setIsAgent(isAgent);
            sysCard.setPrice(cardTpl.getPrice());
            sysCard.setQuota(cardTpl.getQuota());
            sysCard.setStatus(UserConstants.NORMAL);
            sysCard.setRemark(remark);
            sysCard.setCardLoginLimitU(cardTpl.getCardLoginLimitU());
            sysCard.setCardLoginLimitM(cardTpl.getCardLoginLimitM());
            sysCard.setCardCustomParams(cardTpl.getCardCustomParams());
            try {
                sysCard.setCreateBy(SecurityUtils.getUsername());
            } catch (Exception ignored) {
            }
            sysCardList.add(sysCard);
        }
        sysCardService.insertSysCardBatch(sysCardList);
        return sysCardList;
    }

    private String generate(Integer length, GenRule genRule, String pattern) {
        if (length <= 0) {
            return "";
        }
        String random = "";
        if (genRule == GenRule.LOWERCASE) {
            random = RandomStringUtils.randomAlphabetic(length).toLowerCase();
        } else if (genRule == GenRule.NUM) {
            random = RandomStringUtils.randomNumeric(length);
        } else if (genRule == GenRule.NUM_LOWERCASE) {
            random = RandomStringUtils.randomAlphanumeric(length).toLowerCase();
        }else if(genRule == GenRule.NUM_UPPERCASE) {
            random = RandomStringUtils.randomAlphanumeric(length).toUpperCase();
        }else if(genRule == GenRule.NUM_UPPERCASE_LOWERCASE) {
            random = RandomStringUtils.randomAlphanumeric(length);
        }else if(genRule == GenRule.UPPERCASE) {
            random = RandomStringUtils.randomAlphabetic(length).toUpperCase();
        }else if(genRule == GenRule.UPPERCASE_LOWERCASE) {
            random = RandomStringUtils.randomAlphabetic(length);
        }else if(genRule == GenRule.REGEX) {
//            random = ("regex"+RandomStringUtils.randomAlphanumeric(length)).substring(0, length);
            Xeger generator = new Xeger(pattern);
            random = generator.generate();
        }
        return random;
    }

    private String genNo(String prefix, String suffix, Integer length, GenRule genRule, String pattern) {
        if (StringUtils.isBlank(prefix)) {
            prefix = "";
        }
        if (StringUtils.isBlank(suffix)) {
            suffix = "";
        }
        String random = prefix + generate(length, genRule, pattern) + suffix;
//        while (sysLoginCodeService.selectSysLoginCodeByCardNo(random) != null) {
//            random = prefix + generate(length, genRule) + suffix;
//        }
//      数据库添加了唯一索引保证不重复
        return random;
    }

    private String genPass(Integer length, GenRule genRule, String pattern) {
        return generate(length, genRule, pattern);
    }
}