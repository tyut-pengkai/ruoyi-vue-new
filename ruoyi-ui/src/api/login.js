import request from '@/utils/request'

// 检查安全入口是否正确
export function checkSafeEntrance(query) {
  return request({
    url: '/checkSafeEntrance',
    params: query,
    method: 'get'
  })
}

// 登录方法
export function login(username, password, code, uuid, vstr) {
  const data = {
    username,
    password,
    code,
    uuid,
    vstr
  }
  return request({
    url: '/login',
    headers: {
      isToken: false
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
