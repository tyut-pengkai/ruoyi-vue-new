package com.coordsoft.hysdk.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.coordsoft.hysdk.enums.EncryptType;
import com.coordsoft.hysdk.vo.JsonResult;
import com.coordsoft.hysdk.vo.RequestResult;
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
            return Base64Util.encode(text);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String decryptBase64(String text) {
        try {
            return Base64Util.decode(text);
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
            return AesCbcPKCS5PaddingUtil.encode(text, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String decryptAesCbcPKCS5Padding(String text, String password) {
        if ("".equals(password)) {
            return text;
        }
        try {
            return AesCbcPKCS5PaddingUtil.decode(text, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String encryptAesCbcZeroPadding(String text, String password) {
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
            return AesCbcZeroPaddingUtil.decode(text, password);
        } catch (Exception e) {
            e.printStackTrace();
            return (null);
        }
    }

    protected static String calcAnonApi(String text, String password) {
        return (encryptAesCbcZeroPadding(text, password));
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
            return (encryptAesCbcZeroPadding(text, password));
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
            return (decryptAesCbcPKCS5Padding(text, password));
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
