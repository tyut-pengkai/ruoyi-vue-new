package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.enums.GenRule;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.mapper.SysLoginCodeTemplateMapper;
import com.ruoyi.system.service.ISysLoginCodeService;
import com.ruoyi.system.service.ISysLoginCodeTemplateService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 登录码类别Service业务层处理
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

    /**
     * 查询登录码类别
     *
     * @param templateId 登录码类别主键
     * @return 登录码类别
     */
    @Override
    public SysLoginCodeTemplate selectSysLoginCodeTemplateByTemplateId(Long templateId) {
        return sysLoginCodeTemplateMapper.selectSysLoginCodeTemplateByTemplateId(templateId);
    }

    /**
     * 查询登录码类别列表
     *
     * @param sysLoginCodeTemplate 登录码类别
     * @return 登录码类别
     */
    @Override
    public List<SysLoginCodeTemplate> selectSysLoginCodeTemplateList(SysLoginCodeTemplate sysLoginCodeTemplate) {
        return sysLoginCodeTemplateMapper.selectSysLoginCodeTemplateList(sysLoginCodeTemplate);
    }

    /**
     * 新增登录码类别
     *
     * @param sysLoginCodeTemplate 登录码类别
     * @return 结果
     */
    @Override
    public int insertSysLoginCodeTemplate(SysLoginCodeTemplate sysLoginCodeTemplate) {
        sysLoginCodeTemplate.setCreateTime(DateUtils.getNowDate());
        return sysLoginCodeTemplateMapper.insertSysLoginCodeTemplate(sysLoginCodeTemplate);
    }

    /**
     * 修改登录码类别
     *
     * @param sysLoginCodeTemplate 登录码类别
     * @return 结果
     */
    @Override
    public int updateSysLoginCodeTemplate(SysLoginCodeTemplate sysLoginCodeTemplate) {
        sysLoginCodeTemplate.setUpdateTime(DateUtils.getNowDate());
        return sysLoginCodeTemplateMapper.updateSysLoginCodeTemplate(sysLoginCodeTemplate);
    }

    /**
     * 批量删除登录码类别
     *
     * @param templateIds 需要删除的登录码类别主键
     * @return 结果
     */
    @Override
    public int deleteSysLoginCodeTemplateByTemplateIds(Long[] templateIds) {
        return sysLoginCodeTemplateMapper.deleteSysLoginCodeTemplateByTemplateIds(templateIds);
    }

    /**
     * 删除登录码类别信息
     *
     * @param templateId 登录码类别主键
     * @return 结果
     */
    @Override
    public int deleteSysLoginCodeTemplateByTemplateId(Long templateId) {
        return sysLoginCodeTemplateMapper.deleteSysLoginCodeTemplateByTemplateId(templateId);
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
    public int genSysLoginCodeBatch(SysLoginCodeTemplate loginCodeTpl, Integer quantity, String onSale, String remark) {
        List<SysLoginCode> sysLoginCodeList = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            SysLoginCode sysLoginCode = new SysLoginCode();
            sysLoginCode.setCardName(loginCodeTpl.getCardName());
            sysLoginCode.setCardNo(genNo(loginCodeTpl.getCardNoPrefix(), loginCodeTpl.getCardNoSuffix(), loginCodeTpl.getCardNoLen(), loginCodeTpl.getCardNoGenRule()));
            sysLoginCode.setApp(loginCodeTpl.getApp());
            sysLoginCode.setTemplateId(loginCodeTpl.getTemplateId());
            sysLoginCode.setAppId(loginCodeTpl.getAppId());
            if (loginCodeTpl.getEffectiveDuration() == -1) {
                sysLoginCode.setExpireTime(DateUtils.parseDate(UserConstants.MAX_DATE));
            } else {
                sysLoginCode.setExpireTime(DateUtils.addSeconds(new Date(), loginCodeTpl.getEffectiveDuration().intValue()));
            }
            sysLoginCode.setIsCharged(UserConstants.NO);
            sysLoginCode.setIsSold(UserConstants.NO);
            sysLoginCode.setOnSale(onSale);
            sysLoginCode.setPrice(loginCodeTpl.getPrice());
            sysLoginCode.setQuota(loginCodeTpl.getQuota());
            sysLoginCode.setStatus(UserConstants.NORMAL);
            sysLoginCode.setRemark(remark);
            sysLoginCode.setCreateBy(SecurityUtils.getUsername());
            sysLoginCodeList.add(sysLoginCode);
        }
        return sysLoginCodeService.insertSysLoginCodeBatch(sysLoginCodeList);
    }

    private String generate(Integer length, GenRule genRule) {
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
            random = ("regex" + RandomStringUtils.randomAlphanumeric(length)).substring(0, length);
        }
        return random;
    }

    private String genNo(String prefix, String suffix, Integer length, GenRule genRule) {
        if (StringUtils.isBlank(prefix)) {
            prefix = "";
        }
        if (StringUtils.isBlank(suffix)) {
            suffix = "";
        }
        String random = prefix + generate(length, genRule) + suffix;
        while (sysLoginCodeService.selectSysLoginCodeByCardNo(random) != null) {
            random = prefix + generate(length, genRule) + suffix;
        }
        return random;
    }

}
