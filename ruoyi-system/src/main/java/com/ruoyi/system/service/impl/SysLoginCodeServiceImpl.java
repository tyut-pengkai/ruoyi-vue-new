package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.mapper.SysLoginCodeMapper;
import com.ruoyi.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param SysLoginCode 单码
     * @return 单码
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysLoginCode> selectSysLoginCodeList(SysLoginCode SysLoginCode) {
        return sysLoginCodeMapper.selectSysLoginCodeList(SysLoginCode);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public int countSysLoginCode(SysLoginCode SysLoginCode) {
        return sysLoginCodeMapper.countSysLoginCode(SysLoginCode);
    }

    /**
     * 新增单码
     *
     * @param SysLoginCode 单码
     * @return 结果
     */
    @Override
    public int insertSysLoginCode(SysLoginCode SysLoginCode) {
        SysLoginCode.setCreateTime(DateUtils.getNowDate());
        return sysLoginCodeMapper.insertSysLoginCode(SysLoginCode);
    }

    /**
     * 修改单码
     *
     * @param SysLoginCode 单码
     * @return 结果
     */
    @Override
    public int updateSysLoginCode(SysLoginCode SysLoginCode) {
        SysLoginCode.setUpdateTime(DateUtils.getNowDate());
        return sysLoginCodeMapper.updateSysLoginCode(SysLoginCode);
    }

    /**
     * 批量删除单码
     *
     * @param cardIds 需要删除的单码主键
     * @return 结果
     */
    @Override
    public int deleteSysLoginCodeByCardIds(Long[] cardIds) {
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
        return sysLoginCodeMapper.selectSysLoginCodeByCardNo(cardNo);
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
     * @param SysLoginCodeList
     */
    @Override
    public int insertSysLoginCodeBatch(List<SysLoginCode> SysLoginCodeList) {
        return sysLoginCodeMapper.insertSysLoginCodeBatch(SysLoginCodeList);
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
            card.setCreateBy(SecurityUtils.getUsername());
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
                        if (templateMap.containsKey(appName + "|" + templateName)) {
                            template = templateMap.get(templateName);
                        } else {
                            template = sysLoginCodeTemplateService.selectSysLoginCodeTemplateByAppIdAndTemplateName(app.getAppId(), templateName);
                            if (template != null) {
                                templateMap.put(appName + "|" + templateName, template);
                            }
                        }
                        if (template != null) {
                            // 验证卡密是否存在
                            SysLoginCode card1 = selectSysLoginCodeByAppIdAndCardNo(app.getAppId(), card.getCardNo());
                            if (card1 == null) {
                                BeanValidators.validateWithException(validator, card);
                                card.setTemplateId(template.getTemplateId());
                                card.setAppId(app.getAppId());
                                SysAppUser appUser = new SysAppUser();
                                boolean errFlag = false;
                                if (UserConstants.YES.equals(card.getIsAgent()) && card.getAgentUser() != null && StringUtils.isNotBlank(card.getAgentUser().getUserName())) {
                                    // 验证代理是否存在
                                    String agentUserName = card.getAgentUser().getUserName();
                                    SysUser agentUser = sysUserService.selectUserByUserName(agentUserName);
                                    if (agentUser != null) {
                                        card.setAgentId(agentUser.getUserId());
                                        appUser.setAgentId(agentUser.getUserId());
                                    } else {
                                        errFlag = true;
                                        failureNum++;
                                        failureMsg.append("<br/>" + failureNum + "、单码[" + card.getCardNo() + "]导入失败：代理[" + agentUserName + "]不存在");
                                    }
                                }
                                if (!errFlag) {
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
                                        appUser.setLastChargeCardId(card.getCardId());
                                        appUser.setLastChargeTemplateId(card.getTemplateId());
                                        sysAppUserService.insertSysAppUser(appUser);
                                        card.setIsCharged(UserConstants.YES);
                                        card.setChargeTime(DateUtils.getNowDate());
                                        card.setOnSale(UserConstants.NO);
                                    }
                                    this.insertSysLoginCode(card);
                                    successNum++;
                                    successMsg.append("<br/>" + successNum + "、单码[" + card.getCardNo() + "]导入成功");
                                    if (importUser) {
                                        successMsg.append(";软件用户导入成功");
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
}
