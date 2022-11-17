import request from '@/utils/request'
import { parseStrEmpty } from '@/utils/index'

// 超级管理员重置其他用户密码
export function resetUserPwd(id, value) {
  const data = {
    id: id,
    password: value
  }
  return request({
    url: '/system/user/resetUserPwd',
    method: 'post',
    data: data
  })
}

// 更新用户的角色信息
export function updateAuthRole(data) {
  return request({
    url: '/system/user/updateAuthRole',
    method: 'post',
    data
  })
}

// 查询授权角色
export function getAuthRole(id) {
  return request({
    url: '/system/user/authRole/' + id,
    method: 'get'
  })
}

// 上传用户头像
export function uploadAvatar(data) {
  return request({
    url: '/system/user/uploadAvatar',
    method: 'post',
    data
  })
}

// 更新密码
export function updateUserPwd(oldPassword, newPassword) {
  const data = {
    oldPassword,
    newPassword
  }
  return request({
    url: '/system/user/updateUserPwd',
    method: 'post',
    data: data
  })
}

// 获取用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/system/user/updateUserProfile',
    method: 'post',
    data
  })
}

// 获取用户个人信息
export function getUserProfile() {
  return request({
    url: '/system/user/getUserProfile',
    method: 'get'
  })
}

// 删除用户
export function delUser(ids) {
  return request({
    url: '/system/user/' + ids,
    method: 'delete'
  })
}

// 停用用户
export function changeUserStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/system/user/changeUserStatus',
    method: 'post',
    data: data
  })
}

// 编辑用户
export function updateUser(data) {
  return request({
    url: '/system/user/updateUser',
    method: 'post',
    data
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/system/user/addUser',
    method: 'post',
    data
  })
}

// 查询用户详细
export function getUser(userId) {
  return request({
    url: '/system/user/' + parseStrEmpty(userId),
    method: 'get'
  })
}

// 查询用户列表
export function listUser(query) {
  return request({
    url: '/system/user/list',
    method: 'get',
    params: query
  })
}

export function login(data) {
  return request({
    url: '/system/user/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/system/user/info',
    method: 'get'
  })
}

export function getRouters() {
  return request({
    url: '/system/user/getRouters',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}
