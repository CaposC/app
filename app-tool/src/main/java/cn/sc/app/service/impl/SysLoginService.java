package cn.sc.app.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.sc.app.constant.Constants;
import cn.sc.app.core.domain.model.LoginUser;
import cn.sc.app.core.domain.system.SysRole;
import cn.sc.app.core.domain.system.SysUser;
import cn.sc.app.exception.ServiceException;
import cn.sc.app.exception.user.UserPasswordNotMatchException;
import cn.sc.app.manager.AsyncManager;
import cn.sc.app.manager.task.SystemTaskFactory;
import cn.sc.app.security.service.TokenService;
import cn.sc.app.utils.MessageUtils;
import cn.sc.app.utils.ServletUtils;
import cn.sc.app.utils.ip.IpUtils;
import cn.sc.app.core.domain.repository.SysRoleRepository;
import cn.sc.app.service.ISysUserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SysLoginService {

    @Resource
    private TokenService tokenService;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private SysRoleRepository sysRoleRepository;

    @Resource
    private ISysUserService userService;

    public String login(String username, String password, String code, String uuid) {
        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(SystemTaskFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(SystemTaskFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        AsyncManager.me().execute(SystemTaskFactory.recordLoginInfo(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        initSysUserInfo(loginUser);
        // 生成token
        return tokenService.createToken(loginUser);
    }

    private void initSysUserInfo(LoginUser loginUser) {
        SysUser sysUser = loginUser.getSysUser();
        sysUser.setPassword(null);
        //查找role
        List<SysRole> sysRoles = sysRoleRepository.findSysUserRoleByUserId(sysUser.getId());
        if (ObjectUtil.isNotEmpty(sysRoles)) {
            loginUser.setRoles(sysRoles.stream().map(SysRole::getRoleKey).collect(Collectors.toSet()));
            sysUser.setSysRoles(sysRoles);
        }
        loginUser.setSysUser(sysUser);
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(new DateTime());
        userService.updateUserProfile(sysUser);
    }
}
