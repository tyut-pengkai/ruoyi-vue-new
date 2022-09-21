package com.ruoyi.web.controller.system;

import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysAppUserDeviceCode;
import com.ruoyi.common.enums.BatchOperationObject;
import com.ruoyi.common.enums.BatchOperationType;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.domain.vo.BatchOperationVo;
import com.ruoyi.system.service.ISysAppUserDeviceCodeService;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysCardService;
import com.ruoyi.system.service.ISysLoginCodeService;
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
    private ISysAppUserDeviceCodeService sysAppUserDeviceCodeService;

    @GetMapping("/operation")
    public AjaxResult batchOperation(BatchOperationVo vo) {
        String[] split = vo.getContent().split("\n");
        Set<String> set = new HashSet<>(Arrays.asList(split));
        if (set.size() == 0) {
            throw new ServiceException("请输入有效内容");
        }
        List<String> list = set.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        LinkedHashMap<String, List<String>> result = new LinkedHashMap<>();

        for (String item : list) {
            if (vo.getOperationObject() == BatchOperationObject.CARD) {
                SysCard search = new SysCard();
                search.setAppId(vo.getAppId());
                search.setCardNo(item);
                List<SysCard> cardList = sysCardService.selectSysCardList(search);
                if (vo.getOperationType() == BatchOperationType.QUERY) {
                    String a = "正常的卡密";
                    String b = "冻结的卡密";
                    String c = "已使用的卡密";
                    String d = "不存在的卡密";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    result.put(c, new ArrayList<>());
                    result.put(d, new ArrayList<>());
                    for (SysCard card : cardList) {
                        if (card == null) {
                            result.get(d).add(item + "【卡密不存在】");
                        } else if (Objects.equals(card.getIsCharged(), UserConstants.YES)) {
                            result.get(c).add(item + getCardSimpleDesc(card));
                        } else if (Objects.equals(card.getStatus(), UserConstants.NORMAL)) {
                            result.get(a).add(item + getCardSimpleDesc(card));
                        } else {
                            result.get(b).add(item + getCardSimpleDesc(card));
                        }
                    }
                } else if (vo.getOperationType() == BatchOperationType.BAN) {
                    String a = "冻结成功";
                    String b = "冻结失败";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    for (SysCard card : cardList) {
                        if (card == null) {
                            result.get(b).add(item + "【卡密不存在】");
                        } else if (Objects.equals(card.getIsCharged(), UserConstants.YES)) {
                            result.get(b).add(item + "【卡密已使用】");
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
                    }
                } else if (vo.getOperationType() == BatchOperationType.UNBAN) {
                    String a = "解冻成功";
                    String b = "解冻失败";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    for (SysCard card : cardList) {
                        if (card == null) {
                            result.get(b).add(item + "【卡密不存在】");
                        } else if (Objects.equals(card.getIsCharged(), UserConstants.YES)) {
                            result.get(b).add(item + "【卡密已使用】");
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
                    }
                } else if (vo.getOperationType() == BatchOperationType.UNBIND) {
                    return AjaxResult.success("卡密无解绑操作");
                } else if (vo.getOperationType() == BatchOperationType.DELETE) {
                    String a = "删除成功";
                    String b = "删除失败";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    for (SysCard card : cardList) {
                        if (card == null) {
                            result.get(b).add(item + "【卡密不存在】");
                        } else if (Objects.equals(card.getIsCharged(), UserConstants.YES)) {
                            result.get(b).add(item + "【卡密已使用】");
                        } else {
                            try {
                                sysCardService.deleteSysCardByCardId(card.getCardId());
                                result.get(a).add(item + "【删除成功】");
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
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
                } else {
                    return AjaxResult.error("执行操作参数有误");
                }
            } else if (vo.getOperationObject() == BatchOperationObject.LOGIN_CODE) {
                SysLoginCode search = new SysLoginCode();
                search.setAppId(vo.getAppId());
                search.setCardNo(item);
                List<SysLoginCode> loginCodeList = sysLoginCodeService.selectSysLoginCodeList(search);
                if (vo.getOperationType() == BatchOperationType.QUERY) {
                    String a = "正常的单码";
                    String b = "冻结的单码";
                    String c = "已使用的单码";
                    String d = "不存在的单码";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    result.put(c, new ArrayList<>());
                    result.put(d, new ArrayList<>());
                    for (SysLoginCode loginCode : loginCodeList) {
                        if (loginCode == null) {
                            result.get(d).add(item + "【单码不存在】");
                        } else if (Objects.equals(loginCode.getIsCharged(), UserConstants.YES)) {
                            result.get(c).add(item + getLoginCodeSimpleDesc(loginCode));
                        } else if (Objects.equals(loginCode.getStatus(), UserConstants.NORMAL)) {
                            result.get(a).add(item + getLoginCodeSimpleDesc(loginCode));
                        } else {
                            result.get(b).add(item + getLoginCodeSimpleDesc(loginCode));
                        }
                    }
                } else if (vo.getOperationType() == BatchOperationType.BAN) {
                    String a = "冻结成功";
                    String b = "冻结失败";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    for (SysLoginCode loginCode : loginCodeList) {
                        if (loginCode == null) {
                            result.get(b).add(item + "【单码不存在】");
                        } else if (Objects.equals(loginCode.getIsCharged(), UserConstants.YES)) {
                            result.get(b).add(item + "【单码已使用】");
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
                    }
                } else if (vo.getOperationType() == BatchOperationType.UNBAN) {
                    String a = "解冻成功";
                    String b = "解冻失败";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    for (SysLoginCode loginCode : loginCodeList) {
                        if (loginCode == null) {
                            result.get(b).add(item + "【单码不存在】");
                        } else if (Objects.equals(loginCode.getIsCharged(), UserConstants.YES)) {
                            result.get(b).add(item + "【单码已使用】");
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
                    }
                } else if (vo.getOperationType() == BatchOperationType.UNBIND) {
                    return AjaxResult.success("单码无解绑操作");
                } else if (vo.getOperationType() == BatchOperationType.DELETE) {
                    String a = "删除成功";
                    String b = "删除失败";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    for (SysLoginCode loginCode : loginCodeList) {
                        if (loginCode == null) {
                            result.get(b).add(item + "【单码不存在】");
                        } else if (Objects.equals(loginCode.getIsCharged(), UserConstants.YES)) {
                            result.get(b).add(item + "【单码已使用】");
                        } else {
                            try {
                                sysLoginCodeService.deleteSysLoginCodeByCardId(loginCode.getCardId());
                                result.get(a).add(item + "【删除成功】");
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
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
                } else {
                    return AjaxResult.error("执行操作参数有误");
                }
            } else if (vo.getOperationObject() == BatchOperationObject.ACCOUNT_USER
                    || vo.getOperationObject() == BatchOperationObject.LOGIN_CODE_USER) {
                SysAppUser search = new SysAppUser();
                search.setAppId(vo.getAppId());
                if (vo.getOperationObject() == BatchOperationObject.ACCOUNT_USER) {
                    search.setUserName(item);
                } else if (vo.getOperationObject() == BatchOperationObject.LOGIN_CODE_USER) {
                    search.setLoginCode(item);
                }
                List<SysAppUser> appUserList = sysAppUserService.selectSysAppUserList(search);
                if (vo.getOperationType() == BatchOperationType.QUERY) {
                    String a = "正常的用户";
                    String b = "冻结的用户";
                    String d = "不存在的用户";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    result.put(d, new ArrayList<>());
                    for (SysAppUser appUser : appUserList) {
                        if (appUser == null) {
                            result.get(d).add(item + "【软件用户不存在】");
                        } else if (Objects.equals(appUser.getStatus(), UserConstants.NORMAL)) {
                            result.get(a).add(item + getAppUserSimpleDesc(appUser));
                        } else {
                            result.get(b).add(item + getAppUserSimpleDesc(appUser));
                        }
                    }
                } else if (vo.getOperationType() == BatchOperationType.BAN) {
                    String a = "冻结成功";
                    String b = "冻结失败";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    for (SysAppUser appUser : appUserList) {
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
                    }
                } else if (vo.getOperationType() == BatchOperationType.UNBAN) {
                    String a = "解冻成功";
                    String b = "解冻失败";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    for (SysAppUser appUser : appUserList) {
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
                    }
                } else if (vo.getOperationType() == BatchOperationType.UNBIND) {
                    String a = "解绑成功";
                    String b = "解绑失败";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    for (SysAppUser appUser : appUserList) {
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
                } else if (vo.getOperationType() == BatchOperationType.DELETE) {
                    String a = "删除成功";
                    String b = "删除失败";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    for (SysAppUser appUser : appUserList) {
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
                    }
                } else if (vo.getOperationType() == BatchOperationType.ADD_TIME) {
                    String a = "加时成功";
                    String b = "加时失败";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    for (SysAppUser appUser : appUserList) {
                        if (appUser == null) {
                            result.get(b).add(item + "【软件用户不存在】");
                        } else {
                            try {
                                if (appUser.getApp().getBillType() == BillType.TIME) {
                                    Date newExpiredTime = MyUtils.getNewExpiredTimeAdd(appUser.getExpireTime(), Long.parseLong(vo.getOperationValue()));
                                    appUser.setExpireTime(newExpiredTime);
                                    sysAppUserService.updateSysAppUser(appUser);
                                    result.get(a).add(item + getAppUserSimpleDesc(appUser));
                                } else {
                                    result.get(b).add(item + "【非计时模式软件用户】");
                                }
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
                        }
                    }
                } else if (vo.getOperationType() == BatchOperationType.SUB_TIME) {
                    String a = "扣时成功";
                    String b = "扣时失败";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    for (SysAppUser appUser : appUserList) {
                        if (appUser == null) {
                            result.get(b).add(item + "【软件用户不存在】");
                        } else {
                            try {
                                if (appUser.getApp().getBillType() == BillType.TIME) {
                                    Date newExpiredTime = MyUtils.getNewExpiredTimeSub(appUser.getExpireTime(), Long.parseLong(vo.getOperationValue()));
                                    appUser.setExpireTime(newExpiredTime);
                                    sysAppUserService.updateSysAppUser(appUser);
                                    result.get(a).add(item + getAppUserSimpleDesc(appUser));
                                } else {
                                    result.get(b).add(item + "【非计时模式软件用户】");
                                }
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
                        }
                    }
                } else if (vo.getOperationType() == BatchOperationType.ADD_POINT) {
                    String a = "加点成功";
                    String b = "加点失败";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    for (SysAppUser appUser : appUserList) {
                        if (appUser == null) {
                            result.get(b).add(item + "【软件用户不存在】");
                        } else {
                            try {
                                if (appUser.getApp().getBillType() == BillType.POINT) {
                                    BigDecimal newPoint = MyUtils.getNewPointAdd(appUser.getPoint(), Long.parseLong(vo.getOperationValue()));
                                    appUser.setPoint(newPoint);
                                    sysAppUserService.updateSysAppUser(appUser);
                                    result.get(a).add(item + getAppUserSimpleDesc(appUser));
                                } else {
                                    result.get(b).add(item + "【非计点模式软件用户】");
                                }
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
                        }
                    }
                } else if (vo.getOperationType() == BatchOperationType.SUB_POINT) {
                    String a = "扣点成功";
                    String b = "扣点失败";
                    result.put(a, new ArrayList<>());
                    result.put(b, new ArrayList<>());
                    for (SysAppUser appUser : appUserList) {
                        if (appUser == null) {
                            result.get(b).add(item + "【软件用户不存在】");
                        } else {
                            try {
                                if (appUser.getApp().getBillType() == BillType.POINT) {
                                    BigDecimal newPoint = MyUtils.getNewPointSub(appUser.getPoint(), Long.parseLong(vo.getOperationValue()));
                                    appUser.setPoint(newPoint);
                                    sysAppUserService.updateSysAppUser(appUser);
                                    result.get(a).add(item + getAppUserSimpleDesc(appUser));
                                } else {
                                    result.get(b).add(item + "【非计点模式软件用户】");
                                }
                            } catch (Exception e) {
                                result.get(b).add(item + "【" + e.getMessage() + "】");
                            }
                        }
                    }
                } else {
                    return AjaxResult.error("执行操作参数有误");
                }
            } else {
                return AjaxResult.error("操作对象参数有误");
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
                + (appUser.getApp().getBillType() == BillType.TIME ?
                "到期时间：" + DateUtils.parseDateToStr(appUser.getExpireTime()) : "剩余点数：" + appUser.getPoint()) +
                "附件自定义参数" + appUser.getCardCustomParams() +
                " 备注：" + appUser.getRemark() + "】";
    }


}
