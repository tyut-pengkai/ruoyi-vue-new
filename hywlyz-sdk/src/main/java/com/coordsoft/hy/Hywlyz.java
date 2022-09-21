package com.coordsoft.hy;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.coordsoft.hy.encrypt.EncryptType;
import com.coordsoft.hy.utils.HttpUtil;
import com.coordsoft.hy.utils.HyUtils;
import com.coordsoft.hy.utils.RandomUtil;
import com.coordsoft.hy.vo.RequestResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
            log.debug("requestServer: " + replaceNewLine(data.toString()) + ", " + sign);

            data.put("sign", sign);
            if (checkToken && (gToken == null || "".equals(gToken))) {
                log.debug("requestServer: token为空，请先调用【软件_置当前TOKEN】函数设置TOKEN");
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
                log.debug("requestServer: response：" + response);
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
                            if (JSON.isValidObject(dataStr) || JSON.isValidArray(dataStr)) {
                                requestResult.setData(dataStr);
                            } else {
                                JSONObject jsonNew = new JSONObject();
                                jsonNew.put("data", dataStr);
                                requestResult.setData(Hywlyz.replaceNewLine(jsonNew.toString()));
                            }
                        } else {
                            requestResult.setCode(0);
                            requestResult.setMsg("vstr校验失败，该数据可能被人为篡改");
                            log.debug("requestServer: " + requestResult.getMsg());
                        }
                    } else {
                        requestResult.setCode(0);
                        requestResult.setMsg("sign校验失败，该数据可能被人为篡改");
                        log.debug("requestServer: " + requestResult.getMsg());
                    }
                }
                log.debug("requestServer: 请求API：" + api);
                log.debug("requestServer: 返回数据json解析成功[密文]：" + string);
                log.debug("requestServer: data结构[明文]：" + requestResult.getData());
                log.debug("requestServer: ======================");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestResult;
    }

    public RequestResult login_nu(String username, String password, String deviceCode, String md5) {
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

    public RequestResult logoutAll_nu(String username, String password) {
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

    public RequestResult rechargeCard_nu(String username, String password, boolean validPassword, String cardNo, String cardPassword) {
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

    public RequestResult register_nu(String username, String password, String passwordRepeat) {
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

    public RequestResult updatePassword_nu(String username, String password, String newPassword, String newPasswordRepeat) {
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

    public RequestResult unbindDevice_nu(String deviceCode, String username, String password, boolean enableNegative) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("enableNegative", enableNegative);
            postBody.put("deviceCode", deviceCode);
            postBody.put("username", username);
            postBody.put("password", password);
            postBody.put("api", "unbindDevice.nu");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult appInfo_ng() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "appInfo.ng");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult appUserInfo_ag() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "appUserInfo.ag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult heartbeat_ag() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "heartbeat.ag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult isConnected_ng() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "isConnected.ng");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult isOnline_ag() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "isOnline.ag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult latestVersionInfo_ng() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "latestVersionInfo.ng");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult versionInfo_ng() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "versionInfo.ng");
            postBody.put("appVer", gVersionNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult latestVersionInfoForceUpdate_ng() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "latestVersionInfoForceUpdate.ng");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult logout_ag() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "logout.ag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult serverTime_ng() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "serverTime.ng");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult unbindDevice_ag(boolean enableNegative) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("enableNegative", enableNegative);
            postBody.put("api", "unbindDevice.ag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult isUserNameExist_nu(String username) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("username", username);
            postBody.put("api", "isUserNameExist.nu");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult userExpireTime_ag() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "userExpireTime.ag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult reduceTime_ag(int seconds, boolean enableNegative) {
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

    public RequestResult reducePoint_ag(int point, boolean enableNegative) {
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

    public RequestResult userPoint_ag() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "userPoint.ag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, true));
    }

    public RequestResult login_nc(String loginCode, String deviceCode, String md5) {
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

    public RequestResult unbindDevice_nc(String deviceCode, String loginCode, boolean enableNegative) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "unbindDevice.nc");
            postBody.put("deviceCode", deviceCode);
            postBody.put("loginCode", loginCode);
            postBody.put("enableNegative", enableNegative);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult globalScript_ng(String scriptKey, String scriptParams) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "globalScript.ng");
            postBody.put("scriptKey", scriptKey);
            postBody.put("scriptParams", scriptParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult globalVariableGet_ng(String variableName, boolean errorIfNotExist) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "globalVariableGet.ng");
            postBody.put("variableName", variableName);
            postBody.put("errorIfNotExist", errorIfNotExist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult globalVariableSet_ng(String variableName, String variableValue, boolean errorIfNotExist, boolean checkToken, boolean checkVip) {
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
        return (requestServer(postBody, false));
    }

    public RequestResult trialLogin_ng(String deviceCode, String md5) {
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

    public RequestResult bindDeviceInfo_nu(String username) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "bindDeviceInfo.nu");
            postBody.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult bindDeviceInfo_nc(String loginCode) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "bindDeviceInfo.nc");
            postBody.put("loginCode", loginCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult userExpireTime_nc(String loginCode) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "userExpireTime.nc");
            postBody.put("loginCode", loginCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult userPoint_nc(String loginCode) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "userPoint.nc");
            postBody.put("loginCode", loginCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult userExpireTime_nu(String username) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "userExpireTime.nu");
            postBody.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult userPoint_nu(String username) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("api", "userPoint.nu");
            postBody.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (requestServer(postBody, false));
    }

    public RequestResult rechargeLoginCode_nc(String loginCode, String newLoginCode) {
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
}
