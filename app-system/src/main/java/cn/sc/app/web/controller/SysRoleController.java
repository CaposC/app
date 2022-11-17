package cn.sc.app.web.controller;

import cn.sc.app.annotation.Log;
import cn.sc.app.controller.BaseController;
import cn.sc.app.core.domain.AjaxResult;
import cn.sc.app.core.domain.system.SysRole;
import cn.sc.app.core.domain.system.SysUser;
import cn.sc.app.enums.BusinessType;
import cn.sc.app.helper.PageHelper;
import cn.sc.app.helper.QueryHelper;
import cn.sc.app.core.domain.repository.SysRoleMenuRepository;
import cn.sc.app.core.domain.repository.SysRoleRepository;
import cn.sc.app.core.domain.repository.SysUserRepository;
import cn.sc.app.core.domain.repository.SysUserRoleRepository;
import cn.sc.app.service.ISysRoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {

    private static final String MODULE_NAME = "角色管理";
    
    @Resource
    private ISysRoleService sysRoleService;

    @Resource
    private SysRoleMenuRepository sysRoleMenuRepository;

    @Resource
    private SysRoleRepository sysRoleRepository;

    @Resource
    private SysUserRepository sysUserRepository;

    @Resource
    private SysUserRoleRepository sysUserRoleRepository;

    //授权的所选用户
    @PostMapping("/authUserSelectAll")
    @Log(title = MODULE_NAME, businessType = BusinessType.GRANT)
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    public AjaxResult authUserSelectAll(@RequestBody SysRole sysRole) {
        sysRoleService.authUserSelectAll(sysRole);
        return AjaxResult.success();
    }

    //查询未授权的用户列表
    @GetMapping("/unallocatedUserList")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    public AjaxResult unallocatedUserList(SysRole sysRole) {
        Pageable pageable = PageHelper.getPageable(sysRole);
        Page<SysUser> allUnallocatedUserList = sysUserRepository.findAllUnallocatedUserList(sysRole, pageable);
        return AjaxResult.success(allUnallocatedUserList);
    }

    // 查询角色列表
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    public AjaxResult list(SysRole sysRole) {
        QueryHelper<SysRole> queryHelper = new QueryHelper<>();
        Specification<SysRole> specification = queryHelper.getSpecification(sysRole);
        Pageable pageable = PageHelper.getPageable(sysRole);
        return AjaxResult.success(sysRoleRepository.findAll(specification, pageable));
    }

    //新增角色
    @PostMapping("/addRole")
    @Log(title = MODULE_NAME, businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    public AjaxResult addRole(@RequestBody SysRole sysRole) {
        sysRoleService.addRole(sysRole);
        return AjaxResult.success();
    }

    //更新角色信息
    @PostMapping("/updateRole")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = MODULE_NAME, businessType = BusinessType.UPDATE)
    public AjaxResult updateRole(@RequestBody SysRole sysRole) {
        sysRoleService.updateRole(sysRole);
        return AjaxResult.success();
    }

    //获取角色信息
    @GetMapping("/getRole/{id}")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    public AjaxResult getRole(@PathVariable("id") Long id) {
        SysRole sysRole = new SysRole();
        Optional<SysRole> optional = sysRoleRepository.findById(id);
        if (optional.isPresent()) {
            sysRole = optional.get();
        }
        return AjaxResult.success(sysRole);
    }

    //改变该角色状态
    @PostMapping("/changeRoleStatus")
    @Log(title = MODULE_NAME, businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    public AjaxResult changeRoleStatus(@RequestBody SysRole sysRole) {
        Long id = sysRole.getId();
        Optional<SysRole> optional = sysRoleRepository.findById(id);
        if (optional.isPresent()) {
            SysRole record = optional.get();
            record.setStatus(sysRole.getStatus());
            sysRoleRepository.save(record);
        }
        return AjaxResult.success();
    }

    //删除角色
    @DeleteMapping("/delRole/{ids}")
    @Log(title = MODULE_NAME, businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    public AjaxResult delRole(@PathVariable("ids") List<Long> ids) {
        sysRoleService.delRole(ids);
        return AjaxResult.success();
    }

    //已分配的所有用户列表
    @GetMapping("/allocatedUserList")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    public AjaxResult allocatedUserList(SysRole sysRole) {
        Pageable pageable = PageHelper.getPageable(sysRole);
        return AjaxResult.success(sysUserRepository.findAllocatedUserList(sysRole, pageable));
    }

    //单个用户取消授权
    @PostMapping("/authUserCancel")
    @Log(title = MODULE_NAME, businessType = BusinessType.GRANT)
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    public AjaxResult authUserCancel(@RequestBody SysUser sysUser) {
        sysRoleService.deleteByUserIdAndRoleId(sysUser);
        return AjaxResult.success();
    }

    //批量用户取消授权
    @PostMapping("/authUserCancelAll")
    @Log(title = MODULE_NAME, businessType = BusinessType.GRANT)
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    public AjaxResult authUserCancelAll(@RequestBody SysRole sysRole) {
        sysRoleService.authUserCancelAll(sysRole);
        return AjaxResult.success();
    }

}
