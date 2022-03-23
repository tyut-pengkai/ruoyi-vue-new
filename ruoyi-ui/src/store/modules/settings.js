import defaultSettings from '@/settings'

const { sideTheme, showSettings, topNav, tagsView, fixedHeader, sidebarLogo, dynamicTitle } = defaultSettings

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
  websiteShortName: '',
  websiteLogo: ''
}
const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  }
}

const actions = {
  // 修改布局设置
  changeSetting({commit}, data) {
    commit('CHANGE_SETTING', data)
  },
  // 设置网页标题
  setTitle({commit}, title) {
    state.title = title
  },
  // 设置网站名称
  setWebsiteName({commit}, websiteName) {
    state.websiteName = websiteName
  },
  // 设置网站简称
  setWebsiteShortName({commit}, websiteShortName) {
    state.websiteShortName = websiteShortName
  },
  // 设置网站简称
  setWebsiteLogo({commit}, websiteLogo) {
    state.websiteLogo = websiteLogo
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

