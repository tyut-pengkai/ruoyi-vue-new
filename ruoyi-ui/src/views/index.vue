<template>
  <div class="app-container home">
    <span>
      <el-alert
        v-if="remainTimeSeconds / 86400 <= 10"
        style="margin-bottom: 20px"
        title="您的授权即将到期"
        :description="'您的授权将于' + expireTime + '到期（剩余 ' + remainTimeReadable + ' 天），请您尽快续费以免影响您的正常使用'"
        type="warning"
        :closable="false"
        :show-icon="true">
      </el-alert>
    </span>
    <el-card shadow="never" style="margin-bottom: 10px;padding-top: 5px;">
      <span style="font-size: 16px;"><i class="el-icon-user">&nbsp;{{ nickName }}</i></span>
      <span style="float: right" v-if="checkRole(['sadmin', 'admin'])">
        <span>
          <b>主程序版本：</b>
          <span>{{ version }}</span>
        </span>
        <span style="margin-left: 10px">
          <b>数据库版本：</b>
          <span>{{ dbVersion }}</span>
        </span>
        <el-link style="margin-left: 10px" type="primary" @click="checkUpdate"
        >更新
        </el-link>
        <el-link style="margin-left: 10px" type="primary" @click="doRestart"
        >重启
        </el-link>
        <el-link style="margin-left: 10px" type="primary" @click="handleExport"
        >导出日志
        </el-link>
      </span>
    </el-card>
      <!-- <el-alert
        v-if="userNotice && userNotice['content']"
        :closable="false"
        :title="userNotice['title'] || '系统公告'"
        style="margin-bottom: 20px"
        type="info"
      >
        <div class="ql-container ql-bubble">
          <div class="ql-editor">
            <div>
              <div v-if="userNotice && userNotice['content']">
                <span v-html="userNotice['content']"></span>
              </div>
            </div>
          </div>
        </div>
      </el-alert>
      <div v-if="checkRole(['agent'])">
        <el-alert
          v-if="agentNotice && agentNotice['content']"
          :closable="false"
          :title="agentNotice['title'] || '系统公告(仅代理可见)'"
          style="margin-bottom: 20px"
          type="info"
        >
          <div class="ql-container ql-bubble">
            <div class="ql-editor">
              <div>
                <div v-if="agentNotice && agentNotice['content']">
                  <span v-html="agentNotice['content']"></span>
                </div>
              </div>
            </div>
          </div>
        </el-alert>
      </div> -->
    <div v-if="checkRole(['admin', 'sadmin'])">
      <el-row :gutter="20">
        <!-- <el-col :lg="12" :sm="24" style="padding-left: 20px">
          <h2>红叶网络验证与软件管理系统</h2>
          <p>软件授权管理计费销售一站式解决方案</p> -->
          <!-- <span>
            <b>主程序版本：</b>
            <span>{{ version }}</span>
          </span>
          <span style="margin-left: 10px">
            <b>数据库版本：</b>
            <span>{{ dbVersion }}</span>
          </span>
          <el-link style="margin-left: 10px" type="primary" @click="checkUpdate"
          >检查更新
          </el-link> -->
          <!-- <p>
          <el-tag type="danger">&yen; 限时免费</el-tag>
        </p> -->
          <!-- <p>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-cloudy"
            plain
            @click="goTarget('https://gitee.com/y_project/RuoYi-Vue')"
            >访问码云</el-button
          >
          <el-button
            size="mini"
            icon="el-icon-s-home"
            plain
            @click="goTarget('http://ruoyi.vip')"
            >访问主页</el-button
          >
        </p> -->
        <!-- </el-col> -->

        <!-- <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>技术选型</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <h4>后端技术</h4>
            <ul>
              <li>SpringBoot</li>
              <li>Spring Security</li>
              <li>JWT</li>
              <li>MyBatis</li>
              <li>Druid</li>
              <li>Fastjson</li>
              <li>...</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h4>前端技术</h4>
            <ul>
              <li>Vue</li>
              <li>Vuex</li>
              <li>Element-ui</li>
              <li>Axios</li>
              <li>Sass</li>
              <li>Quill</li>
              <li>...</li>
            </ul>
          </el-col>
        </el-row>
      </el-col> -->
      </el-row>
      <!-- <el-divider/> -->
      <el-row :gutter="10" style="margin-top: 10px">
        <el-col :lg="12" :md="12" :sm="24" :xs="24">
          <el-row :gutter="20" style="margin-bottom: 10px">
            <el-col :span="24">
              <el-card class="update-log">
                <div slot="header" class="clearfix">
                  <span>更新日志</span>
                </div>
                <el-collapse accordion>
                  <el-collapse-item title="v1.7.0 - 2023-09-10">
                    <ol>
                      <li>修复：修复批量操作按范围操作时卡种类超过10种不显示的问题（感谢@a阳丶帅帅a）</li>
                      <li>修复：修复仪表盘柱状统计图、饼状统计图不显示的问题（感谢@手游大咖）</li>
                      <li>修复：修复计点模式登录自动扣点错误的问题（使用了计点模式的用户请检查loginApi的autoReducePoint参数，保持默认或者设置为true则需要升级）（感谢@小Ω猪）</li>
                      <li>修复：添加软件用户页面限制软件用户所属账号/单码为必填项，防止生成错误数据</li>
                      <li>修复：修复isUserNameExist.nu接口返回值错误的问题，原返回值（不存在/存在）：true/false，现返回值（不存在/存在）：0/1</li>
                      <li>修复：修复删除软件后再次创建同名软件无法批量导入卡密的问题</li>
                      <li>修复：修复添加卡类时点击确定按钮窗口不关闭的问题</li>
                      <li>修复：修复批量导出数据时会报请求超时的问题（实测10w数据耗时4分钟，如果数据过多建议分批多次下载）</li>
                      <li>修复：修复API接口：latestVersionInfoCompute.ng返回结果可能为空的问题</li>
                      <li>修复：修复API接口：appUserInfo.nc/nu/ag返回的用户到期时间少8个小时的问题（感谢@诱惑）</li>
                      <li>修复：修复计点模式下用户剩余1点时不能自动扣除的问题（感谢@小Ω猪）</li>
                      <li>修复：修复解绑当前设备后再操作需要登录验证的API（*.ag）时，返回500错误的问题（感谢@小Ω猪）</li>
                      <li>新增：系统首页新增导出日志按钮，便于排查问题</li>
                      <li>新增：卡密类别和单码类别页面添加【快速新增】按钮，可使用系统预定义参数快速创建一系列卡类</li>
                      <li>优化：限制单码不能添加重复数据</li>
                      <li>优化：优化仪表盘图标悬浮窗显示效果</li>
                      <li>优化：移除【代理系统-代理授权】菜单，功能整合到【代理系统-我的子代】中</li>
                      <li>优化：大多数功能模块支持按[备注]信息进行搜索</li>
                      <li>优化：授权小于10天，系统将进行到期提示</li>
                      <li>优化：同一软件下卡类不允许重名</li>
                      <li>优化：修改软件默认绑定模式为[多用户可绑定同一设备]</li>
                      <li>优化：页面输入内容限制输入长度，防止用户输入过长内容触发报错</li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v1.6.1 - 2023-07-17">
                    <ol>
                      <li>修复：修复软件用户信息缓存异常的问题</li>
                      <li>修复：补充缓存列表页缺失的缓存名称</li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v1.6.0 - 2023-06-20">
                      <ol>
                        <li>修复：修复卡密、单码使用时间字段不生效的问题</li>
                        <li>修复：删除软件版本后再创建同版本号版本不生效的问题</li>
                        <li>修复：删除软件后会导致软件在线数据统计失败的问题</li>
                        <li>修复：代理用户代理授权添加代理卡类时零售价格显示undefined的问题</li>
                        <li>修复：修复MYSQL配置了ONLY_FULL_GROUP_BY模式情况下仪表盘数据统计报错的问题</li>
                        <li>修复：修复登录码不区分大小写可登录的问题</li>
                        <li>新增：系统首页新增验证系统重启功能</li>
                        <li>新增：API接口：latestVersionInfoCompute.ng，用于获取软件最新版本信息，详情查看API文档</li>
                        <li>优化：查询登录码时如已使用则显示对应用户到期时间</li>
                        <li>优化：增加对APK二次注入的检测，如检测到二次注入将报错提示</li>
                        <li>优化：调整前台登录模式和单码模式相关功能展示顺序，优先展示单码(登录码)模式</li>
                        <li>优化：APK快速接入支持注入之前由于父类为final导致的注入失败的APK</li>
                        <li>优化：APK快速接入体积优化，精简重复类注入</li>
                        <li>优化：后台首页调整优化</li>
                        <li>优化：APK注入模板升级2.0</li>
                        <ol>
                          <li>新增官网、购卡、试用、解绑等功能</li>
                          <li>新增取消自动登录按钮，方便用户随时更换卡密</li>
                          <li>新增一个APK离线模板</li>
                          <li>修复填写新卡密后还使用旧卡密登录导致登录失败的问题</li>
                          <li>修复TOAST提示长时间弹出的问题</li>
                          <li>修复某些设备记住卡密无法登录的问题</li>
                          <li>修复公告过长显示不全的问题</li>
                          <li>修复设备码不稳定问题，更新设备码算法，可能导致用户设备码变化，请引导用户点击解绑按钮自行解绑</li>
                          <li>修复某些APK注入完毕闪退的问题</li>
                          <li>优化直链下载更新逻辑，可自动运行新版本安装包，如果无法自动安装，也支持跳转浏览器下载安装包</li>
                          <li>优化断网重连逻辑，临时断网会尝试重连</li>
                        </ol>
                        <li>优化：APK注入完成下载失败后将显示一个下载链接供下载重试</li>
                        <li>优化：远程脚本执行前缀支持设置，用户可根据实际环境进行配置</li>
                        <li>优化：代理用户可批量添加代理授权</li>
                        <li>优化：代理用户添加代理授权时会显示自己的提卡价格供代理参考</li>
                        <li>优化：官网激活功能支持续费及恢复授权(需设备码一致)功能</li>
                      </ol>
                    </el-collapse-item>
                  <el-collapse-item title="v1.5.2 - 2023-03-30">
                    <ol>
                      <li>修复：修复卡密、单码无法批量导入的问题</li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v1.5.1 - 2023-03-18">
                    <ol>
                      <li>修复：修复APK全局注入闪退的问题（感谢@手游大咖）</li>
                      <li>
                        修复：修复APK单例注入无法选择&lt;init&gt;、&lt;cinit&gt;等方法的问题
                      </li>
                      <li>
                        修复：修复手机访问首页无法点击导航菜单的问题（感谢@冲你的风）
                      </li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v1.5.0 - 2023-03-11">
                    <ol>
                      <li>修复：更正API文档个别API分类错误的问题</li>
                      <li>修复：修复首页系统提示信息被遮挡的问题</li>
                      <li>修复：软件用户在用户列表页切换用户状态不生效的问题</li>
                      <li>新增：软件用户信息增加最后登录IP字段</li>
                      <li>新增：批量增减已过期用户时长支持设置基准时间</li>
                      <li>
                        新增：新增不登录获取用户信息API接口【appUserInfo.nc】和【appUserInfo.nu】
                      </li>
                      <li>新增：支持批量导入/导出卡密和单码数据</li>
                      <li>
                        新增：新增日志自动清理，当日志文件超过1G，日志记录超过10w条将自动执行清理任务，阈值可在【系统配置】-【参数设置】中调整
                      </li>
                      <li>优化：扣时扣点日志列表调整为倒序显示</li>
                      <li>
                        优化：软件配置新增【开启前台充值】选项，用于设置是否在系统首页【充值续费】模块中显示该软件，默认为开启
                      </li>
                      <li>优化：APK快速接入优化</li>
                      <ol>
                        <li>支持单例模式接入</li>
                        <li>支持选择弹窗或全屏模式</li>
                        <li>支持有效期内不再弹窗</li>
                      </ol>
                      <li>
                        优化：默认关闭系统账号注册功能，如需开启请在【系统配置】-【参数设置】中开启
                      </li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v1.4.0 - 2023-01-13">
                    <ol>
                      <li>新增：用户过期时间/剩余点数变动日志</li>
                      <li>新增：按条件批量操作单码/卡密/软件用户</li>
                      <li>优化：数据库自动升级优化</li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v1.3.0 - 2023-01-02">
                    <ol>
                      <li>
                        修复：修复限开顶号无效的问题（感谢@挽时 摆烂中的摆烂王）
                      </li>
                      <li>
                        修复：修复多客户端同时登录可能绕过多开限制的问题（感谢@挽时
                        摆烂中的摆烂王）
                      </li>
                      <li>新增：代理系统新增软件用户管理</li>
                      <li>
                        新增：APK快速接入增加模板皮肤选择并增加一个黑白登录皮肤
                      </li>
                      <li>优化：系统中的"年"时长由360天更改为365天</li>
                      <li>
                        优化：去除配置文件中【文件存储路径】的配置，由系统自动获取
                      </li>
                      <li>
                        优化：远程文件下载链接不再限制下载次数，可自定义设置链接有效期，默认为1小时
                      </li>
                      <li>
                        优化：单码被停用/删除后，对应软件用户将被停用（旧版本已使用的单码必须在软件用户中停用才生效）
                      </li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v1.2.0 - 2022-12-07">
                    <ol>
                      <li>
                        修复：修复APK快速接入如果版本更新链接为空更新崩溃的问题
                      </li>
                      <li>修复：软件试用功能异常的问题</li>
                      <li>修复：修复试用次数设置为1次时无法正常试用的问题</li>
                      <li>新增：系统自动更新功能</li>
                      <li>优化：新建软件时，绑定设置中允许扣到负数提供默认值</li>
                      <li>优化：新建卡类时，可选择不关闭添加窗口继续添加</li>
                      <li>优化：新增授权时显示卡类零售价格</li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v1.1.0 - 2022-11-19">
                    <ol>
                      <li>
                        修复：修复卡类/单码类别下架后商城依然显示的问题（感谢@挽时、
                        ）
                      </li>
                      <li>
                        修复：修复当设备码不存在时可能引起在线列表显示报错的问题
                      </li>
                      <li>
                        修复：修复管理员账号打开代理制卡页面时提示无操作权限的问题
                      </li>
                      <li>
                        修复：修复某些页面pageSize配置未生效的问题（感谢@挽时、 ）
                      </li>
                      <li>修复：修复软件删除后无法创建同名软件的问题</li>
                      <li>
                        修复：修复可以创建同版本号软件版本的问题，避免版本号重复导致的问题
                      </li>
                      <li>
                        修复：修复代理端未根据所选软件显示对应卡类的问题（感谢@挽时、
                        ）
                      </li>
                      <li>
                        修复：修复仪表盘软件数据总登录码数量[今日制码]不统计数量的问题
                      </li>
                      <li>
                        修复：修复修改卡密密码为空时无法提交的问题（感谢@Jin县森）
                      </li>
                      <li>修复：修复商城按订单号查询订单报订单号长度有误的问题</li>
                      <li>修复：修复在首页解绑设备时未校验密码的问题</li>
                      <li>修复：修复删除代理时报错的问题</li>
                      <li>
                        新增：新增解绑设置可设定是否开启软件的解绑功能以及可解绑次数
                      </li>
                      <li>新增：前台页面导航栏支持自定义显示的栏目</li>
                      <li>新增：前台页面支持自定义首页</li>
                      <li>新增：前台页面支持关闭，如不需要可选择关闭，默认开启</li>
                      <li>新增：软件增加购卡地址字段，可通过取软件信息API获取</li>
                      <li>新增：可设置卡类是否支持解绑</li>
                      <li>
                        优化：优化充值余额显示的商品名称为[{网站简称}-余额充值]账号xx，xx元
                      </li>
                      <li>优化：商城下单时无需再填写查询密码</li>
                      <li>
                        优化：更新EXE注入方式，新增快速接入工具，注入成功率更高，更安全
                      </li>
                      <li>
                        优化：新增系统日志开关，关闭后系统将不记录任何日志，默认为开
                      </li>
                      <li>优化：IP转换地址使用redis缓存</li>
                      <li>优化：软件版本下载链接允许最长为5000字符</li>
                      <li>
                        调整：由于手机浏览器不兼容卡密下载，商城不再对手机用户显示卡密下载按钮
                      </li>
                      <li>
                        调整：调整API接口【unbindDevice.ag】【unbindDevice.nu】【unbindDevice.nc】移除参数[enableNegative]，将以软件后台设置为准（已发布的软件不受影响）
                      </li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v1.0.0 - 2022-10-18">
                    <ol>
                      <li>
                        修复：修复代理生成的单码可能被系统商城售出的问题，代理制卡默认不上架（感谢@挽时、
                        ）
                      </li>
                      <li>
                        新增：远程文件功能（文件上传API默认关闭，需在系统参数设置中开启）
                      </li>
                      <li>新增：商城导航新增查询卡密、充值续费、解绑设备选项</li>
                      <li>
                        新增：apk一键注入新增自动下载后自动启动安装（需在后台配置版本直链地址，被注入的apk拥有文件读写权限）
                      </li>
                      <li>新增：网站首页备案号设置</li>
                      <li>优化：批量操作功能，增加同时操作单码与单码用户选项</li>
                      <li>
                        优化：软件用户查询页面可按到期时间/剩余点数和VIP状态查询用户
                      </li>
                      <li>优化：代理系统不再列出所有用户，避免用户数据泄露</li>
                      <li>优化：卡密/单码增加是否代理制卡信息显示</li>
                      <li>优化：完善代理查看卡密/单码页面信息显示</li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v0.0.9 - 2022-09-24">
                    <ol>
                      <li>修复：修复试用用户无法删除的问题</li>
                      <li>
                        修复：修复全局脚本中当多个内置变量在同一行时解析错误的问题（感谢@Jin县森）
                      </li>
                      <li>
                        修复：修复根据正则表达式生成卡密/单码无效的问题（感谢@Jin县森）
                      </li>
                      <li>新增：在线授权功能，可支持一键在线授权或续费</li>
                      <li>
                        新增：服务器返回数据同时返回sign签名，双向SIGN签名安全性提升[注意：使用旧版SDK接入验证且通讯加密为明文的软件会无法正常使用]
                      </li>
                      <li>新增：非登录状态获取用户到期时间/剩余点数接口</li>
                      <li>新增：批量操作功能，可批量操作卡密/单码/用户</li>
                      <li>
                        新增：批量生成的卡密/单码添加制卡批次号，卡密/单码列表支持使用批次号查询
                      </li>
                      <li>
                        新增：以卡充卡接口，单码用户支持使用新单码进行充值，充值后用户剩余时长/点数将累加，由卡密继承来的多开/自定义参数信息将被新单码覆盖
                      </li>
                      <li>
                        优化：优化【unbindDevice】解除绑定指定设备接口，当绑定关系不存在时返回-1
                      </li>
                      <li>
                        优化：添加或更换授权文件后重新登录后台即可，无需再重启服务器
                      </li>
                      <li>
                        优化：服务器所有接口时间格式化为"yyyy-MM-dd
                        HH:mm:ss"标准格式
                      </li>
                      <li>优化：软件心跳间隔限制最低为30秒</li>
                      <li>优化：减少冗余日志输出</li>
                      <li>
                        优化：单码用户不再同步单码的冻结删除状态，且每个单码只可被激活一次
                      </li>
                      <li>
                        优化：试用功能增加【试用有效时间内重复登录不增加试用次数】选项，默认关闭（感谢@手游大咖）
                      </li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v0.0.8 - 2022-08-27">
                    <ol>
                      <li>修复：易支付支付宝方式无法正常支付的问题（感谢@11）</li>
                      <li>
                        修复：修复卡密/单码面值过大会造成用户到期时间计算错误导致用户过期的问题（感谢@小客观）
                      </li>
                      <li>
                        修复：修复软件用户页“所属软件”下拉列表显示不全的问题（感谢@小客观）
                      </li>
                      <li>
                        修复：修复批量生成卡密时有几率显示undefined的问题（感谢@手游大咖）
                      </li>
                      <li>
                        修复：修复给用户加款会导致用户角色丢失的bug（感谢@Jin县森）
                      </li>
                      <li>
                        修复：修复RechargeCard
                        API充值无密码充值卡时提示输入密码的问题（感谢@Jin县森）
                      </li>
                      <li>
                        修复：修复全局脚本无法输入大于小于号的问题（感谢@小Ω猪）
                      </li>
                      <li>
                        修复：修复清空软件用户登录日志报无权限的问题（感谢@小客观）
                      </li>
                      <li>
                        新增：卡密支持携带自定义参数，使用卡密登录或充值后该参数将被附加到用户信息中，可通过用户信息API接口获取
                      </li>
                      <li>
                        新增：卡密支持携带多开信息，使用卡密登录或充值后该用户的多开数将以卡密为准（多开数生效优先级：卡密设置>软件用户设置>软件设置）
                      </li>
                      <li>
                        新增：软件用户页可实时查看各个软件用户在线用户数量和在线设备数量
                      </li>
                      <li>
                        新增：全局脚本支持内置变量功能，形如${对象名.属性名}，具体使用方法请参看验证后台说明
                      </li>
                      <li>新增：新增获取用户绑定设备API接口</li>
                      <li>
                        优化：批量制卡中的卡密是否上架选项默认与卡类中的设置一致，默认制卡数量设置为1
                      </li>
                      <li>优化：优化跳转支付方式的用户体验</li>
                      <li>优化：优化API文档，对每个API接口的返回值做了详细说明</li>
                      <li>
                        优化：充值卡/单码页面增加卡密使用日期显示（感谢@手游大咖）
                      </li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v0.0.7 - 2022-08-06">
                    <ol>
                      <li>修复：修复windows服务器cmd窗口中文乱码的问题</li>
                      <li>
                        修复：修复软件最新版本API和软件最新强制版本API接口当版本停用时依然提示更新的问题
                      </li>
                      <li>
                        修复：修复在线用户单码用户搜索不生效的问题（感谢@手游大咖）
                      </li>
                      <li>
                        修复：修复仪表盘统计信息中今日激活充值卡/单码统计数据错误的问题（感谢@手游大咖）
                      </li>
                      <li>
                        修复：修复仪表盘统计信息当充值卡/单码数据量过大时请求超时的问题
                      </li>
                      <li>
                        修复：重复打开软件版本管理标签页不自动刷新为来源软件的问题（感谢@手游大咖）
                      </li>
                      <li>
                        新增：安卓程序(apk)快速接入功能，支持自动签名，目前只支持[单码计时]模式
                      </li>
                      <li>
                        新增：软件试用功能，支持按时间段或次数多种方式配置试用规则，同步增加试用登录API接口
                      </li>
                      <li>新增：增加代理批量制卡回显，支持复制和下载</li>
                      <li>优化：简化安装步骤，去除logback.xml配置文件</li>
                      <li>
                        优化：用户账号/软件用户/用户设备/用户单码被冻结/过期后将立刻作用于API接口，用户将立刻下线（旧版本需等用户自行下线或后台踢掉）
                      </li>
                      <li>
                        优化：优化客户端下线相关提示（系统强制下线/账号异地登录等）
                      </li>
                      <li>
                        优化：用户/卡密登录API接口增加"autoReducePoint"参数，用于指定计点模式下是否登录成功后自动扣除用户点数（扣除1），默认值为true
                      </li>
                      <li>优化：手机访问商城体验优化</li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v0.0.6 - 2022-07-09">
                    <ol>
                      <li>修复：软件用户列表不显示单码用户的问题</li>
                      <li>修复：新建版本是否检查MD5无默认值的问题</li>
                      <li>修复：单码模式取用户信息报错的问题</li>
                      <li>修复：登录失败的日志记录失败的问题</li>
                      <li>新增：增加微信支付官方接口</li>
                      <li>新增：易支付接口，包括支付宝、微信、QQ钱包通道</li>
                      <li>新增：批量制卡回显，支持复制和下载</li>
                      <li>
                        新增：为减少不必要的日志记录，用户可配置是否记录WEBAPI调用日志：可选记录全部/不记录/只记录失败
                      </li>
                      <li>优化：win启动脚本设置为utf-8编码</li>
                      <li>优化：商城卡密显示有效期文字调整，避免歧义</li>
                      <li>优化：缓存IP与地址的映射，避免重复获取</li>
                      <li>优化：列表内容默认显示条数支持自定义修改</li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v0.0.5 - 2022-07-02">
                    <ol>
                      <li>修复：修复注册账号API报错的问题</li>
                      <li>
                        修复：修复商城未填写联系方式和查询密码依然可以提交订单的问题
                      </li>
                      <li>修复：修复商城购登录码自动提卡失败的问题</li>
                      <li>
                        修复：修复批量生成卡密/登录码数量过多的情况下页面提示接口超时的问题
                      </li>
                      <li>修复：修复代理授权提示无法重复授权的问题</li>
                      <li>修复：修复管理员批量制卡显示403无权限的问题</li>
                      <li>修复：商城购买总价为0的商品无法通过付款的问题</li>
                      <li>修复：快速接入后的软件无法识别中文用户名的问题</li>
                      <li>
                        新增：添加全局远程变量功能，同步添加远程脚本调用API接口
                      </li>
                      <li>
                        新增：管理员可在首页发布用户公告和代理公告，用户公告全员可见，代理公告仅代理可见
                      </li>
                      <li>
                        优化：软件用户信息API可以获取用户名、昵称、余额等账号相关信息
                      </li>
                      <li>优化：修改支付配置后可立即生效，不再需要重启服务</li>
                      <li>
                        优化：优化数据库结构，关键数据的删除改为逻辑删除避免误删
                      </li>
                      <li>优化：软件版本管理可设置下载直链地址</li>
                      <li>优化：软件版本管理可设置是否校验MD5</li>
                      <li>
                        优化：限制子代理制卡价格不能低于父代理制卡价格，避免0元购漏洞
                      </li>
                      <li>
                        优化：所有用户(包括新注册)都可看到左侧的个人中心与余额变动菜单
                      </li>
                      <li>优化：快速接入功能不再限制软件大小</li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v0.0.4 - 2022-06-23">
                    <ol>
                      <li>
                        修复：修复仪表盘软件数据页内中英文软件名有时无法对齐的问题
                      </li>
                      <li>修复：修复订单ID生成算法可能报错的问题</li>
                      <li>新增：代理系统</li>
                      <ol>
                        <li>
                          新增：代理用户管理（可无限级发展子代理，自定义子代理有效期）
                        </li>
                        <li>
                          新增：代理卡类授权（可自定义有效期，自定义代理价格）
                        </li>
                        <li>新增：代理批量制卡</li>
                        <li>新增：代理制卡计费</li>
                        <li>新增：代理卡密管理</li>
                        <li>新增：代理单码管理</li>
                      </ol>
                      <li>新增：用户余额</li>
                      <ol>
                        <li>新增：余额充值</li>
                        <li>新增：后台加减款</li>
                        <li>新增：余额变动日志</li>
                        <li>新增：我的订单</li>
                      </ol>
                      <li>
                        优化：规范API接口参数，删除冗余参数，同步修改易模块及调用例程
                      </li>
                      <li>优化：优化列表页编号</li>
                      <li>
                        优化：如果网站开启https会导致系统错误获取站点域名为http，修改为优先加载网站配置中的站点地址，如果开启了https，请在网站配置中配置完整的https站点地址
                      </li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v0.0.3 - 2022-05-21">
                    <ol>
                      <li>修复：修复后台启动时支付插件偶发加载失败的问题</li>
                      <li>新增：添加后台安全入口功能，可自定义后台登录地址</li>
                      <li>
                        新增：添加全局远程脚本功能，可支持JavaScript/Python/PHP三种语言，同步添加远程脚本调用API接口
                      </li>
                      <li>优化：优化订单号生成逻辑</li>
                      <li>
                        优化：卡密列表/登录码列表/订单列表展示优化，单击表项可展开详情信息
                      </li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v0.0.2 - 2022-05-14">
                    <ol>
                      <li>优化：降低系统内存占用</li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v0.0.1 - 2022-05-12">
                    <ol>
                      <li>
                        修复：修复选择了API输出加密选项后返回客户端的枚举值不正确的问题（感谢@ゞ孤独旅程℡.）
                      </li>
                      <li>修复：修复无数据时sale dashboard页面超出下标的问题</li>
                      <li>优化：购物时联系方式和查询密码改为必填项</li>
                      <li>优化：订单记录增加字段区分自动发货还是手动发货</li>
                    </ol>
                  </el-collapse-item>
                  <el-collapse-item title="v0.0.0 - 2022-05-01">
                    <ol>
                      <li>系统内测版v0.0.0完成</li>
                    </ol>
                  </el-collapse-item>
                </el-collapse>
              </el-card>
            </el-col>
            <!-- <el-col :xs="24" :sm="24" :md="12" :lg="8">
              <el-card class="update-log">
                <div slot="header" class="clearfix">
                  <span>捐赠支持</span>
                </div>
                <div class="body">
                  <img
                    src="https://oscimg.oschina.net/oscnet/up-d6695f82666e5018f715c41cb7ee60d3b73.png"
                    alt="donate"
                    width="100%"
                  />
                  <span
                    style="display: inline-block; height: 30px; line-height: 30px"
                  >
                    您可以请作者喝杯咖啡表示鼓励
                  </span>
                </div>
              </el-card>
            </el-col> -->
          </el-row>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24" :xs="24">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-card style="margin-bottom: 10px">
                <div slot="header" class="clearfix">
                  <span>系统公告</span>
                </div>
                <el-collapse>
                  <div v-if="userNotice && userNotice['content']">
                    <el-collapse-item name="1">
                    <template slot="title">
                      <el-tag size="mini" style="margin-right: 5px"><i class="el-icon-bell"></i>系统消息</el-tag>{{ userNotice['title'] || '暂无消息' }}
                    </template>
                    <div class="ql-container ql-bubble">
                      <div class="ql-editor">
                        <div>
                          <div v-if="userNotice && userNotice['content']">
                            <span v-html="userNotice['content']"></span>
                          </div>
                          <!-- <div v-else>暂无公告</div> -->
                        </div>
                      </div>
                    </div>
                  </el-collapse-item>
                  </div>
                  <div v-if="checkRole(['agent']) && agentNotice && agentNotice['content']">
                    <el-collapse-item name="2">
                      <template slot="title">
                        <el-tag size="mini" style="margin-right: 5px"><i class="el-icon-bell"></i>系统消息(仅代理可见)</el-tag>{{ agentNotice['title'] || '暂无消息' }}
                      </template>
                      <div class="ql-container ql-bubble">
                          <div class="ql-editor">
                            <div>
                              <div v-if="agentNotice && agentNotice['content']">
                                <span v-html="agentNotice['content']"></span>
                              </div>
                              <!-- <div v-else>暂无公告</div> -->
                            </div>
                          </div>
                        </div>
                    </el-collapse-item>
                  </div>
                </el-collapse>
              </el-card>
              <el-card class="update-log">
                <div slot="header" class="clearfix">
                  <span>联系信息</span>
                </div>
                <div class="body">
                  <p>
                  <i class="el-icon-s-promotion"></i> 官网：<el-link
                    href="http://hy.coordsoft.com"
                    target="_blank"
                    >http://hy.coordsoft.com</el-link>
                </p>
                  <p>
                    <i class="el-icon-user-solid"></i> QQ群：
                    <a href="https://jq.qq.com/?_wv=1027&k=3VxLmWXg" target="_blank"
                    >947144396</a
                    >
                  </p>
                  <p>
                    <i class="el-icon-chat-dot-round"></i> 微信：<a
                    href="javascript:;"
                  >coordsoft</a
                  >
                  </p>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </div>

    <el-dialog
      :title="updateTitle"
      :visible.sync="dialogTableVisible"
      custom-class="customClass"
      style="margin-top: 10vh; height: 80%"
      width="1000px"
    >
      <div
        v-if="update"
        align="center"
        style="font-weight: 1000; font-size: large"
      >
        <i class="el-icon-warning"></i>发现新的版本，是否立即更新？
      </div>
      <div v-else align="center" style="font-weight: 1000; font-size: large">
        <i class="el-icon-success"></i>恭喜您，当前已是最新版本
      </div>
      <div class="update_conter" style="margin-top: 20px">
        <div class="update_version">
          <div>
            最新版本：<a class="hylink">{{ lastestVersion }}</a>
          </div>
          <div>更新日期：{{ updateDate }}</div>
        </div>
        <div class="update_logs">
          <div v-html="updateLog"></div>
        </div>
      </div>
      <div v-show="update">
        <div class="update_conter" style="margin-top: 20px">
          <span style="color: red"
          >注意：更新完毕后如有必要本系统将自动重启，届时所有功能将会暂停约2~3分钟，可能造成部分软件用户连接异常或掉线，请在非高峰时段进行更新。</span
          >
        </div>
        <div class="hy-form-btn">
          <el-button
            class="btn"
            round
            style="background-color: #f9f9f9"
            @click="dialogTableVisible = false"
          >忽略本次更新
          </el-button>
          <el-button class="btn" round type="success" @click="doUpdate"
          >立即更新
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getSimpleLicenseInfo } from "@/api/system/license";
import {getSysInfo} from "@/api/common";
import {getWebsiteConfig} from "@/api/system/website";
import {getNotice} from "@/api/system/index";
import {checkUpdate, doUpdate, getStatus, doRestart, getRestartStatus, exportErrorLog } from "@/api/system/update";
import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数
import { mapGetters } from "vuex";

export default {
  name: "Index",
  computed: {
    ...mapGetters(['nickName']),
  },
  data() {
    return {
      // 版本号
      version: null,
      dbVersion: null,
      // 用户公告
      userNotice: null,
      // 代理公告
      agentNotice: null,
      dialogTableVisible: false,
      updateTitle: "",
      updateLog: "",
      lastestVersion: "",
      updateDate: "",
      update: false,
      timer: null,
      remainTime: 300,
      timer2: null,
      remainTime2: 300,
      exportLoading: false,
      remainTimeReadable: "",
      remainTimeSeconds: 9999999,
      expireTime: null,
    };
  },
  methods: {
    checkPermi,
    checkRole,
    getNotice,
    goTarget(href) {
      window.open(href, "_blank");
    },
    getSysInfo() {
      getSysInfo().then((res) => {
        this.version = res.version + "(" + res.versionNo + ")";
        this.dbVersion = res.dbVersion + "(" + res.dbVersionNo + ")";
      });
      getWebsiteConfig().then((res) => {
        if(!res.data.domain) {
          this.$modal
            .confirm("检测到您尚未配置验证域名，可能影响到验证功能的正常使用，是否立即配置？")
            .then(() => {
              this.$tab.openPage("网站设置", "/sysConfig/website");
            })
            .catch(() => {
            });
        }
      });
    },
    checkUpdate() {
      this.$modal.loading("正在检查更新，请稍后...");
      checkUpdate()
        .then((res) => {
          // console.log(res);
          this.update = res.data.update || res.data.updateDb; //false; //
          if (this.update) {
            this.updateTitle = "检测到新版本";
          } else {
            this.updateTitle = "未发现新版本";
          }
          let updateInfo = res.data.updateInfo;
          this.updateLog = updateInfo.updateLog;
          this.lastestVersion =
            updateInfo.versionName + "(" + updateInfo.versionNo + ")";
          this.updateDate = updateInfo.updateDate;
          this.dialogTableVisible = true;
        })
        .finally(() => {
          this.$modal.closeLoading();
        });
    },
    doUpdate() {
      this.$modal
        .confirm("是否确认更新？")
        .then(() => {
          this.dialogTableVisible = false;
          // console.log("确认");
          doUpdate()
            .then((res) => {
              if (this.timer) {
                clearInterval(this.timer);
              }
              this.timer = setInterval(this.getStatus, 2000);
            })
            .finally(() => {
            });
        })
        .catch(() => {
        });
    },
    doRestart() {
      this.$modal
        .confirm("是否确认重启验证系统？")
        .then(() => {
          this.dialogTableVisible = false;
          // console.log("确认");
          doRestart()
            .then((res) => {
              if (this.timer2) {
                clearInterval(this.timer2);
              }
              this.timer2 = setInterval(this.getRestartStatus, 2000);
            })
            .finally(() => {
            });
        })
        .catch(() => {
        });
    },
    getStatus() {
      if (this.remainTime >= 0) {
        getStatus().then((res) => {
          console.log(res);
          this.$modal.msg(res.msg);
          if (res.finish == 1) {
            clearInterval(this.timer);
          }
        });
        --this.remainTime;
      } else {
        clearInterval(this.timer);
        this.$modal.alert("获取更新结果超时，请登录服务器后台查看");
      }
    },
    getRestartStatus() {
      if (this.remainTime2 >= 0) {
        getRestartStatus().then((res) => {
          console.log(res);
          this.$modal.msg(res.msg);
          if (res.finish == 1) {
            clearInterval(this.timer2);
          }
        });
        --this.remainTime2;
      } else {
        clearInterval(this.timer2);
        this.$modal.alert("获取重启结果超时，请登录服务器后台查看");
      }
    },
    /** 导出按钮操作 */
    handleExport() {
      this.$modal
        .confirm("是否确认导出错误日志？")
        .then(() => {
          this.exportLoading = true;
          return exportErrorLog();
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => { });
    },
    getSimpleLicenseInfo,
  },
  created() {
    getSimpleLicenseInfo().then((res) => {
      this.expireTime = res.data.expireTime;
      this.remainTimeReadable = res.data.remainTimeReadable;
      this.remainTimeSeconds = res.data.remainTimeSeconds;
    });
    this.getSysInfo();
    getNotice({type: 1}).then((res) => {
      this.userNotice = res.data;
    });
    if (checkRole(["agent"])) {
      getNotice({type: 2}).then((res) => {
        this.agentNotice = res.data;
      });
    }
  },
};
</script>

<style lang="scss" scoped>
.home {

  background-color: #f5f7f9;

  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>
<style>
.el-alert__content {
  width: 100%;
}

.customClass {
  max-width: 500px;
}

.update_conter {
  background: #f9f9f9;
  border-radius: 4px;
  padding: 20px;
  margin: 15px;
  margin-top: 15px;
}

.update_version {
  font-size: 12px;
  margin-bottom: 10px;
  text-align: center;
}

.update_logs {
  font-size: 12px;
  color: #555;
  max-height: 200px;
  overflow: auto;
}

.update_conter span {
  display: block;
  font-size: 12px;
  color: #666;
}

.update_version div {
  font-size: 13.5px !important;
  font-weight: 700;
  text-align: left;
}

.hylink {
  color: #20a53a;
  font-style: inherit;
}

.hy-form-btn {
  text-align: center;
  padding: 10px 0;
}

.hy-form-btn .btn {
  display: inline-block;
  line-height: 38px;
  height: 40px;
  border-radius: 20px;
  width: 140px;
  padding: 0;
  margin-right: 30px;
  font-size: 13.5px;
}
</style>

