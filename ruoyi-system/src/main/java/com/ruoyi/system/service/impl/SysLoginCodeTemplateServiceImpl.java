package com.ruoyi.system.service.impl;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.service.ISysAgentUserService;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.ChargeType;
import com.ruoyi.common.enums.GenRule;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.domain.vo.BatchReplaceVo;
import com.ruoyi.system.mapper.SysLoginCodeTemplateMapper;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysLoginCodeService;
import com.ruoyi.system.service.ISysLoginCodeTemplateService;
import nl.flotsam.xeger.Xeger;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 单码类别Service业务层处理
 *
 * @author zwgu
 * @date 2022-01-06
 */
@Service
public class SysLoginCodeTemplateServiceImpl implements ISysLoginCodeTemplateService {
    @Autowired
    private SysLoginCodeTemplateMapper sysLoginCodeTemplateMapper;
    @Autowired
    private ISysLoginCodeService sysLoginCodeService;
    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysAgentUserService sysAgentService;

    /**
     * 查询单码类别
     *
     * @param templateId 单码类别主键
     * @return 单码类别
     */
    @Override
    public SysLoginCodeTemplate selectSysLoginCodeTemplateByTemplateId(Long templateId) {
        return sysLoginCodeTemplateMapper.selectSysLoginCodeTemplateByTemplateId(templateId);
    }

    /**
     * 查询单码类别列表
     *
     * @param sysLoginCodeTemplate 单码类别
     * @return 单码类别
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysLoginCodeTemplate> selectSysLoginCodeTemplateList(SysLoginCodeTemplate sysLoginCodeTemplate) {
        return sysLoginCodeTemplateMapper.selectSysLoginCodeTemplateList(sysLoginCodeTemplate);
    }

    /**
     * 新增单码类别
     *
     * @param sysLoginCodeTemplate 单码类别
     * @return 结果
     */
    @Override
    public int insertSysLoginCodeTemplate(SysLoginCodeTemplate sysLoginCodeTemplate) {
        sysLoginCodeTemplate.setCreateTime(DateUtils.getNowDate());
        sysLoginCodeTemplate.setCreateBy(SecurityUtils.getUsernameNoException());
        return sysLoginCodeTemplateMapper.insertSysLoginCodeTemplate(sysLoginCodeTemplate);
    }

    /**
     * 修改单码类别
     *
     * @param sysLoginCodeTemplate 单码类别
     * @return 结果
     */
    @Override
    public int updateSysLoginCodeTemplate(SysLoginCodeTemplate sysLoginCodeTemplate) {
        sysLoginCodeTemplate.setUpdateTime(DateUtils.getNowDate());
        sysLoginCodeTemplate.setUpdateBy(SecurityUtils.getUsernameNoException());
        return sysLoginCodeTemplateMapper.updateSysLoginCodeTemplate(sysLoginCodeTemplate);
    }

    /**
     * 批量删除单码类别
     *
     * @param templateIds 需要删除的单码类别主键
     * @return 结果
     */
    @Override
    public int deleteSysLoginCodeTemplateByTemplateIds(Long[] templateIds) {
        return sysLoginCodeTemplateMapper.deleteSysLoginCodeTemplateByTemplateIds(templateIds);
    }

    /**
     * 删除单码类别信息
     *
     * @param templateId 单码类别主键
     * @return 结果
     */
    @Override
    public int deleteSysLoginCodeTemplateByTemplateId(Long templateId) {
        return sysLoginCodeTemplateMapper.deleteSysLoginCodeTemplateByTemplateId(templateId);
    }

    /**
     * 批量新增卡密
     *
     * @param loginCodeTpl
     * @param quantity
     * @param remark
     * @return
     */
    @Override
    public List<SysLoginCode> genSysLoginCodeBatch(SysLoginCodeTemplate loginCodeTpl, Integer quantity, String onSale, String isAgent, String remark) {
        List<SysLoginCode> sysLoginCodeList = new ArrayList<>();
        String batchNo = DateUtils.dateTimeNow();
        for (int i = 0; i < quantity; i++) {
            SysLoginCode sysLoginCode = new SysLoginCode();
            sysLoginCode.setCardName(loginCodeTpl.getCardName());
            sysLoginCode.setCardNo(genNo(loginCodeTpl.getCardNoPrefix(), loginCodeTpl.getCardNoSuffix(), loginCodeTpl.getCardNoLen(), loginCodeTpl.getCardNoGenRule(), loginCodeTpl.getCardNoRegex()));
            sysLoginCode.setApp(loginCodeTpl.getApp());
            sysLoginCode.setTemplateId(loginCodeTpl.getTemplateId());
            sysLoginCode.setAppId(loginCodeTpl.getAppId());
            if (loginCodeTpl.getEffectiveDuration() == -1) {
                sysLoginCode.setExpireTime(DateUtils.parseDate(UserConstants.MAX_DATE));
            } else {
//                sysLoginCode.setExpireTime(DateUtils.addSeconds(new Date(), loginCodeTpl.getEffectiveDuration().intValue()));
                try {
                    Instant ldt = Instant.now();
                    ldt = ldt.plusSeconds(loginCodeTpl.getEffectiveDuration());
                    sysLoginCode.setExpireTime(Date.from(ldt));
                }  catch (DateTimeException e) {
                    sysLoginCode.setExpireTime(DateUtils.parseDate(UserConstants.MAX_DATE));
                }
            }
            sysLoginCode.setIsCharged(UserConstants.NO);
            sysLoginCode.setIsSold(UserConstants.NO);
            sysLoginCode.setOnSale(onSale);
            sysLoginCode.setIsAgent(isAgent);
            sysLoginCode.setPrice(loginCodeTpl.getPrice());
            sysLoginCode.setQuota(loginCodeTpl.getQuota());
            sysLoginCode.setStatus(UserConstants.NORMAL);
            sysLoginCode.setRemark(remark);
            sysLoginCode.setCardLoginLimitU(loginCodeTpl.getCardLoginLimitU());
            sysLoginCode.setCardLoginLimitM(loginCodeTpl.getCardLoginLimitM());
            sysLoginCode.setCardCustomParams(loginCodeTpl.getCardCustomParams());
            sysLoginCode.setBatchNo(batchNo);
            try {
                sysLoginCode.setCreateBy(SecurityUtils.getUsernameNoException());
                SysAgent agent = sysAgentService.selectSysAgentByUserId(SecurityUtils.getUserId());
                sysLoginCode.setAgentId(agent.getAgentId());
            } catch (Exception ignore) {
            }
            sysLoginCodeList.add(sysLoginCode);
        }
        sysLoginCodeService.insertSysLoginCodeBatch(sysLoginCodeList);
        return sysLoginCodeList;
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
        } else if (genRule == GenRule.NUM_UPPERCASE) {
            random = RandomStringUtils.randomAlphanumeric(length).toUpperCase();
        } else if (genRule == GenRule.NUM_UPPERCASE_LOWERCASE) {
            random = RandomStringUtils.randomAlphanumeric(length);
        } else if (genRule == GenRule.UPPERCASE) {
            random = RandomStringUtils.randomAlphabetic(length).toUpperCase();
        } else if (genRule == GenRule.UPPERCASE_LOWERCASE) {
            random = RandomStringUtils.randomAlphabetic(length);
        } else if (genRule == GenRule.REGEX) {
//            random = ("regex" + RandomStringUtils.randomAlphanumeric(length)).substring(0, length);
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

    /**
     * 查询卡密模板
     *
     * @param appId        APP主键
     * @param templateName 卡类名称
     * @return 卡密模板
     */
    public SysLoginCodeTemplate selectSysLoginCodeTemplateByAppIdAndTemplateName(Long appId, String templateName) {
        return sysLoginCodeTemplateMapper.selectSysLoginCodeTemplateByAppIdAndTemplateName(appId, templateName);
    }

    /**
     * 批量换卡
     *
     * @param template
     * @param loginCode
     * @param vo
     * @param batchNo
     * @return
     */
    @Override
    public SysLoginCode genSysLoginCodeReplace(SysLoginCodeTemplate loginCodeTpl, SysLoginCode loginCode, BatchReplaceVo vo, String batchNo) {
        SysLoginCode newLoginCode = new SysLoginCode();
        BeanUtils.copyProperties(loginCode, newLoginCode);
        newLoginCode.setCardId(null);
        if( Objects.equals(UserConstants.YES, loginCode.getIsCharged())) { // 换残卡
            SysApp app = loginCode.getApp();
            SysAppUser appUser = null;
            if(loginCode.getChargeTo() != null) {
                appUser = appUserService.selectSysAppUserByAppUserId(loginCode.getChargeTo());
            } else {
                appUser = appUserService.selectSysAppUserByAppIdAndLoginCode(loginCode.getAppId(), loginCode.getCardNo());
            }
            if(loginCode.getChargeType() == ChargeType.CHARGE || appUser == null) {
                throw new ServiceException("单码已使用且已经以卡充卡到软件用户");
            }
            if(app.getBillType() == BillType.TIME) {
                long second = DateUtils.differentMillisecond(DateUtils.getNowDate(), appUser.getExpireTime()) / 1000;
                if(second > 0) {
                    if(!Objects.equals(UserConstants.YES, vo.getChangeMode())) {
                        newLoginCode.setQuota(second);
                    }
                } else {
                    throw new ServiceException("单码已无剩余时间");
                }
            } else if(app.getBillType() == BillType.POINT) {
                if(appUser.getPoint().compareTo(BigDecimal.ZERO) > 0) {
                    if(!Objects.equals(UserConstants.YES, vo.getChangeMode())) {
                        newLoginCode.setQuota(appUser.getPoint().longValue());
                    }
                } else {
                    throw new ServiceException("单码已无剩余点数");
                }
            } else {
                throw new ServiceException("软件计费方式设置有误");
            }
        }
        newLoginCode.setCardNo(genNo(loginCodeTpl.getCardNoPrefix(), loginCodeTpl.getCardNoSuffix(), loginCodeTpl.getCardNoLen(), loginCodeTpl.getCardNoGenRule(), loginCodeTpl.getCardNoRegex()));
        newLoginCode.setIsCharged(UserConstants.NO);
        newLoginCode.setChargeTime(null);
        newLoginCode.setRemark("此卡用于替换旧卡：" + loginCode.getCardNo()
                + (StringUtils.isNotBlank(vo.getRemark())? "\n换卡备注：" + vo.getRemark() : "")
                + (StringUtils.isNotBlank(loginCode.getRemark())? "\n旧卡备注：" + loginCode.getRemark() : ""));
        newLoginCode.setBatchNo(batchNo);
        try {
            newLoginCode.setCreateBy(SecurityUtils.getUsernameNoException());
        } catch (Exception ignore) {
        }
        if(sysLoginCodeService.insertSysLoginCode(newLoginCode)>0) {
            return newLoginCode;
        }
        throw new ServiceException("保存新卡失败");
    }

}
