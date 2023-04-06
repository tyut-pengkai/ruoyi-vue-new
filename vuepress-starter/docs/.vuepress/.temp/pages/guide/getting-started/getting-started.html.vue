<template><div><h1 id="快速上手" tabindex="-1"><a class="header-anchor" href="#快速上手" aria-hidden="true">#</a> 快速上手</h1>
<div class="custom-container tip"><p class="custom-container-title">提示</p>
<p>不会使用某个功能时可以将鼠标悬浮在对应功能标题后的?获取帮助<br>
项目名称前带<code v-pre>*</code>的表示该项为必填项</p>
</div>
<h2 id="一、数据准备" tabindex="-1"><a class="header-anchor" href="#一、数据准备" aria-hidden="true">#</a> 一、数据准备</h2>
<h3 id="_1、新建软件" tabindex="-1"><a class="header-anchor" href="#_1、新建软件" aria-hidden="true">#</a> 1、新建软件</h3>
<p>找到侧边栏的验证管理下的软件管理并点击进入，点击<code v-pre>新增</code>按钮。<br>
<img src="@source/guide/getting-started/1.jpg" alt="image"><br>
<img src="@source/guide/getting-started/2.jpg" alt="image"><br>
<strong>*软件名称</strong>：软件的名称，如果使用快速接入功能此名称会在软件的登录框显示<br>
<strong>*认证类型</strong><br>
- 账号登录：用户需要首先注册账号设置密码后，使用账号和密码的方式登录。<br>
- 单码登录：仅使用单个登录码进行登录。<br>
<strong>*计费类型</strong><br>
- 计时模式：用户在有效时间范围内可无限次使用。<br>
- 计点模式：按账户剩余的点数进行控制，当点数小于等于0时，用户将无法继续使用。</p>
<details class="custom-container details"><summary>更多设置项（非必填项或默认配置项）</summary>
<h5 id="基本信息" tabindex="-1"><a class="header-anchor" href="#基本信息" aria-hidden="true">#</a> 基本信息</h5>
<p><strong>软件状态</strong>：停用后用户无法登录和使用该软件。停用后在线用户也会被强制退出系统。<br>
<strong>首次登录赠送</strong>：首次登录赠送的免费额度，默认为0，如果软件为计时模式，则此处代表赠送的时长，单位为秒；如果软件为计点模式，则此处代表赠送的点数。<br>
<strong>软件公告/停机公告</strong>：软件的公告信息，如果使用快速接入功能此信息会在软件的登录框显示。<br>
<strong>软件主页</strong>：使用场景：您有自己的软件官网或交流论坛，可将链接填写在此处，供用户点击跳转了解软件详情及用法。<br>
<strong>购卡地址</strong>：使用场景：您有自己的店铺或商品链接，可将店铺链接填写在此处，供用户点击跳转到您的店铺下单。<br>
<strong>备注</strong>：备注信息。</p>
<h5 id="通信安全" tabindex="-1"><a class="header-anchor" href="#通信安全" aria-hidden="true">#</a> 通信安全</h5>
<blockquote>
<p>数据输入：从软件端到验证后台的数据传输。<br>
数据输出：从验证后台返回给软件端的数据传输。</p>
</blockquote>
<p><strong>数据输入加密</strong>：<br>
- 明文传输：安全性极低，仅建议开发阶段使用，方便开发者查看与调试数据，无需提供密码。<br>
- BASE64：安全性较低，无需提供密码。<br>
- AES：安全性较高，建议对外发布的软件选择此加密方式，需要提供密码。<br>
<strong>数据包过期时间</strong>：校验软件端到后台端的时间差，当差值大于设定值时，后台将认为该数据为非法数据。<br>
<strong>心跳包时间</strong>：单位为秒，软件端若在设定时间内无任何操作将自动下线，-1为不检测，默认为300秒。<br>
<strong>API匿名密码</strong>：如果设定此密码，软件端与后台通信时将使用密文作为API名称。</p>
<h5 id="绑定设置-用户绑定及解绑设备相关设置" tabindex="-1"><a class="header-anchor" href="#绑定设置-用户绑定及解绑设备相关设置" aria-hidden="true">#</a> 绑定设置：用户绑定及解绑设备相关设置</h5>
<blockquote>
<p>此处的<code v-pre>用户</code>指卡密，每一张卡密视为一个用户</p>
</blockquote>
<p><strong>绑定模式</strong>：<br>
- 不绑定/无限制：卡密和设备可随意更换，没有任何限制。<br>
- 用户与设备一对一绑定：卡密和设备都不能更换，比如卡密1与设备1进行了绑定，那么卡密1只能在设备1上登录，无法在设备2上登录，设备1也只能使用卡密1进行登录，不能使用卡密2进行登录。<br>
- 一用户可绑定多个设备：同一卡密可更换设备，但设备不能更换卡密，比如卡密1与设备1进行了绑定，那么卡密1除了能在设备1上登录，也可以在设备2上登录，但设备1只能使用卡密1进行登录，设备2也只能使用卡密1进行登录。<br>
- 多用户可绑定同一设备：同一设备可更换卡密，但卡密不能更换设备，比如卡密1与设备1进行了绑定，那么卡密1只能在设备1上登录，无法在设备2上登录，设备1可以使用卡密1进行登录，也可以使用卡密2进行登录。</p>
<p><strong>限制解绑次数</strong>：解绑时优先扣除解绑次数，当解绑次数为0时，将根据[使用余额解绑]来决定是否可以继续解绑，修改后新用户会生效，已有用户不生效。<br>
<strong>使用余额解绑</strong>：解绑时优先扣除解绑次数，当解绑次数为0时，将根据本选项来决定是否可以继续解绑，如果为否，用户将无法继续解绑，如果为是，系统将根据下方配置扣除对应余额。</p>
<h5 id="限开设置-限制同一用户下可同时在线软件端数量" tabindex="-1"><a class="header-anchor" href="#限开设置-限制同一用户下可同时在线软件端数量" aria-hidden="true">#</a> 限开设置：限制同一用户下可同时在线软件端数量</h5>
<blockquote>
<p>此处的<code v-pre>用户</code>指卡密，每一张卡密视为一个用户</p>
</blockquote>
<p><strong>登录用户数限制</strong>：同一张卡密可同时在线数量，根据token计算。<br>
<strong>登录设备数限制</strong>：同一张卡密可同时在线设备数量，根据设备码进行计算，相同设备码视为1个设备。<br>
<strong>达到登录上限后</strong>：<br>
- 提示用户：后登录的用户需等待先登录的用户下线后才可以登录。<br>
- 注销最早登录的用户：后登录的用户无需等待，但先登录的用户将被顶号下线。</p>
<h5 id="试用设置" tabindex="-1"><a class="header-anchor" href="#试用设置" aria-hidden="true">#</a> 试用设置</h5>
<blockquote>
<p>建议：打开<code v-pre>试用有效时间内重复登录不增加试用次数</code>，为了避免有电脑死机、网络中断等导致的软件端意外退出浪费试用次数。</p>
</blockquote>
</details>
<h3 id="_2、新建软件版本" tabindex="-1"><a class="header-anchor" href="#_2、新建软件版本" aria-hidden="true">#</a> 2、新建软件版本</h3>
<div class="custom-container tip"><p class="custom-container-title">提示</p>
<p>提示：如果需要更新版本时请新建一个版本，并设置一个更大的版本号即可，而不要修改或删除已有的版本，如果版本被删除，那么对应版本的软件将无法使用。</p>
</div>
<p>找到新建的软件条目，点击右侧版本按钮，可进入版本管理，点击<code v-pre>新增</code>添加软件版本信息。<br>
<img src="@source/guide/getting-started/3.jpg" alt="image"><br>
<strong>*版本名称</strong>：版本名称可随意填写，如测试版v1.0、正式版2.0、国庆特别版等，用于软件显示，如果使用快速接入功能此名称会在软件的登录框显示<br>
<strong>*版本号</strong>：版本号用于升级，只限填写整数，同一软件版本号不可重复，版本越新，数字应该越大，建议使用发布时间，如20220101</p>
<details class="custom-container details"><summary>更多设置项（非必填项或默认配置项）</summary>
<p><strong>下载链接</strong>：填写内容应为软件的下载网站链接，如有更新时，用户点击更新按钮后将使用浏览器跳转到该地址。<br>
<strong>下载地址（直链）</strong>：填写内容为新版本的软件的直链链接，如有更新时，用户点击更新按钮后将启动APK内置的下载器进行下载，下载完毕后将尝试启动安装。</p>
<div class="custom-container tip"><p class="custom-container-title">提示</p>
<p>下载链接和下载地址（直链）至少选择一个填写，如果同时填写则直链生效。如果都不填写将导致更新异常。</p>
</div>
</details>
<h2 id="二、快速接入-一键接入-成品接入" tabindex="-1"><a class="header-anchor" href="#二、快速接入-一键接入-成品接入" aria-hidden="true">#</a> 二、快速接入/一键接入/成品接入</h2>
<p>本系统提供快速接入功能，又可称为一键接入、成品接入等，本功能主要用于开发者临时接单需要快速接入快速测试的场景，接入软件建议小于20M，目前可支持<code v-pre>全部模式</code>的电脑软件(EXE)接入与<code v-pre>单码计时</code>模式的安卓软件(APK)接入。</p>
<div class="custom-container tip"><p class="custom-container-title">提示</p>
<p>快速接入安全性较低，建议接入后自行加固或加壳后再对外发布。</p>
</div>
<h3 id="_1、安卓软件-apk-快速接入" tabindex="-1"><a class="header-anchor" href="#_1、安卓软件-apk-快速接入" aria-hidden="true">#</a> 1、安卓软件(APK)快速接入</h3>
<p>点击新建的软件版本条目后面的快速接入按钮，选择APK接入并拖入APK文件。<br>
<img src="@source/guide/getting-started/7.jpg" alt="image"><br>
<strong>接入方式</strong>：<br>
- 全局模式：全局模式为全自动注入模式，无需人工操作。<br>
- 单例模式：单例模式为半自动注入模式，需指定要注入的Activity和Method。</p>
<div class="custom-container tip"><p class="custom-container-title">提示</p>
<p><a href='/guide/qa/qa.html#%E5%8D%95%E4%BE%8B%E6%B3%A8%E5%85%A5%E7%9A%84activity%E5%92%8Cmethod%E5%BA%94%E8%AF%A5%E5%A6%82%E4%BD%95%E9%80%89%E6%8B%A9'>点此查看Activity和Method该如何选择</a></p>
</div>
<p><strong>接入类别</strong>：<br>
- 注入并加签：自动将上传的APK对接到当前系统并自动为APK签名。相当于下方的注入与加签两步合并在一起。<br>
- 仅注入：自动将上传的APK对接到当前系统。如果想正常使用需要用户自行加签。<br>
- 仅加签：自动将上传的APK进行加签。使用加固工具加固完APK后可使用此选项为APK加签。</p>
<div class="custom-container tip"><p class="custom-container-title">提示</p>
<p>提示：APK只有签名后才能被设备安装使用。</p>
</div>
<h3 id="_2、电脑软件-exe-快速接入" tabindex="-1"><a class="header-anchor" href="#_2、电脑软件-exe-快速接入" aria-hidden="true">#</a> 2、电脑软件(EXE)快速接入</h3>
<p>点击新建的软件版本条目后面的快速接入按钮，选择EXE接入，点击EXE接入下方的<code v-pre>一键复制对接参数</code>。<br>
<img src="@source/guide/getting-started/5.jpg" alt="image"><br>
在官方群共享下载<strong>最新版本</strong>的<code v-pre>红叶快速接入工具</code>后打开，选择要对接的软件和输出文件保存目录。<br>
<img src="@source/guide/getting-started/6.jpg" alt="image"><br>
点击快速接入工具的<code v-pre>一键粘贴对接参数</code>即可自动填写相关参数。<br>
点击<code v-pre>立即接入</code>按钮即可将软件一键对接到当前系统。</p>
<h2 id="三、代码编程接入" tabindex="-1"><a class="header-anchor" href="#三、代码编程接入" aria-hidden="true">#</a> 三、代码编程接入</h2>
<h3 id="_1、下载sdk和例程" tabindex="-1"><a class="header-anchor" href="#_1、下载sdk和例程" aria-hidden="true">#</a> 1、下载SDK和例程</h3>
<p>可在官方群共享下载对应语言的SDK和对应例程，目前提供有易语言、按键精灵(安卓)、懒人精灵、火山PC的相关例程。</p>
<h3 id="_2、修改对接参数" tabindex="-1"><a class="header-anchor" href="#_2、修改对接参数" aria-hidden="true">#</a> 2、修改对接参数</h3>
<p>进入系统后台<code v-pre>验证管理</code>-<code v-pre>软件管理</code>，点击软件右侧的<code v-pre>配置</code>按钮找到接口信息，将例程中的初始化函数修改为与后台一致即可对接成功。</p>
</div></template>


