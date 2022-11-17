import request from '@/utils/request'

// 授权的所选用户
export function authUserSelectAll(data){
  return request({
    url: '/system/role/authUserSelectAll',
    method: 'post',
    data
  })
}

// 查询未授权的用户列表
export function unallocatedUserList(query){
  return request({
    url: '/system/role/unallocatedUserList',
    method: 'get',
    params: query
  })
}

// 批量用户取消授权
export function authUserCancelAll(data){
  return request({
    url: '/system/role/authUserCancelAll',
    method: 'post',
    data
  })
}

// 单个用户取消授权
export function authUserCancel(data) {
  return request({
    url: '/system/role/authUserCancel',
    method: 'post',
    data
  })
}

// 已分配的所有用户列表
export function allocatedUserList(query) {
  return request({
    url: '/system/role/allocatedUserList',
    method: 'get',
    params: query
  })
}

// 获取角色信息
export function delRole(ids) {
  return request({
    url: '/system/role/delRole/' + ids,
    method: 'delete'
  })
}

// 改变状态
export function changeRoleStatus(id, status) {
  const data = {
    id: id,
    status: status
  }
  return request({
    url: '/system/role/changeRoleStatus',
    method: 'post',
    data: data
  })
}

// 获取角色信息
export function getRole(id) {
  return request({
    url: '/system/role/getRole/' + id,
    method: 'get'
  })
}

// 编辑角色
export function updateRole(data) {
  return request({
    url: '/system/role/updateRole',
    method: 'post',
    data
  })
}

// 新增角色
export function addRole(data) {
  return request({
    url: '/system/role/addRole',
    method: 'post',
    data
  })
}

// 查询用户列表
export function listRole(query) {
  return request({
    url: '/system/role/list',
    method: 'get',
    params: query
  })
}
