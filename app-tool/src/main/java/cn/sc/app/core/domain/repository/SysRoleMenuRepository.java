package cn.sc.app.core.domain.repository;

import cn.sc.app.core.domain.repository.IRepository;
import cn.sc.app.core.domain.system.SysRoleMenu;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface SysRoleMenuRepository extends IRepository<SysRoleMenu, Long> {

    @Modifying
    void deleteAllByRoleId(Long id);

    List<SysRoleMenu> findAllByRoleId(Long id);

    @Modifying
    void deleteAllByRoleIdIn(List<Long> ids);
}
