package cn.sc.app.config;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.sc.app.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Component
public class AppLoginInterceptor implements HandlerInterceptor {

    //默认七天
    @Value("${app.jwt.maxAge}")
    private int maxAge;

    //密钥
    @Value("${app.jwt.secret}")
    private String secret;

    //token请求头
    @Value("${app.jwt.header}")
    private String appJwtTokenHeader;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(appJwtTokenHeader);
        //没有token 拦截
        if (token == null) {
            loginFail(response);
            return false;
        }
        //解析不通过 拦截
        if (!JWTUtil.verify(token, secret.getBytes(StandardCharsets.UTF_8))) {
            loginFail(response);
            return false;
        }
        JWT jwt = JWTUtil.parseToken(token);
        Long expireTime = (Long) jwt.getPayload().getClaim("expire_time");
        //jwt过期
        if (System.currentTimeMillis() > expireTime) {
            loginFail(response);
            return false;
        }
        return true;
    }

    private static void loginFail(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        AjaxResult error = new AjaxResult();
        error.put("code", 401);
        error.put("msg", "invalid_identity");
        printWriter.write(JSONUtil.toJsonStr(error));
        printWriter.close();
    }

}
