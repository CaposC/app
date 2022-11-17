package cn.sc.app.web.controller;

import cn.sc.app.controller.BaseController;
import cn.sc.app.core.domain.repository.SysDictTypeRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 数据字典信息
 */
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController {

    @Resource
    private SysDictTypeRepository sysDictTypeRepository;

}
