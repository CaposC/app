package cn.sc.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.sc.app.constant.Constants;
import cn.sc.app.core.domain.model.LoginUser;
import cn.sc.app.core.domain.system.SysUser;
import cn.sc.app.exception.ServiceException;
import cn.sc.app.core.domain.repository.SysUserRepository;
import cn.sc.app.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private SysPermissionService permissionService;

    @Resource
    private SysUserRepository sysUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserRepository.findByUsername(username);
        if (ObjectUtil.isEmpty(sysUser)) {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException("登录用户：" + username + " 不存在");
        } else if (Constants.STOP_STATUS.equals(sysUser.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }
        return createLoginUser(sysUser);
    }

    public UserDetails createLoginUser(SysUser sysUser) {
        return new LoginUser(sysUser.getId(), sysUser, permissionService.getMenuPermission(sysUser));
    }
}
