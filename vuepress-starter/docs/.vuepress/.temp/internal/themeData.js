export const themeData = JSON.parse("{\"logo\":\"/images/favico.png\",\"notFound\":[\"红叶温馨提示：这里什么都没有哦\"],\"backToHome\":\"去首页看看吧\",\"tip\":\"提示\",\"warning\":\"警告\",\"danger\":\"注意\",\"navbar\":[{\"text\":\"首页\",\"link\":\"/\"},{\"text\":\"演示网站\",\"link\":\"http://demo.coordsoft.com\"},{\"text\":\"交流论坛\",\"link\":\"http://bbs.coordsoft.com\"},{\"text\":\"购买授权\",\"link\":\"http://shop.coordsoft.com\"},{\"text\":\"加入群聊\",\"link\":\"https://jq.qq.com/?_wv=1027&k=tT0T695Q\"}],\"sidebar\":[{\"text\":\"系统介绍\",\"collapsible\":false,\"children\":[{\"text\":\"系统简介\",\"collapsible\":true,\"link\":\"/introduce/introduce/introduce.md\"},{\"text\":\"主要功能\",\"collapsible\":true,\"link\":\"/introduce/function/function.md\"},{\"text\":\"演示站点\",\"collapsible\":true,\"link\":\"/introduce/demo/demo.md\"},\"/introduce/update-log/update-log.md\"]},{\"text\":\"部署搭建\",\"collapsible\":false,\"children\":[{\"text\":\"需求环境\",\"collapsible\":true,\"link\":\"/install/install-env.md\"},{\"text\":\"部署教程\",\"collapsible\":true,\"children\":[{\"text\":\"Linux服务器\",\"collapsible\":true,\"children\":[\"/install/linux/bt/linux-bt.md\"]},{\"text\":\"Windows服务器\",\"collapsible\":true,\"children\":[\"/install/windows/bt/windows-bt.md\",\"/install/windows/upupw/windows-upupw.md\"]}]},{\"text\":\"激活授权\",\"collapsible\":true,\"link\":\"/install/license/license.md\"},{\"text\":\"初始配置\",\"collapsible\":true,\"link\":\"/install/init-config/init-config.md\"}]},{\"text\":\"使用指南\",\"collapsible\":false,\"children\":[\"/guide/getting-started/getting-started.md\",\"/guide/quick-access/quick-access.md\",{\"text\":\"代码接入\",\"collapsible\":true,\"children\":[\"/guide/code-access/code-access.md\",\"/guide/code-access/api-docs.md\"]},\"/guide/function-introduce/function-introduce.md\",{\"text\":\"支付对接\",\"collapsible\":true,\"children\":[\"/guide/payment/alipay-f2f/alipay-f2f.md\",\"/guide/payment/wechatpay-native/wechatpay-native.md\",\"/guide/payment/epay/epay.md\"]},\"/guide/qa/qa.md\"]}],\"locales\":{\"/\":{\"selectLanguageName\":\"English\"}},\"colorMode\":\"auto\",\"colorModeSwitch\":true,\"repo\":null,\"selectLanguageText\":\"Languages\",\"selectLanguageAriaLabel\":\"Select language\",\"sidebarDepth\":2,\"editLink\":true,\"editLinkText\":\"Edit this page\",\"lastUpdated\":true,\"lastUpdatedText\":\"Last Updated\",\"contributors\":true,\"contributorsText\":\"Contributors\",\"openInNewWindow\":\"open in new window\",\"toggleColorMode\":\"toggle color mode\",\"toggleSidebar\":\"toggle sidebar\"}")

if (import.meta.webpackHot) {
  import.meta.webpackHot.accept()
  if (__VUE_HMR_RUNTIME__.updateThemeData) {
    __VUE_HMR_RUNTIME__.updateThemeData(themeData)
  }
}

if (import.meta.hot) {
  import.meta.hot.accept(({ themeData }) => {
    __VUE_HMR_RUNTIME__.updateThemeData(themeData)
  })
}
