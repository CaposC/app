package cn.sc.app.service.impl;

import cn.sc.app.core.domain.repository.*;
import cn.sc.app.core.domain.system.SysRole;
import cn.sc.app.core.domain.system.SysRoleMenu;
import cn.sc.app.core.domain.system.SysUser;
import cn.sc.app.core.domain.system.SysUserRole;
import cn.sc.app.service.ISysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl implements ISysRoleService {

    @Resource
    private SysMenuRepository sysMenuRepository;

    @Resource
    private SysRoleMenuRepository sysRoleMenuRepository;

    @Resource
    private SysRoleRepository sysRoleRepository;

    @Resource
    private SysUserRepository sysUserRepository;

    @Resource
    private SysUserRoleRepository sysUserRoleRepository;

    @Override
    @Transactional
    public void addRole(SysRole sysRole) {
        SysRole save = sysRoleRepository.save(sysRole);
        List<Long> menuIds = sysRole.getMenuIds();
        if (!menuIds.isEmpty()) {
            List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
            for (Long menuId : menuIds) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(save.getId());
                sysRoleMenu.setMenuId(menuId);
                sysRoleMenus.add(sysRoleMenu);
            }
            sysRoleMenuRepository.saveAllAndFlush(sysRoleMenus);
        }
    }

    @Override
    @Transactional
    public void updateRole(SysRole sysRole) {
        SysRole save = sysRoleRepository.save(sysRole);
        List<Long> menuIds = sysRole.getMenuIds();
        if (!menuIds.isEmpty()) {
            //先删除
            sysRoleMenuRepository.deleteAllByRoleId(save.getId());
            List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
            for (Long menuId : menuIds) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(save.getId());
                sysRoleMenu.setMenuId(menuId);
                sysRoleMenus.add(sysRoleMenu);
            }
            sysRoleMenuRepository.saveAllAndFlush(sysRoleMenus);
        }
    }

    @Override
    @Transactional
    public void delRole(List<Long> ids) {
        if (!ids.isEmpty()) {
            ids = ids.stream().filter(i -> !i.equals(1L)).collect(Collectors.toList());
            sysRoleMenuRepository.deleteAllByRoleIdIn(ids);
            sysRoleRepository.deleteAllById(ids);
        }
    }

    @Override
    @Transactional
    public void deleteByUserIdAndRoleId(SysUser sysUser) {
        Long id = sysUser.getId();
        Long roleId = sysUser.getRoleId();
        sysUserRoleRepository.deleteByUserIdAndRoleId(id, roleId);
    }

    @Override
    @Transactional
    public void authUserCancelAll(SysRole sysRole) {
        Long id = sysRole.getId();
        List<Long> userIds = sysRole.getUserIds();
        sysUserRoleRepository.deleteByUserIdInAndRoleId(userIds, id);
    }

    @Override
    @Transactional
    public void authUserSelectAll(SysRole sysRole) {
        Long id = sysRole.getId();
        List<Long> userIds = sysRole.getUserIds();
        sysUserRoleRepository.deleteByUserIdInAndRoleId(userIds, id);
        List<SysUserRole> sysUserRoles = new ArrayList<>();
        for (Long userId : userIds) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(id);
            sysUserRoles.add(sysUserRole);
        }
        sysUserRoleRepository.saveAllAndFlush(sysUserRoles);
    }

}
