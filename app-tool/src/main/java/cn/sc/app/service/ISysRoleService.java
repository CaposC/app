package cn.sc.app.service;

import cn.sc.app.core.domain.system.SysRole;
import cn.sc.app.core.domain.system.SysUser;

import java.util.List;

public interface ISysRoleService {

    void addRole(SysRole sysRole);

    void updateRole(SysRole sysRole);

    void delRole(List<Long> ids);

    void deleteByUserIdAndRoleId(SysUser sysUser);

    void authUserCancelAll(SysRole sysRole);

    void authUserSelectAll(SysRole sysRole);

}
