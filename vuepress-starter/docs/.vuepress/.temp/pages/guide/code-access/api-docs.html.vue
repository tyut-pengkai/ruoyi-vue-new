<template><div><h1 id="api-接口" tabindex="-1"><a class="header-anchor" href="#api-接口" aria-hidden="true">#</a> API 接口 <img src="/images/version.svg"></h1>
<h2 id="账号计时api" tabindex="-1"><a class="header-anchor" href="#账号计时api" aria-hidden="true">#</a> 账号计时API</h2>
<h3 id="获取用户信息" tabindex="-1"><a class="header-anchor" href="#获取用户信息" aria-hidden="true">#</a> 获取用户信息</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?appUserInfo.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户信息</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>AppUserInfo.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为appUserInfo.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]账号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td></td>
<td>AppUserInfo.nuAjaxResultVoData</td>
</tr>
<tr>
<td>  expireTime</td>
<td>用户过期时间，计时模式下有效</td>
<td>string</td>
</tr>
<tr>
<td>  point</td>
<td>用户剩余点数，计点模式下有效</td>
<td>integer</td>
</tr>
<tr>
<td>  status</td>
<td>用户状态，0为正常</td>
<td>string</td>
</tr>
<tr>
<td>  loginLimitU</td>
<td>同时在线用户数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  loginLimitM</td>
<td>同时在线设备数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  cardLoginLimitU</td>
<td>由卡密继承来的同时在线用户数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  cardLoginLimitM</td>
<td>由卡密继承来的同时在线设备数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  loginTimes</td>
<td>用户登录次数</td>
<td>integer</td>
</tr>
<tr>
<td>  lastLoginTime</td>
<td>最近登录时间</td>
<td>string</td>
</tr>
<tr>
<td>  loginIp</td>
<td>最近登录IP</td>
<td>string</td>
</tr>
<tr>
<td>  freeBalance</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  payBalance</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  freePayment</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  payPayment</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  cardCustomParams</td>
<td>由卡密继承来的自定义参数</td>
<td>string</td>
</tr>
<tr>
<td>  remark</td>
<td>备注信息</td>
<td>string</td>
</tr>
<tr>
<td>  userInfo</td>
<td>账号信息，账号模式下有效</td>
<td>AppUserInfo.nuAjaxResultVoDataUserInfo</td>
</tr>
<tr>
<td>    avatar</td>
<td>头像</td>
<td>string</td>
</tr>
<tr>
<td>    userName</td>
<td>用户名</td>
<td>string</td>
</tr>
<tr>
<td>    nickName</td>
<td>用户昵称</td>
<td>string</td>
</tr>
<tr>
<td>    sex</td>
<td>性别，0男1女2未知</td>
<td>string</td>
</tr>
<tr>
<td>    phonenumber</td>
<td>手机号码</td>
<td>string</td>
</tr>
<tr>
<td>    email</td>
<td>邮箱</td>
<td>string</td>
</tr>
<tr>
<td>    admin</td>
<td>是否为管理员账号</td>
<td>bool</td>
</tr>
<tr>
<td>    availablePayBalance</td>
<td>可用余额</td>
<td>number</td>
</tr>
<tr>
<td>    freezePayBalance</td>
<td>冻结余额</td>
<td>number</td>
</tr>
<tr>
<td>    loginDate</td>
<td>最近登录时间</td>
<td>string</td>
</tr>
<tr>
<td>    loginIp</td>
<td>最近登录IP</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token punctuation">{</span>
		<span class="token string-property property">"expireTime"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"point"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"status"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginLimitU"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginLimitM"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"cardLoginLimitU"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"cardLoginLimitM"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginTimes"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"lastLoginTime"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginIp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"freeBalance"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"payBalance"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"freePayment"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"payPayment"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"cardCustomParams"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"remark"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"userInfo"</span><span class="token operator">:</span> <span class="token punctuation">{</span>
			<span class="token string-property property">"avatar"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"userName"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"nickName"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"sex"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"phonenumber"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"email"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"admin"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"availablePayBalance"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
			<span class="token string-property property">"freezePayBalance"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
			<span class="token string-property property">"loginDate"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"loginIp"</span><span class="token operator">:</span> <span class="token string">""</span>
		<span class="token punctuation">}</span>
	<span class="token punctuation">}</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取用户绑定设备" tabindex="-1"><a class="header-anchor" href="#获取用户绑定设备" aria-hidden="true">#</a> 获取用户绑定设备</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?bindDeviceInfo.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户当前绑定的设备信息</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>BindDeviceInfo.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为bindDeviceInfo.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]账号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>设备信息列表，注意此处为数组</td>
<td>BindDeviceInfo.nuAjaxResultVoData</td>
</tr>
<tr>
<td>  deviceCodeStr</td>
<td>设备码</td>
<td>string</td>
</tr>
<tr>
<td>  lastLoginTime</td>
<td>设备最后登录时间</td>
<td>string</td>
</tr>
<tr>
<td>  loginTimes</td>
<td>设备登录次数</td>
<td>string</td>
</tr>
<tr>
<td>  status</td>
<td>设备状态（0正常 1停用）</td>
<td>string</td>
</tr>
<tr>
<td>  remark</td>
<td>备注信息</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token punctuation">{</span>
		<span class="token string-property property">"deviceCodeStr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"lastLoginTime"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginTimes"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"status"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"remark"</span><span class="token operator">:</span> <span class="token string">""</span>
	<span class="token punctuation">}</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="用户名是否存在" tabindex="-1"><a class="header-anchor" href="#用户名是否存在" aria-hidden="true">#</a> 用户名是否存在</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?isUserNameExist.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>检查用户名是否已存在，存在返回1，否则返回0</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>IsUserNameExist.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为isUserNameExist.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]要检查的用户名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>存在返回1，否则返回0</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="账号登录" tabindex="-1"><a class="header-anchor" href="#账号登录" aria-hidden="true">#</a> 账号登录</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?login.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>账号登录接口</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"password"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appVer"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"deviceCode"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"md5"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"autoReducePoint"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>Login.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为login.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]账号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  password</td>
<td>[私有]密码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appVer</td>
<td>[私有]软件版本号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  deviceCode</td>
<td>[私有]设备码，如果开启设备绑定，则必须提供</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  md5</td>
<td>[私有]软件MD5</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  autoReducePoint</td>
<td>[私有]计点模式下是否登录成功后自动扣除用户点数（扣除1），默认值为true</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>登录成功返回token</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="注销所有登录" tabindex="-1"><a class="header-anchor" href="#注销所有登录" aria-hidden="true">#</a> 注销所有登录</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?logoutAll.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>注销指定账号在本软件的所有登录</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"password"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>LogoutAll.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为logoutAll.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]要注销的账号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  password</td>
<td>[私有]密码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回0</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="充值卡充值" tabindex="-1"><a class="header-anchor" href="#充值卡充值" aria-hidden="true">#</a> 充值卡充值</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?rechargeCard.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>使用充值卡充值</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"password"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"validPassword"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"cardNo"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"cardPassword"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>RechargeCard.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为rechargeCard.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]充值用户</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  password</td>
<td>[私有]充值用户密码</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  validPassword</td>
<td>[私有]是否验证充值用户密码，防止充错用户，验证传1，不验证传0，默认为0</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  cardNo</td>
<td>[私有]充值卡号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  cardPassword</td>
<td>[私有]充值卡密码</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回新的到期时间或点数</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="扣减用户时间" tabindex="-1"><a class="header-anchor" href="#扣减用户时间" aria-hidden="true">#</a> 扣减用户时间</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?reduceTime.ag</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>扣减用户时间，返回扣减后到期时间</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"seconds"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"enableNegative"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>Authorization</td>
<td>token标记</td>
<td>header</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>ReduceTime.agParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为reduceTime.ag</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  vstr</td>
<td>[公共]用作标记或验证的冗余数据，将原样返回</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  timestamp</td>
<td>[公共]13位时间戳（精确到毫秒）</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  seconds</td>
<td>[私有]扣减的秒数，需传入正整数</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  enableNegative</td>
<td>[私有]是否允许用户过期，允许传1，不允许传0，默认为0</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>扣减后到期时间</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
<tr>
<td>vstr</td>
<td>用作标记或验证的冗余数据，与输入保持一致</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="注册新账号" tabindex="-1"><a class="header-anchor" href="#注册新账号" aria-hidden="true">#</a> 注册新账号</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?register.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>注册新账号</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"password"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"passwordRepeat"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>Register.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为register.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]账号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  password</td>
<td>[私有]密码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  passwordRepeat</td>
<td>[私有]重复密码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回0</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="解除绑定指定设备" tabindex="-1"><a class="header-anchor" href="#解除绑定指定设备" aria-hidden="true">#</a> 解除绑定指定设备</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?unbindDevice.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>解除绑定当前设备，解绑成功会根据软件设定扣减用户余额</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"deviceCode"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"password"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>UnbindDevice.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为unbindDevice.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  deviceCode</td>
<td>[私有]设备码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]账号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  password</td>
<td>[私有]密码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回0，设备码不存在返回-1</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="修改账号密码" tabindex="-1"><a class="header-anchor" href="#修改账号密码" aria-hidden="true">#</a> 修改账号密码</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?updatePassword.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>修改账号密码</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"password"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"newPassword"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"newPasswordRepeat"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>UpdatePassword.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为updatePassword.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]账号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  password</td>
<td>[私有]原密码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  newPassword</td>
<td>[私有]新密码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  newPasswordRepeat</td>
<td>[私有]重复新密码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回0</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取用户过期时间" tabindex="-1"><a class="header-anchor" href="#获取用户过期时间" aria-hidden="true">#</a> 获取用户过期时间</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?userExpireTime.ag</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户过期时间</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>Authorization</td>
<td>token标记</td>
<td>header</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>UserExpireTime.agParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为userExpireTime.ag</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  vstr</td>
<td>[公共]用作标记或验证的冗余数据，将原样返回</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  timestamp</td>
<td>[公共]13位时间戳（精确到毫秒）</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>用户过期时间</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
<tr>
<td>vstr</td>
<td>用作标记或验证的冗余数据，与输入保持一致</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取用户过期时间-1" tabindex="-1"><a class="header-anchor" href="#获取用户过期时间-1" aria-hidden="true">#</a> 获取用户过期时间</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?userExpireTime.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户过期时间</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>UserExpireTime.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为userExpireTime.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]账号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>用户过期时间</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="账号计点api" tabindex="-1"><a class="header-anchor" href="#账号计点api" aria-hidden="true">#</a> 账号计点API</h2>
<h3 id="获取用户信息-1" tabindex="-1"><a class="header-anchor" href="#获取用户信息-1" aria-hidden="true">#</a> 获取用户信息</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?appUserInfo.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户信息</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>AppUserInfo.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为appUserInfo.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]账号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td></td>
<td>AppUserInfo.nuAjaxResultVoData</td>
</tr>
<tr>
<td>  expireTime</td>
<td>用户过期时间，计时模式下有效</td>
<td>string</td>
</tr>
<tr>
<td>  point</td>
<td>用户剩余点数，计点模式下有效</td>
<td>integer</td>
</tr>
<tr>
<td>  status</td>
<td>用户状态，0为正常</td>
<td>string</td>
</tr>
<tr>
<td>  loginLimitU</td>
<td>同时在线用户数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  loginLimitM</td>
<td>同时在线设备数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  cardLoginLimitU</td>
<td>由卡密继承来的同时在线用户数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  cardLoginLimitM</td>
<td>由卡密继承来的同时在线设备数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  loginTimes</td>
<td>用户登录次数</td>
<td>integer</td>
</tr>
<tr>
<td>  lastLoginTime</td>
<td>最近登录时间</td>
<td>string</td>
</tr>
<tr>
<td>  loginIp</td>
<td>最近登录IP</td>
<td>string</td>
</tr>
<tr>
<td>  freeBalance</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  payBalance</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  freePayment</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  payPayment</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  cardCustomParams</td>
<td>由卡密继承来的自定义参数</td>
<td>string</td>
</tr>
<tr>
<td>  remark</td>
<td>备注信息</td>
<td>string</td>
</tr>
<tr>
<td>  userInfo</td>
<td>账号信息，账号模式下有效</td>
<td>AppUserInfo.nuAjaxResultVoDataUserInfo</td>
</tr>
<tr>
<td>    avatar</td>
<td>头像</td>
<td>string</td>
</tr>
<tr>
<td>    userName</td>
<td>用户名</td>
<td>string</td>
</tr>
<tr>
<td>    nickName</td>
<td>用户昵称</td>
<td>string</td>
</tr>
<tr>
<td>    sex</td>
<td>性别，0男1女2未知</td>
<td>string</td>
</tr>
<tr>
<td>    phonenumber</td>
<td>手机号码</td>
<td>string</td>
</tr>
<tr>
<td>    email</td>
<td>邮箱</td>
<td>string</td>
</tr>
<tr>
<td>    admin</td>
<td>是否为管理员账号</td>
<td>bool</td>
</tr>
<tr>
<td>    availablePayBalance</td>
<td>可用余额</td>
<td>number</td>
</tr>
<tr>
<td>    freezePayBalance</td>
<td>冻结余额</td>
<td>number</td>
</tr>
<tr>
<td>    loginDate</td>
<td>最近登录时间</td>
<td>string</td>
</tr>
<tr>
<td>    loginIp</td>
<td>最近登录IP</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token punctuation">{</span>
		<span class="token string-property property">"expireTime"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"point"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"status"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginLimitU"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginLimitM"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"cardLoginLimitU"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"cardLoginLimitM"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginTimes"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"lastLoginTime"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginIp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"freeBalance"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"payBalance"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"freePayment"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"payPayment"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"cardCustomParams"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"remark"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"userInfo"</span><span class="token operator">:</span> <span class="token punctuation">{</span>
			<span class="token string-property property">"avatar"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"userName"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"nickName"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"sex"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"phonenumber"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"email"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"admin"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"availablePayBalance"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
			<span class="token string-property property">"freezePayBalance"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
			<span class="token string-property property">"loginDate"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"loginIp"</span><span class="token operator">:</span> <span class="token string">""</span>
		<span class="token punctuation">}</span>
	<span class="token punctuation">}</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取用户绑定设备-1" tabindex="-1"><a class="header-anchor" href="#获取用户绑定设备-1" aria-hidden="true">#</a> 获取用户绑定设备</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?bindDeviceInfo.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户当前绑定的设备信息</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>BindDeviceInfo.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为bindDeviceInfo.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]账号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>设备信息列表，注意此处为数组</td>
<td>BindDeviceInfo.nuAjaxResultVoData</td>
</tr>
<tr>
<td>  deviceCodeStr</td>
<td>设备码</td>
<td>string</td>
</tr>
<tr>
<td>  lastLoginTime</td>
<td>设备最后登录时间</td>
<td>string</td>
</tr>
<tr>
<td>  loginTimes</td>
<td>设备登录次数</td>
<td>string</td>
</tr>
<tr>
<td>  status</td>
<td>设备状态（0正常 1停用）</td>
<td>string</td>
</tr>
<tr>
<td>  remark</td>
<td>备注信息</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token punctuation">{</span>
		<span class="token string-property property">"deviceCodeStr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"lastLoginTime"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginTimes"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"status"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"remark"</span><span class="token operator">:</span> <span class="token string">""</span>
	<span class="token punctuation">}</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="用户名是否存在-1" tabindex="-1"><a class="header-anchor" href="#用户名是否存在-1" aria-hidden="true">#</a> 用户名是否存在</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?isUserNameExist.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>检查用户名是否已存在，存在返回1，否则返回0</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>IsUserNameExist.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为isUserNameExist.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]要检查的用户名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>存在返回1，否则返回0</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="账号登录-1" tabindex="-1"><a class="header-anchor" href="#账号登录-1" aria-hidden="true">#</a> 账号登录</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?login.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>账号登录接口</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"password"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appVer"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"deviceCode"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"md5"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"autoReducePoint"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>Login.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为login.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]账号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  password</td>
<td>[私有]密码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appVer</td>
<td>[私有]软件版本号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  deviceCode</td>
<td>[私有]设备码，如果开启设备绑定，则必须提供</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  md5</td>
<td>[私有]软件MD5</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  autoReducePoint</td>
<td>[私有]计点模式下是否登录成功后自动扣除用户点数（扣除1），默认值为true</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>登录成功返回token</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="注销所有登录-1" tabindex="-1"><a class="header-anchor" href="#注销所有登录-1" aria-hidden="true">#</a> 注销所有登录</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?logoutAll.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>注销指定账号在本软件的所有登录</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"password"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>LogoutAll.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为logoutAll.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]要注销的账号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  password</td>
<td>[私有]密码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回0</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="充值卡充值-1" tabindex="-1"><a class="header-anchor" href="#充值卡充值-1" aria-hidden="true">#</a> 充值卡充值</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?rechargeCard.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>使用充值卡充值</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"password"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"validPassword"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"cardNo"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"cardPassword"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>RechargeCard.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为rechargeCard.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]充值用户</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  password</td>
<td>[私有]充值用户密码</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  validPassword</td>
<td>[私有]是否验证充值用户密码，防止充错用户，验证传1，不验证传0，默认为0</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  cardNo</td>
<td>[私有]充值卡号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  cardPassword</td>
<td>[私有]充值卡密码</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回新的到期时间或点数</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="扣减用户点数" tabindex="-1"><a class="header-anchor" href="#扣减用户点数" aria-hidden="true">#</a> 扣减用户点数</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?reducePoint.ag</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>扣减用户点数，返回扣减后点数余额</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"point"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"enableNegative"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>Authorization</td>
<td>token标记</td>
<td>header</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>ReducePoint.agParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为reducePoint.ag</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  vstr</td>
<td>[公共]用作标记或验证的冗余数据，将原样返回</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  timestamp</td>
<td>[公共]13位时间戳（精确到毫秒）</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  point</td>
<td>[私有]扣减的点数，需传入正数，可精确到两位小数</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  enableNegative</td>
<td>[私有]是否允许余额为负数，允许传1，不允许传0，默认为0</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>扣减后点数余额</td>
<td>number</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
<tr>
<td>vstr</td>
<td>用作标记或验证的冗余数据，与输入保持一致</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="注册新账号-1" tabindex="-1"><a class="header-anchor" href="#注册新账号-1" aria-hidden="true">#</a> 注册新账号</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?register.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>注册新账号</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"password"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"passwordRepeat"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>Register.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为register.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]账号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  password</td>
<td>[私有]密码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  passwordRepeat</td>
<td>[私有]重复密码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回0</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="解除绑定指定设备-1" tabindex="-1"><a class="header-anchor" href="#解除绑定指定设备-1" aria-hidden="true">#</a> 解除绑定指定设备</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?unbindDevice.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>解除绑定当前设备，解绑成功会根据软件设定扣减用户余额</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"deviceCode"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"password"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>UnbindDevice.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为unbindDevice.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  deviceCode</td>
<td>[私有]设备码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]账号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  password</td>
<td>[私有]密码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回0，设备码不存在返回-1</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="修改账号密码-1" tabindex="-1"><a class="header-anchor" href="#修改账号密码-1" aria-hidden="true">#</a> 修改账号密码</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?updatePassword.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>修改账号密码</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"password"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"newPassword"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"newPasswordRepeat"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>UpdatePassword.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为updatePassword.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]账号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  password</td>
<td>[私有]原密码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  newPassword</td>
<td>[私有]新密码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  newPasswordRepeat</td>
<td>[私有]重复新密码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回0</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取用户点数余额" tabindex="-1"><a class="header-anchor" href="#获取用户点数余额" aria-hidden="true">#</a> 获取用户点数余额</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?userPoint.ag</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户点数余额</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>Authorization</td>
<td>token标记</td>
<td>header</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>UserPoint.agParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为userPoint.ag</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  vstr</td>
<td>[公共]用作标记或验证的冗余数据，将原样返回</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  timestamp</td>
<td>[公共]13位时间戳（精确到毫秒）</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>用户用户点数余额</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
<tr>
<td>vstr</td>
<td>用作标记或验证的冗余数据，与输入保持一致</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取用户点数余额-1" tabindex="-1"><a class="header-anchor" href="#获取用户点数余额-1" aria-hidden="true">#</a> 获取用户点数余额</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?userPoint.nu</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户点数余额</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"username"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>UserPoint.nuParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为userPoint.nu</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  username</td>
<td>[私有]账号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>用户用户点数余额</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="单码计时api" tabindex="-1"><a class="header-anchor" href="#单码计时api" aria-hidden="true">#</a> 单码计时API</h2>
<h3 id="获取用户信息-2" tabindex="-1"><a class="header-anchor" href="#获取用户信息-2" aria-hidden="true">#</a> 获取用户信息</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?appUserInfo.nc</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户信息</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"loginCode"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>AppUserInfo.ncParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为appUserInfo.nc</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  loginCode</td>
<td>[私有]单码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td></td>
<td>AppUserInfo.ncAjaxResultVoData</td>
</tr>
<tr>
<td>  expireTime</td>
<td>用户过期时间，计时模式下有效</td>
<td>string</td>
</tr>
<tr>
<td>  point</td>
<td>用户剩余点数，计点模式下有效</td>
<td>integer</td>
</tr>
<tr>
<td>  status</td>
<td>用户状态，0为正常</td>
<td>string</td>
</tr>
<tr>
<td>  loginLimitU</td>
<td>同时在线用户数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  loginLimitM</td>
<td>同时在线设备数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  cardLoginLimitU</td>
<td>由卡密继承来的同时在线用户数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  cardLoginLimitM</td>
<td>由卡密继承来的同时在线设备数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  loginTimes</td>
<td>用户登录次数</td>
<td>integer</td>
</tr>
<tr>
<td>  lastLoginTime</td>
<td>最近登录时间</td>
<td>string</td>
</tr>
<tr>
<td>  loginIp</td>
<td>最近登录IP</td>
<td>string</td>
</tr>
<tr>
<td>  freeBalance</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  payBalance</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  freePayment</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  payPayment</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  cardCustomParams</td>
<td>由卡密继承来的自定义参数</td>
<td>string</td>
</tr>
<tr>
<td>  remark</td>
<td>备注信息</td>
<td>string</td>
</tr>
<tr>
<td>  loginCode</td>
<td>单码信息，单码模式下有效</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token punctuation">{</span>
		<span class="token string-property property">"expireTime"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"point"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"status"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginLimitU"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginLimitM"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"cardLoginLimitU"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"cardLoginLimitM"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginTimes"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"lastLoginTime"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginIp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"freeBalance"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"payBalance"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"freePayment"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"payPayment"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"cardCustomParams"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"remark"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginCode"</span><span class="token operator">:</span> <span class="token string">""</span>
	<span class="token punctuation">}</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取用户绑定设备-2" tabindex="-1"><a class="header-anchor" href="#获取用户绑定设备-2" aria-hidden="true">#</a> 获取用户绑定设备</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?bindDeviceInfo.nc</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户当前绑定的设备信息</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"loginCode"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>BindDeviceInfo.ncParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为bindDeviceInfo.nc</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  loginCode</td>
<td>[私有]单码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>设备信息列表，注意此处为数组</td>
<td>BindDeviceInfo.ncAjaxResultVoData</td>
</tr>
<tr>
<td>  deviceCodeStr</td>
<td>设备码</td>
<td>string</td>
</tr>
<tr>
<td>  lastLoginTime</td>
<td>设备最后登录时间</td>
<td>string</td>
</tr>
<tr>
<td>  loginTimes</td>
<td>设备登录次数</td>
<td>string</td>
</tr>
<tr>
<td>  status</td>
<td>设备状态（0正常 1停用）</td>
<td>string</td>
</tr>
<tr>
<td>  remark</td>
<td>备注信息</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token punctuation">{</span>
		<span class="token string-property property">"deviceCodeStr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"lastLoginTime"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginTimes"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"status"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"remark"</span><span class="token operator">:</span> <span class="token string">""</span>
	<span class="token punctuation">}</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="单码登录" tabindex="-1"><a class="header-anchor" href="#单码登录" aria-hidden="true">#</a> 单码登录</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?login.nc</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>单码登录接口</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"loginCode"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appVer"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"deviceCode"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"md5"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"autoReducePoint"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>Login.ncParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为login.nc</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  loginCode</td>
<td>[私有]单码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appVer</td>
<td>[私有]软件版本号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  deviceCode</td>
<td>[私有]设备码，如果开启设备绑定，则必须提供</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  md5</td>
<td>[私有]软件MD5</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  autoReducePoint</td>
<td>[私有]计点模式下是否登录成功后自动扣除用户点数（扣除1），默认值为true</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>登录成功返回token</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="登录码充值" tabindex="-1"><a class="header-anchor" href="#登录码充值" aria-hidden="true">#</a> 登录码充值</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?rechargeLoginCode.nc</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>使用登录码充值</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"loginCode"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"newLoginCode"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>RechargeLoginCode.ncParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为rechargeLoginCode.nc</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  loginCode</td>
<td>[私有]充值到的登录码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  newLoginCode</td>
<td>[私有]用于充值的新登录码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回新的到期时间或点数</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="扣减用户时间-1" tabindex="-1"><a class="header-anchor" href="#扣减用户时间-1" aria-hidden="true">#</a> 扣减用户时间</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?reduceTime.ag</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>扣减用户时间，返回扣减后到期时间</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"seconds"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"enableNegative"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>Authorization</td>
<td>token标记</td>
<td>header</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>ReduceTime.agParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为reduceTime.ag</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  vstr</td>
<td>[公共]用作标记或验证的冗余数据，将原样返回</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  timestamp</td>
<td>[公共]13位时间戳（精确到毫秒）</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  seconds</td>
<td>[私有]扣减的秒数，需传入正整数</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  enableNegative</td>
<td>[私有]是否允许用户过期，允许传1，不允许传0，默认为0</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>扣减后到期时间</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
<tr>
<td>vstr</td>
<td>用作标记或验证的冗余数据，与输入保持一致</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="解除绑定指定设备-2" tabindex="-1"><a class="header-anchor" href="#解除绑定指定设备-2" aria-hidden="true">#</a> 解除绑定指定设备</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?unbindDevice.nc</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>解除绑定当前设备，解绑成功会根据软件设定扣减用户余额</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"deviceCode"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"loginCode"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>UnbindDevice.ncParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为unbindDevice.nc</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  deviceCode</td>
<td>[私有]设备码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  loginCode</td>
<td>[私有]单码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回0，设备码不存在返回-1</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取用户过期时间-2" tabindex="-1"><a class="header-anchor" href="#获取用户过期时间-2" aria-hidden="true">#</a> 获取用户过期时间</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?userExpireTime.ag</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户过期时间</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>Authorization</td>
<td>token标记</td>
<td>header</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>UserExpireTime.agParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为userExpireTime.ag</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  vstr</td>
<td>[公共]用作标记或验证的冗余数据，将原样返回</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  timestamp</td>
<td>[公共]13位时间戳（精确到毫秒）</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>用户过期时间</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
<tr>
<td>vstr</td>
<td>用作标记或验证的冗余数据，与输入保持一致</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取用户过期时间-3" tabindex="-1"><a class="header-anchor" href="#获取用户过期时间-3" aria-hidden="true">#</a> 获取用户过期时间</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?userExpireTime.nc</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户过期时间</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"loginCode"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>UserExpireTime.ncParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为userExpireTime.nc</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  loginCode</td>
<td>[私有]单码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>用户过期时间</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="单码计点api" tabindex="-1"><a class="header-anchor" href="#单码计点api" aria-hidden="true">#</a> 单码计点API</h2>
<h3 id="获取用户信息-3" tabindex="-1"><a class="header-anchor" href="#获取用户信息-3" aria-hidden="true">#</a> 获取用户信息</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?appUserInfo.nc</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户信息</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"loginCode"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>AppUserInfo.ncParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为appUserInfo.nc</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  loginCode</td>
<td>[私有]单码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td></td>
<td>AppUserInfo.ncAjaxResultVoData</td>
</tr>
<tr>
<td>  expireTime</td>
<td>用户过期时间，计时模式下有效</td>
<td>string</td>
</tr>
<tr>
<td>  point</td>
<td>用户剩余点数，计点模式下有效</td>
<td>integer</td>
</tr>
<tr>
<td>  status</td>
<td>用户状态，0为正常</td>
<td>string</td>
</tr>
<tr>
<td>  loginLimitU</td>
<td>同时在线用户数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  loginLimitM</td>
<td>同时在线设备数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  cardLoginLimitU</td>
<td>由卡密继承来的同时在线用户数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  cardLoginLimitM</td>
<td>由卡密继承来的同时在线设备数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  loginTimes</td>
<td>用户登录次数</td>
<td>integer</td>
</tr>
<tr>
<td>  lastLoginTime</td>
<td>最近登录时间</td>
<td>string</td>
</tr>
<tr>
<td>  loginIp</td>
<td>最近登录IP</td>
<td>string</td>
</tr>
<tr>
<td>  freeBalance</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  payBalance</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  freePayment</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  payPayment</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  cardCustomParams</td>
<td>由卡密继承来的自定义参数</td>
<td>string</td>
</tr>
<tr>
<td>  remark</td>
<td>备注信息</td>
<td>string</td>
</tr>
<tr>
<td>  loginCode</td>
<td>单码信息，单码模式下有效</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token punctuation">{</span>
		<span class="token string-property property">"expireTime"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"point"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"status"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginLimitU"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginLimitM"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"cardLoginLimitU"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"cardLoginLimitM"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginTimes"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"lastLoginTime"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginIp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"freeBalance"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"payBalance"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"freePayment"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"payPayment"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"cardCustomParams"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"remark"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginCode"</span><span class="token operator">:</span> <span class="token string">""</span>
	<span class="token punctuation">}</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取用户绑定设备-3" tabindex="-1"><a class="header-anchor" href="#获取用户绑定设备-3" aria-hidden="true">#</a> 获取用户绑定设备</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?bindDeviceInfo.nc</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户当前绑定的设备信息</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"loginCode"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>BindDeviceInfo.ncParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为bindDeviceInfo.nc</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  loginCode</td>
<td>[私有]单码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>设备信息列表，注意此处为数组</td>
<td>BindDeviceInfo.ncAjaxResultVoData</td>
</tr>
<tr>
<td>  deviceCodeStr</td>
<td>设备码</td>
<td>string</td>
</tr>
<tr>
<td>  lastLoginTime</td>
<td>设备最后登录时间</td>
<td>string</td>
</tr>
<tr>
<td>  loginTimes</td>
<td>设备登录次数</td>
<td>string</td>
</tr>
<tr>
<td>  status</td>
<td>设备状态（0正常 1停用）</td>
<td>string</td>
</tr>
<tr>
<td>  remark</td>
<td>备注信息</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token punctuation">{</span>
		<span class="token string-property property">"deviceCodeStr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"lastLoginTime"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginTimes"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"status"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"remark"</span><span class="token operator">:</span> <span class="token string">""</span>
	<span class="token punctuation">}</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="单码登录-1" tabindex="-1"><a class="header-anchor" href="#单码登录-1" aria-hidden="true">#</a> 单码登录</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?login.nc</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>单码登录接口</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"loginCode"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appVer"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"deviceCode"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"md5"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"autoReducePoint"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>Login.ncParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为login.nc</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  loginCode</td>
<td>[私有]单码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appVer</td>
<td>[私有]软件版本号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  deviceCode</td>
<td>[私有]设备码，如果开启设备绑定，则必须提供</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  md5</td>
<td>[私有]软件MD5</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  autoReducePoint</td>
<td>[私有]计点模式下是否登录成功后自动扣除用户点数（扣除1），默认值为true</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>登录成功返回token</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="登录码充值-1" tabindex="-1"><a class="header-anchor" href="#登录码充值-1" aria-hidden="true">#</a> 登录码充值</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?rechargeLoginCode.nc</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>使用登录码充值</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"loginCode"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"newLoginCode"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>RechargeLoginCode.ncParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为rechargeLoginCode.nc</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  loginCode</td>
<td>[私有]充值到的登录码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  newLoginCode</td>
<td>[私有]用于充值的新登录码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回新的到期时间或点数</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="扣减用户点数-1" tabindex="-1"><a class="header-anchor" href="#扣减用户点数-1" aria-hidden="true">#</a> 扣减用户点数</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?reducePoint.ag</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>扣减用户点数，返回扣减后点数余额</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"point"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"enableNegative"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>Authorization</td>
<td>token标记</td>
<td>header</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>ReducePoint.agParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为reducePoint.ag</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  vstr</td>
<td>[公共]用作标记或验证的冗余数据，将原样返回</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  timestamp</td>
<td>[公共]13位时间戳（精确到毫秒）</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  point</td>
<td>[私有]扣减的点数，需传入正数，可精确到两位小数</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  enableNegative</td>
<td>[私有]是否允许余额为负数，允许传1，不允许传0，默认为0</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>扣减后点数余额</td>
<td>number</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
<tr>
<td>vstr</td>
<td>用作标记或验证的冗余数据，与输入保持一致</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="解除绑定指定设备-3" tabindex="-1"><a class="header-anchor" href="#解除绑定指定设备-3" aria-hidden="true">#</a> 解除绑定指定设备</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?unbindDevice.nc</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>解除绑定当前设备，解绑成功会根据软件设定扣减用户余额</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"deviceCode"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"loginCode"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>UnbindDevice.ncParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为unbindDevice.nc</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  deviceCode</td>
<td>[私有]设备码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  loginCode</td>
<td>[私有]单码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回0，设备码不存在返回-1</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取用户点数余额-2" tabindex="-1"><a class="header-anchor" href="#获取用户点数余额-2" aria-hidden="true">#</a> 获取用户点数余额</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?userPoint.ag</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户点数余额</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>Authorization</td>
<td>token标记</td>
<td>header</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>UserPoint.agParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为userPoint.ag</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  vstr</td>
<td>[公共]用作标记或验证的冗余数据，将原样返回</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  timestamp</td>
<td>[公共]13位时间戳（精确到毫秒）</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>用户用户点数余额</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
<tr>
<td>vstr</td>
<td>用作标记或验证的冗余数据，与输入保持一致</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取用户点数余额-3" tabindex="-1"><a class="header-anchor" href="#获取用户点数余额-3" aria-hidden="true">#</a> 获取用户点数余额</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?userPoint.nc</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户点数余额</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"loginCode"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>UserPoint.ncParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为userPoint.nc</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  loginCode</td>
<td>[私有]单码</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>用户用户点数余额</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="通用api" tabindex="-1"><a class="header-anchor" href="#通用api" aria-hidden="true">#</a> 通用API</h2>
<h3 id="获取软件配置信息" tabindex="-1"><a class="header-anchor" href="#获取软件配置信息" aria-hidden="true">#</a> 获取软件配置信息</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?appInfo.ng</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取软件配置信息</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>AppInfo.ngParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为appInfo.ng</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>软件配置信息</td>
<td>AppInfo.ngAjaxResultVoData</td>
</tr>
<tr>
<td>  appName</td>
<td>软件名称</td>
<td>string</td>
</tr>
<tr>
<td>  description</td>
<td>软件描述</td>
<td>string</td>
</tr>
<tr>
<td>  status</td>
<td>软件状态（0正常 1停用）</td>
<td>string</td>
</tr>
<tr>
<td>  bindType</td>
<td>绑定模式</td>
<td>string</td>
</tr>
<tr>
<td>  isCharge</td>
<td>是否开启计费</td>
<td>string</td>
</tr>
<tr>
<td>  idxUrl</td>
<td>软件主页</td>
<td>string</td>
</tr>
<tr>
<td>  freeQuotaReg</td>
<td>首次登录赠送免费时间或点数，单位秒或点</td>
<td>integer</td>
</tr>
<tr>
<td>  reduceQuotaUnbind</td>
<td>换绑设备扣减时间或点数，单位秒或点</td>
<td>integer</td>
</tr>
<tr>
<td>  authType</td>
<td>认证类型</td>
<td>string</td>
</tr>
<tr>
<td>  billType</td>
<td>计费类型</td>
<td>string</td>
</tr>
<tr>
<td>  dataExpireTime</td>
<td>数据包过期时间，单位秒，-1为不限制，默认为-1</td>
<td>integer</td>
</tr>
<tr>
<td>  loginLimitU</td>
<td>登录用户数量限制，整数，-1为不限制，默认为-1</td>
<td>integer</td>
</tr>
<tr>
<td>  loginLimitM</td>
<td>登录机器数量限制，整数，-1为不限制，默认为-</td>
<td>integer</td>
</tr>
<tr>
<td>  limitOper</td>
<td>达到上限后的操作</td>
<td>string</td>
</tr>
<tr>
<td>  heartBeatTime</td>
<td>心跳包时间，单位秒，客户端若在此时间范围内无任何操作将自动下线，默认为300秒</td>
<td>integer</td>
</tr>
<tr>
<td>  welcomeNotice</td>
<td>启动公告</td>
<td>string</td>
</tr>
<tr>
<td>  offNotice</td>
<td>停机公告</td>
<td>string</td>
</tr>
<tr>
<td>  icon</td>
<td>软件图标地址，当前无实际作用</td>
<td>string</td>
</tr>
<tr>
<td>  enableTrial</td>
<td>是否开启试用</td>
<td>string</td>
</tr>
<tr>
<td>  trialTimesPerIp</td>
<td>每个ip可试用设备数，-1为不限制</td>
<td>integer</td>
</tr>
<tr>
<td>  trialCycle</td>
<td>每个设备试用时间周期，单位秒，0只能试用一次</td>
<td>integer</td>
</tr>
<tr>
<td>  trialTimes</td>
<td>每个设备每周期试用次数</td>
<td>integer</td>
</tr>
<tr>
<td>  trialTime</td>
<td>每个设备每次试用时长，单位秒</td>
<td>integer</td>
</tr>
<tr>
<td>  enableTrialByTimeQuantum</td>
<td>是否开启按时间试用</td>
<td>string</td>
</tr>
<tr>
<td>  enableTrialByTimes</td>
<td>是否开启按次数试用</td>
<td>string</td>
</tr>
<tr>
<td>  trialTimeQuantum</td>
<td>试用时间段</td>
<td>string</td>
</tr>
<tr>
<td>  remark</td>
<td>备注信息</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token punctuation">{</span>
		<span class="token string-property property">"appName"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"description"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"status"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"bindType"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"isCharge"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"idxUrl"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"freeQuotaReg"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"reduceQuotaUnbind"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"authType"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"billType"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"dataExpireTime"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginLimitU"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginLimitM"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"limitOper"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"heartBeatTime"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"welcomeNotice"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"offNotice"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"icon"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"enableTrial"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"trialTimesPerIp"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"trialCycle"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"trialTimes"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"trialTime"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"enableTrialByTimeQuantum"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"enableTrialByTimes"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"trialTimeQuantum"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"remark"</span><span class="token operator">:</span> <span class="token string">""</span>
	<span class="token punctuation">}</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取用户信息-4" tabindex="-1"><a class="header-anchor" href="#获取用户信息-4" aria-hidden="true">#</a> 获取用户信息</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?appUserInfo.ag</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取用户信息</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>Authorization</td>
<td>token标记</td>
<td>header</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>AppUserInfo.agParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为appUserInfo.ag</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  vstr</td>
<td>[公共]用作标记或验证的冗余数据，将原样返回</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  timestamp</td>
<td>[公共]13位时间戳（精确到毫秒）</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td></td>
<td>AppUserInfo.agAjaxResultVoData</td>
</tr>
<tr>
<td>  expireTime</td>
<td>用户过期时间，计时模式下有效</td>
<td>string</td>
</tr>
<tr>
<td>  point</td>
<td>用户剩余点数，计点模式下有效</td>
<td>integer</td>
</tr>
<tr>
<td>  status</td>
<td>用户状态，0为正常</td>
<td>string</td>
</tr>
<tr>
<td>  loginLimitU</td>
<td>同时在线用户数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  loginLimitM</td>
<td>同时在线设备数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  cardLoginLimitU</td>
<td>由卡密继承来的同时在线用户数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  cardLoginLimitM</td>
<td>由卡密继承来的同时在线设备数限制</td>
<td>integer</td>
</tr>
<tr>
<td>  loginTimes</td>
<td>用户登录次数</td>
<td>integer</td>
</tr>
<tr>
<td>  lastLoginTime</td>
<td>最近登录时间</td>
<td>string</td>
</tr>
<tr>
<td>  loginIp</td>
<td>最近登录IP</td>
<td>string</td>
</tr>
<tr>
<td>  freeBalance</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  payBalance</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  freePayment</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  payPayment</td>
<td>当前无实际作用</td>
<td>number</td>
</tr>
<tr>
<td>  cardCustomParams</td>
<td>由卡密继承来的自定义参数</td>
<td>string</td>
</tr>
<tr>
<td>  remark</td>
<td>备注信息</td>
<td>string</td>
</tr>
<tr>
<td>  userInfo</td>
<td>账号信息，账号模式下有效</td>
<td>AppUserInfo.agAjaxResultVoDataUserInfo</td>
</tr>
<tr>
<td>    avatar</td>
<td>头像</td>
<td>string</td>
</tr>
<tr>
<td>    userName</td>
<td>用户名</td>
<td>string</td>
</tr>
<tr>
<td>    nickName</td>
<td>用户昵称</td>
<td>string</td>
</tr>
<tr>
<td>    sex</td>
<td>性别，0男1女2未知</td>
<td>string</td>
</tr>
<tr>
<td>    phonenumber</td>
<td>手机号码</td>
<td>string</td>
</tr>
<tr>
<td>    email</td>
<td>邮箱</td>
<td>string</td>
</tr>
<tr>
<td>    admin</td>
<td>是否为管理员账号</td>
<td>bool</td>
</tr>
<tr>
<td>    availablePayBalance</td>
<td>可用余额</td>
<td>number</td>
</tr>
<tr>
<td>    freezePayBalance</td>
<td>冻结余额</td>
<td>number</td>
</tr>
<tr>
<td>    loginDate</td>
<td>最近登录时间</td>
<td>string</td>
</tr>
<tr>
<td>    loginIp</td>
<td>最近登录IP</td>
<td>string</td>
</tr>
<tr>
<td>  loginCode</td>
<td>单码信息，单码模式下有效</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
<tr>
<td>vstr</td>
<td>用作标记或验证的冗余数据，与输入保持一致</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token punctuation">{</span>
		<span class="token string-property property">"expireTime"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"point"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"status"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginLimitU"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginLimitM"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"cardLoginLimitU"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"cardLoginLimitM"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginTimes"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"lastLoginTime"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginIp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"freeBalance"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"payBalance"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"freePayment"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"payPayment"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"cardCustomParams"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"remark"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"userInfo"</span><span class="token operator">:</span> <span class="token punctuation">{</span>
			<span class="token string-property property">"avatar"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"userName"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"nickName"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"sex"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"phonenumber"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"email"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"admin"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"availablePayBalance"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
			<span class="token string-property property">"freezePayBalance"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
			<span class="token string-property property">"loginDate"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
			<span class="token string-property property">"loginIp"</span><span class="token operator">:</span> <span class="token string">""</span>
		<span class="token punctuation">}</span><span class="token punctuation">,</span>
		<span class="token string-property property">"loginCode"</span><span class="token operator">:</span> <span class="token string">""</span>
	<span class="token punctuation">}</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="远程文件下载" tabindex="-1"><a class="header-anchor" href="#远程文件下载" aria-hidden="true">#</a> 远程文件下载</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?globalFileDownload.ng</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>远程文件下载</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"fileName"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"returnUrl"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"errorIfNotExist"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>GlobalFileDownload.ngParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为globalFileDownload.ng</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  fileName</td>
<td>[私有]文件名称</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  returnUrl</td>
<td>[私有]是返回下载链接，否返回文件base64编码文本，是传1否传0默认为0</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  errorIfNotExist</td>
<td>[私有]当文件不存在时是否报错，如果为否则返回空，是传1否传0默认为0</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回文件下载链接或文件base64编码文本，文件不存在返回空</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="远程文件上传" tabindex="-1"><a class="header-anchor" href="#远程文件上传" aria-hidden="true">#</a> 远程文件上传</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?globalFileUpload.ng</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>远程文件上传</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"fileName"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"base64Str"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"overrideIfExist"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"checkToken"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"checkVip"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>GlobalFileUpload.ngParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为globalFileUpload.ng</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  fileName</td>
<td>[私有]文件名称</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  base64Str</td>
<td>[私有]文件base64编码文本</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  overrideIfExist</td>
<td>[私有]当文件已存在时是否覆盖，是传1否传0默认为1</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  checkToken</td>
<td>[私有]文件是否需要登录才能读写，是传1否传0默认为0</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  checkVip</td>
<td>[私有]文件是否需要VIP才能读写，是传1否传0默认为0</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回0</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="执行远程脚本" tabindex="-1"><a class="header-anchor" href="#执行远程脚本" aria-hidden="true">#</a> 执行远程脚本</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?globalScript.ng</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>执行远程脚本</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"scriptKey"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"scriptParams"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>GlobalScript.ngParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为globalScript.ng</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  scriptKey</td>
<td>[私有]脚本Key</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  scriptParams</td>
<td>[私有]脚本参数</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>执行结果</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="读远程变量" tabindex="-1"><a class="header-anchor" href="#读远程变量" aria-hidden="true">#</a> 读远程变量</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?globalVariableGet.ng</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>读远程变量</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"variableName"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"errorIfNotExist"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>GlobalVariableGet.ngParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为globalVariableGet.ng</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  variableName</td>
<td>[私有]变量名称</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  errorIfNotExist</td>
<td>[私有]当变量不存在时是否报错，如果为否则返回空文本，是传1否传0默认为0</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>变量值</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="写远程变量" tabindex="-1"><a class="header-anchor" href="#写远程变量" aria-hidden="true">#</a> 写远程变量</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?globalVariableSet.ng</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>写远程变量</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"variableName"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"variableValue"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"errorIfNotExist"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"checkToken"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"checkVip"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>GlobalVariableSet.ngParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为globalVariableSet.ng</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  variableName</td>
<td>[私有]变量名称</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  variableValue</td>
<td>[私有]变量值</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  errorIfNotExist</td>
<td>[私有]当变量不存在时是否报错，如果为否则自动创建该名称变量，是传1否传0默认为0</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  checkToken</td>
<td>[私有]创建的变量是否需要登录才能读写，是传1否传0默认为0</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  checkVip</td>
<td>[私有]创建的变量是否需要VIP才能读写，是传1否传0默认为0</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回0</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="心跳" tabindex="-1"><a class="header-anchor" href="#心跳" aria-hidden="true">#</a> 心跳</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?heartbeat.ag</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>刷新token过期时间，在软件配置的心跳时间内应至少请求一次本接口，否则系统将自动下线当前用户，请求后返回下次心跳截止时间</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>Authorization</td>
<td>token标记</td>
<td>header</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>Heartbeat.agParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为heartbeat.ag</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  vstr</td>
<td>[公共]用作标记或验证的冗余数据，将原样返回</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  timestamp</td>
<td>[公共]13位时间戳（精确到毫秒）</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>下次心跳截止时间</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
<tr>
<td>vstr</td>
<td>用作标记或验证的冗余数据，与输入保持一致</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="是否连接到服务器" tabindex="-1"><a class="header-anchor" href="#是否连接到服务器" aria-hidden="true">#</a> 是否连接到服务器</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?isConnected.ng</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>判断是否正常连接到服务器，成功返回服务器13位时间戳（精确到毫秒）</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>IsConnected.ngParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为isConnected.ng</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>服务器13位时间戳（精确到毫秒）</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取用户在线状态" tabindex="-1"><a class="header-anchor" href="#获取用户在线状态" aria-hidden="true">#</a> 获取用户在线状态</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?isOnline.ag</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>在线返回1</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>Authorization</td>
<td>token标记</td>
<td>header</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>IsOnline.agParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为isOnline.ag</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  vstr</td>
<td>[公共]用作标记或验证的冗余数据，将原样返回</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  timestamp</td>
<td>[公共]13位时间戳（精确到毫秒）</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>在线值为1</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
<tr>
<td>vstr</td>
<td>用作标记或验证的冗余数据，与输入保持一致</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取最新版本信息" tabindex="-1"><a class="header-anchor" href="#获取最新版本信息" aria-hidden="true">#</a> 获取最新版本信息</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?latestVersionInfo.ng</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取软件最新版本信息</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>LatestVersionInfo.ngParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为latestVersionInfo.ng</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>版本信息</td>
<td>LatestVersionInfo.ngAjaxResultVoData</td>
</tr>
<tr>
<td>  versionName</td>
<td>版本名称</td>
<td>string</td>
</tr>
<tr>
<td>  versionNo</td>
<td>版本号</td>
<td>integer</td>
</tr>
<tr>
<td>  updateLog</td>
<td>更新日志</td>
<td>string</td>
</tr>
<tr>
<td>  downloadUrl</td>
<td>下载地址</td>
<td>string</td>
</tr>
<tr>
<td>  status</td>
<td>版本状态（0正常 1停用）</td>
<td>string</td>
</tr>
<tr>
<td>  md5</td>
<td>软件MD5</td>
<td>string</td>
</tr>
<tr>
<td>  forceUpdate</td>
<td>是否强制更新</td>
<td>string</td>
</tr>
<tr>
<td>  checkMd5</td>
<td>是否校验MD5</td>
<td>string</td>
</tr>
<tr>
<td>  downloadUrlDirect</td>
<td>直链地址</td>
<td>string</td>
</tr>
<tr>
<td>  remark</td>
<td>备注信息</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token punctuation">{</span>
		<span class="token string-property property">"versionName"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"versionNo"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"updateLog"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"downloadUrl"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"status"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"md5"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"forceUpdate"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"checkMd5"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"downloadUrlDirect"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"remark"</span><span class="token operator">:</span> <span class="token string">""</span>
	<span class="token punctuation">}</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取强制更新版本信息" tabindex="-1"><a class="header-anchor" href="#获取强制更新版本信息" aria-hidden="true">#</a> 获取强制更新版本信息</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?latestVersionInfoForceUpdate.ng</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取需要被强制更新到的最低软件版本，如果当前版本低于此版本，则应该启动强制更新策略</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>LatestVersionInfoForceUpdate.ngParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为latestVersionInfoForceUpdate.ng</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>版本信息</td>
<td>LatestVersionInfoForceUpdate.ngAjaxResultVoData</td>
</tr>
<tr>
<td>  versionName</td>
<td>版本名称</td>
<td>string</td>
</tr>
<tr>
<td>  versionNo</td>
<td>版本号</td>
<td>integer</td>
</tr>
<tr>
<td>  updateLog</td>
<td>更新日志</td>
<td>string</td>
</tr>
<tr>
<td>  downloadUrl</td>
<td>下载地址</td>
<td>string</td>
</tr>
<tr>
<td>  status</td>
<td>版本状态（0正常 1停用）</td>
<td>string</td>
</tr>
<tr>
<td>  md5</td>
<td>软件MD5</td>
<td>string</td>
</tr>
<tr>
<td>  forceUpdate</td>
<td>是否强制更新</td>
<td>string</td>
</tr>
<tr>
<td>  checkMd5</td>
<td>是否校验MD5</td>
<td>string</td>
</tr>
<tr>
<td>  downloadUrlDirect</td>
<td>直链地址</td>
<td>string</td>
</tr>
<tr>
<td>  remark</td>
<td>备注信息</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token punctuation">{</span>
		<span class="token string-property property">"versionName"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"versionNo"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"updateLog"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"downloadUrl"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"status"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"md5"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"forceUpdate"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"checkMd5"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"downloadUrlDirect"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"remark"</span><span class="token operator">:</span> <span class="token string">""</span>
	<span class="token punctuation">}</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="注销登录" tabindex="-1"><a class="header-anchor" href="#注销登录" aria-hidden="true">#</a> 注销登录</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?logout.ag</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>注销登录接口</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>Authorization</td>
<td>token标记</td>
<td>header</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>Logout.agParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为logout.ag</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  vstr</td>
<td>[公共]用作标记或验证的冗余数据，将原样返回</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  timestamp</td>
<td>[公共]13位时间戳（精确到毫秒）</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回0</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
<tr>
<td>vstr</td>
<td>用作标记或验证的冗余数据，与输入保持一致</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取服务器时间" tabindex="-1"><a class="header-anchor" href="#获取服务器时间" aria-hidden="true">#</a> 获取服务器时间</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?serverTime.ng</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取服务器时间，格式yyyy-MM-dd HH:mm:ss</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>ServerTime.ngParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为serverTime.ng</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>服务器时间</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取时间差" tabindex="-1"><a class="header-anchor" href="#获取时间差" aria-hidden="true">#</a> 获取时间差</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?timeDiff.ng</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取时间差，格式yyyy-MM-dd HH:mm:ss</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"time1"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"time2"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"formatType"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>TimeDiff.ngParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为timeDiff.ng</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  time1</td>
<td>[私有]时间1，默认为当前时间</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  time2</td>
<td>[私有]时间2，默认为当前时间</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  formatType</td>
<td>[私有]结果格式，0.毫秒数 1.天数 2.友好文本，默认为0</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>时间2-时间1相差的时间</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="试用登录" tabindex="-1"><a class="header-anchor" href="#试用登录" aria-hidden="true">#</a> 试用登录</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?trialLogin.ng</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>试用登录接口</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appVer"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"deviceCode"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"md5"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>TrialLogin.ngParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为trialLogin.ng</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appVer</td>
<td>[私有]软件版本号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  deviceCode</td>
<td>[私有]设备码，如果开启设备绑定，则必须提供</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  md5</td>
<td>[私有]软件MD5</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>登录成功返回token</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="解除绑定当前设备" tabindex="-1"><a class="header-anchor" href="#解除绑定当前设备" aria-hidden="true">#</a> 解除绑定当前设备</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?unbindDevice.ag</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>解除绑定当前设备，解绑成功会根据软件设定扣减用户余额</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>Authorization</td>
<td>token标记</td>
<td>header</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>UnbindDevice.agParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为unbindDevice.ag</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  vstr</td>
<td>[公共]用作标记或验证的冗余数据，将原样返回</td>
<td></td>
<td>false</td>
<td>string</td>
</tr>
<tr>
<td>  timestamp</td>
<td>[公共]13位时间戳（精确到毫秒）</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>成功返回0，设备码不存在返回-1</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
<tr>
<td>vstr</td>
<td>用作标记或验证的冗余数据，与输入保持一致</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"vstr"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="获取软件版本信息" tabindex="-1"><a class="header-anchor" href="#获取软件版本信息" aria-hidden="true">#</a> 获取软件版本信息</h3>
<p><strong>接口地址</strong>:<code v-pre>/prod-api/api/v1/{appkey}?versionInfo.ng</code></p>
<p><strong>请求方式</strong>:<code v-pre>POST</code></p>
<p><strong>请求数据类型</strong>:<code v-pre>application/json</code></p>
<p><strong>响应数据类型</strong>:<code v-pre>application/json;charset=UTF-8</code></p>
<p><strong>接口描述</strong>:<p>获取软件当前版本信息</p></p>
<p><strong>请求示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
  <span class="token string-property property">"api"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appSecret"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
  <span class="token string-property property">"appVer"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p><strong>请求参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>请求类型</th>
<th>是否必须</th>
<th>数据类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>appkey</td>
<td>AppKey</td>
<td>path</td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>params</td>
<td>请求参数</td>
<td>body</td>
<td>true</td>
<td>VersionInfo.ngParamsVo</td>
</tr>
<tr>
<td>  api</td>
<td>[公共]请求的API接口，此处为versionInfo.ng</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appSecret</td>
<td>[公共]AppSecret</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  sign</td>
<td>[公共]数据签名</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
<tr>
<td>  appVer</td>
<td>[私有]软件版本号</td>
<td></td>
<td>true</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应状态</strong>:</p>
<table>
<thead>
<tr>
<th>状态码</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>200</td>
<td>请求完成</td>
</tr>
</tbody>
</table>
<p><strong>响应参数</strong>:</p>
<table>
<thead>
<tr>
<th>参数名称</th>
<th>参数说明</th>
<th>类型</th>
</tr>
</thead>
<tbody>
<tr>
<td>code</td>
<td>执行成功返回200，否则返回错误码</td>
<td>integer(int64)</td>
</tr>
<tr>
<td>data</td>
<td>版本信息</td>
<td>VersionInfo.ngAjaxResultVoData</td>
</tr>
<tr>
<td>  versionName</td>
<td>版本名称</td>
<td>string</td>
</tr>
<tr>
<td>  versionNo</td>
<td>版本号</td>
<td>integer</td>
</tr>
<tr>
<td>  updateLog</td>
<td>更新日志</td>
<td>string</td>
</tr>
<tr>
<td>  downloadUrl</td>
<td>下载地址</td>
<td>string</td>
</tr>
<tr>
<td>  status</td>
<td>版本状态（0正常 1停用）</td>
<td>string</td>
</tr>
<tr>
<td>  md5</td>
<td>软件MD5</td>
<td>string</td>
</tr>
<tr>
<td>  forceUpdate</td>
<td>是否强制更新</td>
<td>string</td>
</tr>
<tr>
<td>  checkMd5</td>
<td>是否校验MD5</td>
<td>string</td>
</tr>
<tr>
<td>  downloadUrlDirect</td>
<td>直链地址</td>
<td>string</td>
</tr>
<tr>
<td>  remark</td>
<td>备注信息</td>
<td>string</td>
</tr>
<tr>
<td>msg</td>
<td>结果说明</td>
<td>string</td>
</tr>
<tr>
<td>sign</td>
<td>数据签名</td>
<td>string</td>
</tr>
<tr>
<td>timestamp</td>
<td>结果生成时间</td>
<td>string</td>
</tr>
</tbody>
</table>
<p><strong>响应示例</strong>:</p>
<div class="language-javascript ext-js line-numbers-mode"><pre v-pre class="language-javascript"><code><span class="token punctuation">{</span>
	<span class="token string-property property">"code"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
	<span class="token string-property property">"data"</span><span class="token operator">:</span> <span class="token punctuation">{</span>
		<span class="token string-property property">"versionName"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"versionNo"</span><span class="token operator">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
		<span class="token string-property property">"updateLog"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"downloadUrl"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"status"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"md5"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"forceUpdate"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"checkMd5"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"downloadUrlDirect"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
		<span class="token string-property property">"remark"</span><span class="token operator">:</span> <span class="token string">""</span>
	<span class="token punctuation">}</span><span class="token punctuation">,</span>
	<span class="token string-property property">"msg"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"sign"</span><span class="token operator">:</span> <span class="token string">""</span><span class="token punctuation">,</span>
	<span class="token string-property property">"timestamp"</span><span class="token operator">:</span> <span class="token string">""</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div></div></template>


