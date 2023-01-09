package com.ruoyi.web.controller.system;

import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysAppUserDeviceCode;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.*;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.domain.SysAppUserExpireLog;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.domain.vo.BatchOperationVo;
import com.ruoyi.system.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system/batchOperation")
public class SysBatchOperationController extends BaseController {

    @Resource
    private ISysCardService sysCardService;
    @Resource
    private ISysLoginCodeService sysLoginCodeService;
    @Resource
    private ISysAppUserService sysAppUserService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysAppUserDeviceCodeService sysAppUserDeviceCodeService;
    @Resource
    private ISysAppService sysAppService;

    @GetMapping("/operation")
    public AjaxResult batchOperation(BatchOperationVo vo) {
        String[] split = vo.getContent().split("\n");
        Set<String> set = new HashSet<>(Arrays.asList(split));
        if (set.size() == 0) {
            return AjaxResult.success("请输入要操作的内容，每行一个");
        }
        List<String> list = set.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        LinkedHashMap<String, List<String>> result = new LinkedHashMap<>();
        SysApp sysApp = sysAppService.selectSysAppByAppId(vo.getAppId());
        if (sysApp == null) {
            return AjaxResult.success("软件不存在");
        }
        for (String item : list) {
            if (vo.getOperationObject() == BatchOperationObject.CARD_AND_LOGIN_CODE) {
                if (sysApp.getAuthType() == AuthType.ACCOUNT) {
                    SysCard card = sysCardService.selectSysCardByAppIdAndCardNo(vo.getAppId(), item);
                    if (vo.getOperationType() == BatchOperationType.QUERY) {
                        String a = "正常的卡密";
                        String b = "冻结的卡密";
                        String c = "已使用的卡密";
                        String d = "不存在的卡密";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                            result.put(c, new ArrayList<>());
                            result.put(d, new ArrayList<>());
                        }
                        if (card == null) {
                            result.get(d).add(item + "【卡密不存在】");
                        } else if (Objects.equals(card.getIsCharged(), UserConstants.YES)) {
                            result.get(c).add(item + getCardSimpleDesc(card));
                        } else if (Objects.equals(card.getStatus(), UserConstants.NORMAL)) {
                            result.get(a).add(item + getCardSimpleDesc(card));
                        } else {
                            result.get(b).add(item + getCardSimpleDesc(card));
                        }
                    } else if (vo.getOperationType() == BatchOperationType.BAN) {
                        String a = "冻结成功";
                        String b = "冻结失败";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                        }
                        if (card == null) {
                            result.get(b).add(item + "【卡密不存在】");
                        } else if (Objects.equals(card.getStatus(), UserConstants.NORMAL)) {
                            try {
                                SysCard newCard = new SysCard();
                                newCard.setCardId(card.getCardId());
                                newCard.setStatus(UserConstants.USER_DISABLE);
                                sysCardService.updateSysCard(newCard);
                                result.get(a).add(item + "【冻结成功】");
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
                        } else {
                            result.get(a).add(item + "【已经是冻结状态】");
                        }
                    } else if (vo.getOperationType() == BatchOperationType.UNBAN) {
                        String a = "解冻成功";
                        String b = "解冻失败";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                        }
                        if (card == null) {
                            result.get(b).add(item + "【卡密不存在】");
                        } else if (Objects.equals(card.getStatus(), UserConstants.NORMAL)) {
                            result.get(a).add(item + "【已经是正常状态】");
                        } else {
                            try {
                                SysCard newCard = new SysCard();
                                newCard.setCardId(card.getCardId());
                                newCard.setStatus(UserConstants.NORMAL);
                                sysCardService.updateSysCard(newCard);
                                result.get(a).add(item + "【解冻成功】");
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
                        }
                    } else if (vo.getOperationType() == BatchOperationType.UNBIND) {
                        return AjaxResult.success("卡密无解绑操作");
                    } else if (vo.getOperationType() == BatchOperationType.DELETE) {
                        String a = "删除成功";
                        String b = "删除失败";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                        }
                        if (card == null) {
                            result.get(b).add(item + "【卡密不存在】");
                        } else {
                            try {
                                sysCardService.deleteSysCardByCardId(card.getCardId());
                                result.get(a).add(item + "【删除成功】");
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
                        }
                    } else if (vo.getOperationType() == BatchOperationType.ADD_TIME) {
                        return AjaxResult.success("卡密无加时操作");
                    } else if (vo.getOperationType() == BatchOperationType.SUB_TIME) {
                        return AjaxResult.success("卡密无扣时操作");
                    } else if (vo.getOperationType() == BatchOperationType.ADD_POINT) {
                        return AjaxResult.success("卡密无加点操作");
                    } else if (vo.getOperationType() == BatchOperationType.SUB_POINT) {
                        return AjaxResult.success("卡密无扣点操作");
                    } else if (vo.getOperationType() == BatchOperationType.REMARK) {
                        if (StringUtils.isBlank(vo.getOperationValue())) {
                            return AjaxResult.success("参数：备注内容，不能为空");
                        }
                        String a = "备注成功";
                        String b = "备注失败";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                        }
                        if (card == null) {
                            result.get(b).add(item + "【卡密不存在】");
                        } else {
                            try {
                                SysCard newCard = new SysCard();
                                newCard.setCardId(card.getCardId());
                                newCard.setRemark(vo.getOperationValue());
                                sysCardService.updateSysCard(newCard);
                                result.get(a).add(item + "【备注成功】");
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
                        }
                    } else {
                        return AjaxResult.success("执行操作参数有误");
                    }
                } else if (sysApp.getAuthType() == AuthType.LOGIN_CODE) {
                    SysLoginCode loginCode = sysLoginCodeService.selectSysLoginCodeByAppIdAndCardNo(vo.getAppId(), item);
                    if (vo.getOperationType() == BatchOperationType.QUERY) {
                        String a = "正常的单码";
                        String b = "冻结的单码";
                        String c = "已使用的单码";
                        String d = "不存在的单码";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                            result.put(c, new ArrayList<>());
                            result.put(d, new ArrayList<>());
                        }
                        if (loginCode == null) {
                            result.get(d).add(item + "【单码不存在】");
                        } else if (Objects.equals(loginCode.getIsCharged(), UserConstants.YES)) {
                            result.get(c).add(item + getLoginCodeSimpleDesc(loginCode));
                        } else if (Objects.equals(loginCode.getStatus(), UserConstants.NORMAL)) {
                            result.get(a).add(item + getLoginCodeSimpleDesc(loginCode));
                        } else {
                            result.get(b).add(item + getLoginCodeSimpleDesc(loginCode));
                        }
                    } else if (vo.getOperationType() == BatchOperationType.BAN) {
                        String a = "冻结成功";
                        String b = "冻结失败";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                        }
                        if (loginCode == null) {
                            result.get(b).add(item + "【单码不存在】");
                        } else if (Objects.equals(loginCode.getStatus(), UserConstants.NORMAL)) {
                            try {
                                SysLoginCode newLoginCode = new SysLoginCode();
                                newLoginCode.setCardId(loginCode.getCardId());
                                newLoginCode.setStatus(UserConstants.USER_DISABLE);
                                sysLoginCodeService.updateSysLoginCode(newLoginCode);
                                result.get(a).add(item + "【冻结成功】");
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
                        } else {
                            result.get(a).add(item + "【已经是冻结状态】");
                        }
                    } else if (vo.getOperationType() == BatchOperationType.UNBAN) {
                        String a = "解冻成功";
                        String b = "解冻失败";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                        }
                        if (loginCode == null) {
                            result.get(b).add(item + "【单码不存在】");
                        } else if (Objects.equals(loginCode.getStatus(), UserConstants.NORMAL)) {
                            result.get(a).add(item + "【已经是正常状态】");
                        } else {
                            try {
                                SysLoginCode newLoginCode = new SysLoginCode();
                                newLoginCode.setCardId(loginCode.getCardId());
                                newLoginCode.setStatus(UserConstants.NORMAL);
                                sysLoginCodeService.updateSysLoginCode(newLoginCode);
                                result.get(a).add(item + "【解冻成功】");
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
                        }
                    } else if (vo.getOperationType() == BatchOperationType.UNBIND) {
                        return AjaxResult.success("单码无解绑操作");
                    } else if (vo.getOperationType() == BatchOperationType.DELETE) {
                        String a = "删除成功";
                        String b = "删除失败";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                        }
                        if (loginCode == null) {
                            result.get(b).add(item + "【单码不存在】");
                        } else {
                            try {
                                sysLoginCodeService.deleteSysLoginCodeByCardId(loginCode.getCardId());
                                result.get(a).add(item + "【删除成功】");
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
                        }
                    } else if (vo.getOperationType() == BatchOperationType.ADD_TIME) {
                        return AjaxResult.success("单码无加时操作");
                    } else if (vo.getOperationType() == BatchOperationType.SUB_TIME) {
                        return AjaxResult.success("单码无扣时操作");
                    } else if (vo.getOperationType() == BatchOperationType.ADD_POINT) {
                        return AjaxResult.success("单码无加点操作");
                    } else if (vo.getOperationType() == BatchOperationType.SUB_POINT) {
                        return AjaxResult.success("单码无扣点操作");
                    } else if (vo.getOperationType() == BatchOperationType.REMARK) {
                        if (StringUtils.isBlank(vo.getOperationValue())) {
                            return AjaxResult.success("参数：备注内容，不能为空");
                        }
                        String a = "备注成功";
                        String b = "备注失败";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                        }
                        if (loginCode == null) {
                            result.get(b).add(item + "【单码不存在】");
                        } else {
                            try {
                                SysLoginCode newLoginCode = new SysLoginCode();
                                newLoginCode.setCardId(loginCode.getCardId());
                                newLoginCode.setRemark(vo.getOperationValue());
                                sysLoginCodeService.updateSysLoginCode(newLoginCode);
                                result.get(a).add(item + "【备注成功】");
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
                        }
                    } else {
                        return AjaxResult.success("执行操作参数有误");
                    }
                }
            } else if (vo.getOperationObject() == BatchOperationObject.APP_USER) {
                SysAppUser appUser = null;
                if (sysApp.getAuthType() == AuthType.ACCOUNT) {
                    SysUser sysUser = sysUserService.selectUserByUserName(item);
                    if (sysUser != null) {
                        appUser = sysAppUserService.selectSysAppUserByAppIdAndUserId(vo.getAppId(), sysUser.getUserId());
                    }
                } else if (sysApp.getAuthType() == AuthType.LOGIN_CODE) {
                    appUser = sysAppUserService.selectSysAppUserByAppIdAndLoginCode(vo.getAppId(), item);
                }

                if (vo.getOperationType() == BatchOperationType.QUERY) {
                    String a = "正常的用户";
                    String b = "冻结的用户";
                    String d = "不存在的用户";
                    if (result.size() == 0) {
                        result.put(a, new ArrayList<>());
                        result.put(b, new ArrayList<>());
                        result.put(d, new ArrayList<>());
                    }
                    if (appUser == null) {
                        result.get(d).add(item + "【软件用户不存在】");
                    } else if (Objects.equals(appUser.getStatus(), UserConstants.NORMAL)) {
                        result.get(a).add(item + getAppUserSimpleDesc(appUser));
                    } else {
                        result.get(b).add(item + getAppUserSimpleDesc(appUser));
                    }
                } else if (vo.getOperationType() == BatchOperationType.BAN) {
                    String a = "冻结成功";
                    String b = "冻结失败";
                    if (result.size() == 0) {
                        result.put(a, new ArrayList<>());
                        result.put(b, new ArrayList<>());
                    }
                    if (appUser == null) {
                        result.get(b).add(item + "【软件用户不存在】");
                    } else if (Objects.equals(appUser.getStatus(), UserConstants.NORMAL)) {
                        try {
                            SysAppUser newAppUser = new SysAppUser();
                            newAppUser.setAppUserId(appUser.getAppUserId());
                            newAppUser.setStatus(UserConstants.USER_DISABLE);
                            sysAppUserService.updateSysAppUser(newAppUser);
                            result.get(a).add(item + "【冻结成功】");
                        } catch (Exception e) {
                            result.get(b).add(item + "【" + e.getMessage() + "】");
                        }
                    } else {
                        result.get(a).add(item + "【已经是冻结状态】");
                    }
                } else if (vo.getOperationType() == BatchOperationType.UNBAN) {
                    String a = "解冻成功";
                    String b = "解冻失败";
                    if (result.size() == 0) {
                        result.put(a, new ArrayList<>());
                        result.put(b, new ArrayList<>());
                    }
                    if (appUser == null) {
                        result.get(b).add(item + "【软件用户不存在】");
                    } else if (Objects.equals(appUser.getStatus(), UserConstants.NORMAL)) {
                        result.get(a).add(item + "【已经是正常状态】");
                    } else {
                        try {
                            SysAppUser newAppUser = new SysAppUser();
                            newAppUser.setAppUserId(appUser.getAppUserId());
                            newAppUser.setStatus(UserConstants.NORMAL);
                            sysAppUserService.updateSysAppUser(newAppUser);
                            result.get(a).add(item + "【解冻成功】");
                        } catch (Exception e) {
                            result.get(b).add(item + "【" + e.getMessage() + "】");
                        }
                    }
                } else if (vo.getOperationType() == BatchOperationType.UNBIND) {
                    doAppUserUnbind(result, item, appUser);
                } else if (vo.getOperationType() == BatchOperationType.DELETE) {
                    String a = "删除成功";
                    String b = "删除失败";
                    if (result.size() == 0) {
                        result.put(a, new ArrayList<>());
                        result.put(b, new ArrayList<>());
                    }
                    if (appUser == null) {
                        result.get(b).add(item + "【软件用户不存在】");
                    } else {
                        try {
                            sysAppUserService.deleteSysAppUserByAppUserId(appUser.getAppUserId());
                            result.get(a).add(item + "【删除成功】");
                        } catch (Exception e) {
                            result.get(b).add(item + "【" + e.getMessage() + "】");
                        }
                    }
                } else if (vo.getOperationType() == BatchOperationType.ADD_TIME) {
                    if (vo.getOperationValue() == null || Convert.toLong(vo.getOperationValue(), 0L) <= 0) {
                        return AjaxResult.success("参数必须为整数且大于0");
                    }
                    doAppUserAddTime(result, item, appUser, vo);
                } else if (vo.getOperationType() == BatchOperationType.SUB_TIME) {
                    if (vo.getOperationValue() == null || Convert.toLong(vo.getOperationValue(), 0L) <= 0) {
                        return AjaxResult.success("参数必须为整数且大于0");
                    }
                    doAppUserSubTime(result, item, appUser, vo);
                } else if (vo.getOperationType() == BatchOperationType.ADD_POINT) {
                    if (vo.getOperationValue() == null || Convert.toLong(vo.getOperationValue(), 0L) <= 0) {
                        return AjaxResult.success("参数必须为整数且大于0");
                    }
                    doAppUserAddPoint(result, item, appUser, vo);
                } else if (vo.getOperationType() == BatchOperationType.SUB_POINT) {
                    if (vo.getOperationValue() == null || Convert.toLong(vo.getOperationValue(), 0L) <= 0) {
                        return AjaxResult.success("参数必须为整数且大于0");
                    }
                    doAppUserSubPoint(result, item, appUser, vo);
                } else if (vo.getOperationType() == BatchOperationType.REMARK) {
                    if (StringUtils.isBlank(vo.getOperationValue())) {
                        return AjaxResult.success("参数：备注内容，不能为空");
                    }
                    String a = "备注成功";
                    String b = "备注失败";
                    if (result.size() == 0) {
                        result.put(a, new ArrayList<>());
                        result.put(b, new ArrayList<>());
                    }
                    if (appUser == null) {
                        result.get(b).add(item + "【软件用户不存在】");
                    } else {
                        try {
                            SysAppUser newAppUser = new SysAppUser();
                            newAppUser.setAppUserId(appUser.getAppUserId());
                            newAppUser.setRemark(vo.getOperationValue());
                            sysAppUserService.updateSysAppUser(newAppUser);
                            result.get(a).add(item + "【备注成功】");
                        } catch (Exception e) {
                            result.get(b).add(item + "【" + e.getMessage() + "】");
                        }
                    }
                } else {
                    return AjaxResult.success("执行操作参数有误");
                }
            } else {
                return AjaxResult.success("操作对象参数有误");
            }
        }
        StringBuilder resultStr = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : result.entrySet()) {
            resultStr.append("--------------------------【")
                    .append(entry.getKey()).append("】").append(entry.getValue().size())
                    .append("个--------------------------").append("\n\n")
                    .append(String.join("\n", entry.getValue()))
                    .append("\n\n");
        }
        return AjaxResult.success(resultStr.toString());
    }

    @GetMapping("/scopeOperation")
    public AjaxResult scopeOperation(BatchOperationVo vo) {
        LinkedHashMap<String, List<String>> result = new LinkedHashMap<>();
        SysApp sysApp = sysAppService.selectSysAppByAppId(vo.getAppId());
        if (sysApp == null) {
            return AjaxResult.success("软件不存在");
        }
        if (vo.getOperationObject() == BatchOperationObject.CARD_AND_LOGIN_CODE) {
            if (sysApp.getAuthType() == AuthType.ACCOUNT) {
                SysCard sCard = new SysCard();
                sCard.setAppId(sysApp.getAppId());
                if (vo.getTemplateId() != 0) {
                    sCard.setTemplateId(vo.getTemplateId());
                }
                if (vo.getOperationScope() == BatchOperationScope.ALL) {
                    // ignore
//                } else if(vo.getOperationScope() == BatchOperationScope.VIP) {
//                    return AjaxResult.success("操作范围参数有误");
//                } else if(vo.getOperationScope() == BatchOperationScope.NO_VIP) {
//                    return AjaxResult.success("操作范围参数有误");
                } else if (vo.getOperationScope() == BatchOperationScope.USED) {
                    sCard.setIsCharged(UserConstants.YES);
                } else if (vo.getOperationScope() == BatchOperationScope.UNUSED) {
                    sCard.setIsCharged(UserConstants.NO);
                } else if (vo.getOperationScope() == BatchOperationScope.BANNED) {
                    sCard.setStatus(UserConstants.USER_DISABLE);
                } else if (vo.getOperationScope() == BatchOperationScope.UNBANNED) {
                    sCard.setStatus(UserConstants.NORMAL);
                }
                List<SysCard> sysCardList = sysCardService.selectSysCardList(sCard);
                for (SysCard card : sysCardList) {
                    String item = card.getCardNo();
                    if (vo.getOperationType() == BatchOperationType.QUERY) {
                        String a = "正常的卡密";
                        String b = "冻结的卡密";
                        String c = "已使用的卡密";
                        String d = "不存在的卡密";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                            result.put(c, new ArrayList<>());
                            result.put(d, new ArrayList<>());
                        }
                        if (Objects.equals(card.getIsCharged(), UserConstants.YES)) {
                            result.get(c).add(item + getCardSimpleDesc(card));
                        } else if (Objects.equals(card.getStatus(), UserConstants.NORMAL)) {
                            result.get(a).add(item + getCardSimpleDesc(card));
                        } else {
                            result.get(b).add(item + getCardSimpleDesc(card));
                        }
                    } else if (vo.getOperationType() == BatchOperationType.BAN) {
                        String a = "冻结成功";
                        String b = "冻结失败";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                        }
                        if (Objects.equals(card.getStatus(), UserConstants.NORMAL)) {
                            try {
                                SysCard newCard = new SysCard();
                                newCard.setCardId(card.getCardId());
                                newCard.setStatus(UserConstants.USER_DISABLE);
                                sysCardService.updateSysCard(newCard);
                                result.get(a).add(item + "【冻结成功】");
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
                        } else {
                            result.get(a).add(item + "【已经是冻结状态】");
                        }
                    } else if (vo.getOperationType() == BatchOperationType.UNBAN) {
                        String a = "解冻成功";
                        String b = "解冻失败";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                        }
                        if (Objects.equals(card.getStatus(), UserConstants.NORMAL)) {
                            result.get(a).add(item + "【已经是正常状态】");
                        } else {
                            try {
                                SysCard newCard = new SysCard();
                                newCard.setCardId(card.getCardId());
                                newCard.setStatus(UserConstants.NORMAL);
                                sysCardService.updateSysCard(newCard);
                                result.get(a).add(item + "【解冻成功】");
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
                        }
                    } else if (vo.getOperationType() == BatchOperationType.UNBIND) {
                        return AjaxResult.success("卡密无解绑操作");
                    } else if (vo.getOperationType() == BatchOperationType.DELETE) {
                        String a = "删除成功";
                        String b = "删除失败";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                        }
                        try {
                            sysCardService.deleteSysCardByCardId(card.getCardId());
                            result.get(a).add(item + "【删除成功】");
                        } catch (Exception e) {
                            result.get(b).add(item + "【" + e.getMessage() + "】");
                        }
                    } else if (vo.getOperationType() == BatchOperationType.ADD_TIME) {
                        return AjaxResult.success("卡密无加时操作");
                    } else if (vo.getOperationType() == BatchOperationType.SUB_TIME) {
                        return AjaxResult.success("卡密无扣时操作");
                    } else if (vo.getOperationType() == BatchOperationType.ADD_POINT) {
                        return AjaxResult.success("卡密无加点操作");
                    } else if (vo.getOperationType() == BatchOperationType.SUB_POINT) {
                        return AjaxResult.success("卡密无扣点操作");
                    } else if (vo.getOperationType() == BatchOperationType.REMARK) {
                        if (StringUtils.isBlank(vo.getOperationValue())) {
                            return AjaxResult.success("参数：备注内容，不能为空");
                        }
                        String a = "备注成功";
                        String b = "备注失败";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                        }
                        try {
                            SysCard newCard = new SysCard();
                            newCard.setCardId(card.getCardId());
                            newCard.setRemark(vo.getOperationValue());
                            sysCardService.updateSysCard(newCard);
                            result.get(a).add(item + "【备注成功】");
                        } catch (Exception e) {
                            result.get(b).add(item + "【" + e.getMessage() + "】");
                        }
                    } else {
                        return AjaxResult.success("执行操作参数有误");
                    }
                }
            } else if (sysApp.getAuthType() == AuthType.LOGIN_CODE) {
                SysLoginCode sCode = new SysLoginCode();
                sCode.setAppId(sysApp.getAppId());
                if (vo.getTemplateId() != 0) {
                    sCode.setTemplateId(vo.getTemplateId());
                }
                if (vo.getOperationScope() == BatchOperationScope.ALL) {
                    // ignore
//                } else if(vo.getOperationScope() == BatchOperationScope.VIP) {
//                    return AjaxResult.success("操作范围参数有误");
//                } else if(vo.getOperationScope() == BatchOperationScope.NO_VIP) {
//                    return AjaxResult.success("操作范围参数有误");
                } else if (vo.getOperationScope() == BatchOperationScope.USED) {
                    sCode.setIsCharged(UserConstants.YES);
                } else if (vo.getOperationScope() == BatchOperationScope.UNUSED) {
                    sCode.setIsCharged(UserConstants.NO);
                } else if (vo.getOperationScope() == BatchOperationScope.BANNED) {
                    sCode.setStatus(UserConstants.USER_DISABLE);
                } else if (vo.getOperationScope() == BatchOperationScope.UNBANNED) {
                    sCode.setStatus(UserConstants.NORMAL);
                }
                List<SysLoginCode> sysLoginCodeList = sysLoginCodeService.selectSysLoginCodeList(sCode);
                for (SysLoginCode loginCode : sysLoginCodeList) {
                    String item = loginCode.getCardNo();
                    if (vo.getOperationType() == BatchOperationType.QUERY) {
                        String a = "正常的单码";
                        String b = "冻结的单码";
                        String c = "已使用的单码";
                        String d = "不存在的单码";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                            result.put(c, new ArrayList<>());
                            result.put(d, new ArrayList<>());
                        }
                        if (Objects.equals(loginCode.getIsCharged(), UserConstants.YES)) {
                            result.get(c).add(item + getLoginCodeSimpleDesc(loginCode));
                        } else if (Objects.equals(loginCode.getStatus(), UserConstants.NORMAL)) {
                            result.get(a).add(item + getLoginCodeSimpleDesc(loginCode));
                        } else {
                            result.get(b).add(item + getLoginCodeSimpleDesc(loginCode));
                        }
                    } else if (vo.getOperationType() == BatchOperationType.BAN) {
                        String a = "冻结成功";
                        String b = "冻结失败";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                        }
                        if (Objects.equals(loginCode.getStatus(), UserConstants.NORMAL)) {
                            try {
                                SysLoginCode newLoginCode = new SysLoginCode();
                                newLoginCode.setCardId(loginCode.getCardId());
                                newLoginCode.setStatus(UserConstants.USER_DISABLE);
                                sysLoginCodeService.updateSysLoginCode(newLoginCode);
                                result.get(a).add(item + "【冻结成功】");
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
                        } else {
                            result.get(a).add(item + "【已经是冻结状态】");
                        }
                    } else if (vo.getOperationType() == BatchOperationType.UNBAN) {
                        String a = "解冻成功";
                        String b = "解冻失败";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                        }
                        if (Objects.equals(loginCode.getStatus(), UserConstants.NORMAL)) {
                            result.get(a).add(item + "【已经是正常状态】");
                        } else {
                            try {
                                SysLoginCode newLoginCode = new SysLoginCode();
                                newLoginCode.setCardId(loginCode.getCardId());
                                newLoginCode.setStatus(UserConstants.NORMAL);
                                sysLoginCodeService.updateSysLoginCode(newLoginCode);
                                result.get(a).add(item + "【解冻成功】");
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
                        }
                    } else if (vo.getOperationType() == BatchOperationType.UNBIND) {
                        return AjaxResult.success("单码无解绑操作");
                    } else if (vo.getOperationType() == BatchOperationType.DELETE) {
                        String a = "删除成功";
                        String b = "删除失败";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                        }
                        try {
                            sysLoginCodeService.deleteSysLoginCodeByCardId(loginCode.getCardId());
                            result.get(a).add(item + "【删除成功】");
                        } catch (Exception e) {
                            result.get(b).add(item + "【" + e.getMessage() + "】");
                        }
                    } else if (vo.getOperationType() == BatchOperationType.ADD_TIME) {
                        return AjaxResult.success("卡密无加时操作");
                    } else if (vo.getOperationType() == BatchOperationType.SUB_TIME) {
                        return AjaxResult.success("卡密无扣时操作");
                    } else if (vo.getOperationType() == BatchOperationType.ADD_POINT) {
                        return AjaxResult.success("卡密无加点操作");
                    } else if (vo.getOperationType() == BatchOperationType.SUB_POINT) {
                        return AjaxResult.success("卡密无扣点操作");
                    } else if (vo.getOperationType() == BatchOperationType.REMARK) {
                        if (StringUtils.isBlank(vo.getOperationValue())) {
                            return AjaxResult.success("参数：备注内容，不能为空");
                        }
                        String a = "备注成功";
                        String b = "备注失败";
                        if (result.size() == 0) {
                            result.put(a, new ArrayList<>());
                            result.put(b, new ArrayList<>());
                        }
                        try {
                            SysLoginCode newLoginCode = new SysLoginCode();
                            newLoginCode.setCardId(loginCode.getCardId());
                            newLoginCode.setRemark(vo.getOperationValue());
                            sysLoginCodeService.updateSysLoginCode(newLoginCode);
                            result.get(a).add(item + "【备注成功】");
                        } catch (Exception e) {
                            result.get(b).add(item + "【" + e.getMessage() + "】");
                        }
                    } else {
                        return AjaxResult.success("执行操作参数有误");
                    }
                }
            }
        } else if (vo.getOperationObject() == BatchOperationObject.APP_USER) {
            SysAppUser sAppUser = new SysAppUser();
            sAppUser.setAppId(sysApp.getAppId());
            if (vo.getTemplateId() != 0) {
                sAppUser.setLastChargeTemplateId(vo.getTemplateId());
            }
            if (vo.getOperationScope() == BatchOperationScope.ALL) {
                // ignore
//            } else if(vo.getOperationScope() == BatchOperationScope.VIP) {
//                HashMap<String, Object> map = new HashMap<>();
//                map.put("isVip", UserConstants.YES);
//                sAppUser.setParams(map);
//            } else if(vo.getOperationScope() == BatchOperationScope.NO_VIP) {
//                HashMap<String, Object> map = new HashMap<>();
//                map.put("isVip", UserConstants.NO);
//                sAppUser.setParams(map);
            } else if (vo.getOperationScope() == BatchOperationScope.USED) {
                return AjaxResult.success("操作范围参数有误");
            } else if (vo.getOperationScope() == BatchOperationScope.UNUSED) {
                return AjaxResult.success("操作范围参数有误");
            } else if (vo.getOperationScope() == BatchOperationScope.BANNED) {
                sAppUser.setStatus(UserConstants.USER_DISABLE);
            } else if (vo.getOperationScope() == BatchOperationScope.UNBANNED) {
                sAppUser.setStatus(UserConstants.NORMAL);
            }
            if (vo.getOperationAppUserType() == null) {
                return AjaxResult.success("VIP范围参数有误");
            }
            if (vo.getOperationAppUserType() == BatchOperationAppUserType.ALL) {
                // ignore
            } else if (vo.getOperationAppUserType() == BatchOperationAppUserType.VIP) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("isVip", UserConstants.YES);
                sAppUser.setParams(map);
            } else if (vo.getOperationAppUserType() == BatchOperationAppUserType.NO_VIP) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("isVip", UserConstants.NO);
                sAppUser.setParams(map);
            }
            List<SysAppUser> sysAppUserList = sysAppUserService.selectSysAppUserList(sAppUser);
            for (SysAppUser appUser : sysAppUserList) {
                String item = "";
                if (sysApp.getAuthType() == AuthType.ACCOUNT) {
                    item = appUser.getUser().getUserName();
                } else if (sysApp.getAuthType() == AuthType.LOGIN_CODE) {
                    item = appUser.getLoginCode();
                }
                if (vo.getOperationType() == BatchOperationType.QUERY) {
                    String a = "正常的用户";
                    String b = "冻结的用户";
                    String d = "不存在的用户";
                    if (result.size() == 0) {
                        result.put(a, new ArrayList<>());
                        result.put(b, new ArrayList<>());
                        result.put(d, new ArrayList<>());
                    }
                    if (Objects.equals(appUser.getStatus(), UserConstants.NORMAL)) {
                        result.get(a).add(item + getAppUserSimpleDesc(appUser));
                    } else {
                        result.get(b).add(item + getAppUserSimpleDesc(appUser));
                    }
                } else if (vo.getOperationType() == BatchOperationType.BAN) {
                    String a = "冻结成功";
                    String b = "冻结失败";
                    if (result.size() == 0) {
                        result.put(a, new ArrayList<>());
                        result.put(b, new ArrayList<>());
                    }
                    if (appUser == null) {
                        result.get(b).add(item + "【软件用户不存在】");
                    } else if (Objects.equals(appUser.getStatus(), UserConstants.NORMAL)) {
                        try {
                            SysAppUser newAppUser = new SysAppUser();
                            newAppUser.setAppUserId(appUser.getAppUserId());
                            newAppUser.setStatus(UserConstants.USER_DISABLE);
                            sysAppUserService.updateSysAppUser(newAppUser);
                            result.get(a).add(item + "【冻结成功】");
                        } catch (Exception e) {
                            result.get(b).add(item + "【" + e.getMessage() + "】");
                        }
                    } else {
                        result.get(a).add(item + "【已经是冻结状态】");
                    }
                } else if (vo.getOperationType() == BatchOperationType.UNBAN) {
                    String a = "解冻成功";
                    String b = "解冻失败";
                    if (result.size() == 0) {
                        result.put(a, new ArrayList<>());
                        result.put(b, new ArrayList<>());
                    }
                    if (appUser == null) {
                        result.get(b).add(item + "【软件用户不存在】");
                    } else if (Objects.equals(appUser.getStatus(), UserConstants.NORMAL)) {
                        result.get(a).add(item + "【已经是正常状态】");
                    } else {
                        try {
                            SysAppUser newAppUser = new SysAppUser();
                            newAppUser.setAppUserId(appUser.getAppUserId());
                            newAppUser.setStatus(UserConstants.NORMAL);
                            sysAppUserService.updateSysAppUser(newAppUser);
                            result.get(a).add(item + "【解冻成功】");
                        } catch (Exception e) {
                            result.get(b).add(item + "【" + e.getMessage() + "】");
                        }
                    }
                } else if (vo.getOperationType() == BatchOperationType.UNBIND) {
                    doAppUserUnbind(result, item, appUser);
                } else if (vo.getOperationType() == BatchOperationType.DELETE) {
                    String a = "删除成功";
                    String b = "删除失败";
                    if (result.size() == 0) {
                        result.put(a, new ArrayList<>());
                        result.put(b, new ArrayList<>());
                    }
                    if (appUser == null) {
                        result.get(b).add(item + "【软件用户不存在】");
                    } else {
                        try {
                            sysAppUserService.deleteSysAppUserByAppUserId(appUser.getAppUserId());
                            result.get(a).add(item + "【删除成功】");
                        } catch (Exception e) {
                            result.get(b).add(item + "【" + e.getMessage() + "】");
                        }
                    }
                } else if (vo.getOperationType() == BatchOperationType.ADD_TIME) {
                    if (vo.getOperationValue() == null || Convert.toLong(vo.getOperationValue(), 0L) <= 0) {
                        return AjaxResult.success("参数必须为整数且大于0");
                    }
                    doAppUserAddTime(result, item, appUser, vo);
                } else if (vo.getOperationType() == BatchOperationType.SUB_TIME) {
                    if (vo.getOperationValue() == null || Convert.toLong(vo.getOperationValue(), 0L) <= 0) {
                        return AjaxResult.success("参数必须为整数且大于0");
                    }
                    doAppUserSubTime(result, item, appUser, vo);
                } else if (vo.getOperationType() == BatchOperationType.ADD_POINT) {
                    if (vo.getOperationValue() == null || Convert.toLong(vo.getOperationValue(), 0L) <= 0) {
                        return AjaxResult.success("参数必须为整数且大于0");
                    }
                    doAppUserAddPoint(result, item, appUser, vo);
                } else if (vo.getOperationType() == BatchOperationType.SUB_POINT) {
                    if (vo.getOperationValue() == null || Convert.toLong(vo.getOperationValue(), 0L) <= 0) {
                        return AjaxResult.success("参数必须为整数且大于0");
                    }
                    doAppUserSubPoint(result, item, appUser, vo);
                } else if (vo.getOperationType() == BatchOperationType.REMARK) {
                    if (StringUtils.isBlank(vo.getOperationValue())) {
                        return AjaxResult.success("参数：备注内容，不能为空");
                    }
                    String a = "备注成功";
                    String b = "备注失败";
                    if (result.size() == 0) {
                        result.put(a, new ArrayList<>());
                        result.put(b, new ArrayList<>());
                    }
                    if (appUser == null) {
                        result.get(b).add(item + "【软件用户不存在】");
                    } else {
                        try {
                            SysAppUser newAppUser = new SysAppUser();
                            newAppUser.setAppUserId(appUser.getAppUserId());
                            newAppUser.setRemark(vo.getOperationValue());
                            sysAppUserService.updateSysAppUser(newAppUser);
                            result.get(a).add(item + "【备注成功】");
                        } catch (Exception e) {
                            result.get(b).add(item + "【" + e.getMessage() + "】");
                        }
                    }
                } else {
                    return AjaxResult.success("执行操作参数有误");
                }
            }
        } else {
            return AjaxResult.success("操作对象参数有误");
        }
        StringBuilder resultStr = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : result.entrySet()) {
            resultStr.append("--------------------------【")
                    .append(entry.getKey()).append("】").append(entry.getValue().size())
                    .append("个--------------------------").append("\n\n")
                    .append(String.join("\n", entry.getValue()))
                    .append("\n\n");
        }
        return AjaxResult.success(resultStr.toString());
    }

    private void doAppUserSubPoint(LinkedHashMap<String, List<String>> result, String item, SysAppUser appUser, BatchOperationVo vo) {
        String a = "扣点成功";
        String b = "扣点失败";
        if (result.size() == 0) {
            result.put(a, new ArrayList<>());
            result.put(b, new ArrayList<>());
        }
        if (appUser == null) {
            result.get(b).add(item + "【软件用户不存在】");
        } else {
            try {
                if (appUser.getApp().getBillType() == BillType.POINT) {
                    SysAppUserExpireLog expireLog = new SysAppUserExpireLog();
                    expireLog.setPointBefore(appUser.getPoint());
                    BigDecimal newPoint = MyUtils.getNewPointSub(appUser.getPoint(), Long.parseLong(vo.getOperationValue()));
                    appUser.setPoint(newPoint);
                    SysAppUser newAppUser = new SysAppUser();
                    newAppUser.setAppUserId(appUser.getAppUserId());
                    newAppUser.setPoint(newPoint);
                    sysAppUserService.updateSysAppUser(newAppUser);
                    result.get(a).add(item + getAppUserSimpleDesc(appUser));
                    expireLog.setPointAfter(newPoint);
                    // 记录用户时长变更日志
                    expireLog.setAppUserId(appUser.getAppUserId());
                    expireLog.setTemplateId(null);
                    expireLog.setCardId(null);
                    expireLog.setChangeDesc("批量扣点");
                    expireLog.setChangeType(AppUserExpireChangeType.ADMIN_UPDATE);
                    expireLog.setChangeAmount(-Long.parseLong(vo.getOperationValue()));
                    expireLog.setCardNo(null);
                    expireLog.setAppId(appUser.getAppId());
                    AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog));
                } else {
                    result.get(b).add(item + "【非计点模式软件用户】");
                }
            } catch (Exception e) {
                result.get(b).add(item + "【" + e.getMessage() + "】");
            }
        }
    }

    private void doAppUserAddPoint(LinkedHashMap<String, List<String>> result, String item, SysAppUser appUser, BatchOperationVo vo) {
        String a = "加点成功";
        String b = "加点失败";
        if (result.size() == 0) {
            result.put(a, new ArrayList<>());
            result.put(b, new ArrayList<>());
        }
        if (appUser == null) {
            result.get(b).add(item + "【软件用户不存在】");
        } else {
            try {
                if (appUser.getApp().getBillType() == BillType.POINT) {
                    SysAppUserExpireLog expireLog = new SysAppUserExpireLog();
                    expireLog.setPointBefore(appUser.getPoint());
                    BigDecimal newPoint = MyUtils.getNewPointAdd(appUser.getPoint(), Long.parseLong(vo.getOperationValue()));
                    appUser.setPoint(newPoint);
                    SysAppUser newAppUser = new SysAppUser();
                    newAppUser.setAppUserId(appUser.getAppUserId());
                    newAppUser.setPoint(newPoint);
                    sysAppUserService.updateSysAppUser(newAppUser);
                    result.get(a).add(item + getAppUserSimpleDesc(appUser));
                    expireLog.setPointAfter(newPoint);
                    // 记录用户时长变更日志
                    expireLog.setAppUserId(appUser.getAppUserId());
                    expireLog.setTemplateId(null);
                    expireLog.setCardId(null);
                    expireLog.setChangeDesc("批量加点");
                    expireLog.setChangeType(AppUserExpireChangeType.ADMIN_UPDATE);
                    expireLog.setChangeAmount(Long.parseLong(vo.getOperationValue()));
                    expireLog.setCardNo(null);
                    expireLog.setAppId(appUser.getAppId());
                    AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog));
                } else {
                    result.get(b).add(item + "【非计点模式软件用户】");
                }
            } catch (Exception e) {
                result.get(b).add(item + "【" + e.getMessage() + "】");
            }
        }
    }

    private void doAppUserSubTime(LinkedHashMap<String, List<String>> result, String item, SysAppUser appUser, BatchOperationVo vo) {
        String a = "扣时成功";
        String b = "扣时失败";
        if (result.size() == 0) {
            result.put(a, new ArrayList<>());
            result.put(b, new ArrayList<>());
        }
        if (appUser == null) {
            result.get(b).add(item + "【软件用户不存在】");
        } else {
            try {
                if (appUser.getApp().getBillType() == BillType.TIME) {
                    SysAppUserExpireLog expireLog = new SysAppUserExpireLog();
                    expireLog.setExpireTimeBefore(appUser.getExpireTime());
                    Date newExpiredTime = MyUtils.getNewExpiredTimeSub(appUser.getExpireTime(), Long.parseLong(vo.getOperationValue()));
                    appUser.setExpireTime(newExpiredTime);
                    SysAppUser newAppUser = new SysAppUser();
                    newAppUser.setAppUserId(appUser.getAppUserId());
                    newAppUser.setExpireTime(newExpiredTime);
                    sysAppUserService.updateSysAppUser(newAppUser);
                    result.get(a).add(item + getAppUserSimpleDesc(appUser));
                    expireLog.setExpireTimeAfter(newExpiredTime);
                    // 记录用户时长变更日志
                    expireLog.setAppUserId(appUser.getAppUserId());
                    expireLog.setTemplateId(null);
                    expireLog.setCardId(null);
                    expireLog.setChangeDesc("批量扣时");
                    expireLog.setChangeType(AppUserExpireChangeType.ADMIN_UPDATE);
                    expireLog.setChangeAmount(-Long.parseLong(vo.getOperationValue()));
                    expireLog.setCardNo(null);
                    expireLog.setAppId(appUser.getAppId());
                    AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog));
                } else {
                    result.get(b).add(item + "【非计时模式软件用户】");
                }
            } catch (Exception e) {
                result.get(b).add(item + "【" + e.getMessage() + "】");
            }
        }
    }

    private void doAppUserAddTime(LinkedHashMap<String, List<String>> result, String item, SysAppUser appUser, BatchOperationVo vo) {
        String a = "加时成功";
        String b = "加时失败";
        if (result.size() == 0) {
            result.put(a, new ArrayList<>());
            result.put(b, new ArrayList<>());
        }
        if (appUser == null) {
            result.get(b).add(item + "【软件用户不存在】");
        } else {
            try {
                if (appUser.getApp().getBillType() == BillType.TIME) {
                    SysAppUserExpireLog expireLog = new SysAppUserExpireLog();
                    expireLog.setExpireTimeBefore(appUser.getExpireTime());
                    Date newExpiredTime = MyUtils.getNewExpiredTimeAdd(appUser.getExpireTime(), Long.parseLong(vo.getOperationValue()));
                    appUser.setExpireTime(newExpiredTime);
                    SysAppUser newAppUser = new SysAppUser();
                    newAppUser.setAppUserId(appUser.getAppUserId());
                    newAppUser.setExpireTime(newExpiredTime);
                    sysAppUserService.updateSysAppUser(newAppUser);
                    result.get(a).add(item + getAppUserSimpleDesc(appUser));
                    expireLog.setExpireTimeAfter(newExpiredTime);
                    // 记录用户时长变更日志
                    expireLog.setAppUserId(appUser.getAppUserId());
                    expireLog.setTemplateId(null);
                    expireLog.setCardId(null);
                    expireLog.setChangeDesc("批量加时");
                    expireLog.setChangeType(AppUserExpireChangeType.ADMIN_UPDATE);
                    expireLog.setChangeAmount(Long.parseLong(vo.getOperationValue()));
                    expireLog.setCardNo(null);
                    expireLog.setAppId(appUser.getAppId());
                    AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog));
                } else {
                    result.get(b).add(item + "【非计时模式软件用户】");
                }
            } catch (Exception e) {
                result.get(b).add(item + "【" + e.getMessage() + "】");
            }
        }
    }

    private void doAppUserUnbind(LinkedHashMap<String, List<String>> result, String item, SysAppUser appUser) {
        String a = "解绑成功";
        String b = "解绑失败";
        if (result.size() == 0) {
            result.put(a, new ArrayList<>());
            result.put(b, new ArrayList<>());
        }
        if (appUser == null) {
            result.get(b).add(item + "【软件用户不存在】");
        } else {
            List<SysAppUserDeviceCode> deviceCodeList =
                    sysAppUserDeviceCodeService.selectSysAppUserDeviceCodeByAppUserId(appUser.getAppUserId());
            if (deviceCodeList.size() == 0) {
                result.get(a).add(item + "【未绑定任何设备】");
            } else {
                try {
                    for (SysAppUserDeviceCode item1 : deviceCodeList) {
                        sysAppUserDeviceCodeService.deleteSysAppUserDeviceCodeById(item1.getId());
                    }
                    result.get(a).add(item + "【解绑成功】");
                } catch (Exception e) {
                    result.get(b).add(item + "【" + e.getMessage() + "】");
                }
            }
        }
    }

    private String getCardSimpleDesc(SysCard card) {
        return "【名称：" + card.getCardName() +
                " 制卡人：" + card.getCreateBy() +
                " 制卡时间：" + DateUtils.parseDateToStr(card.getCreateTime()) +
                " 备注：" + card.getRemark() + "】";
    }

    private String getLoginCodeSimpleDesc(SysLoginCode loginCode) {
        return "【名称：" + loginCode.getCardName() +
                " 制卡人：" + loginCode.getCreateBy() +
                " 制卡时间：" + DateUtils.parseDateToStr(loginCode.getCreateTime()) +
                " 备注：" + loginCode.getRemark() + "】";
    }

    private String getAppUserSimpleDesc(SysAppUser appUser) {
        return "【"
                + (UserConstants.NORMAL.equals(appUser.getStatus()) ? "正常的用户" : "冻结的用户")
                + " " + (appUser.getApp().getBillType() == BillType.TIME ?
                "到期时间：" + DateUtils.parseDateToStr(appUser.getExpireTime()) : "剩余点数：" + appUser.getPoint()) +
                " 自定义参数：" + appUser.getCardCustomParams() +
                " 所属代理：" + (appUser.getAgentUser() != null ? appUser.getAgentUser().getNickName() + "(" + appUser.getAgentUser().getUserName() + ")" : "null") +
                " 备注：" + appUser.getRemark() + "】";
    }

}
