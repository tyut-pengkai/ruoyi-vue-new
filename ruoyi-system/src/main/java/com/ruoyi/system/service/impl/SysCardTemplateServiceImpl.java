package com.ruoyi.system.service.impl;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.service.ISysAgentUserService;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.enums.GenRule;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.domain.vo.BatchReplaceVo;
import com.ruoyi.system.mapper.SysCardTemplateMapper;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysCardService;
import com.ruoyi.system.service.ISysCardTemplateService;
import nl.flotsam.xeger.Xeger;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.DateTimeException;
import java.time.Instant;
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
    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysAgentUserService sysAgentService;

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
        sysCardTemplate.setCreateBy(SecurityUtils.getUsernameNoException());
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
        sysCardTemplate.setUpdateBy(SecurityUtils.getUsernameNoException());
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
        String batchNo = DateUtils.dateTimeNow();
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
                try {
                    Instant ldt = Instant.now();
                    ldt = ldt.plusSeconds(cardTpl.getEffectiveDuration());
                    sysCard.setExpireTime(Date.from(ldt));
                }  catch (DateTimeException e) {
                    sysCard.setExpireTime(DateUtils.parseDate(UserConstants.MAX_DATE));
                }
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
            sysCard.setBatchNo(batchNo);
            try {
                sysCard.setCreateBy(SecurityUtils.getUsernameNoException());
                SysAgent agent = sysAgentService.selectSysAgentByUserId(SecurityUtils.getUserId());
                sysCard.setAgentId(agent.getAgentId());
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

    /**
     * 查询卡密模板
     *
     * @param appId        APP主键
     * @param templateName 卡类名称
     * @return 卡密模板
     */
    public SysCardTemplate selectSysCardTemplateByAppIdAndTemplateName(Long appId, String templateName) {
        return sysCardTemplateMapper.selectSysCardTemplateByAppIdAndTemplateName(appId, templateName);
    }

    /**
     * 批量换卡
     *
     * @param template
     * @param card
     * @param vo
     * @param batchNo
     * @return
     */
    @Override
    public SysCard genCardReplace(SysCardTemplate cardTpl, SysCard card, BatchReplaceVo vo, String batchNo) {
        SysCard newCard = new SysCard();
        BeanUtils.copyProperties(card, newCard);
        newCard.setCardId(null);
//        if( Objects.equals(UserConstants.YES, card.getIsCharged())) { // 换残卡
//            SysApp app = card.getApp();
//            SysAppUser appUser = null;
//            if(card.getChargeTo() != null) {
//                appUser = appUserService.selectSysAppUserByAppUserId(card.getChargeTo());
//            }
//            if(card.getChargeType() == ChargeType.CHARGE) {
//                throw new ServiceException("卡密已使用且已经以卡充卡到软件用户");
//            } else if(appUser == null) {
//                throw new ServiceException("v1.8.2及之前版本使用的卡密，无法关联到软件用户，不支持换卡");
//            }
//            if(app.getBillType() == BillType.TIME) {
//                long second = DateUtils.differentMillisecond(DateUtils.getNowDate(), appUser.getExpireTime()) / 1000;
//                if(second > 0) {
//                    if(!Objects.equals(UserConstants.YES, vo.getChangeMode())) {
//                        newCard.setQuota(second);
//                    }
//                } else {
//                    throw new ServiceException("单码已无剩余时间");
//                }
//            } else if(app.getBillType() == BillType.POINT) {
//                if(appUser.getPoint().compareTo(BigDecimal.ZERO) > 0) {
//                    if(!Objects.equals(UserConstants.YES, vo.getChangeMode())) {
//                        newCard.setQuota(appUser.getPoint().longValue());
//                    }
//                } else {
//                    throw new ServiceException("单码已无剩余点数");
//                }
//            } else {
//                throw new ServiceException("软件计费方式设置有误");
//            }
//        }
        newCard.setCardNo(genNo(cardTpl.getCardNoPrefix(), cardTpl.getCardNoSuffix(), cardTpl.getCardNoLen(), cardTpl.getCardNoGenRule(), cardTpl.getCardNoRegex()));
        newCard.setCardPass(genPass(cardTpl.getCardPassLen(), cardTpl.getCardPassGenRule(), cardTpl.getCardPassRegex()));
        newCard.setIsCharged(UserConstants.NO);
        newCard.setChargeTime(null);
        newCard.setChargeType(null);
        newCard.setChargeTo(null);
        newCard.setRemark("此卡用于替换旧卡：" + card.getCardNo()
                + (StringUtils.isNotBlank(vo.getRemark())? "\n换卡备注：" + vo.getRemark() : "")
                + (StringUtils.isNotBlank(card.getRemark())? "\n旧卡备注：" + card.getRemark() : ""));
        newCard.setBatchNo(batchNo);
        try {
            newCard.setCreateBy(SecurityUtils.getUsernameNoException());
        } catch (Exception ignore) {
        }
        if(sysCardService.insertSysCard(newCard)>0) {
            return newCard;
        }
        throw new ServiceException("保存新卡失败");
    }
}