package cn.sc.app.security.handle;

import cn.hutool.json.JSONUtil;
import cn.sc.app.constant.HttpStatus;
import cn.sc.app.core.domain.AjaxResult;
import cn.sc.app.utils.ServletUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        int code = HttpStatus.UNAUTHORIZED;
        ServletUtils.renderString(response, JSONUtil.toJsonStr(AjaxResult.error(code, "认证失败,无法访问系统资源,请重新登录!")));
    }
}
