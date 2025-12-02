import axios from 'axios'
import { Message, MessageBox, Loading } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { tansParams, blobValidate } from '@/utils/ruoyi'
import cache from '@/plugins/cache'
import { saveAs } from 'file-saver'

export let isRelogin = { show: false } // 登录失效标记

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json;charset=utf-8' }
})

/* ===== 请求拦截 ===== */
service.interceptors.request.use(config => {
  const { headers, method, data, url } = config
  const isToken = headers.isToken === false
  const isRepeat = headers.repeatSubmit === false

  // Token
  if (getToken() && !isToken) headers.Authorization = 'Bearer ' + getToken()

  // GET 参数拼接到 URL
  if (method === 'get' && config.params) {
    config.url = `${url}?${tansParams(config.params).slice(0, -1)}`
    config.params = {}
  }

  // 防重复提交（仅 POST/PUT）
  if (!isRepeat && ['post', 'put'].includes(method)) {
    const requestObj = { url, data: JSON.stringify(data), time: Date.now() }
    const size = new Blob([JSON.stringify(requestObj)]).size
    if (size >= 5 * 1024 * 1024) return config // 超 5M 跳过

    const sessionObj = cache.session.getJSON('sessionObj')
    if (sessionObj &&
        sessionObj.data === requestObj.data &&
        sessionObj.url === requestObj.url &&
        Date.now() - sessionObj.time < 1000) {
      Message.warning('数据正在处理，请勿重复提交')
      return Promise.reject(new Error('repeat'))
    }
    cache.session.setJSON('sessionObj', requestObj)
  }
  return config
}, err => Promise.reject(err))

/* ===== 响应拦截 ===== */
service.interceptors.response.use(
  res => {
    if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') return res.data

    const { code, msg } = res.data
    const message = errorCode[code] || msg || errorCode.default

    if (code === 401) {
      if (isRelogin.show) return Promise.reject('invalid token')
      isRelogin.show = true
      MessageBox.confirm('登录已过期，是否重新登录？', '提示', { type: 'warning' })
        .then(() => {
          isRelogin.show = false
          store.dispatch('LogOut').then(() => (location.href = '/index'))
        })
        .catch(() => (isRelogin.show = false))
      return Promise.reject('invalid token')
    }
    if ([500, 601].includes(code) || code !== 200) {
      code === 500 ? Message.error(message) : Message.warning(message)
      return Promise.reject(new Error(message))
    }
    return res.data
  },
  err => {
    let msg = '系统错误'
    if (err.message.includes('timeout')) msg = '请求超时'
    else if (err.message.includes('Network')) msg = '网络异常'
    else if (err.message.includes('status code')) msg = `接口异常 ${err.response.status}`
    Message.error(msg)
    return Promise.reject(err)
  }
)

/* ===== 通用下载 ===== */
let downloadLoadingInstance
export function download(url, params, filename, config = {}) {
  downloadLoadingInstance = Loading.service({
    text: '正在下载数据，请稍候',
    spinner: 'el-icon-loading',
    background: 'rgba(0,0,0,.7)'
  })

  return service
    .post(url, params, {
      transformRequest: [() => tansParams(params)],
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      responseType: 'blob',
      ...config
    })
    .then(async data => {
      if (blobValidate(data)) {
        saveAs(new Blob([data]), filename)
      } else {
        const txt = await data.text()
        const obj = JSON.parse(txt)
        Message.error(errorCode[obj.code] || obj.msg || '下载失败')
      }
    })
    .catch(() => Message.error('下载出错，请联系管理员'))
    .finally(() => downloadLoadingInstance.close())
}

export default service