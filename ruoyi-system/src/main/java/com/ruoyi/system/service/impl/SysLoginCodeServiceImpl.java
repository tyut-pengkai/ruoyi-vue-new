package com.ruoyi.system.service.impl;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.service.ISysAgentUserService;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.ChargeType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.SysCache;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.domain.vo.BatchNoVo;
import com.ruoyi.system.mapper.SysLoginCodeMapper;
import com.ruoyi.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 单码Service业务层处理
 *
 * @author zwgu
 * @date 2021-12-03
 */
@Service
public class SysLoginCodeServiceImpl implements ISysLoginCodeService {
    private static final Logger log = LoggerFactory.getLogger(SysLoginCodeServiceImpl.class);
    @Autowired
    protected Validator validator;
    @Autowired
    private SysLoginCodeMapper sysLoginCodeMapper;
    @Resource
    private ISysLoginCodeTemplateService sysLoginCodeTemplateService;
    @Resource
    private ISysAppService sysAppService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysAppUserService sysAppUserService;
    @Resource
    private ISysAgentUserService sysAgentService;

    static Long DAY = 86400L; // 86400秒为一天

    /**
     * 查询单码
     *
     * @param cardId 单码主键
     * @return 单码
     */
    @Override
    public SysLoginCode selectSysLoginCodeByCardId(Long cardId) {
        return sysLoginCodeMapper.selectSysLoginCodeByCardId(cardId);
    }

    /**
     * 查询单码
     *
     * @param cardIds 单码主键
     * @return 单码
     */
    @Override
    public List<SysLoginCode> selectSysLoginCodeByCardIds(Long[] cardIds) {
        return sysLoginCodeMapper.selectSysLoginCodeByCardIds(cardIds);
    }

    /**
     * 查询单码列表
     *
     * @param sysLoginCode 单码
     * @return 单码
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysLoginCode> selectSysLoginCodeList(SysLoginCode sysLoginCode) {
        return sysLoginCodeMapper.selectSysLoginCodeList(sysLoginCode);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public int countSysLoginCode(SysLoginCode sysLoginCode) {
        return sysLoginCodeMapper.countSysLoginCode(sysLoginCode);
    }

    /**
     * 新增单码
     *
     * @param sysLoginCode 单码
     * @return 结果
     */
    @Override
    public int insertSysLoginCode(SysLoginCode sysLoginCode) {
        sysLoginCode.setCreateTime(DateUtils.getNowDate());
        sysLoginCode.setCreateBy(SecurityUtils.getUsernameNoException());
        if(sysLoginCode.getQuota() < 0 || sysLoginCode.getQuota() > 1000 * 365 * DAY) {
            throw new ServiceException("面值设置错误，面值需在0-1000年之间");
        }
        SysLoginCode loginCode = selectSysLoginCodeByCardNo(sysLoginCode.getCardNo());
        if(loginCode != null) {
            throw new ServiceException("单码不可重复，此单码已存在");
        }
        return sysLoginCodeMapper.insertSysLoginCode(sysLoginCode);
    }

    /**
     * 修改单码
     *
     * @param sysLoginCode 单码
     * @return 结果
     */
    @Override
    public int updateSysLoginCode(SysLoginCode sysLoginCode) {
        sysLoginCode.setUpdateTime(DateUtils.getNowDate());
        sysLoginCode.setUpdateBy(SecurityUtils.getUsernameNoException());
        if(sysLoginCode.getQuota() < 0 || sysLoginCode.getQuota() > 1000 * 365 * DAY) {
            throw new ServiceException("面值设置错误，面值需在0-1000年之间");
        }
        SysLoginCode loginCode = selectSysLoginCodeByCardNo(sysLoginCode.getCardNo());
        if(loginCode != null && !Objects.equals(loginCode.getCardId(), sysLoginCode.getCardId())) {
            throw new ServiceException("单码不可重复，此单码已存在");
        }
        int i = sysLoginCodeMapper.updateSysLoginCode(sysLoginCode);
        if (i > 0) {
            loginCode = sysLoginCodeMapper.selectSysLoginCodeByCardId(sysLoginCode.getCardId());
            SysCache.set(CacheConstants.SYS_LOGIN_CODE_KEY + loginCode.getCardNo(), loginCode, 86400000);
        }
        return i;
    }

    /**
     * 批量删除单码
     *
     * @param cardIds 需要删除的单码主键
     * @return 结果
     */
    @Override
    public int deleteSysLoginCodeByCardIds(Long[] cardIds) {
        for (Long cardId : cardIds) {
            SysLoginCode loginCode = sysLoginCodeMapper.selectSysLoginCodeByCardId(cardId);
            SysCache.delete(CacheConstants.SYS_LOGIN_CODE_KEY + loginCode.getCardNo());
        }
        return sysLoginCodeMapper.deleteSysLoginCodeByCardIds(cardIds);
    }

    /**
     * 删除单码信息
     *
     * @param cardId 单码主键
     * @return 结果
     */
    @Override
    public int deleteSysLoginCodeByCardId(Long cardId) {
        SysLoginCode loginCode = sysLoginCodeMapper.selectSysLoginCodeByCardId(cardId);
        SysCache.delete(CacheConstants.SYS_LOGIN_CODE_KEY + loginCode.getCardNo());
        return sysLoginCodeMapper.deleteSysLoginCodeByCardId(cardId);
    }

    /**
     * 查询单码
     *
     * @param cardNo
     * @return
     */
    @Override
    public SysLoginCode selectSysLoginCodeByCardNo(String cardNo) {
        if(StringUtils.isBlank(cardNo)) {
            return null;
        }
        SysLoginCode loginCode = (SysLoginCode) SysCache.get(CacheConstants.SYS_LOGIN_CODE_KEY + cardNo);
        if (loginCode == null) {
            loginCode = sysLoginCodeMapper.selectSysLoginCodeByCardNo(cardNo);
            SysCache.set(CacheConstants.SYS_LOGIN_CODE_KEY + cardNo, loginCode, 86400000);
        }
        return loginCode;
    }

    /**
     * 查询单码
     */
    public SysLoginCode selectSysLoginCodeByAppIdAndCardNo(Long appId, String cardNo) {
        return sysLoginCodeMapper.selectSysLoginCodeByAppIdAndCardNo(appId, cardNo);
    }

    /**
     * 新增单码
     *
     * @param sysLoginCodeList
     */
    @Override
    public int insertSysLoginCodeBatch(List<SysLoginCode> sysLoginCodeList) {
        return sysLoginCodeMapper.insertSysLoginCodeBatch(sysLoginCodeList);
    }

    /**
     * 导入卡密数据
     *
     * @param cardList        卡密数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据，目前未起作用
     * @param operName        操作用户
     * @return 结果
     */
    public String importLoginCode(List<SysLoginCode> cardList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(cardList) || cardList.size() == 0) {
            throw new ServiceException("导入单码数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        Map<String, SysApp> appMap = new HashMap<>();
        Map<String, SysLoginCodeTemplate> templateMap = new HashMap<>();
        Date nowDate = DateUtils.getNowDate();
        for (SysLoginCode card : cardList) {
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
                        SysLoginCodeTemplate template;
                        String templateKey = appName + "|" + templateName;
                        if (templateMap.containsKey(templateKey)) {
                            template = templateMap.get(templateKey);
                        } else {
                            template = sysLoginCodeTemplateService.selectSysLoginCodeTemplateByAppIdAndTemplateName(app.getAppId(), templateName);
                            if (template != null) {
                                templateMap.put(templateKey, template);
                            }
                        }
                        if (template != null) {
                            // 验证卡密是否存在
                            SysLoginCode card1 = selectSysLoginCodeByAppIdAndCardNo(app.getAppId(), card.getCardNo());
                            if (card1 == null) {
                                BeanValidators.validateWithException(validator, card);
                                card.setTemplateId(template.getTemplateId());
                                card.setAppId(template.getAppId());
                                SysAppUser appUser = new SysAppUser();
                                boolean errFlag = false;
                                if (card.getAgentUser() != null && StringUtils.isNotBlank(card.getAgentUser().getUserName())) {
                                    // 验证代理是否存在
                                    String agentUserName = card.getAgentUser().getUserName();
                                    SysUser agentUser = sysUserService.selectUserByUserName(agentUserName);
                                    if (agentUser != null) {
                                        SysAgent agent = sysAgentService.selectSysAgentByUserId(agentUser.getUserId());
                                        if (agent != null) {
                                            card.setAgentId(agent.getAgentId());
                                            appUser.setAgentId(agent.getAgentId());
                                            card.setIsAgent(UserConstants.YES);
                                        } else {
                                            errFlag = true;
                                            failureNum++;
                                            failureMsg.append("<br/>" + failureNum + "、单码[" + card.getCardNo() + "]导入失败：代理[" + agentUserName + "]不存在");
                                        }
                                    } else {
                                        errFlag = true;
                                        failureNum++;
                                        failureMsg.append("<br/>" + failureNum + "、单码[" + card.getCardNo() + "]导入失败：代理账号[" + agentUserName + "]不存在");
                                    }
                                } else {
                                    card.setIsAgent(UserConstants.NO);
                                }
                                if (!errFlag) {
                                    // card默认值填充
                                    card.setApp(app);
                                    if (template.getEffectiveDuration() == -1) {
                                        card.setExpireTime(DateUtils.parseDate(UserConstants.MAX_DATE));
                                    } else {
                                        // sysLoginCode.setExpireTime(DateUtils.addSeconds(new Date(), loginCodeTpl.getEffectiveDuration().intValue()));
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
                                    } catch (Exception ignore) {
                                    }
                                    // 导入软件用户
                                    boolean importUser = false;
                                    if (app.getBillType() == BillType.TIME && card.getUserExpireTime() != null) {
                                        appUser.setExpireTime(card.getUserExpireTime());
                                        appUser.setPoint(null);
                                        importUser = true;
                                    } else if (app.getBillType() == BillType.POINT && card.getPoint() != null) {
                                        appUser.setExpireTime(null);
                                        appUser.setPoint(card.getPoint());
                                        importUser = true;
                                    }
                                    if (importUser) {
                                        card.setIsCharged(UserConstants.YES);
                                        if (card.getChargeTime() == null) {
                                            card.setChargeTime(DateUtils.getNowDate());
                                        }
                                        card.setOnSale(UserConstants.NO);
                                    }
                                    // 保存
                                    boolean doImportUser = false;
                                    if (importUser) {
                                        SysAppUser appUser1 = sysAppUserService.selectSysAppUserByAppIdAndLoginCode(app.getAppId(), card.getCardNo());
                                        if (appUser1 == null) {
                                            appUser.setAppId(app.getAppId());
                                            appUser.setUserId(null);
                                            appUser.setLastLoginTime(null);
                                            appUser.setLoginCode(card.getCardNo());
                                            appUser.setLoginLimitM(-2);
                                            appUser.setLoginLimitU(-2);
                                            appUser.setCardLoginLimitM(card.getCardLoginLimitM());
                                            appUser.setCardLoginLimitU(card.getCardLoginLimitU());
                                            appUser.setCardCustomParams(card.getCardCustomParams());
                                            appUser.setLoginTimes(0L);
                                            appUser.setPwdErrorTimes(0);
                                            appUser.setStatus(UserConstants.NORMAL);
                                            appUser.setFreeBalance(BigDecimal.ZERO);
                                            appUser.setPayBalance(BigDecimal.ZERO);
                                            appUser.setFreePayment(BigDecimal.ZERO);
                                            appUser.setPayPayment(BigDecimal.ZERO);
                                            appUser.setUnbindTimes(app.getUnbindTimes());
                                            appUser.setApp(app);
                                            appUser.setRemark(card.getRemark());
                                            appUser.setLastChargeCardId(card.getCardId());
                                            appUser.setLastChargeTemplateId(card.getTemplateId());
                                            sysAppUserService.insertSysAppUser(appUser);
                                            doImportUser = true;
                                            card.setChargeType(ChargeType.LOGIN);
                                            card.setChargeTo(appUser.getAppUserId());
                                        }
                                    }
                                    this.insertSysLoginCode(card);
                                    successNum++;
                                    successMsg.append("<br/>" + successNum + "、单码[" + card.getCardNo() + "]导入成功");
                                    if (importUser) {
                                        if (doImportUser) {
                                            successMsg.append(";软件用户导入成功");
                                        } else {
                                            successMsg.append(";软件用户导入失败，软件用户已存在");
                                        }
                                    }
                                }
                            } else {
                                failureNum++;
                                failureMsg.append("<br/>" + failureNum + "、单码[" + card.getCardNo() + "]导入失败：单码已存在");
                            }
                        } else {
                            failureNum++;
                            failureMsg.append("<br/>" + failureNum + "、单码[" + card.getCardNo() + "]导入失败：类别[" + templateName + "]不存在");
                        }
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、单码[" + card.getCardNo() + "]导入失败：软件[" + appName + "]不存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、单码[" + card.getCardNo() + "]导入失败：";
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
        return sysLoginCodeMapper.selectBatchNoList(agentId);
    }
}
