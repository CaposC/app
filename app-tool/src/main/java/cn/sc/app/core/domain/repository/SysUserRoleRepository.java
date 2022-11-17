package cn.sc.app.core.domain.repository;

import cn.sc.app.core.domain.repository.IRepository;
import cn.sc.app.core.domain.system.SysUserRole;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface SysUserRoleRepository extends IRepository<SysUserRole, Long> {

    @Modifying
    void deleteByUserId(Long id);

    List<SysUserRole> findAllByUserIdIn(Long[] ids);

    List<SysUserRole> findAllByUserId(Long id);

    @Modifying
    void deleteByUserIdAndRoleId(Long id, Long roleId);

    @Modifying
    void deleteByUserIdInAndRoleId(List<Long> userIds, Long id);
}
