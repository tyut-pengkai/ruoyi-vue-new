package com.coordsoft.hysdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.coordsoft.hysdk.encrypt.EncryptType;
import com.coordsoft.hysdk.utils.HttpUtil;
import com.coordsoft.hysdk.utils.HyUtils;
import com.coordsoft.hysdk.utils.RandomUtil;
import com.coordsoft.hysdk.vo.RequestResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.*;

@Slf4j
public class Hywlyz extends HyUtils {

    private String gToken = "";
    private String gApiUrl = "";
    private String gAppSecret = "";
    private String gVersionNo = "";
    private EncryptType gDataInEnc;
    private String gDataInPwd = "";
    private EncryptType gDataOutEnc;
    private String gDataOutPwd = "";
    private String gApiPwd = "";
    private boolean gLog = false;

    public String getSdkVer() {
        return "v1.7.0_20230903";
    }

    public void setShowLog(boolean showLog) {
        this.gLog = showLog;
    }

    private void showLogInfo(String str) {
        if (gLog) {
            log.info(str);
        }
    }

    private void showLogDebug(String str) {
        if (gLog) {
            log.debug(str);
        }
    }

    private void showLogError(String str) {
        if (gLog) {
            log.error(str);
        }
    }

    public void setToken(String token) {
        this.gToken = token;
    }

    public void init(String apiUrl, String appSecret, String versionNo, EncryptType dataInEnc, String dataInPwd, EncryptType dataOutEnc, String dataOutPwd, String apiPwd) {
        gApiUrl = apiUrl;
        gAppSecret = appSecret;
        gVersionNo = versionNo;
        gDataInEnc = dataInEnc;
        gDataInPwd = dataInPwd;
        gDataOutEnc = dataOutEnc;
        gDataOutPwd = dataOutPwd;
        gApiPwd = apiPwd;
    }

    public RequestResult requestServer(JSONObject data, boolean checkToken) {
        RequestResult requestResult = new RequestResult();
        try {
            String vstr = null;
            data.put("appSecret", gAppSecret);
            String api = data.getString("api");
            if (gApiPwd != null && !gApiPwd.equals("")) {
                String anonApi = calcAnonApi(api, gApiPwd);
                data.put("api", anonApi);
            }
            if (checkToken) {
                vstr = data.getString("vstr");
                if ("".equals(vstr)) {
                    vstr = RandomUtil.getNumLargeSmallLetter(8);
                    data.put("vstr", vstr);
                }
                String timestamp = data.getString("timestamp");
                if ("".equals(timestamp)) {
                    timestamp = String.valueOf(System.currentTimeMillis());
                    data.put("timestamp", timestamp);
                }
            }
            String sign = generalCalcSign(data, gAppSecret);
            showLogDebug("requestServer: " + replaceNewLine(data.toString()) + ", " + sign);

            data.put("sign", sign);

            @SuppressWarnings("unchecked")
            List ignoreList = new ArrayList(Arrays.asList(
                    "globalScript.ng",
                    "globalVariableGet.ng",
                    "globalVariableSet.ng",
                    "globalFileUpload.ng",
                    "globalFileDownload.ng"
            ));

            if (checkToken && (gToken == null || "".equals(gToken)) && !ignoreList.contains(api)) {
                showLogDebug("requestServer: token为空，请先调用【setToken】函数设置TOKEN");
                return requestResult;
            }

            Map<String, String> headers = new HashMap<>();
            headers.put("content-type", "application/json");
            if (checkToken) {
                headers.put("authorization", gToken);
            }

            String requestContent = null;
            try {
                requestContent = Hywlyz.generalDataEncrypt(Hywlyz.replaceNewLine(data.toString()), gDataInPwd, gDataInEnc);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            assert requestContent != null;
            try {
                String response = HttpUtil.post(gApiUrl, requestContent, headers);
                showLogDebug("requestServer: response：" + response);
                String string = Objects.requireNonNull(response);
                JSONObject jsonObject = JSON.parseObject(string);
                requestResult.setCode(jsonObject.getInteger("code"));
                requestResult.setMsg(jsonObject.getString("msg"));
                requestResult.setVstr(jsonObject.getString("vstr"));
                requestResult.setDetail(jsonObject.getString("detail"));
                String dataStr = jsonObject.getString("data");

                if (requestResult.getCode() == 200) {
                    if (HyUtils.verifySign(jsonObject, gAppSecret)) {
                        if (StringUtils.isBlank(vstr) || vstr.equals(requestResult.getVstr())) {
                            dataStr = Hywlyz.generalDataDecrypt(dataStr, gDataOutPwd, gDataOutEnc);
                            if ((JSON.isValidObject(dataStr) || JSON.isValidArray(dataStr)) && !api.equals("globalVariableGet.ng")) {
                                requestResult.setData(dataStr);
                            } else {
                                JSONObject jsonNew = new JSONObject();
                                jsonNew.put("data", dataStr);
                                requestResult.setData(Hywlyz.replaceNewLine(jsonNew.toString()));
                            }
                        } else {
                            requestResult.setCode(0);
                            requestResult.setMsg("vstr校验失败，该数据可能被人为篡改");
                            showLogDebug("requestServer: " + requestResult.getMsg());
                        }
                    } else {
                        requestResult.setCode(0);
                        requestResult.setMsg("sign校验失败，该数据可能被人为篡改");
                        showLogDebug("requestServer: " + requestResult.getMsg());
                    }
                }
                showLogDebug("requestServer: 请求API：" + api);
                showLogDebug("requestServer: 返回数据json解析成功[密文]：" + string);
                showLogDebug("requestServer: data结构[明文]：" + requestResult.getData());
                showLogDebug("requestServer: ======================");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestResult;
    }

    public RequestResult loginNu(String username, String password, String deviceCode, String md5) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "login.nu");
            postBody.put("appVer", gVersionNo);
            postBody.put("deviceCode", deviceCode);
            postBody.put("username", username);
            postBody.put("password", password);
            postBody.put("md5", md5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult logoutAllNu(String username, String password) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("username", username);
            postBody.put("password", password);
            postBody.put("api", "logoutAll.nu");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult rechargeCardNu(String username, String password, boolean validPassword, String cardNo, String cardPassword) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("username", username);
            postBody.put("password", password);
            postBody.put("validPassword", validPassword);
            postBody.put("cardNo", cardNo);
            postBody.put("cardPassword", cardPassword);
            postBody.put("api", "rechargeCard.nu");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult registerNu(String username, String password, String passwordRepeat) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("username", username);
            postBody.put("password", password);
            postBody.put("passwordRepeat", passwordRepeat);
            postBody.put("api", "register.nu");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult updatePasswordNu(String username, String password, String newPassword, String newPasswordRepeat) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("username", username);
            postBody.put("password", password);
            postBody.put("newPassword", newPassword);
            postBody.put("newPasswordRepeat", newPasswordRepeat);
            postBody.put("api", "updatePassword.nu");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult unbindDeviceNu(String deviceCode, String username, String password) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("deviceCode", deviceCode);
            postBody.put("username", username);
            postBody.put("password", password);
            postBody.put("api", "unbindDevice.nu");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult appInfoNg() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "appInfo.ng");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult appUserInfoAg() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "appUserInfo.ag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult heartbeatAg() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "heartbeat.ag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult isConnectedNg() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "isConnected.ng");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult isOnlineAg() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "isOnline.ag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult latestVersionInfoNg() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "latestVersionInfo.ng");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult versionInfoNg() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "versionInfo.ng");
            postBody.put("appVer", gVersionNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult latestVersionInfoForceUpdateNg() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "latestVersionInfoForceUpdate.ng");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult logoutAg() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "logout.ag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult serverTimeNg() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "serverTime.ng");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult unbindDeviceAg() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "unbindDevice.ag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult isUserNameExistNu(String username) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("username", username);
            postBody.put("api", "isUserNameExist.nu");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult userExpireTimeAg() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "userExpireTime.ag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult reduceTimeAg(int seconds, boolean enableNegative) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("seconds", seconds);
            postBody.put("enableNegative", enableNegative);
            postBody.put("api", "reduceTime.ag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult reducePointAg(int point, boolean enableNegative) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("point", point);
            postBody.put("enableNegative", enableNegative);
            postBody.put("api", "reducePoint.ag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult userPointAg() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "userPoint.ag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult loginNc(String loginCode, String deviceCode, String md5) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "login.nc");
            postBody.put("appVer", gVersionNo);
            postBody.put("deviceCode", deviceCode);
            postBody.put("loginCode", loginCode);
            postBody.put("md5", md5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult unbindDeviceNc(String deviceCode, String loginCode) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "unbindDevice.nc");
            postBody.put("deviceCode", deviceCode);
            postBody.put("loginCode", loginCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult globalScriptNg(String scriptKey, String scriptParams) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "globalScript.ng");
            postBody.put("scriptKey", scriptKey);
            postBody.put("scriptParams", scriptParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult globalVariableGetNg(String variableName, boolean errorIfNotExist) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "globalVariableGet.ng");
            postBody.put("variableName", variableName);
            postBody.put("errorIfNotExist", errorIfNotExist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult globalVariableSetNg(String variableName, String variableValue, boolean errorIfNotExist, boolean checkToken, boolean checkVip) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "globalVariableSet.ng");
            postBody.put("variableName", variableName);
            postBody.put("variableValue", variableValue);
            postBody.put("errorIfNotExist", errorIfNotExist);
            postBody.put("checkToken", checkToken);
            postBody.put("checkVip", checkVip);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult globalFileUploadNg(String fileName, String base64Str, boolean overrideIfExist, boolean checkToken, boolean checkVip) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "globalFileUpload.ng");
            postBody.put("fileName", fileName);
            postBody.put("base64Str", base64Str);
            postBody.put("overrideIfExist", overrideIfExist);
            postBody.put("checkToken", checkToken);
            postBody.put("checkVip", checkVip);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult globalFileDownloadNg(String fileName, boolean returnUrl, boolean errorIfNotExist) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "globalFileDownload.ng");
            postBody.put("fileName", fileName);
            postBody.put("returnUrl", returnUrl);
            postBody.put("errorIfNotExist", errorIfNotExist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult trialLoginNg(String deviceCode, String md5) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "trialLogin.ng");
            postBody.put("appVer", gVersionNo);
            postBody.put("deviceCode", deviceCode);
            postBody.put("md5", md5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult bindDeviceInfoNu(String username) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "bindDeviceInfo.nu");
            postBody.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult bindDeviceInfoNc(String loginCode) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "bindDeviceInfo.nc");
            postBody.put("loginCode", loginCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult userExpireTimeNc(String loginCode) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "userExpireTime.nc");
            postBody.put("loginCode", loginCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult userPointNc(String loginCode) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "userPoint.nc");
            postBody.put("loginCode", loginCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult userExpireTimeNu(String username) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "userExpireTime.nu");
            postBody.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult userPointNu(String username) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "userPoint.nu");
            postBody.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult rechargeLoginCodeNc(String loginCode, String newLoginCode) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "rechargeLoginCode.nc");
            postBody.put("loginCode", loginCode);
            postBody.put("newLoginCode", newLoginCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult timeDiffNg(String time1, String time2, Integer formatType) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "timeDiff.ng");
            postBody.put("time1", time1);
            postBody.put("time2", time2);
            postBody.put("formatType", formatType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult appUserInfoNu(String username) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "appUserInfo.nu");
            postBody.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult appUserInfoNc(String loginCode) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "appUserInfo.nc");
            postBody.put("loginCode", loginCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult latestVersionInfoComputeNg() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "latestVersionInfoCompute.ng");
            postBody.put("appVer", gVersionNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }
}
