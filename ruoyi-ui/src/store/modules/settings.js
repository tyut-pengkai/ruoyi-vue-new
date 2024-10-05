import defaultSettings from '@/settings'
import {getWebsiteConfig} from '@/api/system/website'

const {
  sideTheme,
  showSettings,
  topNav,
  tagsView,
  fixedHeader,
  sidebarLogo,
  dynamicTitle
} = defaultSettings

const storageSetting = JSON.parse(localStorage.getItem('layout-setting')) || ''
const state = {
  title: '',
  theme: storageSetting.theme || '#409EFF',
  sideTheme: storageSetting.sideTheme || sideTheme,
  showSettings: showSettings,
  topNav: storageSetting.topNav === undefined ? topNav : storageSetting.topNav,
  tagsView: storageSetting.tagsView === undefined ? tagsView : storageSetting.tagsView,
  fixedHeader: storageSetting.fixedHeader === undefined ? fixedHeader : storageSetting.fixedHeader,
  sidebarLogo: storageSetting.sidebarLogo === undefined ? sidebarLogo : storageSetting.sidebarLogo,
  dynamicTitle: storageSetting.dynamicTitle === undefined ? dynamicTitle : storageSetting.dynamicTitle,
  // 自定义添加非ruoyi框架
  websiteName: '',
  shopName: '',
  websiteShortName: '',
  websiteLogo: '',
  safeEntrance: '',
  pageSize: '10',
  icp: '',
  enableFrontEnd: 'Y',
  domain: '',
}
const mutations = {
  CHANGE_SETTING: (state, {
    key,
    value
  }) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  },
  SET_SAFE_ENTRANCE: (state, safeEntrance) => {
    state.safeEntrance = safeEntrance
  },
}

const actions = {
  // 修改布局设置
  changeSetting({
    commit
  }, data) {
    commit('CHANGE_SETTING', data)
  },
  // 设置网页标题
  setTitle({
    commit
  }, title) {
    state.title = title
  },
  // 设置网站名称
  setWebsiteName({
    commit
  }, websiteName) {
    state.websiteName = websiteName
  },
  // 设置商城名称
  setShopName({
    commit
  }, shopName) {
    state.shopName = shopName
  },
  // 设置网站简称
  setWebsiteShortName({
    commit
  }, websiteShortName) {
    state.websiteShortName = websiteShortName
  },
  // 设置网站Logo
  setWebsiteLogo({
    commit
  }, websiteLogo) {
    state.websiteLogo = websiteLogo
  },
  // 设置默认pageSize
  setPageSize({
    commit
  }, pageSize) {
    state.pageSize = pageSize
  },
  // 获取安全入口
  GetSafeEntrance({
                    commit,
                    state
                  }) {
    return new Promise((resolve, reject) => {
      getWebsiteConfig().then(res => {
        commit('SET_SAFE_ENTRANCE', res.data.isSafeEntrance)
        resolve(res)
      }).catch(error => {
        reject(error)
      })
    })
  },
  // 设置网站备案号
  setIcp({
           commit
         }, icp) {
    state.icp = icp
  },
  // 是否开启前台
  setEnableFrontEnd({
                      commit
                    }, enableFrontEnd) {
    state.enableFrontEnd = enableFrontEnd
  },
  // 域名
  setDomain({
    commit
  }, domain) {
    state.domain = domain
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
