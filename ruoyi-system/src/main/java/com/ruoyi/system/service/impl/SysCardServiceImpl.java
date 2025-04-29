package com.ruoyi.system.service.impl;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.service.ISysAgentUserService;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.SysCache;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.domain.vo.BatchNoVo;
import com.ruoyi.system.mapper.SysCardMapper;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysCardService;
import com.ruoyi.system.service.ISysCardTemplateService;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 卡密Service业务层处理
 *
 * @author zwgu
 * @date 2021-12-03
 */
@Service
public class SysCardServiceImpl implements ISysCardService {
    private static final Logger log = LoggerFactory.getLogger(SysCardServiceImpl.class);

    @Autowired
    private SysCardMapper sysCardMapper;
    @Autowired
    protected Validator validator;
    @Resource
    private ISysCardTemplateService sysCardTemplateService;
    @Resource
    private ISysAppService sysAppService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysAgentUserService sysAgentService;

    static Long DAY = 86400L; // 86400秒为一天

    /**
     * 查询卡密
     *
     * @param cardId 卡密主键
     * @return 卡密
     */
    @Override
    public SysCard selectSysCardByCardId(Long cardId) {
        return sysCardMapper.selectSysCardByCardId(cardId);
    }

    /**
     * 查询卡密
     *
     * @param cardIds 卡密主键
     * @return 卡密
     */
    @Override
    public List<SysCard> selectSysCardByCardIds(Long[] cardIds) {
        return sysCardMapper.selectSysCardByCardIds(cardIds);
    }

    /**
     * 查询卡密列表
     *
     * @param sysCard 卡密
     * @return 卡密
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysCard> selectSysCardList(SysCard sysCard) {
        return sysCardMapper.selectSysCardList(sysCard);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public int countSysCard(SysCard sysCard) {
        return sysCardMapper.countSysCard(sysCard);
    }

    /**
     * 查询卡密列表
     *
     * @return 卡密
     */
    @Override
    public List<String> selectSysCardNoList() {
        return sysCardMapper.selectSysCardNoList();
    }

    /**
     * 新增卡密
     *
     * @param sysCard 卡密
     * @return 结果
     */
    @Override
    public int insertSysCard(SysCard sysCard) {
        sysCard.setCreateTime(DateUtils.getNowDate());
        sysCard.setCreateBy(SecurityUtils.getUsernameNoException());
        if(sysCard.getQuota() < 0 || sysCard.getQuota() > 1000 * 365 * DAY) {
            throw new ServiceException("面值设置错误，面值需在0-1000年之间");
        }
        SysCard card = selectSysCardByCardNo(sysCard.getCardNo());
        if(card != null && !Objects.equals(card.getCardId(), sysCard.getCardId())) {
            throw new ServiceException("卡号不可重复，此卡号已存在");
        }
        return sysCardMapper.insertSysCard(sysCard);
    }

    /**
     * 修改卡密
     *
     * @param sysCard 卡密
     * @return 结果
     */
    @Override
    public int updateSysCard(SysCard sysCard) {
        sysCard.setUpdateTime(DateUtils.getNowDate());
        sysCard.setUpdateBy(SecurityUtils.getUsernameNoException());
        if(sysCard.getQuota() < 0 || sysCard.getQuota() > 1000 * 365 * DAY) {
            throw new ServiceException("面值设置错误，面值需在0-1000年之间");
        }
        SysCard card = selectSysCardByCardNo(sysCard.getCardNo());
        if(card != null && !Objects.equals(card.getCardId(), sysCard.getCardId())) {
            throw new ServiceException("卡号不可重复，此卡号已存在");
        }
        int i = sysCardMapper.updateSysCard(sysCard);
        if (i > 0) {
            card = sysCardMapper.selectSysCardByCardId(sysCard.getCardId());
            SysCache.set(CacheConstants.SYS_CARD_KEY + card.getCardNo(), card, 86400000);
        }
        return i;
    }

    /**
     * 批量删除卡密
     *
     * @param cardIds 需要删除的卡密主键
     * @return 结果
     */
    @Override
    public int deleteSysCardByCardIds(Long[] cardIds)
    {
        return sysCardMapper.deleteSysCardByCardIds(cardIds);
    }

    /**
     * 删除卡密信息
     *
     * @param cardId 卡密主键
     * @return 结果
     */
    @Override
    public int deleteSysCardByCardId(Long cardId)
    {
        return sysCardMapper.deleteSysCardByCardId(cardId);
    }

    /**
     * 查询卡密
     *
     * @param cardNo
     * @return
     */
    @Override
    public SysCard selectSysCardByCardNo(String cardNo) {
        if(StringUtils.isBlank(cardNo)) {
            return null;
        }
        SysCard card = (SysCard) SysCache.get(CacheConstants.SYS_CARD_KEY + cardNo);
        if (card == null) {
            card = sysCardMapper.selectSysCardByCardNo(cardNo);
            SysCache.set(CacheConstants.SYS_CARD_KEY + cardNo, card, 86400000);
        }
        return card;
    }

    /**
     * 查询卡密
     */
    public SysCard selectSysCardByAppIdAndCardNo(Long appId, String cardNo) {
        return sysCardMapper.selectSysCardByAppIdAndCardNo(appId, cardNo);
    }

    /**
     * 新增卡密
     *
     * @param sysCardList 卡密列表
     */
    @Override
    public int insertSysCardBatch(List<SysCard> sysCardList) {
        return sysCardMapper.insertSysCardBatch(sysCardList);
    }

    /**
     * 导入卡密数据
     *
     * @param cardList        卡密数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据，目前未起作用
     * @param operName        操作用户
     * @return 结果
     */
    public String importCard(List<SysCard> cardList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(cardList) || cardList.size() == 0) {
            throw new ServiceException("导入卡密数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        Map<String, SysApp> appMap = new HashMap<>();
        Map<String, SysCardTemplate> templateMap = new HashMap<>();
        Date nowDate = DateUtils.getNowDate();
        for (SysCard card : cardList) {
            if (card.getExpireTime() == null) {
                card.setExpireTime(DateUtils.parseDate(UserConstants.MAX_DATE));
            }
            if (!UserConstants.YES.equals(card.getIsCharged())) {
                card.setChargeTime(null);
            } else if (card.getChargeTime() == null) {
                card.setChargeTime(nowDate);
            }
            card.setCreateTime(nowDate);
            card.setCreateBy(SecurityUtils.getUsernameNoException());
            try {
                // 验证软件是否存在
                SysApp app;
                String appName = card.getApp().getAppName();
                if (StringUtils.isNotBlank(appName)) {
                    if (appMap.containsKey(appName)) {
                        app = appMap.get(appName);
                    } else {
                        app = sysAppService.selectSysAppByAppName(appName);
                        if (app != null) {
                            appMap.put(appName, app);
                        }
                    }
                    if (app != null) {
                        // 验证卡类是否存在
                        String templateName = card.getCardName();
                        SysCardTemplate template;
                        String templateKey = appName + "|" + templateName;
                        if (templateMap.containsKey(templateKey)) {
                            template = templateMap.get(templateKey);
                        } else {
                            template = sysCardTemplateService.selectSysCardTemplateByAppIdAndTemplateName(app.getAppId(), templateName);
                            if (template != null) {
                                templateMap.put(templateKey, template);
                            }
                        }
                        if (template != null) {
                            // 验证卡密是否存在
                            SysCard card1 = selectSysCardByAppIdAndCardNo(app.getAppId(), card.getCardNo());
                            if (card1 == null) {
                                BeanValidators.validateWithException(validator, card);
                                card.setTemplateId(template.getTemplateId());
                                card.setAppId(app.getAppId());
                                boolean errFlag = false;
                                if (card.getAgentUser() != null && StringUtils.isNotBlank(card.getAgentUser().getUserName())) {
                                    // 验证代理是否存在
                                    String agentUserName = card.getAgentUser().getUserName();
                                    SysUser agentUser = sysUserService.selectUserByUserName(agentUserName);
                                    if(agentUser != null) {
                                        SysAgent agent = sysAgentService.selectSysAgentByUserId(agentUser.getUserId());
                                        if (agent != null) {
                                            card.setAgentId(agent.getAgentId());
                                            card.setIsAgent(UserConstants.YES);
                                        } else {
                                            errFlag = true;
                                            failureNum++;
                                            failureMsg.append("<br/>" + failureNum + "、卡密[" + card.getCardNo() + "]导入失败：代理[" + agentUserName + "]不存在");
                                        }
                                    } else {
                                        errFlag = true;
                                        failureNum++;
                                        failureMsg.append("<br/>" + failureNum + "、卡密[" + card.getCardNo() + "]导入失败：代理账号[" + agentUserName + "]不存在");
                                    }
                                } else {
                                    card.setIsAgent(UserConstants.NO);
                                }
                                if (!errFlag) {
                                    // card默认值填充
                                    card.setApp(app);
                                    card.setChargeRule(template.getChargeRule());
                                    if (template.getEffectiveDuration() == -1) {
                                        card.setExpireTime(DateUtils.parseDate(UserConstants.MAX_DATE));
                                    } else {
                                        // sysCard.setExpireTime(DateUtils.addSeconds(new Date(), cardTpl.getEffectiveDuration().intValue()));
                                        LocalDateTime ldt = DateUtils.toLocalDateTime(new Date());
                                        ldt = ldt.plus(template.getEffectiveDuration(), ChronoUnit.SECONDS);
                                        card.setExpireTime(DateUtils.toDate(ldt));
                                    }
                                    card.setPrice(template.getPrice());
                                    card.setQuota(template.getQuota());
                                    card.setCardLoginLimitU(template.getCardLoginLimitU());
                                    card.setCardLoginLimitM(template.getCardLoginLimitM());
                                    card.setCardCustomParams(template.getCardCustomParams());
                                    try {
                                        card.setCreateBy(SecurityUtils.getUsernameNoException());
                                    } catch (Exception ignored) {
                                    }
                                    // 保存
                                    this.insertSysCard(card);
                                    successNum++;
                                    successMsg.append("<br/>" + successNum + "、卡密[" + card.getCardNo() + "]导入成功");
                                }
                            } else {
                                failureNum++;
                                failureMsg.append("<br/>" + failureNum + "、卡密[" + card.getCardNo() + "]导入失败：卡号已存在");
                            }
                        } else {
                            failureNum++;
                            failureMsg.append("<br/>" + failureNum + "、卡密[" + card.getCardNo() + "]导入失败：卡类[" + templateName + "]不存在");
                        }
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、卡密[" + card.getCardNo() + "]导入失败：软件[" + appName + "]不存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、卡密[" + card.getCardNo() + "]导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 获取批次号列表
     *
     * @return
     */
    @Override
    public List<BatchNoVo> selectBatchNoList(Long agentId) {
        return sysCardMapper.selectBatchNoList(agentId);
    }
}
