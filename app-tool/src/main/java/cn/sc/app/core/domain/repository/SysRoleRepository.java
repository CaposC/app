package cn.sc.app.core.domain.repository;

import cn.sc.app.core.domain.repository.IRepository;
import cn.sc.app.core.domain.system.SysRole;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysRoleRepository extends IRepository<SysRole, Long> {

    @Query(value = "select r from SysRole r " +
            "left join SysUserRole ur on r.id=ur.roleId " +
            "left join SysUser u on u.id = ur.userId " +
            "where u.id = ?1 and r.status = '0'")
    List<SysRole> findSysUserRoleByUserId(Long id);

}
