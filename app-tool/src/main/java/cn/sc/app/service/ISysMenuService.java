package cn.sc.app.service;

import cn.sc.app.core.domain.system.SysMenu;
import cn.sc.app.core.domain.vo.RouterVo;
import cn.sc.app.elementUi.TreeSelect;

import java.util.Collection;
import java.util.List;

public interface ISysMenuService {

    Collection<String> findMenuPermsByUserId(Long userId);

    List<SysMenu> selectMenuTree(String[] menuTypes);

    List<RouterVo> buildMenus(List<SysMenu> menus);

    List<TreeSelect> buildTreeSelect(List<SysMenu> data);

    List<Long> selectMenuListByRoleId(Long id);

    void delMenu(Long id);

    void updateMenu(SysMenu sysMenu);
}
