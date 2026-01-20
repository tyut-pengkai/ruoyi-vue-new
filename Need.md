# 需求说明
## 接口说明
### 接口内容
| 名称 | 地址 | 请求方式 |
| --- | ---: | :--- |
| 获取用户对文件的操作权限 | open3rd/files/%s/permission | GET |
| 获取文件信息 | open3rd/files/%s | 成熟 |
| 获取文件下载地址 | open3rd/files/%s/download | 需藏 |
### 公共参数Request Headers
! 字段 | 类型 | 必填 | 含义 |
| --- | ---: | :--- | :-- 
|X-TDOCS-Open3rd-Token | string | 否 | 当前请求的用户的第三方系统凭证，WebSDK初始化时设置的fileToken 
|X-TDOCS-Trace-ID | string | 是 | 该请求的trace 
|X-TDOCS-APP-ID | string | 是 | 接入方在文档开放平台注册的APP ID 
|X-TDOCS-Nonce | string | 是 | 随机字符串，64个字符以内 
|X-TDOCS-TimeStamp | string | 是 | 发送请求时的Unix时间戳 
|X-TDOCS-Signature | string | 是 | 请求签名 
|Content-Type | string | 是 | 默认 application/json 

其中，X-TDOCS-Signature的请求签名计算方式如下：
1）参与签名的参数有四个: X-TDOCS-Nonce, X-TDOCS-TimeStamp, X-TDOCS-APP-ID，APP-Secret
2）将这些参数使用URL键值对的格式 （即 key1=value1&key2=value2…）拼接成字符串str。 有两个注意点：1. 字段值采用原始值，不要进行URL转义；2. 必须严格按照如下格式拼接，不可变动字段顺序
3）对str进行SHA1计算，单次签名在60分钟内有效期
### 公共参数Response
第三方存储系统返回 application/json 格式的数据。
| 字段 | 类型 | 必填 | 说明 |
| --- | ---: | :--- | :-- |
|code | int32 | 是 | 错误码 |
|message | string | 是 | 错误说明 |
|data | struct | 是 | 具体业务数据，不同的接口返回不同的结构 |
### 错误代码说明
错误码	说明
| 错误码 | 说明 |
| --- | ---: |
| 10001 | 凭证 X-Open3rd-Token 无效或用户不存在 |
| 10002 | 请求参数错误 |
| 10003 | 系统内部错误 |
| 10004 | 文件不存在 |
| 10005 | 用户操作文件权限不足 |
| 10006 | 签名校验失败 |
### 获取用户对文件的操作权限
#### 请求参数
| 字段 | 位置 | 必填 | 说明 |
| --- | ---: | :--- | :--- |
| file_id | Path | 是 |第三方存储系统文件ID，需要能够被编码的URL中，只允许由数字、字母、下划线、中划线组成，且不能以下划线开头。文件 ID 长度要求不超过 128 位字符串，超出规定长度的将无法校验通过。|
#### 响应参数
| 字段 | 必须 | 类型 | 说明 |
| --- | ---: | :--- | :--- |
| user_id | 否 | string |当前操作的用户 ID，长度100字符以内；如果返回了该字段，则需要实现「2.2.5-批量获取用户信息」接口；预览场景不返回该字段，编辑场景必须返回该字段。|
| read | 否 | bool |当前用户是否具有读权限|
| update | 否 | bool |当前用户是否具有写权限|
| copy | 否 | bool |是否具有拷贝文档内容权限|
| comment | 否 | bool |是否具有评论文档权限|
| print | 否 | bool |是否具有打印文档权限|
| download | 否 | bool |是否具有下载文档权限|
| rename | 否 | bool |是否具有重命名文档权限|
| history | 否 | bool |是否具有查看文档历史记录权限|
| manage | 否 | bool |是否具有管理权限|
### 获取文件信息
#### 请求参数
| 字段 | 位置 | 必填 | 说明 |
| --- | ---: | :--- | :--- |
| file_id | Path | 是 |第三方存储系统文件ID，需要能够被编码的URL中，只允许由数字、字母、下划线、中划线组成，且不能以下划线开头。文件 ID 长度要求不超过 128 位字符串，超出规定长度的将无法校验通过。|
#### 响应参数
| 字段 | 必须 | 类型 | 说明 |
| --- | ---: | :--- | :--- |
| id | 是 | string |文件ID，不能为空,可由数字、字母和下划线组成，但不能以下划线开头。文件 ID 在接入方系统中不能具有二义性（保持唯一）。文件 ID 长度要求不超过 128 位字符串，超出规定长度的将无法校验通过。|
| name | 是 | string |文件名称，不能为空，必须带后缀|
| version | 否 | int64 |文件版本，从1开始，每次编辑保存后递增|
| create_time | 否 | int64 |文件创建的 Unix 时间戳，秒|
| update_time | 否 | int64 |文件最后更新的 Unix 时间戳，秒|
| creator_id | 是 | string |文档所有者用户 ID，编辑场景必需|
| modifier_id | 是 | string |文档最后编辑者ID|
| size | 是 | uint64 |文件大小，byte，不能为0，目前支持的导入大小上限为1G。|
| disable_watermark | 否 | bool |导入文档是否导入原生水印，false：导入原生水印。true: 去除原生水印。暂时只支持doc。不返回默认为false|
### 获取文件下载地址
#### 请求参数
| 字段 | 位置 | 必填 | 说明 |
| --- | ---: | :--- | :--- |
| file_id | Path | 是 |第三方存储系统文件ID，需要能够被编码的URL中，只允许由数字、字母、下划线、中划线组成，且不能以下划线开头。文件 ID 长度要求不超过 128 位字符串，超出规定长度的将无法校验通过。|
#### 响应参数
| 字段 | 必须 | 类型 | 说明 |
| --- | ---: | :--- | :--- |
| url | 是 | string | 文件下载链接，如需鉴权则需要带上鉴权参数,需要支持分片下载，协议参考HTTP 请求范围 - HTTP  MDN。|
| digest | 否 | string | 文件 checksum |
| digest_algorithm | 否 | string | md5/sha1 |
