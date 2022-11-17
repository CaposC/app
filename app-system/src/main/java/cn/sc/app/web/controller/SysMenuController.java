package cn.sc.app.web.controller;

import cn.sc.app.annotation.Log;
import cn.sc.app.controller.BaseController;
import cn.sc.app.core.domain.AjaxResult;
import cn.sc.app.core.domain.system.SysMenu;
import cn.sc.app.enums.BusinessType;
import cn.sc.app.helper.QueryHelper;
import cn.sc.app.core.domain.repository.SysMenuRepository;
import cn.sc.app.core.domain.repository.SysRoleMenuRepository;
import cn.sc.app.core.domain.repository.SysRoleRepository;
import cn.sc.app.service.ISysMenuService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {

    private static final String MODULE_NAME = "菜单管理";

    @Resource
    private SysMenuRepository sysMenuRepository;

    @Resource
    private ISysMenuService sysMenuService;

    @Resource
    private SysRoleMenuRepository sysRoleMenuRepository;

    @Resource
    private SysRoleRepository sysRoleRepository;

    //列表
    @GetMapping("/listMenu")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    public AjaxResult listMenu(SysMenu sysMenu) {
        QueryHelper<SysMenu> queryHelper = new QueryHelper<>();
        Specification<SysMenu> specification = queryHelper.getSpecification(sysMenu);
        return AjaxResult.success(sysMenuRepository.findAll(specification));
    }

    //获取详情
    @GetMapping("/getMenu/{id}")
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    public AjaxResult getMenuById(@PathVariable("id") Long id) {
        return AjaxResult.success(sysMenuRepository.findById(id).get());
    }

    //更新菜单信息
    @PostMapping("/updateMenu")
    @Log(title = MODULE_NAME, businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    public AjaxResult updateMenu(@RequestBody SysMenu sysMenu) {
        sysMenuService.updateMenu(sysMenu);
        return AjaxResult.success();
    }

    //新增
    @PostMapping("/addMenu")
    @Log(title = MODULE_NAME, businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    public AjaxResult addMenu(@RequestBody SysMenu sysMenu) {
        sysMenuRepository.save(sysMenu);
        return AjaxResult.success();
    }

    // 删除
    @DeleteMapping("/delMenu/{id}")
    @Log(title = MODULE_NAME, businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    public AjaxResult delMenu(@PathVariable("id") Long id) {
        sysMenuService.delMenu(id);
        return AjaxResult.success();
    }

    @GetMapping("/roleMenutreeselect/{id}")
    public AjaxResult roleMenutreeselect(@PathVariable("id") Long id) {
        List<SysMenu> menus = sysMenuService.selectMenuTree(new String[]{"M", "C", "F"});
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", sysMenuService.selectMenuListByRoleId(id));
        ajax.put("menus", sysMenuService.buildTreeSelect(menus));
        return ajax;
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/menuTreeSelect")
    public AjaxResult menuTreeSelect() {
        List<SysMenu> data = sysMenuService.selectMenuTree(new String[]{"M", "C", "F"});
        return AjaxResult.success(sysMenuService.buildTreeSelect(data));
    }

}
