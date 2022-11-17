package cn.sc.app.controller;

import cn.sc.app.annotation.Anonymous;
import cn.sc.app.core.domain.AjaxResult;
import cn.sc.app.utils.file.FileUploadUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/common")
@RestController
@Anonymous
public class CommonController {

    //文件上传
    @PostMapping("/uploadFile")
    public AjaxResult uploadFile(@RequestBody MultipartFile file){
        return AjaxResult.success(FileUploadUtils.upload(file));
    }

}
