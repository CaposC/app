package cn.sc.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.sc.app.constant.Constants;
import cn.sc.app.constant.UserConstants;
import cn.sc.app.core.domain.system.SysMenu;
import cn.sc.app.core.domain.system.SysRole;
import cn.sc.app.core.domain.system.SysRoleMenu;
import cn.sc.app.core.domain.vo.MetaVo;
import cn.sc.app.core.domain.vo.RouterVo;
import cn.sc.app.elementUi.TreeSelect;
import cn.sc.app.exception.ServiceException;
import cn.sc.app.helper.PageHelper;
import cn.sc.app.utils.SecurityUtils;
import cn.sc.app.core.domain.repository.SysMenuRepository;
import cn.sc.app.core.domain.repository.SysRoleMenuRepository;
import cn.sc.app.core.domain.repository.SysRoleRepository;
import cn.sc.app.service.ISysMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl implements ISysMenuService {

    @Resource
    private SysMenuRepository sysMenuRepository;

    @Resource
    private SysRoleRepository sysRoleRepository;

    @Resource
    private SysRoleMenuRepository sysRoleMenuRepository;

    @Override
    public Collection<String> findMenuPermsByUserId(Long userId) {
        return sysMenuRepository.findMenuPermsByUserId(userId);
    }

    @Override
    public List<SysMenu> selectMenuTree(String[] menuTypes) {
        List<SysMenu> menus;
        Long userId = SecurityUtils.getUserId();
        SysMenu sysMenu = new SysMenu();
        sysMenu.setOrderByColumn(PageHelper.getOrderSortColumn("parentId", "orderNum"));
        if (ObjectUtil.isEmpty(menuTypes)) {
            menuTypes = new String[]{"M", "C"};
        }
        if (SecurityUtils.isAdmin(userId)) {
            menus = sysMenuRepository.findAllByMenuTypeInAndStatus(menuTypes, Constants.START_STATUS, PageHelper.getSort(sysMenu));
        } else {
            menus = sysMenuRepository.findMenuTreeByUserId(userId, PageHelper.getSort(sysMenu));
        }
        return getChildPerms(menus, 0L);
    }

    /**
     * ????????????????????????????????????
     *
     * @param menus ????????????
     * @return ????????????
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));

            router.setName(StrUtil.upperFirst(menu.getPath()));

            if (menu.getMenuType().equals(UserConstants.TYPE_DIR)) {
                router.setPath("/" + menu.getPath());
            } else {
                router.setPath(menu.getPath());
            }

            router.setComponent(getComponent(menu));
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), false));
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && (UserConstants.TYPE_DIR.equals(menu.getMenuType()) || UserConstants.TYPE_MENU.equals(menu.getMenuType()))) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
            routers.add(router);
        }
        return routers;
    }

    @Override
    public List<TreeSelect> buildTreeSelect(List<SysMenu> sysMenus) {
        List<TreeSelect> treeSelects = new ArrayList<>();
        return buildTreeSelectFn(treeSelects, sysMenus);
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long id) {
        Optional<SysRole> optional = sysRoleRepository.findById(id);
        if (optional.isPresent()) {
            SysRole sysRole = optional.get();
            List<SysRoleMenu> sysRoleMenus = sysRoleMenuRepository.findAllByRoleId(sysRole.getId());
            if (!sysRoleMenus.isEmpty()) {
                return sysRoleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void delMenu(Long id) {
        List<SysMenu> sysMenus = sysMenuRepository.findAll();
        List<SysMenu> collect = sysMenus.stream().filter(s -> s.getId().equals(id)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            SysMenu sysMenu = collect.get(0);
            List<SysMenu> result = sysMenus.stream().filter(s -> s.getParentId().equals(sysMenu.getId())).collect(Collectors.toList());
            if (result.isEmpty()) {
                sysMenuRepository.delete(sysMenu);
            } else {
                throw new ServiceException("???????????????,????????????!");
            }
        }
    }

    @Override
    public void updateMenu(SysMenu sysMenu) {
        Optional<SysMenu> optional = sysMenuRepository.findById(sysMenu.getId());
        if (optional.isPresent()) {
            SysMenu record = optional.get();
            record.setParentId(sysMenu.getParentId());
            record.setMenuType(sysMenu.getMenuType());
            record.setIcon(sysMenu.getIcon());
            record.setMenuName(sysMenu.getMenuName());
            record.setOrderNum(sysMenu.getOrderNum());
            record.setPath(sysMenu.getPath());
            record.setVisible(sysMenu.getVisible());
            record.setStatus(sysMenu.getStatus());
            record.setPerms(sysMenu.getPerms());
            record.setRemark(sysMenu.getRemark());
            record.setComponent(sysMenu.getComponent());
            sysMenuRepository.save(record);
        }
    }

    private List<TreeSelect> buildTreeSelectFn(List<TreeSelect> treeSelects, List<SysMenu> sysMenus) {
        for (SysMenu sysMenu : sysMenus) {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setLabel(sysMenu.getMenuName());
            treeSelect.setId(sysMenu.getId());
            if (!sysMenu.getChildren().isEmpty()) {
                setTreeSelectChildren(treeSelect, sysMenu.getChildren());
            }
            treeSelects.add(treeSelect);
        }
        return treeSelects;
    }

    private void setTreeSelectChildren(TreeSelect target, List<SysMenu> sysMenus) {
        List<TreeSelect> results = new ArrayList<>();
        for (SysMenu sysMenu : sysMenus) {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setLabel(sysMenu.getMenuName());
            treeSelect.setId(sysMenu.getId());
            if (!sysMenu.getChildren().isEmpty()) {
                setTreeSelectChildren(treeSelect, sysMenu.getChildren());
            }
            results.add(treeSelect);
        }
        target.setChildren(results);
    }

    /**
     * ??????????????????
     *
     * @param menu ????????????
     * @return ????????????
     */
    public String getComponent(SysMenu menu) {
        String component = UserConstants.LAYOUT;
        if (ObjectUtil.isNotEmpty(menu.getComponent()) && menu.getMenuType().equals(UserConstants.TYPE_MENU)) {
            component = menu.getComponent();
        } else if (ObjectUtil.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * ?????????parent_view??????
     *
     * @param menu ????????????
     * @return ??????
     */
    public boolean isParentView(SysMenu menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * ??????????????????ID?????????????????????
     *
     * @param menus    ?????????
     * @param parentId ??????????????????ID
     * @return List<SysMenu>
     */
    private List<SysMenu> getChildPerms(List<SysMenu> menus, Long parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext(); ) {
            SysMenu next = iterator.next();
            // ????????????????????????????????????ID,????????????????????????????????????
            if (next.getParentId().equals(parentId)) {
                recursionFn(menus, next);
                returnList.add(next);
            }
        }
        return returnList;
    }

    /**
     * ????????????
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // ?????????????????????
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * ?????????????????????
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().equals(t.getId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * ????????????????????????
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }

}
