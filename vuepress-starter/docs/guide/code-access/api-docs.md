# API 接口 <img src="/images/version.svg">

## 账号计时API

### 获取用户信息

**接口地址**:`/prod-api/api/v1/{appkey}?appUserInfo.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户信息</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|AppUserInfo.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为appUserInfo.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]账号||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data||AppUserInfo.nuAjaxResultVoData|AppUserInfo.nuAjaxResultVoData|
|&emsp;&emsp;expireTime|用户过期时间，计时模式下有效|string|
|&emsp;&emsp;point|用户剩余点数，计点模式下有效|integer|
|&emsp;&emsp;status|用户状态，0为正常|string|
|&emsp;&emsp;loginLimitU|同时在线用户数限制|integer|
|&emsp;&emsp;loginLimitM|同时在线设备数限制|integer|
|&emsp;&emsp;cardLoginLimitU|由卡密继承来的同时在线用户数限制|integer|
|&emsp;&emsp;cardLoginLimitM|由卡密继承来的同时在线设备数限制|integer|
|&emsp;&emsp;loginTimes|用户登录次数|integer|
|&emsp;&emsp;lastLoginTime|最近登录时间|string|
|&emsp;&emsp;loginIp|最近登录IP|string|
|&emsp;&emsp;freeBalance|当前无实际作用|number|
|&emsp;&emsp;payBalance|当前无实际作用|number|
|&emsp;&emsp;freePayment|当前无实际作用|number|
|&emsp;&emsp;payPayment|当前无实际作用|number|
|&emsp;&emsp;cardCustomParams|由卡密继承来的自定义参数|string|
|&emsp;&emsp;remark|备注信息|string|
|&emsp;&emsp;userInfo|账号信息，账号模式下有效|AppUserInfo.nuAjaxResultVoDataUserInfo|AppUserInfo.nuAjaxResultVoDataUserInfo|
|&emsp;&emsp;&emsp;&emsp;avatar|头像|string|
|&emsp;&emsp;&emsp;&emsp;userName|用户名|string|
|&emsp;&emsp;&emsp;&emsp;nickName|用户昵称|string|
|&emsp;&emsp;&emsp;&emsp;sex|性别，0男1女2未知|string|
|&emsp;&emsp;&emsp;&emsp;phonenumber|手机号码|string|
|&emsp;&emsp;&emsp;&emsp;email|邮箱|string|
|&emsp;&emsp;&emsp;&emsp;admin|是否为管理员账号|bool|
|&emsp;&emsp;&emsp;&emsp;availablePayBalance|可用余额|number|
|&emsp;&emsp;&emsp;&emsp;freezePayBalance|冻结余额|number|
|&emsp;&emsp;&emsp;&emsp;loginDate|最近登录时间|string|
|&emsp;&emsp;&emsp;&emsp;loginIp|最近登录IP|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"expireTime": "",
		"point": 0,
		"status": "",
		"loginLimitU": 0,
		"loginLimitM": 0,
		"cardLoginLimitU": 0,
		"cardLoginLimitM": 0,
		"loginTimes": 0,
		"lastLoginTime": "",
		"loginIp": "",
		"freeBalance": 0,
		"payBalance": 0,
		"freePayment": 0,
		"payPayment": 0,
		"cardCustomParams": "",
		"remark": "",
		"userInfo": {
			"avatar": "",
			"userName": "",
			"nickName": "",
			"sex": "",
			"phonenumber": "",
			"email": "",
			"admin": "",
			"availablePayBalance": 0,
			"freezePayBalance": 0,
			"loginDate": "",
			"loginIp": ""
		}
	},
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 获取用户绑定设备

**接口地址**:`/prod-api/api/v1/{appkey}?bindDeviceInfo.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户当前绑定的设备信息</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|BindDeviceInfo.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为bindDeviceInfo.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]账号||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|设备信息列表，注意此处为数组|BindDeviceInfo.nuAjaxResultVoData|BindDeviceInfo.nuAjaxResultVoData|
|&emsp;&emsp;deviceCodeStr|设备码|string|
|&emsp;&emsp;lastLoginTime|设备最后登录时间|string|
|&emsp;&emsp;loginTimes|设备登录次数|string|
|&emsp;&emsp;status|设备状态（0正常 1停用）|string|
|&emsp;&emsp;remark|备注信息|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"deviceCodeStr": "",
		"lastLoginTime": "",
		"loginTimes": "",
		"status": "",
		"remark": ""
	},
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 用户名是否存在

**接口地址**:`/prod-api/api/v1/{appkey}?isUserNameExist.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>检查用户名是否已存在，存在返回1，否则返回0</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|IsUserNameExist.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为isUserNameExist.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]要检查的用户名||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|存在返回1，否则返回0|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 账号登录

**接口地址**:`/prod-api/api/v1/{appkey}?login.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>账号登录接口</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": "",
  "password": "",
  "appVer": "",
  "deviceCode": "",
  "md5": "",
  "autoReducePoint": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|Login.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为login.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]账号||true|string|
|&emsp;&emsp;password|[私有]密码||true|string|
|&emsp;&emsp;appVer|[私有]软件版本号||true|string|
|&emsp;&emsp;deviceCode|[私有]设备码，如果开启设备绑定，则必须提供||false|string|
|&emsp;&emsp;md5|[私有]软件MD5||false|string|
|&emsp;&emsp;autoReducePoint|[私有]计点模式下是否登录成功后自动扣除用户点数（扣除1），默认值为true||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|登录成功返回token|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 注销所有登录

**接口地址**:`/prod-api/api/v1/{appkey}?logoutAll.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>注销指定账号在本软件的所有登录</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": "",
  "password": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|LogoutAll.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为logoutAll.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]要注销的账号||true|string|
|&emsp;&emsp;password|[私有]密码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回0|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 充值卡充值

**接口地址**:`/prod-api/api/v1/{appkey}?rechargeCard.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>使用充值卡充值</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": "",
  "password": "",
  "validPassword": "",
  "cardNo": "",
  "cardPassword": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|RechargeCard.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为rechargeCard.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]充值用户||true|string|
|&emsp;&emsp;password|[私有]充值用户密码||false|string|
|&emsp;&emsp;validPassword|[私有]是否验证充值用户密码，防止充错用户，验证传1，不验证传0，默认为0||false|string|
|&emsp;&emsp;cardNo|[私有]充值卡号||true|string|
|&emsp;&emsp;cardPassword|[私有]充值卡密码||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回新的到期时间或点数|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 扣减用户时间

**接口地址**:`/prod-api/api/v1/{appkey}?reduceTime.ag`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>扣减用户时间，返回扣减后到期时间</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "vstr": "",
  "timestamp": "",
  "sign": "",
  "seconds": "",
  "enableNegative": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|Authorization|token标记|header|true|string|
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|ReduceTime.agParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为reduceTime.ag||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;vstr|[公共]用作标记或验证的冗余数据，将原样返回||false|string|
|&emsp;&emsp;timestamp|[公共]13位时间戳（精确到毫秒）||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;seconds|[私有]扣减的秒数，需传入正整数||true|string|
|&emsp;&emsp;enableNegative|[私有]是否允许用户过期，允许传1，不允许传0，默认为0||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|扣减后到期时间|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|
|vstr|用作标记或验证的冗余数据，与输入保持一致|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": "",
	"vstr": ""
}
```

### 注册新账号

**接口地址**:`/prod-api/api/v1/{appkey}?register.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>注册新账号</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": "",
  "password": "",
  "passwordRepeat": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|Register.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为register.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]账号||true|string|
|&emsp;&emsp;password|[私有]密码||true|string|
|&emsp;&emsp;passwordRepeat|[私有]重复密码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回0|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 解除绑定指定设备

**接口地址**:`/prod-api/api/v1/{appkey}?unbindDevice.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>解除绑定当前设备，解绑成功会根据软件设定扣减用户余额</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "deviceCode": "",
  "username": "",
  "password": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|UnbindDevice.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为unbindDevice.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;deviceCode|[私有]设备码||true|string|
|&emsp;&emsp;username|[私有]账号||true|string|
|&emsp;&emsp;password|[私有]密码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回0，设备码不存在返回-1|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 修改账号密码

**接口地址**:`/prod-api/api/v1/{appkey}?updatePassword.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>修改账号密码</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": "",
  "password": "",
  "newPassword": "",
  "newPasswordRepeat": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|UpdatePassword.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为updatePassword.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]账号||true|string|
|&emsp;&emsp;password|[私有]原密码||true|string|
|&emsp;&emsp;newPassword|[私有]新密码||true|string|
|&emsp;&emsp;newPasswordRepeat|[私有]重复新密码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回0|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 获取用户过期时间

**接口地址**:`/prod-api/api/v1/{appkey}?userExpireTime.ag`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户过期时间</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "vstr": "",
  "timestamp": "",
  "sign": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|Authorization|token标记|header|true|string|
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|UserExpireTime.agParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为userExpireTime.ag||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;vstr|[公共]用作标记或验证的冗余数据，将原样返回||false|string|
|&emsp;&emsp;timestamp|[公共]13位时间戳（精确到毫秒）||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|用户过期时间|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|
|vstr|用作标记或验证的冗余数据，与输入保持一致|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": "",
	"vstr": ""
}
```

### 获取用户过期时间

**接口地址**:`/prod-api/api/v1/{appkey}?userExpireTime.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户过期时间</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|UserExpireTime.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为userExpireTime.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]账号||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|用户过期时间|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

## 账号计点API

### 获取用户信息

**接口地址**:`/prod-api/api/v1/{appkey}?appUserInfo.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户信息</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|AppUserInfo.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为appUserInfo.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]账号||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data||AppUserInfo.nuAjaxResultVoData|AppUserInfo.nuAjaxResultVoData|
|&emsp;&emsp;expireTime|用户过期时间，计时模式下有效|string|
|&emsp;&emsp;point|用户剩余点数，计点模式下有效|integer|
|&emsp;&emsp;status|用户状态，0为正常|string|
|&emsp;&emsp;loginLimitU|同时在线用户数限制|integer|
|&emsp;&emsp;loginLimitM|同时在线设备数限制|integer|
|&emsp;&emsp;cardLoginLimitU|由卡密继承来的同时在线用户数限制|integer|
|&emsp;&emsp;cardLoginLimitM|由卡密继承来的同时在线设备数限制|integer|
|&emsp;&emsp;loginTimes|用户登录次数|integer|
|&emsp;&emsp;lastLoginTime|最近登录时间|string|
|&emsp;&emsp;loginIp|最近登录IP|string|
|&emsp;&emsp;freeBalance|当前无实际作用|number|
|&emsp;&emsp;payBalance|当前无实际作用|number|
|&emsp;&emsp;freePayment|当前无实际作用|number|
|&emsp;&emsp;payPayment|当前无实际作用|number|
|&emsp;&emsp;cardCustomParams|由卡密继承来的自定义参数|string|
|&emsp;&emsp;remark|备注信息|string|
|&emsp;&emsp;userInfo|账号信息，账号模式下有效|AppUserInfo.nuAjaxResultVoDataUserInfo|AppUserInfo.nuAjaxResultVoDataUserInfo|
|&emsp;&emsp;&emsp;&emsp;avatar|头像|string|
|&emsp;&emsp;&emsp;&emsp;userName|用户名|string|
|&emsp;&emsp;&emsp;&emsp;nickName|用户昵称|string|
|&emsp;&emsp;&emsp;&emsp;sex|性别，0男1女2未知|string|
|&emsp;&emsp;&emsp;&emsp;phonenumber|手机号码|string|
|&emsp;&emsp;&emsp;&emsp;email|邮箱|string|
|&emsp;&emsp;&emsp;&emsp;admin|是否为管理员账号|bool|
|&emsp;&emsp;&emsp;&emsp;availablePayBalance|可用余额|number|
|&emsp;&emsp;&emsp;&emsp;freezePayBalance|冻结余额|number|
|&emsp;&emsp;&emsp;&emsp;loginDate|最近登录时间|string|
|&emsp;&emsp;&emsp;&emsp;loginIp|最近登录IP|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"expireTime": "",
		"point": 0,
		"status": "",
		"loginLimitU": 0,
		"loginLimitM": 0,
		"cardLoginLimitU": 0,
		"cardLoginLimitM": 0,
		"loginTimes": 0,
		"lastLoginTime": "",
		"loginIp": "",
		"freeBalance": 0,
		"payBalance": 0,
		"freePayment": 0,
		"payPayment": 0,
		"cardCustomParams": "",
		"remark": "",
		"userInfo": {
			"avatar": "",
			"userName": "",
			"nickName": "",
			"sex": "",
			"phonenumber": "",
			"email": "",
			"admin": "",
			"availablePayBalance": 0,
			"freezePayBalance": 0,
			"loginDate": "",
			"loginIp": ""
		}
	},
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 获取用户绑定设备

**接口地址**:`/prod-api/api/v1/{appkey}?bindDeviceInfo.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户当前绑定的设备信息</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|BindDeviceInfo.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为bindDeviceInfo.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]账号||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|设备信息列表，注意此处为数组|BindDeviceInfo.nuAjaxResultVoData|BindDeviceInfo.nuAjaxResultVoData|
|&emsp;&emsp;deviceCodeStr|设备码|string|
|&emsp;&emsp;lastLoginTime|设备最后登录时间|string|
|&emsp;&emsp;loginTimes|设备登录次数|string|
|&emsp;&emsp;status|设备状态（0正常 1停用）|string|
|&emsp;&emsp;remark|备注信息|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"deviceCodeStr": "",
		"lastLoginTime": "",
		"loginTimes": "",
		"status": "",
		"remark": ""
	},
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 用户名是否存在

**接口地址**:`/prod-api/api/v1/{appkey}?isUserNameExist.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>检查用户名是否已存在，存在返回1，否则返回0</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|IsUserNameExist.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为isUserNameExist.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]要检查的用户名||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|存在返回1，否则返回0|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 账号登录

**接口地址**:`/prod-api/api/v1/{appkey}?login.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>账号登录接口</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": "",
  "password": "",
  "appVer": "",
  "deviceCode": "",
  "md5": "",
  "autoReducePoint": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|Login.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为login.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]账号||true|string|
|&emsp;&emsp;password|[私有]密码||true|string|
|&emsp;&emsp;appVer|[私有]软件版本号||true|string|
|&emsp;&emsp;deviceCode|[私有]设备码，如果开启设备绑定，则必须提供||false|string|
|&emsp;&emsp;md5|[私有]软件MD5||false|string|
|&emsp;&emsp;autoReducePoint|[私有]计点模式下是否登录成功后自动扣除用户点数（扣除1），默认值为true||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|登录成功返回token|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 注销所有登录

**接口地址**:`/prod-api/api/v1/{appkey}?logoutAll.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>注销指定账号在本软件的所有登录</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": "",
  "password": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|LogoutAll.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为logoutAll.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]要注销的账号||true|string|
|&emsp;&emsp;password|[私有]密码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回0|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 充值卡充值

**接口地址**:`/prod-api/api/v1/{appkey}?rechargeCard.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>使用充值卡充值</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": "",
  "password": "",
  "validPassword": "",
  "cardNo": "",
  "cardPassword": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|RechargeCard.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为rechargeCard.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]充值用户||true|string|
|&emsp;&emsp;password|[私有]充值用户密码||false|string|
|&emsp;&emsp;validPassword|[私有]是否验证充值用户密码，防止充错用户，验证传1，不验证传0，默认为0||false|string|
|&emsp;&emsp;cardNo|[私有]充值卡号||true|string|
|&emsp;&emsp;cardPassword|[私有]充值卡密码||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回新的到期时间或点数|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 扣减用户点数

**接口地址**:`/prod-api/api/v1/{appkey}?reducePoint.ag`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>扣减用户点数，返回扣减后点数余额</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "vstr": "",
  "timestamp": "",
  "sign": "",
  "point": "",
  "enableNegative": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|Authorization|token标记|header|true|string|
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|ReducePoint.agParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为reducePoint.ag||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;vstr|[公共]用作标记或验证的冗余数据，将原样返回||false|string|
|&emsp;&emsp;timestamp|[公共]13位时间戳（精确到毫秒）||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;point|[私有]扣减的点数，需传入正数，可精确到两位小数||true|string|
|&emsp;&emsp;enableNegative|[私有]是否允许余额为负数，允许传1，不允许传0，默认为0||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|扣减后点数余额|number|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|
|vstr|用作标记或验证的冗余数据，与输入保持一致|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": 0,
	"msg": "",
	"sign": "",
	"timestamp": "",
	"vstr": ""
}
```

### 注册新账号

**接口地址**:`/prod-api/api/v1/{appkey}?register.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>注册新账号</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": "",
  "password": "",
  "passwordRepeat": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|Register.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为register.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]账号||true|string|
|&emsp;&emsp;password|[私有]密码||true|string|
|&emsp;&emsp;passwordRepeat|[私有]重复密码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回0|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 解除绑定指定设备

**接口地址**:`/prod-api/api/v1/{appkey}?unbindDevice.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>解除绑定当前设备，解绑成功会根据软件设定扣减用户余额</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "deviceCode": "",
  "username": "",
  "password": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|UnbindDevice.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为unbindDevice.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;deviceCode|[私有]设备码||true|string|
|&emsp;&emsp;username|[私有]账号||true|string|
|&emsp;&emsp;password|[私有]密码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回0，设备码不存在返回-1|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 修改账号密码

**接口地址**:`/prod-api/api/v1/{appkey}?updatePassword.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>修改账号密码</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": "",
  "password": "",
  "newPassword": "",
  "newPasswordRepeat": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|UpdatePassword.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为updatePassword.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]账号||true|string|
|&emsp;&emsp;password|[私有]原密码||true|string|
|&emsp;&emsp;newPassword|[私有]新密码||true|string|
|&emsp;&emsp;newPasswordRepeat|[私有]重复新密码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回0|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 获取用户点数余额

**接口地址**:`/prod-api/api/v1/{appkey}?userPoint.ag`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户点数余额</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "vstr": "",
  "timestamp": "",
  "sign": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|Authorization|token标记|header|true|string|
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|UserPoint.agParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为userPoint.ag||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;vstr|[公共]用作标记或验证的冗余数据，将原样返回||false|string|
|&emsp;&emsp;timestamp|[公共]13位时间戳（精确到毫秒）||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|用户用户点数余额|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|
|vstr|用作标记或验证的冗余数据，与输入保持一致|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": "",
	"vstr": ""
}
```

### 获取用户点数余额

**接口地址**:`/prod-api/api/v1/{appkey}?userPoint.nu`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户点数余额</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "username": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|UserPoint.nuParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为userPoint.nu||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;username|[私有]账号||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|用户用户点数余额|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

## 单码计时API

### 获取用户信息

**接口地址**:`/prod-api/api/v1/{appkey}?appUserInfo.nc`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户信息</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "loginCode": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|AppUserInfo.ncParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为appUserInfo.nc||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;loginCode|[私有]单码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data||AppUserInfo.ncAjaxResultVoData|AppUserInfo.ncAjaxResultVoData|
|&emsp;&emsp;expireTime|用户过期时间，计时模式下有效|string|
|&emsp;&emsp;point|用户剩余点数，计点模式下有效|integer|
|&emsp;&emsp;status|用户状态，0为正常|string|
|&emsp;&emsp;loginLimitU|同时在线用户数限制|integer|
|&emsp;&emsp;loginLimitM|同时在线设备数限制|integer|
|&emsp;&emsp;cardLoginLimitU|由卡密继承来的同时在线用户数限制|integer|
|&emsp;&emsp;cardLoginLimitM|由卡密继承来的同时在线设备数限制|integer|
|&emsp;&emsp;loginTimes|用户登录次数|integer|
|&emsp;&emsp;lastLoginTime|最近登录时间|string|
|&emsp;&emsp;loginIp|最近登录IP|string|
|&emsp;&emsp;freeBalance|当前无实际作用|number|
|&emsp;&emsp;payBalance|当前无实际作用|number|
|&emsp;&emsp;freePayment|当前无实际作用|number|
|&emsp;&emsp;payPayment|当前无实际作用|number|
|&emsp;&emsp;cardCustomParams|由卡密继承来的自定义参数|string|
|&emsp;&emsp;remark|备注信息|string|
|&emsp;&emsp;loginCode|单码信息，单码模式下有效|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"expireTime": "",
		"point": 0,
		"status": "",
		"loginLimitU": 0,
		"loginLimitM": 0,
		"cardLoginLimitU": 0,
		"cardLoginLimitM": 0,
		"loginTimes": 0,
		"lastLoginTime": "",
		"loginIp": "",
		"freeBalance": 0,
		"payBalance": 0,
		"freePayment": 0,
		"payPayment": 0,
		"cardCustomParams": "",
		"remark": "",
		"loginCode": ""
	},
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 获取用户绑定设备

**接口地址**:`/prod-api/api/v1/{appkey}?bindDeviceInfo.nc`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户当前绑定的设备信息</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "loginCode": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|BindDeviceInfo.ncParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为bindDeviceInfo.nc||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;loginCode|[私有]单码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|设备信息列表，注意此处为数组|BindDeviceInfo.ncAjaxResultVoData|BindDeviceInfo.ncAjaxResultVoData|
|&emsp;&emsp;deviceCodeStr|设备码|string|
|&emsp;&emsp;lastLoginTime|设备最后登录时间|string|
|&emsp;&emsp;loginTimes|设备登录次数|string|
|&emsp;&emsp;status|设备状态（0正常 1停用）|string|
|&emsp;&emsp;remark|备注信息|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"deviceCodeStr": "",
		"lastLoginTime": "",
		"loginTimes": "",
		"status": "",
		"remark": ""
	},
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 单码登录

**接口地址**:`/prod-api/api/v1/{appkey}?login.nc`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>单码登录接口</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "loginCode": "",
  "appVer": "",
  "deviceCode": "",
  "md5": "",
  "autoReducePoint": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|Login.ncParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为login.nc||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;loginCode|[私有]单码||true|string|
|&emsp;&emsp;appVer|[私有]软件版本号||true|string|
|&emsp;&emsp;deviceCode|[私有]设备码，如果开启设备绑定，则必须提供||false|string|
|&emsp;&emsp;md5|[私有]软件MD5||false|string|
|&emsp;&emsp;autoReducePoint|[私有]计点模式下是否登录成功后自动扣除用户点数（扣除1），默认值为true||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|登录成功返回token|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 登录码充值

**接口地址**:`/prod-api/api/v1/{appkey}?rechargeLoginCode.nc`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>使用登录码充值</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "loginCode": "",
  "newLoginCode": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|RechargeLoginCode.ncParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为rechargeLoginCode.nc||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;loginCode|[私有]充值到的登录码||true|string|
|&emsp;&emsp;newLoginCode|[私有]用于充值的新登录码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回新的到期时间或点数|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 扣减用户时间

**接口地址**:`/prod-api/api/v1/{appkey}?reduceTime.ag`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>扣减用户时间，返回扣减后到期时间</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "vstr": "",
  "timestamp": "",
  "sign": "",
  "seconds": "",
  "enableNegative": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|Authorization|token标记|header|true|string|
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|ReduceTime.agParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为reduceTime.ag||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;vstr|[公共]用作标记或验证的冗余数据，将原样返回||false|string|
|&emsp;&emsp;timestamp|[公共]13位时间戳（精确到毫秒）||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;seconds|[私有]扣减的秒数，需传入正整数||true|string|
|&emsp;&emsp;enableNegative|[私有]是否允许用户过期，允许传1，不允许传0，默认为0||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|扣减后到期时间|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|
|vstr|用作标记或验证的冗余数据，与输入保持一致|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": "",
	"vstr": ""
}
```

### 解除绑定指定设备

**接口地址**:`/prod-api/api/v1/{appkey}?unbindDevice.nc`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>解除绑定当前设备，解绑成功会根据软件设定扣减用户余额</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "deviceCode": "",
  "loginCode": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|UnbindDevice.ncParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为unbindDevice.nc||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;deviceCode|[私有]设备码||true|string|
|&emsp;&emsp;loginCode|[私有]单码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回0，设备码不存在返回-1|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 获取用户过期时间

**接口地址**:`/prod-api/api/v1/{appkey}?userExpireTime.ag`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户过期时间</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "vstr": "",
  "timestamp": "",
  "sign": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|Authorization|token标记|header|true|string|
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|UserExpireTime.agParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为userExpireTime.ag||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;vstr|[公共]用作标记或验证的冗余数据，将原样返回||false|string|
|&emsp;&emsp;timestamp|[公共]13位时间戳（精确到毫秒）||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|用户过期时间|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|
|vstr|用作标记或验证的冗余数据，与输入保持一致|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": "",
	"vstr": ""
}
```

### 获取用户过期时间

**接口地址**:`/prod-api/api/v1/{appkey}?userExpireTime.nc`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户过期时间</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "loginCode": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|UserExpireTime.ncParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为userExpireTime.nc||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;loginCode|[私有]单码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|用户过期时间|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

## 单码计点API

### 获取用户信息

**接口地址**:`/prod-api/api/v1/{appkey}?appUserInfo.nc`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户信息</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "loginCode": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|AppUserInfo.ncParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为appUserInfo.nc||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;loginCode|[私有]单码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data||AppUserInfo.ncAjaxResultVoData|AppUserInfo.ncAjaxResultVoData|
|&emsp;&emsp;expireTime|用户过期时间，计时模式下有效|string|
|&emsp;&emsp;point|用户剩余点数，计点模式下有效|integer|
|&emsp;&emsp;status|用户状态，0为正常|string|
|&emsp;&emsp;loginLimitU|同时在线用户数限制|integer|
|&emsp;&emsp;loginLimitM|同时在线设备数限制|integer|
|&emsp;&emsp;cardLoginLimitU|由卡密继承来的同时在线用户数限制|integer|
|&emsp;&emsp;cardLoginLimitM|由卡密继承来的同时在线设备数限制|integer|
|&emsp;&emsp;loginTimes|用户登录次数|integer|
|&emsp;&emsp;lastLoginTime|最近登录时间|string|
|&emsp;&emsp;loginIp|最近登录IP|string|
|&emsp;&emsp;freeBalance|当前无实际作用|number|
|&emsp;&emsp;payBalance|当前无实际作用|number|
|&emsp;&emsp;freePayment|当前无实际作用|number|
|&emsp;&emsp;payPayment|当前无实际作用|number|
|&emsp;&emsp;cardCustomParams|由卡密继承来的自定义参数|string|
|&emsp;&emsp;remark|备注信息|string|
|&emsp;&emsp;loginCode|单码信息，单码模式下有效|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"expireTime": "",
		"point": 0,
		"status": "",
		"loginLimitU": 0,
		"loginLimitM": 0,
		"cardLoginLimitU": 0,
		"cardLoginLimitM": 0,
		"loginTimes": 0,
		"lastLoginTime": "",
		"loginIp": "",
		"freeBalance": 0,
		"payBalance": 0,
		"freePayment": 0,
		"payPayment": 0,
		"cardCustomParams": "",
		"remark": "",
		"loginCode": ""
	},
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 获取用户绑定设备

**接口地址**:`/prod-api/api/v1/{appkey}?bindDeviceInfo.nc`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户当前绑定的设备信息</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "loginCode": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|BindDeviceInfo.ncParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为bindDeviceInfo.nc||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;loginCode|[私有]单码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|设备信息列表，注意此处为数组|BindDeviceInfo.ncAjaxResultVoData|BindDeviceInfo.ncAjaxResultVoData|
|&emsp;&emsp;deviceCodeStr|设备码|string|
|&emsp;&emsp;lastLoginTime|设备最后登录时间|string|
|&emsp;&emsp;loginTimes|设备登录次数|string|
|&emsp;&emsp;status|设备状态（0正常 1停用）|string|
|&emsp;&emsp;remark|备注信息|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"deviceCodeStr": "",
		"lastLoginTime": "",
		"loginTimes": "",
		"status": "",
		"remark": ""
	},
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 单码登录

**接口地址**:`/prod-api/api/v1/{appkey}?login.nc`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>单码登录接口</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "loginCode": "",
  "appVer": "",
  "deviceCode": "",
  "md5": "",
  "autoReducePoint": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|Login.ncParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为login.nc||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;loginCode|[私有]单码||true|string|
|&emsp;&emsp;appVer|[私有]软件版本号||true|string|
|&emsp;&emsp;deviceCode|[私有]设备码，如果开启设备绑定，则必须提供||false|string|
|&emsp;&emsp;md5|[私有]软件MD5||false|string|
|&emsp;&emsp;autoReducePoint|[私有]计点模式下是否登录成功后自动扣除用户点数（扣除1），默认值为true||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|登录成功返回token|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 登录码充值

**接口地址**:`/prod-api/api/v1/{appkey}?rechargeLoginCode.nc`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>使用登录码充值</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "loginCode": "",
  "newLoginCode": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|RechargeLoginCode.ncParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为rechargeLoginCode.nc||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;loginCode|[私有]充值到的登录码||true|string|
|&emsp;&emsp;newLoginCode|[私有]用于充值的新登录码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回新的到期时间或点数|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 扣减用户点数

**接口地址**:`/prod-api/api/v1/{appkey}?reducePoint.ag`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>扣减用户点数，返回扣减后点数余额</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "vstr": "",
  "timestamp": "",
  "sign": "",
  "point": "",
  "enableNegative": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|Authorization|token标记|header|true|string|
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|ReducePoint.agParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为reducePoint.ag||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;vstr|[公共]用作标记或验证的冗余数据，将原样返回||false|string|
|&emsp;&emsp;timestamp|[公共]13位时间戳（精确到毫秒）||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;point|[私有]扣减的点数，需传入正数，可精确到两位小数||true|string|
|&emsp;&emsp;enableNegative|[私有]是否允许余额为负数，允许传1，不允许传0，默认为0||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|扣减后点数余额|number|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|
|vstr|用作标记或验证的冗余数据，与输入保持一致|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": 0,
	"msg": "",
	"sign": "",
	"timestamp": "",
	"vstr": ""
}
```

### 解除绑定指定设备

**接口地址**:`/prod-api/api/v1/{appkey}?unbindDevice.nc`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>解除绑定当前设备，解绑成功会根据软件设定扣减用户余额</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "deviceCode": "",
  "loginCode": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|UnbindDevice.ncParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为unbindDevice.nc||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;deviceCode|[私有]设备码||true|string|
|&emsp;&emsp;loginCode|[私有]单码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回0，设备码不存在返回-1|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 获取用户点数余额

**接口地址**:`/prod-api/api/v1/{appkey}?userPoint.ag`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户点数余额</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "vstr": "",
  "timestamp": "",
  "sign": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|Authorization|token标记|header|true|string|
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|UserPoint.agParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为userPoint.ag||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;vstr|[公共]用作标记或验证的冗余数据，将原样返回||false|string|
|&emsp;&emsp;timestamp|[公共]13位时间戳（精确到毫秒）||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|用户用户点数余额|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|
|vstr|用作标记或验证的冗余数据，与输入保持一致|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": "",
	"vstr": ""
}
```

### 获取用户点数余额

**接口地址**:`/prod-api/api/v1/{appkey}?userPoint.nc`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户点数余额</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "loginCode": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|UserPoint.ncParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为userPoint.nc||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;loginCode|[私有]单码||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|用户用户点数余额|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

## 通用API

### 获取软件配置信息

**接口地址**:`/prod-api/api/v1/{appkey}?appInfo.ng`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取软件配置信息</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|AppInfo.ngParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为appInfo.ng||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|软件配置信息|AppInfo.ngAjaxResultVoData|AppInfo.ngAjaxResultVoData|
|&emsp;&emsp;appName|软件名称|string|
|&emsp;&emsp;description|软件描述|string|
|&emsp;&emsp;status|软件状态（0正常 1停用）|string|
|&emsp;&emsp;bindType|绑定模式|string|
|&emsp;&emsp;isCharge|是否开启计费|string|
|&emsp;&emsp;idxUrl|软件主页|string|
|&emsp;&emsp;freeQuotaReg|首次登录赠送免费时间或点数，单位秒或点|integer|
|&emsp;&emsp;reduceQuotaUnbind|换绑设备扣减时间或点数，单位秒或点|integer|
|&emsp;&emsp;authType|认证类型|string|
|&emsp;&emsp;billType|计费类型|string|
|&emsp;&emsp;dataExpireTime|数据包过期时间，单位秒，-1为不限制，默认为-1|integer|
|&emsp;&emsp;loginLimitU|登录用户数量限制，整数，-1为不限制，默认为-1|integer|
|&emsp;&emsp;loginLimitM|登录机器数量限制，整数，-1为不限制，默认为-|integer|
|&emsp;&emsp;limitOper|达到上限后的操作|string|
|&emsp;&emsp;heartBeatTime|心跳包时间，单位秒，客户端若在此时间范围内无任何操作将自动下线，默认为300秒|integer|
|&emsp;&emsp;welcomeNotice|启动公告|string|
|&emsp;&emsp;offNotice|停机公告|string|
|&emsp;&emsp;icon|软件图标地址，当前无实际作用|string|
|&emsp;&emsp;enableTrial|是否开启试用|string|
|&emsp;&emsp;trialTimesPerIp|每个ip可试用设备数，-1为不限制|integer|
|&emsp;&emsp;trialCycle|每个设备试用时间周期，单位秒，0只能试用一次|integer|
|&emsp;&emsp;trialTimes|每个设备每周期试用次数|integer|
|&emsp;&emsp;trialTime|每个设备每次试用时长，单位秒|integer|
|&emsp;&emsp;enableTrialByTimeQuantum|是否开启按时间试用|string|
|&emsp;&emsp;enableTrialByTimes|是否开启按次数试用|string|
|&emsp;&emsp;trialTimeQuantum|试用时间段|string|
|&emsp;&emsp;remark|备注信息|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"appName": "",
		"description": "",
		"status": "",
		"bindType": "",
		"isCharge": "",
		"idxUrl": "",
		"freeQuotaReg": 0,
		"reduceQuotaUnbind": 0,
		"authType": "",
		"billType": "",
		"dataExpireTime": 0,
		"loginLimitU": 0,
		"loginLimitM": 0,
		"limitOper": "",
		"heartBeatTime": 0,
		"welcomeNotice": "",
		"offNotice": "",
		"icon": "",
		"enableTrial": "",
		"trialTimesPerIp": 0,
		"trialCycle": 0,
		"trialTimes": 0,
		"trialTime": 0,
		"enableTrialByTimeQuantum": "",
		"enableTrialByTimes": "",
		"trialTimeQuantum": "",
		"remark": ""
	},
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 获取用户信息

**接口地址**:`/prod-api/api/v1/{appkey}?appUserInfo.ag`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取用户信息</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "vstr": "",
  "timestamp": "",
  "sign": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|Authorization|token标记|header|true|string|
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|AppUserInfo.agParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为appUserInfo.ag||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;vstr|[公共]用作标记或验证的冗余数据，将原样返回||false|string|
|&emsp;&emsp;timestamp|[公共]13位时间戳（精确到毫秒）||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data||AppUserInfo.agAjaxResultVoData|AppUserInfo.agAjaxResultVoData|
|&emsp;&emsp;expireTime|用户过期时间，计时模式下有效|string|
|&emsp;&emsp;point|用户剩余点数，计点模式下有效|integer|
|&emsp;&emsp;status|用户状态，0为正常|string|
|&emsp;&emsp;loginLimitU|同时在线用户数限制|integer|
|&emsp;&emsp;loginLimitM|同时在线设备数限制|integer|
|&emsp;&emsp;cardLoginLimitU|由卡密继承来的同时在线用户数限制|integer|
|&emsp;&emsp;cardLoginLimitM|由卡密继承来的同时在线设备数限制|integer|
|&emsp;&emsp;loginTimes|用户登录次数|integer|
|&emsp;&emsp;lastLoginTime|最近登录时间|string|
|&emsp;&emsp;loginIp|最近登录IP|string|
|&emsp;&emsp;freeBalance|当前无实际作用|number|
|&emsp;&emsp;payBalance|当前无实际作用|number|
|&emsp;&emsp;freePayment|当前无实际作用|number|
|&emsp;&emsp;payPayment|当前无实际作用|number|
|&emsp;&emsp;cardCustomParams|由卡密继承来的自定义参数|string|
|&emsp;&emsp;remark|备注信息|string|
|&emsp;&emsp;userInfo|账号信息，账号模式下有效|AppUserInfo.agAjaxResultVoDataUserInfo|AppUserInfo.agAjaxResultVoDataUserInfo|
|&emsp;&emsp;&emsp;&emsp;avatar|头像|string|
|&emsp;&emsp;&emsp;&emsp;userName|用户名|string|
|&emsp;&emsp;&emsp;&emsp;nickName|用户昵称|string|
|&emsp;&emsp;&emsp;&emsp;sex|性别，0男1女2未知|string|
|&emsp;&emsp;&emsp;&emsp;phonenumber|手机号码|string|
|&emsp;&emsp;&emsp;&emsp;email|邮箱|string|
|&emsp;&emsp;&emsp;&emsp;admin|是否为管理员账号|bool|
|&emsp;&emsp;&emsp;&emsp;availablePayBalance|可用余额|number|
|&emsp;&emsp;&emsp;&emsp;freezePayBalance|冻结余额|number|
|&emsp;&emsp;&emsp;&emsp;loginDate|最近登录时间|string|
|&emsp;&emsp;&emsp;&emsp;loginIp|最近登录IP|string|
|&emsp;&emsp;loginCode|单码信息，单码模式下有效|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|
|vstr|用作标记或验证的冗余数据，与输入保持一致|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"expireTime": "",
		"point": 0,
		"status": "",
		"loginLimitU": 0,
		"loginLimitM": 0,
		"cardLoginLimitU": 0,
		"cardLoginLimitM": 0,
		"loginTimes": 0,
		"lastLoginTime": "",
		"loginIp": "",
		"freeBalance": 0,
		"payBalance": 0,
		"freePayment": 0,
		"payPayment": 0,
		"cardCustomParams": "",
		"remark": "",
		"userInfo": {
			"avatar": "",
			"userName": "",
			"nickName": "",
			"sex": "",
			"phonenumber": "",
			"email": "",
			"admin": "",
			"availablePayBalance": 0,
			"freezePayBalance": 0,
			"loginDate": "",
			"loginIp": ""
		},
		"loginCode": ""
	},
	"msg": "",
	"sign": "",
	"timestamp": "",
	"vstr": ""
}
```

### 远程文件下载

**接口地址**:`/prod-api/api/v1/{appkey}?globalFileDownload.ng`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>远程文件下载</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "fileName": "",
  "returnUrl": "",
  "errorIfNotExist": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|GlobalFileDownload.ngParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为globalFileDownload.ng||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;fileName|[私有]文件名称||true|string|
|&emsp;&emsp;returnUrl|[私有]是返回下载链接，否返回文件base64编码文本，是传1否传0默认为0||false|string|
|&emsp;&emsp;errorIfNotExist|[私有]当文件不存在时是否报错，如果为否则返回空，是传1否传0默认为0||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回文件下载链接或文件base64编码文本，文件不存在返回空|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 远程文件上传

**接口地址**:`/prod-api/api/v1/{appkey}?globalFileUpload.ng`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>远程文件上传</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "fileName": "",
  "base64Str": "",
  "overrideIfExist": "",
  "checkToken": "",
  "checkVip": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|GlobalFileUpload.ngParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为globalFileUpload.ng||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;fileName|[私有]文件名称||true|string|
|&emsp;&emsp;base64Str|[私有]文件base64编码文本||true|string|
|&emsp;&emsp;overrideIfExist|[私有]当文件已存在时是否覆盖，是传1否传0默认为1||false|string|
|&emsp;&emsp;checkToken|[私有]文件是否需要登录才能读写，是传1否传0默认为0||false|string|
|&emsp;&emsp;checkVip|[私有]文件是否需要VIP才能读写，是传1否传0默认为0||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回0|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 执行远程脚本

**接口地址**:`/prod-api/api/v1/{appkey}?globalScript.ng`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>执行远程脚本</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "scriptKey": "",
  "scriptParams": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|GlobalScript.ngParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为globalScript.ng||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;scriptKey|[私有]脚本Key||true|string|
|&emsp;&emsp;scriptParams|[私有]脚本参数||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|执行结果|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 读远程变量

**接口地址**:`/prod-api/api/v1/{appkey}?globalVariableGet.ng`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>读远程变量</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "variableName": "",
  "errorIfNotExist": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|GlobalVariableGet.ngParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为globalVariableGet.ng||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;variableName|[私有]变量名称||true|string|
|&emsp;&emsp;errorIfNotExist|[私有]当变量不存在时是否报错，如果为否则返回空文本，是传1否传0默认为0||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|变量值|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 写远程变量

**接口地址**:`/prod-api/api/v1/{appkey}?globalVariableSet.ng`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>写远程变量</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "variableName": "",
  "variableValue": "",
  "errorIfNotExist": "",
  "checkToken": "",
  "checkVip": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|GlobalVariableSet.ngParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为globalVariableSet.ng||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;variableName|[私有]变量名称||true|string|
|&emsp;&emsp;variableValue|[私有]变量值||true|string|
|&emsp;&emsp;errorIfNotExist|[私有]当变量不存在时是否报错，如果为否则自动创建该名称变量，是传1否传0默认为0||false|string|
|&emsp;&emsp;checkToken|[私有]创建的变量是否需要登录才能读写，是传1否传0默认为0||false|string|
|&emsp;&emsp;checkVip|[私有]创建的变量是否需要VIP才能读写，是传1否传0默认为0||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回0|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 心跳

**接口地址**:`/prod-api/api/v1/{appkey}?heartbeat.ag`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>刷新token过期时间，在软件配置的心跳时间内应至少请求一次本接口，否则系统将自动下线当前用户，请求后返回下次心跳截止时间</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "vstr": "",
  "timestamp": "",
  "sign": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|Authorization|token标记|header|true|string|
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|Heartbeat.agParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为heartbeat.ag||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;vstr|[公共]用作标记或验证的冗余数据，将原样返回||false|string|
|&emsp;&emsp;timestamp|[公共]13位时间戳（精确到毫秒）||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|下次心跳截止时间|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|
|vstr|用作标记或验证的冗余数据，与输入保持一致|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": "",
	"vstr": ""
}
```

### 是否连接到服务器

**接口地址**:`/prod-api/api/v1/{appkey}?isConnected.ng`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>判断是否正常连接到服务器，成功返回服务器13位时间戳（精确到毫秒）</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|IsConnected.ngParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为isConnected.ng||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|服务器13位时间戳（精确到毫秒）|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 获取用户在线状态

**接口地址**:`/prod-api/api/v1/{appkey}?isOnline.ag`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>在线返回1</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "vstr": "",
  "timestamp": "",
  "sign": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|Authorization|token标记|header|true|string|
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|IsOnline.agParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为isOnline.ag||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;vstr|[公共]用作标记或验证的冗余数据，将原样返回||false|string|
|&emsp;&emsp;timestamp|[公共]13位时间戳（精确到毫秒）||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|在线值为1|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|
|vstr|用作标记或验证的冗余数据，与输入保持一致|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": "",
	"vstr": ""
}
```

### 获取最新版本信息

**接口地址**:`/prod-api/api/v1/{appkey}?latestVersionInfo.ng`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取软件最新版本信息</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|LatestVersionInfo.ngParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为latestVersionInfo.ng||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|版本信息|LatestVersionInfo.ngAjaxResultVoData|LatestVersionInfo.ngAjaxResultVoData|
|&emsp;&emsp;versionName|版本名称|string|
|&emsp;&emsp;versionNo|版本号|integer|
|&emsp;&emsp;updateLog|更新日志|string|
|&emsp;&emsp;downloadUrl|下载地址|string|
|&emsp;&emsp;status|版本状态（0正常 1停用）|string|
|&emsp;&emsp;md5|软件MD5|string|
|&emsp;&emsp;forceUpdate|是否强制更新|string|
|&emsp;&emsp;checkMd5|是否校验MD5|string|
|&emsp;&emsp;downloadUrlDirect|直链地址|string|
|&emsp;&emsp;remark|备注信息|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"versionName": "",
		"versionNo": 0,
		"updateLog": "",
		"downloadUrl": "",
		"status": "",
		"md5": "",
		"forceUpdate": "",
		"checkMd5": "",
		"downloadUrlDirect": "",
		"remark": ""
	},
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 获取强制更新版本信息

**接口地址**:`/prod-api/api/v1/{appkey}?latestVersionInfoForceUpdate.ng`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取需要被强制更新到的最低软件版本，如果当前版本低于此版本，则应该启动强制更新策略</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|LatestVersionInfoForceUpdate.ngParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为latestVersionInfoForceUpdate.ng||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|版本信息|LatestVersionInfoForceUpdate.ngAjaxResultVoData|LatestVersionInfoForceUpdate.ngAjaxResultVoData|
|&emsp;&emsp;versionName|版本名称|string|
|&emsp;&emsp;versionNo|版本号|integer|
|&emsp;&emsp;updateLog|更新日志|string|
|&emsp;&emsp;downloadUrl|下载地址|string|
|&emsp;&emsp;status|版本状态（0正常 1停用）|string|
|&emsp;&emsp;md5|软件MD5|string|
|&emsp;&emsp;forceUpdate|是否强制更新|string|
|&emsp;&emsp;checkMd5|是否校验MD5|string|
|&emsp;&emsp;downloadUrlDirect|直链地址|string|
|&emsp;&emsp;remark|备注信息|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"versionName": "",
		"versionNo": 0,
		"updateLog": "",
		"downloadUrl": "",
		"status": "",
		"md5": "",
		"forceUpdate": "",
		"checkMd5": "",
		"downloadUrlDirect": "",
		"remark": ""
	},
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 注销登录

**接口地址**:`/prod-api/api/v1/{appkey}?logout.ag`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>注销登录接口</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "vstr": "",
  "timestamp": "",
  "sign": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|Authorization|token标记|header|true|string|
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|Logout.agParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为logout.ag||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;vstr|[公共]用作标记或验证的冗余数据，将原样返回||false|string|
|&emsp;&emsp;timestamp|[公共]13位时间戳（精确到毫秒）||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回0|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|
|vstr|用作标记或验证的冗余数据，与输入保持一致|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": "",
	"vstr": ""
}
```

### 获取服务器时间

**接口地址**:`/prod-api/api/v1/{appkey}?serverTime.ng`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取服务器时间，格式yyyy-MM-dd HH:mm:ss</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|ServerTime.ngParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为serverTime.ng||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|服务器时间|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 获取时间差

**接口地址**:`/prod-api/api/v1/{appkey}?timeDiff.ng`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取时间差，格式yyyy-MM-dd HH:mm:ss</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "time1": "",
  "time2": "",
  "formatType": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|TimeDiff.ngParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为timeDiff.ng||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;time1|[私有]时间1，默认为当前时间||false|string|
|&emsp;&emsp;time2|[私有]时间2，默认为当前时间||false|string|
|&emsp;&emsp;formatType|[私有]结果格式，0.毫秒数 1.天数 2.友好文本，默认为0||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|时间2-时间1相差的时间|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 试用登录

**接口地址**:`/prod-api/api/v1/{appkey}?trialLogin.ng`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>试用登录接口</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "appVer": "",
  "deviceCode": "",
  "md5": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|TrialLogin.ngParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为trialLogin.ng||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;appVer|[私有]软件版本号||true|string|
|&emsp;&emsp;deviceCode|[私有]设备码，如果开启设备绑定，则必须提供||false|string|
|&emsp;&emsp;md5|[私有]软件MD5||false|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|登录成功返回token|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```

### 解除绑定当前设备

**接口地址**:`/prod-api/api/v1/{appkey}?unbindDevice.ag`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>解除绑定当前设备，解绑成功会根据软件设定扣减用户余额</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "vstr": "",
  "timestamp": "",
  "sign": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|Authorization|token标记|header|true|string|
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|UnbindDevice.agParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为unbindDevice.ag||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;vstr|[公共]用作标记或验证的冗余数据，将原样返回||false|string|
|&emsp;&emsp;timestamp|[公共]13位时间戳（精确到毫秒）||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|成功返回0，设备码不存在返回-1|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|
|vstr|用作标记或验证的冗余数据，与输入保持一致|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"sign": "",
	"timestamp": "",
	"vstr": ""
}
```

### 获取软件版本信息

**接口地址**:`/prod-api/api/v1/{appkey}?versionInfo.ng`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`application/json;charset=UTF-8`

**接口描述**:<p>获取软件当前版本信息</p>

**请求示例**:

```javascript
{
  "api": "",
  "appSecret": "",
  "sign": "",
  "appVer": ""
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 |
| -------- | -------- | ----- | -------- | -------- |
|appkey|AppKey|path|true|string|
|params|请求参数|body|true|VersionInfo.ngParamsVo|
|&emsp;&emsp;api|[公共]请求的API接口，此处为versionInfo.ng||true|string|
|&emsp;&emsp;appSecret|[公共]AppSecret||true|string|
|&emsp;&emsp;sign|[公共]数据签名||true|string|
|&emsp;&emsp;appVer|[私有]软件版本号||true|string|

**响应状态**:

| 状态码 | 说明 |
| -------- | -------- | 
|200|请求完成|


**响应参数**:

| 参数名称 | 参数说明 | 类型 |
| -------- | -------- | ----- |
|code|执行成功返回200，否则返回错误码|integer(int64)|
|data|版本信息|VersionInfo.ngAjaxResultVoData|VersionInfo.ngAjaxResultVoData|
|&emsp;&emsp;versionName|版本名称|string|
|&emsp;&emsp;versionNo|版本号|integer|
|&emsp;&emsp;updateLog|更新日志|string|
|&emsp;&emsp;downloadUrl|下载地址|string|
|&emsp;&emsp;status|版本状态（0正常 1停用）|string|
|&emsp;&emsp;md5|软件MD5|string|
|&emsp;&emsp;forceUpdate|是否强制更新|string|
|&emsp;&emsp;checkMd5|是否校验MD5|string|
|&emsp;&emsp;downloadUrlDirect|直链地址|string|
|&emsp;&emsp;remark|备注信息|string|
|msg|结果说明|string|
|sign|数据签名|string|
|timestamp|结果生成时间|string|

**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"versionName": "",
		"versionNo": 0,
		"updateLog": "",
		"downloadUrl": "",
		"status": "",
		"md5": "",
		"forceUpdate": "",
		"checkMd5": "",
		"downloadUrlDirect": "",
		"remark": ""
	},
	"msg": "",
	"sign": "",
	"timestamp": ""
}
```