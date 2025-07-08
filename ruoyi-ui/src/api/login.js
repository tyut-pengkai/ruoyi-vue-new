import request from '@/utils/request'

// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url: '/login',
    headers: {
      isToken: false,
      repeatSubmit: false
    },
    method: 'post',
    data: data
  })
}

// 注册方法
export function register(data) {
  return request({
    url: '/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}

// 微信小程序分步骤登录
export function wxStepLogin(data) {
  return request({
    url: '/wxStepLogin',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 微信小程序完善用户信息并登录
export function wxCompleteUserInfo(data) {
  return request({
    url: '/wxCompleteUserInfo',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 微信小程序一键登录（保留原有接口）
export function wxLogin(data) {
  return request({
    url: '/wxLogin',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}