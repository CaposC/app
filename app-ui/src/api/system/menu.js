import request from '@/utils/request'

// 获取菜单详情
export function updateMenu(data) {
  return request({
    url: '/system/menu/updateMenu',
    method: 'post',
    data
  })
}

// 获取菜单详情
export function getMenu(id) {
  return request({
    url: '/system/menu/getMenu/' + id,
    method: 'get'
  })
}

// 删除
export function delMenu(id) {
  return request({
    url: '/system/menu/delMenu/' + id,
    method: 'delete'
  })
}

// 添加
export function addMenu(data) {
  return request({
    url: '/system/menu/addMenu',
    method: 'post',
    data
  })
}

// 页面列表
export function listMenu(query) {
  return request({
    url: '/system/menu/listMenu',
    method: 'get',
    params: query
  })
}

// 根据角色ID查询菜单树结构
export function roleMenutreeselect(id) {
  return request({
    url: '/system/menu/roleMenutreeselect/' + id,
    method: 'get'
  })
}

// 查询用户列表
export function menuTreeSelect(query) {
  return request({
    url: '/system/menu/menuTreeSelect',
    method: 'get',
    params: query
  })
}
