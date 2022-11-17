package cn.sc.app.core.domain.repository;

import cn.sc.app.core.domain.repository.IRepository;
import cn.sc.app.core.domain.system.SysMenu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface SysMenuRepository extends IRepository<SysMenu, Long> {

    @Query(value = "select distinct m.perms " +
            "from SysMenu m " +
            "left join SysRoleMenu rm on m.id = rm.menuId " +
            "left join SysUserRole ur on rm.roleId = ur.roleId " +
            "left join SysRole r on r.id = ur.roleId " +
            "where m.status = '0' and r.status = '0' and ur.userId = ?1")
    Set<String> findMenuPermsByUserId(Long userId);

    @Query(value = "select distinct m from SysMenu m " +
            "left join SysRoleMenu rm on m.id = rm.menuId " +
            "left join SysUserRole ur on rm.roleId = ur.roleId " +
            "left join SysRole ro on ur.roleId = ro.id " +
            "left join SysUser u on ur.userId = u.id " +
            "where u.id=?1 and m.menuType in ('M','C') and m.status='0' and ro.status='0' ")
    List<SysMenu> findMenuTreeByUserId(Long userId, Sort sort);

    List<SysMenu> findAllByMenuTypeInAndStatus(String[] menuTypes, String startStatus, Sort sort);

}
