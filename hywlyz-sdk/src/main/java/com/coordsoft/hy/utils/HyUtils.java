package com.coordsoft.hy.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.coordsoft.hy.encrypt.EncryptAesCbcPKCS5P;
import com.coordsoft.hy.encrypt.EncryptAesCbcZeroP;
import com.coordsoft.hy.encrypt.EncryptBase64;
import com.coordsoft.hy.encrypt.EncryptType;
import com.coordsoft.hy.vo.JsonResult;
import com.coordsoft.hy.vo.RequestResult;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class HyUtils {

    protected static String replaceNewLine(String text) {
        return (text.replace("\n", ""));
    }

    private static String signDo(String text, String salt) {
        String textNew = text + salt;
        return Md5Util.hash(textNew);
    }

    private static String createLinkString(JSONObject postBody) {
        Set<String> keys = postBody.keySet();
        List<String> keyList = new ArrayList<>(keys);
        Collections.sort(keyList);
        StringBuilder prestr = new StringBuilder();
        for (int i = 0; i < keyList.size(); i++) {
            if (i == keyList.size() - 1) {
                prestr.append(keyList.get(i)).append("=")
                        .append(postBody.getString(keyList.get(i)));
            } else {
                prestr.append(keyList.get(i)).append("=")
                        .append(postBody.getString(keyList.get(i))).append("&");
            }
        }
        return prestr.toString();
    }

    private static JSONObject paramFilter(JSONObject postBody) {
        JSONObject postBodyNew = new JSONObject();
        Set<String> keys = postBody.keySet();
        List<String> keyList = new ArrayList<>(keys);
        for (String s : keyList) {
            if (StringUtils.isBlank(postBody.getString(s)) || "sign".equals(s)) {
                continue;
            }
            postBodyNew.put(s, postBody.getString(s));
        }
        return postBodyNew;
    }

    private static String encryptBase64(String text) {
        try {
            return EncryptBase64.encrypt(text, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String decryptBase64(String text) {
        try {
            return EncryptBase64.decrypt(text, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String encryptAesCbcPKCS5Padding(String text, String password) {
        if ("".equals(password)) {
            return text;
        }
        try {
            return EncryptAesCbcPKCS5P.encrypt(text, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String rg_decrypt_AES_CBC_PKCS5Padding(String text, String password) {
        if ("".equals(password)) {
            return text;
        }
        try {
            return EncryptAesCbcPKCS5P.decrypt(text, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String rgEncryptAESCBCZeroPadding(String text, String password) {
        if ("".equals(password)) {
            return (text);
        }
        try {
            return AesCbcZeroPaddingUtil.encode(text, password);
        } catch (Exception e) {
            e.printStackTrace();
            return (null);
        }
    }

    private static String decryptAesCbcZeroPadding(String text, String password) {
        if ("".equals(password)) {
            return (text);
        }
        try {
            return EncryptAesCbcZeroP.decrypt(text, password);
        } catch (Exception e) {
            e.printStackTrace();
            return (null);
        }
    }

    protected static String calcAnonApi(String text, String password) {
        return (rgEncryptAESCBCZeroPadding(text, password));
    }

    //=====================================以上为私有方法====================================

    public static String generalCalcSign(JSONObject postBody, String salt) {
        JSONObject postBodyNew = paramFilter(postBody);
        String text = createLinkString(postBodyNew);
        return (signDo(text, salt));
    }

    public static JsonResult analyseResult(RequestResult requestResult) {
        JsonResult result = new JsonResult();
        result.setSuccess(requestResult.getCode() == 200);
        if (result.isSuccess()) {
            try {
                result.setData(JSON.parseObject(requestResult.getData()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            result.setErrorMsg(getErrorMsg(requestResult));
        }
        return result;
    }

    private static String getErrorMsg(RequestResult requestResult) {
        return (StringUtils.isBlank(requestResult.getDetail()) ? requestResult.getMsg() : requestResult.getDetail());
    }

    public static String generalDataEncrypt(String text, String password, EncryptType encryptType) {
        if (encryptType == EncryptType.NONE) {
            return (text);
        } else if (encryptType == EncryptType.BASE64) {
            return (encryptBase64(text));
        } else if (encryptType == EncryptType.AES_CBC_PKCS5Padding) {
            return (encryptAesCbcPKCS5Padding(text, password));
        } else if (encryptType == EncryptType.AES_CBC_ZeroPadding) {
            return (rgEncryptAESCBCZeroPadding(text, password));
        } else {
            return (text);
        }
    }

    public static String generalDataDecrypt(String text, String password, EncryptType encryptType) {
        if (encryptType == EncryptType.NONE) {
            return (text);
        } else if (encryptType == EncryptType.BASE64) {
            return (decryptBase64(text));
        } else if (encryptType == EncryptType.AES_CBC_PKCS5Padding) {
            return (rg_decrypt_AES_CBC_PKCS5Padding(text, password));
        } else if (encryptType == EncryptType.AES_CBC_ZeroPadding) {
            return (decryptAesCbcZeroPadding(text, password));
        } else {
            return (text);
        }
    }

    public static Boolean verifySign(JSONObject json, String salt) {
        return signDo(json.getString("data") + json.getString("timestamp") + json.getString("vstr"), salt).equals(json.getString("sign"));
    }
}
