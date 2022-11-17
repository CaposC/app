 /**
 * v-hasPermi 操作权限处理
 * Copyright (c) 2019 ruoyi
 */

import store from '@/store'

export default {
  /**
   * 钩子函数
   * @param el el指定所绑定的元素,可以用来直接操作DOM
   * @param binding 一个对象,包含指令的很多信息
   * @param vnode vue编译生成的虚拟节点
   */
  inserted(el, binding, vnode) {
    // 按钮对应的permission
    const { value } = binding
    // 全部权限
    const all_permission = "*:*:*";
    // 用户拥有的权限list,就是permissions对应的一串
    const permissions = store.getters && store.getters.permissions

    if (value && value instanceof Array && value.length > 0) {
      const permissionFlag = value

      const hasPermissions = permissions.some(permission => {
        // 如果拥有全部权限或者拥有的权限中包含该按钮权限
        return all_permission === permission || permissionFlag.includes(permission)
      })

      if (!hasPermissions) {
        // 如果没有,那么直接移除该按钮
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error(`请设置操作权限标签值`)

    }
  }
}
