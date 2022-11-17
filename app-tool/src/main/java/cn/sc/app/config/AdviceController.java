package cn.sc.app.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.sc.app.core.domain.AjaxResult;
import cn.sc.app.exception.ServiceException;
import cn.sc.app.exception.file.FileException;
import cn.sc.app.utils.SpringContextUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AdviceController implements ApplicationListener<ContextRefreshedEvent> {

    private String env;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
        Environment environment = applicationContext.getEnvironment();
        String[] environmentActiveProfiles = environment.getActiveProfiles();
        if (ObjectUtil.isEmpty(environmentActiveProfiles) || environmentActiveProfiles.length < 1) {
            env = "dev";
        } else {
            env = environmentActiveProfiles[0];
        }
    }

    @ExceptionHandler(FileException.class)
    @ResponseBody
    public AjaxResult fileException(FileException e) {
        return checkEnv(e, null);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseBody
    public AjaxResult serviceException(InvalidDataAccessApiUsageException e) {
        return checkEnv(e, "id不能为空!");
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    public AjaxResult serviceException(DataAccessException e) {
        return checkEnv(e, "已存在该数据!");
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public AjaxResult serviceException(ServiceException e) {
        if (e.getCode() != null) {
            return new AjaxResult(e.getCode(), e.getMessage());
        }
        e.printStackTrace();
        return AjaxResult.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public AjaxResult errorException(Exception e) {
        return checkEnv(e, null);
    }

    private AjaxResult checkEnv(Exception e, String tag) {
        AjaxResult ajaxResult;
        if ("dev".equals(env)) {
            if (StrUtil.isNotBlank(tag)) {
                ajaxResult = AjaxResult.error(tag);
            } else {
                ajaxResult = AjaxResult.error(e.getMessage());
            }
        } else {
            ajaxResult = AjaxResult.error("系统错误");
        }
        e.printStackTrace();
        return ajaxResult;
    }
}
