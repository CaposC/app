package cn.sc.app.core.domain.repository;

import cn.sc.app.core.domain.repository.IRepository;
import cn.sc.app.core.domain.system.SysRole;
import cn.sc.app.core.domain.system.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SysUserRepository extends IRepository<SysUser, Long> {

    SysUser findByUsername(String username);

    List<SysUser> findByPhone(String phone);

    @Query(value = "select u from SysUser u " +
            "left join SysUserRole ur on ur.userId = u.id " +
            "left join SysRole r on r.id = ur.roleId " +
            "where r.id = :#{#sysRole.id} " +
            "and u.username like %:#{#sysRole.username}% " +
            "and IFNULL(u.phone,'') like %:#{#sysRole.phone}%")
    Page<SysUser> findAllocatedUserList(@Param("sysRole") SysRole sysRole, Pageable pageable);

    @Query(value = "select u from SysUser u " +
            "left join SysUserRole ur on ur.userId = u.id " +
            "left join SysRole r on r.id = ur.roleId " +
            "where (r.id != :#{#sysRole.id} or r.id IS NULL) " +
            "and u.username like %:#{#sysRole.username}% " +
            "and IFNULL(u.phone,'') like %:#{#sysRole.phone}% ")
    Page<SysUser> findAllUnallocatedUserList(SysRole sysRole, Pageable pageable);
}
