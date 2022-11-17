package cn.sc.app.service.impl;

import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.sc.app.constant.Constants;
import cn.sc.app.core.domain.system.SysUser;
import cn.sc.app.core.domain.system.SysUserRole;
import cn.sc.app.exception.ServiceException;
import cn.sc.app.helper.PageHelper;
import cn.sc.app.helper.QueryHelper;
import cn.sc.app.core.domain.repository.SysUserRepository;
import cn.sc.app.core.domain.repository.SysUserRoleRepository;
import cn.sc.app.service.ISysUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl implements ISysUserService {

    @Resource
    private SysUserRepository sysUserRepository;

    @Resource
    private SysUserRoleRepository sysUserRoleRepository;

    @Override
    @Transactional
    public void updateUser(SysUser sysUser) {
        Long id = sysUser.getId();
        List<Long> roleIds = sysUser.getRoleIds();
        if (ObjectUtil.isNotEmpty(roleIds)) {
            //先清空权限
            sysUserRoleRepository.deleteByUserId(id);
            List<SysUserRole> result = new ArrayList<>();
            //再插入权限
            roleIds.stream().forEach(r -> {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(id);
                sysUserRole.setRoleId(r);
                result.add(sysUserRole);
            });
            sysUserRoleRepository.saveAll(result);
        }
        Optional<SysUser> optional = sysUserRepository.findById(id);
        if (optional.isPresent()) {
            SysUser record = optional.get();
            record.setNickname(sysUser.getNickname());
            record.setPhone(sysUser.getPhone());
            record.setSex(sysUser.getSex());
            record.setStatus(sysUser.getStatus());
            record.setRemark(sysUser.getRemark());
            sysUserRepository.save(record);
        }
    }

    @Override
    public void changeUserStatus(SysUser sysUser) {
        Long id = sysUser.getId();
        Optional<SysUser> optional = sysUserRepository.findById(id);
        if (optional.isPresent()) {
            SysUser record = optional.get();
            record.setStatus(sysUser.getStatus());
            sysUserRepository.save(record);
        }
    }

    @Override
    @Transactional
    public void delUser(Long[] ids) {
        ids = ArrayUtil.filter(ids, new Filter<Long>() {
            @Override
            public boolean accept(Long aLong) {
                return !aLong.equals(1L);
            }
        });
        if (ids.length == 0) {
            throw new ServiceException("超级管理员不能删除!");
        }
        List<SysUserRole> records = sysUserRoleRepository.findAllByUserIdIn(ids);
        sysUserRoleRepository.deleteAll(records);
        sysUserRepository.deleteAllByIdInBatch(Arrays.asList(ids));
    }

    @Override
    @Transactional
    public void updateAuthRole(SysUser sysUser) {
        Long id = sysUser.getId();
        List<Long> roleIds = sysUser.getRoleIds();
        if (!roleIds.isEmpty()) {
            //删除所有角色除了admin
            List<SysUserRole> sysUserRoles = sysUserRoleRepository.findAllByUserId(id);
            List<SysUserRole> filter = sysUserRoles.stream().filter(sysUserRole -> !sysUserRole.getUserId().equals(1L)).collect(Collectors.toList());
            sysUserRoleRepository.deleteAll(filter);
            //分配角色
            List<SysUserRole> results = new ArrayList<>();
            for (Long roleId : roleIds) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(roleId);
                sysUserRole.setUserId(id);
                results.add(sysUserRole);
            }
            sysUserRoleRepository.saveAll(results);
        }
    }

    @Override
    public Page<SysUser> findList(SysUser sysUser) {
        QueryHelper<SysUser> queryHelper = new QueryHelper<>();
        Specification<SysUser> specification = queryHelper.getSpecification(sysUser);
        Pageable pageable = PageHelper.getPageable(sysUser);
        Page<SysUser> all = sysUserRepository.findAll(specification, pageable);
        List<SysUser> content = all.getContent();
        if (ObjectUtil.isNotEmpty(content)) {
            all.stream().forEach(s -> {
                s.setPassword(Constants.BLANK);
            });
        }
        return all;
    }

    @Override
    @Transactional
    public void addUser(SysUser sysUser) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        SysUser save = sysUserRepository.save(sysUser);
        List<Long> roleIds = sysUser.getRoleIds();
        if (!roleIds.isEmpty()) {
            List<SysUserRole> results = new ArrayList<>();
            roleIds.stream().forEach(r -> {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(save.getId());
                sysUserRole.setRoleId(r);
                results.add(sysUserRole);
            });
            sysUserRoleRepository.saveAll(results);
        }
    }

    @Override
    public void updateUserProfile(SysUser sysUser) {
        SysUser record = sysUserRepository.findById(sysUser.getId()).get();
        record.setLoginIp(sysUser.getLoginIp());
        record.setLoginDate(sysUser.getLoginDate());
        sysUserRepository.save(record);
    }
}
