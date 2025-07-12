import request from '@/utils/request'

// 用户注册
export function register(data) {
  return request({
    url: '/register',
    method: 'post',
    data: data
  })
}

// 校验用户名唯一性
export function checkUsernameUnique(data) {
  return request({
    url: '/checkUsernameUnique',
    method: 'post',
    data: data
  })
}

// 校验邮箱唯一性
export function checkEmailUnique(data) {
  return request({
    url: '/checkEmailUnique',
    method: 'post',
    data: data
  })
}

// 发送邮箱验证码
export function sendEmailCode(data) {
  return request({
    url: '/sendEmailCode',
    method: 'post',
    data: data
  })
}

// 邮箱验证码登录
export function emailLogin(data) {
  return request({
    url: '/emailLogin',
    method: 'post',
    data: data
  })
} 