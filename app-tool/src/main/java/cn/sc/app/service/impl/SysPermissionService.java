package cn.sc.app.service.impl;

import cn.sc.app.core.domain.system.SysUser;
import cn.sc.app.service.ISysMenuService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户权限处理
 */
@Component
public class SysPermissionService {

    @Resource
    private ISysMenuService sysMenuService;

    /**
     * 获取菜单数据权限
     */
    public Set<String> getMenuPermission(SysUser sysUser) {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (sysUser.isAdmin()) {
            perms.add("*:*:*");
        } else {
            perms.addAll(sysMenuService.findMenuPermsByUserId(sysUser.getId()));
        }
        return perms;
    }
}
