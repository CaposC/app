package cn.sc.app.web.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.sc.app.controller.BaseController;
import cn.sc.app.core.domain.AjaxResult;
import cn.sc.app.core.domain.system.SysDictData;
import cn.sc.app.core.domain.repository.SysDictDataRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 */
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {

    @Resource
    private SysDictDataRepository sysDictDataRepository;

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType) {
        List<SysDictData> data = sysDictDataRepository.findByDictType(dictType);
        if (ObjectUtil.isNull(data)) {
            data = new ArrayList<SysDictData>();
        }
        return AjaxResult.success(data);
    }

}
