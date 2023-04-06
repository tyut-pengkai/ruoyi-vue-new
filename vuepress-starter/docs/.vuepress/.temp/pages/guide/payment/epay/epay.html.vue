<template><div><h1 id="易支付-码支付" tabindex="-1"><a class="header-anchor" href="#易支付-码支付" aria-hidden="true">#</a> 易支付/码支付</h1>
<h2 id="注册申请易支付" tabindex="-1"><a class="header-anchor" href="#注册申请易支付" aria-hidden="true">#</a> 注册申请易支付</h2>
<p>需要您自行寻找可靠的易支付提供商</p>
<h2 id="获取对接参数" tabindex="-1"><a class="header-anchor" href="#获取对接参数" aria-hidden="true">#</a> 获取对接参数</h2>
<p>进入易支付后台，进入<code v-pre>个人资料-API信息</code>，即可看到<code v-pre>接口地址</code>、<code v-pre>商户ID</code>和<code v-pre>商户密钥</code>
<img src="@source/guide/payment/epay/1.jpg" alt="image"></p>
<h2 id="对接红叶系统" tabindex="-1"><a class="header-anchor" href="#对接红叶系统" aria-hidden="true">#</a> 对接红叶系统</h2>
<ol>
<li>
<p>进入红叶系统后台，打开<code v-pre>系统配置</code>-<code v-pre>支付配置</code>，找到<code v-pre>易支付-XXX</code>(找需要对接的支付方式，可选支付宝/微信/QQ钱包)，点击右侧<code v-pre>修改</code>按钮进入配置页面<br>
<img src="@source/guide/payment/epay/2.jpg" alt="image"></p>
</li>
<li>
<p>填写下列参数后保存</p>
</li>
</ol>
<blockquote>
<p><strong>key</strong>：商户密钥<br>
<strong>pid</strong>：商户ID<br>
<strong>serverUrl</strong>：接口地址</p>
</blockquote>
<p><img src="@source/guide/payment/epay/3.jpg" alt="image"></p>
<ol start="3">
<li>
<p>启用该支付方式</p>
</li>
<li>
<p>测试购卡<br>
进入红叶商城，随意选择商品后，选择<code v-pre>易支付-XXX</code>支付并提交订单，如果可以正常生成付款二维码即配置成功</p>
</li>
</ol>
</div></template>


