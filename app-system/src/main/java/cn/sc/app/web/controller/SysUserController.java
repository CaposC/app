package cn.sc.app.web.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.sc.app.annotation.Log;
import cn.sc.app.constant.Constants;
import cn.sc.app.controller.BaseController;
import cn.sc.app.core.domain.AjaxResult;
import cn.sc.app.core.domain.model.LoginUser;
import cn.sc.app.core.domain.system.SysMenu;
import cn.sc.app.core.domain.system.SysRole;
import cn.sc.app.core.domain.system.SysUser;
import cn.sc.app.core.domain.vo.RouterVo;
import cn.sc.app.enums.BusinessType;
import cn.sc.app.exception.ServiceException;
import cn.sc.app.security.service.TokenService;
import cn.sc.app.utils.file.FileUploadUtils;
import cn.sc.app.core.domain.repository.SysRoleRepository;
import cn.sc.app.core.domain.repository.SysUserRepository;
import cn.sc.app.service.ISysMenuService;
import cn.sc.app.service.ISysUserService;
import cn.sc.app.service.impl.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {

    private static final String MODULE_NAME = "用户管理";

    @Value("${token.header}")
    private String header;

    @Resource
    private TokenService tokenService;

    @Resource
    private SysLoginService sysLoginService;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ISysMenuService sysMenuService;

    @Resource
    private SysUserRepository sysUserRepository;

    @Resource
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //更新用户的角色信息
    @PostMapping("/updateAuthRole")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = MODULE_NAME, businessType = BusinessType.GRANT)
    public AjaxResult updateAuthRole(@RequestBody SysUser sysUser) {
        sysUserService.updateAuthRole(sysUser);
        return AjaxResult.success();
    }

    //查询授权角色
    @GetMapping("/authRole/{id}")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public AjaxResult getAuthRole(@PathVariable("id") Long id) {
        AjaxResult ajax = AjaxResult.success();
        Optional<SysUser> optional = sysUserRepository.findById(id);
        if (optional.isPresent()) {
            SysUser sysUser = optional.get();
            List<SysRole> roles = sysRoleRepository.findSysUserRoleByUserId(sysUser.getId());
            List<SysRole> allRoles = sysRoleRepository.findAll();
            for (SysRole allRole : allRoles) {
                for (SysRole role : roles) {
                    if (allRole.getId().equals(role.getId())) {
                        allRole.setFlag(true);
                    }
                }
            }
            ajax.put("user", sysUser);
            ajax.put("roles", SysUser.isAdmin(id) ? allRoles : allRoles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        }
        return ajax;
    }

    //上传用户头像
    @PostMapping("/uploadAvatar")
    public AjaxResult uploadAvatar(@RequestParam("avatarfile") MultipartFile file) {
        String upload = FileUploadUtils.upload(file);
        Optional<SysUser> optional = sysUserRepository.findById(getSysUserId());
        if (optional.isPresent()) {
            SysUser sysUser = optional.get();
            sysUser.setAvatar(upload);
            sysUserRepository.save(sysUser);
            return AjaxResult.success(upload);
        }
        return AjaxResult.success();
    }

    //超级管理员重置其他用户密码
    @PostMapping("/resetUserPwd")
    @Log(title = MODULE_NAME, businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    public AjaxResult resetUserPwd(@RequestBody SysUser sysUser) {
        Optional<SysUser> optional = sysUserRepository.findById(sysUser.getId());
        if (optional.isPresent()) {
            SysUser record = optional.get();
            record.setPassword(passwordEncoder.encode(sysUser.getPassword()));
            sysUserRepository.save(record);
        }
        return AjaxResult.success();
    }

    //更新密码
    @PostMapping("/updateUserPwd")
//    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    public AjaxResult updateUserPwd(@RequestBody SysUser sysUser) {
        Long id = getSysUserId();
        Optional<SysUser> optional = sysUserRepository.findById(id);
        if (optional.isPresent()) {
            SysUser record = optional.get();
            if (!passwordEncoder.matches(sysUser.getOldPassword(), record.getPassword())) {
                throw new ServiceException("旧密码不匹配,修改失败!");
            }
            String newPassword = passwordEncoder.encode(sysUser.getNewPassword());
            record.setPassword(newPassword);
            sysUserRepository.save(record);
        }
        return AjaxResult.success();
    }

    //更新用户个人信息
    @PostMapping("/updateUserProfile")
    public AjaxResult updateUserProfile(@RequestBody SysUser sysUser) {
        LoginUser loginUser = getLoginUser();
        Long id = sysUser.getId();
        Optional<SysUser> optional = sysUserRepository.findById(id);
        if (optional.isPresent()) {
            String phone = sysUser.getPhone();
            if (StrUtil.isNotBlank(phone)) {
                List<SysUser> records = sysUserRepository.findByPhone(phone);
                if (records.size() >= 2) {
                    throw new ServiceException("手机号码重复!");
                }
            }
            SysUser record = optional.get();
            record.setNickname(sysUser.getNickname());
            record.setPhone(phone);
            record.setStatus(sysUser.getSex());
            SysUser save = sysUserRepository.save(record);
            //更新缓存
            loginUser.setSysUser(save);
            tokenService.setLoginUser(loginUser);
        }
        return AjaxResult.success();
    }

    //获取用户个人信息
    @GetMapping("/getUserProfile")
    public AjaxResult getUserProfile() {
        LoginUser loginUser = getLoginUser();
        SysUser sysUser = loginUser.getSysUser();
        sysUser.setRoleGroup(sysUser.getSysRoles().stream().map(SysRole::getRoleName).collect(Collectors.toList()));
        return AjaxResult.success(sysUser);
    }

    //删除用户
    @DeleteMapping("/{ids}")
    @Log(title = MODULE_NAME, businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    public AjaxResult delUser(@PathVariable("ids") Long[] ids) {
        sysUserService.delUser(ids);
        return AjaxResult.success();
    }

    //改变用户状态
    @PostMapping("/changeUserStatus")
    @Log(title = MODULE_NAME, businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    public AjaxResult changeUserStatus(@RequestBody SysUser sysUser) {
        sysUserService.changeUserStatus(sysUser);
        return AjaxResult.success();
    }

    //编辑用户
    @PostMapping("/updateUser")
    @Log(title = MODULE_NAME, businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    public AjaxResult updateUser(@RequestBody SysUser sysUser) {
        sysUserService.updateUser(sysUser);
        return AjaxResult.success();
    }

    //新增用户
    @PostMapping("/addUser")
    @Log(title = MODULE_NAME, businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    public AjaxResult addUser(@RequestBody SysUser sysUser) {
        sysUserService.addUser(sysUser);
        return AjaxResult.success();
    }

    /**
     * 根据用户编号获取详细信息
     */
    @GetMapping(value = {"/{userId}", "/"})
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = sysRoleRepository.findAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        if (ObjectUtil.isNotEmpty(userId)) {
            SysUser sysUser = new SysUser();
            Optional<SysUser> optional = sysUserRepository.findById(userId);
            if (optional.isPresent()) {
                sysUser = optional.get();
            }
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("roleIds", sysUser.getSysRoles().stream().map(SysRole::getId).collect(Collectors.toList()));
        }
        return ajax;
    }

    // 查询用户列表
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    public AjaxResult list(SysUser sysUser) {
        return AjaxResult.success(sysUserService.findList(sysUser));
    }

    //登录
    @PostMapping("/login")
    public AjaxResult login(@RequestBody SysUser sysUser) {
        // 生成令牌
        String token = sysLoginService.login(sysUser.getUsername(), sysUser.getPassword(), sysUser.getCode(), sysUser.getUuid());
        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    //获取用户信息
    @GetMapping("/info")
    public AjaxResult getUserInfo(HttpServletRequest request) {
        return AjaxResult.success(tokenService.getLoginUser(request));
    }

    //获取路由信息
    @GetMapping("/getRouters")
    public AjaxResult getRouters() {
        List<SysMenu> menus = sysMenuService.selectMenuTree(null);
        List<RouterVo> data = sysMenuService.buildMenus(menus);
        return AjaxResult.success(data);
    }

}
