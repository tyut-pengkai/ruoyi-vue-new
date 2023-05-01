# 对接说明

## 一、基本约定及开发规范

### 1、API接口后缀说明  
**接口后缀类别列表**： .ng/.nc/.nu/.ag/.ac/.au  
**第一个字母含义**：是否需要登录后才可以请求  
    - a代表auth，即需要登录验证  
    - n代表noAuth，即无需登录验证  
**第二个字母含义**：可调用此API的软件的登录模式，只有与接口相匹配的计费模式的软件才能调用该接口  
    - c代表card，即单码登录  
    - u代表user，即用户(账号密码)登录  
    - g代表generic，即通用的，不区分登录模式  

### 2、全局对接参数
| 参数 | 参数说明 | 说明 | 后台位置 |  
| :--- | :---: | :---: | :---: |    
|apiUrl|API接口地址|验证后台地址|软件管理-配置-接口信息|  
|appSecret|APP SECRET|软件与验证后台交互的重要密钥，在软件中尽量以非明文编码，不能泄露给他人|软件管理-配置-接口信息|  
|versionNo|版本号|用于提示自动更新|软件管理-版本-修改|  
|dataInEnc|数据输入加密|数据加密方式，明文用于测试，AES用于生产，可加大破解难度|软件管理-配置-通信安全|  
|dataInPwd|数据输入加密密码|仅AES加密时需要提供，数据加密密码，尽量复杂，可加大破解难度|软件管理-配置-通信安全|  
|dataOutEnc|数据输出加密|数据加密方式，明文用于测试，AES用于生产，可加大破解难度|软件管理-配置-通信安全|  
|dataOutPwd|数据输出加密密码|仅AES加密时需要提供，数据加密密码，尽量复杂，可加大破解难度|软件管理-配置-通信安全|  
|apiPwd|API匿名密码|用于混淆API接口名称，可加大破解难度|软件管理-配置-通信安全| 

### 3、接口请求方法
接口请求一律使用`POST`方式  

### 4、接口统一结构
##### 请求
| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true||
|&emsp;&emsp;api|[公共]请求的API接口||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;...|[私有]私有参数||true|string|
```json
{
  "api": "",
  "appSecret": "",
  "sign": "",
  ...
}
```
##### 响应
| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|响应数据|object|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|
`````json
{
	"code": 200,
	"data": {
		...
	},
	"msg": "",
	"sign": "",
	"timestamp": ""
}
`````

### 5、错误代码
```java
SUCCESS(0, "成功"),

// 400090000~400090099 参数错误
ERROR_PARAMETERS_MISSING(400090001, "缺少参数"),
ERROR_PARAMETERS_ERROR(400090002, "参数有误"),
ERROR_PARAMETER_DECRYPT_EXCEPTION(400090003, "请求参数解密错误"),
ERROR_PARAMETER_ANALYSE_EXCEPTION(400090004, "请求参数解析错误"),
ERROR_RESPONSE_ENCRYPT_EXCEPTION(400090005, "返回数据加密出现错误"),
ERROR_SIGN_INVALID(400090006, "sign验证未通过"),
ERROR_APP_MD5_MISMATCH(400090007, "程序完整性校验未通过"),
ERROR_DATA_EXPIRED(400090008, "数据包已过期，系统拒绝接收"),
ERROR_API_NOT_EXIST(400090009, "API接口不存在"),

// 400090100~400090199 认证类错
ERROR_UNAUTHORIZED(400090001, "未登录"),
ERROR_USERNAME_OR_PASSWORD_ERROR(400090002, "账号或密码错误"),
ERROR_ACCOUNT_NOT_EXIST(400090003, "账号不存在"),
ERROR_ACCOUNT_LOCKED(400090004, "账号被冻结"),
ERROR_LOGIN_CODE_NOT_EXIST(400090005, "登录码不存在"),
ERROR_LOGIN_CODE_LOCKED(400090006, "登录码被冻结"),
ERROR_LOGIN_CODE_EXPIRED(400090007, "登录码已过期"),
ERROR_LOGIN_CODE_APP_MISMATCH(400090008, "登录码与软件不匹配"),
ERROR_APP_NOT_EXIST(400090009, "软件不存在"),
ERROR_APP_VERSION_NOT_EXIST(400090010, "软件版本不存在"),
ERROR_APPKEY_OR_APPSECRET_ERROR(400090011, "软件APPKEY或APPSECRET错误"),
ERROR_API_CALLED_MISMATCH(400090012, "该软件不适用于此接口"),
ERROR_APP_USER_LOCKED(400090013, "软件用户被冻结"),
ERROR_APP_USER_NOT_EXIST(400090014, "软件用户不存在"),
ERROR_REGISTER_FAILED(400090015, "注册账号失败"),
ERROR_DEVICE_CODE_NOT_EXIST(400090016, "设备码不存在"),
ERROR_LOGIN_USER_APP_MISMATCH(400090017, "当前登录的软件与接口所在软件不匹配"),
ERROR_APP_TRIAL_USER_LOCKED(400090018, "软件试用被冻结"),
ERROR_APP_TRIAL_USER_EXPIRED(400090019, "软件试用已过期"),
ERROR_APP_TRIAL_NOT_ENABLE(400090020, "软件试用未开启"),
ERROR_NOT_IN_APP_TRIAL_TIME_QUANTUM(400090021, "未在软件试用时间段内"),
ERROR_TRIAL_OVER_LIMIT_TIMES(400090022, "超出软件试用次数"),
ERROR_TRIAL_OVER_LIMIT_TIMES_PER_IP(400090023, "超出同IP软件试用次数"),

// 400090200~400090999 业务相关
ERROR_CARD_NOT_EXIST(400090201, "充值卡不存在"),
ERROR_CARD_IS_USED(400090202, "充值卡已被使用"),
ERROR_CARD_APP_MISMATCH(400090203, "此充值卡不能用于该软件"),
ERROR_CARD_EXPIRED(400090204, "充值卡已过期"),
ERROR_CHARGE_RULE_MISMATCH(400090205, "不符合充值卡充值限制"),
ERROR_CARD_PASSWORD_MISMATCH(400090206, "充值卡密码有误"),
ERROR_CARD_LOCKED(400090207, "充值卡被冻结"),
ERROR_APP_OFF(400090208, "软件维护中"),
ERROR_APP_VERSION_OFF(400090209, "当前版本已停用"),
ERROR_BIND_MACHINE_LIMIT(400090210, "软件用户已绑定其他设备"),
ERROR_BIND_USER_LIMIT(400090211, "已有其他软件用户绑定此设备"),
ERROR_APP_USER_EXPIRED(400090212, "软件用户已过期"),
ERROR_APP_USER_NO_TIME(400090213, "软件用户剩余时间不足"),
ERROR_APP_USER_NO_POINT(400090214, "软件用户点数不足"),
ERROR_DEVICE_CODE_LOCKED(400090215, "设备码被锁定"),
ERROR_LOGIN_MACHINE_LIMIT(400090216, "同时登录设备数量达到上限"),
ERROR_LOGIN_USER_LIMIT(400090217, "同时在线数量达到上限"),
ERROR_APP_CUSTOM_PARAM_NOT_EXIST(400090218, "软件自定义参数不存在"),
ERROR_GLOBAL_SCRIPT_NOT_EXIST(400090219, "云端脚本不存在"),
ERROR_GLOBAL_VARIABLE_NOT_EXIST(400090220, "云端变量不存在"),
ERROR_GLOBAL_FILE_NOT_EXIST(400090221, "云端文件不存在"),
ERROR_GLOBAL_FILE_ALREADY_EXIST(400090221, "云端文件已存在"),
ERROR_NOT_LOGIN(400090222, "未登录"),
ERROR_NOT_VIP(400090223, "非VIP用户"),
ERROR_TRIAL_USER_NOT_ALLOWED(400090224, "试用用户无法执行此操作"),
ERROR_GLOBAL_SCRIPT_INNER_VARIABLE_ANALYTICS_FAILED(400090225, "全局脚本内置变量解析出错"),
ERROR_LOGIN_CODE_IS_USED(400090226, "新单码已被使用"),
ERROR_API_NOT_ENABLED(400090227, "此API不被允许调用"),
ERROR_APP_UNBIND_NOT_ENABLE(400090228, "软件解绑功能未开启"),
ERROR_CARD_UNBIND_NOT_ENABLE(400090229, "卡密解绑功能未开启"),
ERROR_UNBIND_NO_TIMES(400090230, "解绑次数已用尽，无法继续解绑"),
ERROR_OTHER_FAULTS(400099999, "未知错误"),
```


## 二、数据加密算法
### 1、AES_CBC_PKCS5Padding
##### 参考代码
```java
public class AesCbcPKCS5PaddingUtil {
    private final static int AES_KEY_LENGTH = 16; //密钥长度16字节，128位
    private final static String AES_ALGORITHM = "AES"; //算法名字
    private final static String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding"; //算法/模式/填充
    private final static Charset UTF_8 = Charset.forName("UTF-8"); //编码格式

    /**
     * 使用AES加密
     *
     * @param aesKey AES Key
     * @param data   被加密的数据
     * @return AES加密后的数据
     */
    public static String encode(String data, String aesKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        if (aesKey == null) {
            return data;
        }
        if (aesKey.getBytes().length != AES_KEY_LENGTH) {
            aesKey = DigestUtils.md5Hex(aesKey).substring(8, 24);
        }
        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        String ivStr = DigestUtils.md5Hex(aesKey).substring(0, 16);
        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes(UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        return Base64.encodeBase64String(cipher.doFinal(data.getBytes(UTF_8)));
    }

    /**
     * 使用AES解密
     *
     * @param aesKey AES Key
     * @param data   被解密的数据
     * @return AES解密后的数据
     */
    public static String decode(String data, String aesKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        if (aesKey == null) {
            return data;
        }
        if (aesKey.getBytes().length != AES_KEY_LENGTH) {
            aesKey = DigestUtils.md5Hex(aesKey).substring(8, 24);
        }
        byte[] decodeBase64 = Base64.decodeBase64(data.getBytes());
        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        String ivStr = DigestUtils.md5Hex(aesKey).substring(0, 16);
        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes(UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
        return new String(cipher.doFinal(decodeBase64), UTF_8);
    }
}
```

### 2、AES_CBC_ZeroPadding
##### 参考代码
```java
public class AesCbcZeroPaddingUtil {
	private final static int AES_KEY_LENGTH = 16;//密钥长度16字节，128位
	private final static String AES_ALGORITHM = "AES";//算法名字
	private final static String AES_TRANSFORMATION = "AES/CBC/NoPadding";//算法/模式/填充
	private final static Charset UTF_8 = Charset.forName("UTF-8");//编码格式

	// 加密
	public static String encode(String data, String aesKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if (aesKey == null) {
			return data;
		}
		if (aesKey.getBytes().length != AES_KEY_LENGTH) {
			aesKey = DigestUtils.md5Hex(aesKey).substring(8, 24);
		}
		String ivStr = DigestUtils.md5Hex(aesKey).substring(0, 16);
		// 偏移量
		byte[] iv = ivStr.getBytes(UTF_8);
		Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
		int blockSize = cipher.getBlockSize();
		byte[] dataBytes = data.getBytes();
		int length = dataBytes.length;
		// 计算需填充长度
		if (length % blockSize != 0) {
			length = length + (blockSize - (length % blockSize));
		}
		byte[] plaintext = new byte[length];
		// 填充
		System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
		SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), AES_ALGORITHM);
		// 设置偏移量参数
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
		byte[] encryped = cipher.doFinal(plaintext);
		return Base64.encodeBase64String(encryped);
	}

	// 解密
	public static String decode(String data, String aesKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if (aesKey == null) {
			return data;
		}
		if (aesKey.getBytes().length != AES_KEY_LENGTH) {
			aesKey = DigestUtils.md5Hex(aesKey).substring(8, 24);
		}
		String ivStr = DigestUtils.md5Hex(aesKey).substring(0, 16);
		// 偏移量
		byte[] iv = ivStr.getBytes(UTF_8);
		byte[] encryp = Base64.decodeBase64(data);
		Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
		SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), AES_ALGORITHM);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		byte[] original = cipher.doFinal(encryp);
		return new String(original).replace("\0", "");
	}

}
```

## 三、SIGN计算方法  
### 1、入参sign计算方法  
##### 参数过滤  
过滤掉请求参数中key为"sign"或value为空的参数  

##### 参数排序  
将过滤后的参数按key进行升序排列，按排列顺序将参数拼接为k1=v1&k2=v2&k3=v3&...&kn=vn的形式  

##### 取MD5  
将上一步得到的字符串拼接AppSecret后取MD5  

##### 参考代码  
```java
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

private static String signDo(String text, String salt) {  
    String textNew = text + salt;  
    return Md5Util.hash(textNew);  
}

public static String generalCalcSign(JSONObject postBody, String salt) {  
    JSONObject postBodyNew = paramFilter(postBody);  
    String text = createLinkString(postBodyNew);  
    return (signDo(text, salt));  
}

String sign = generalCalcSign(data, gAppSecret);

```

### 2、出参sign校验方法
##### 参数拼接
将接口返回的json数据中的data字段取出，并拼接json数据中的timestamp字段和vstr字段

##### 取MD5
将上一步得到的字符串拼接AppSecret后取MD5

##### 参考代码
```java
private static String signDo(String text, String salt) {  
    String textNew = text + salt;  
    return Md5Util.hash(textNew);  
}

public static Boolean verifySign(JSONObject json, String salt) {
    return signDo(json.optString("data") + json.optString("timestamp") + json.optString("vstr"), salt).equals(json.optString("sign"));
}

if (verifySign(responseJsonObject, gAppSecret)) {
    // sign校验通过
} else {
    // sign校验失败
}
```