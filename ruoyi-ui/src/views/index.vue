<template>
  <div class="app-container home">
    <el-alert
      v-if="userNotice && userNotice['content']"
      :closable="false"
      :title="userNotice['title'] || '用户公告'"
      style="margin-bottom: 20px"
      type="success"
    >
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
    </el-alert>
    <div v-if="checkRole(['agent'])">
      <el-alert
        v-if="agentNotice && agentNotice['content']"
        :closable="false"
        :title="agentNotice['title'] || '代理公告'"
        style="margin-bottom: 20px"
        type="success"
      >
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
      </el-alert>
    </div>
    <div v-if="checkRole(['admin', 'sadmin'])">
      <el-row :gutter="20">
        <el-col :lg="12" :sm="24" style="padding-left: 20px">
          <h2>红叶网络验证与软件管理系统</h2>
          <p>软件授权管理计费销售一站式解决方案</p>
          <p>
            <b>当前版本:</b> <span>{{ version }}</span>
          </p>
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
        </el-col>

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
      <el-divider/>
      <el-row :gutter="20">
        <el-col :lg="12" :md="12" :sm="24" :xs="24">
          <el-card class="update-log">
            <div slot="header" class="clearfix">
              <span>联系信息</span>
            </div>
            <div class="body">
              <!-- <p>
              <i class="el-icon-s-promotion"></i> 官网：<el-link
                href="http://www.ruoyi.vip"
                target="_blank"
                >http://www.ruoyi.vip</el-link
              >
            </p> -->
              <p>
                <i class="el-icon-user-solid"></i> QQ群：
                <!-- <s>满937441</s> -->
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
      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :lg="12" :md="12" :sm="24" :xs="24">
          <el-card class="update-log">
            <div slot="header" class="clearfix">
              <span>更新日志</span>
            </div>
            <el-collapse accordion>
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
    </div>
  </div>
</template>

<script>
import {getSysInfo} from "@/api/common";
import {getNotice} from "@/api/system/index";
import {checkPermi, checkRole} from "@/utils/permission"; // 权限判断函数

export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: null,
      // 用户公告
      userNotice: null,
      // 代理公告
      agentNotice: null,
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
      });
    },
  },
  created() {
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
</style>

