package cn.sc.app.service;

import cn.sc.app.core.domain.system.SysUser;
import org.springframework.data.domain.Page;

public interface ISysUserService {

    void updateUserProfile(SysUser sysUser);

    Page<SysUser> findList(SysUser sysUser);

    void addUser(SysUser sysUser);

    void updateUser(SysUser sysUser);

    void changeUserStatus(SysUser sysUser);

    void delUser(Long[] ids);

    void updateAuthRole(SysUser sysUser);

}
