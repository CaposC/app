package cn.sc.app.core.domain.repository;

import cn.sc.app.core.domain.repository.IRepository;
import cn.sc.app.core.domain.system.SysDictData;

import java.util.List;

public interface SysDictDataRepository extends IRepository<SysDictData, Long> {
    List<SysDictData> findByDictType(String dictType);
}
