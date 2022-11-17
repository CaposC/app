package cn.sc.app.manager.task;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.sc.app.constant.Constants;
import cn.sc.app.core.domain.repository.SysLoginInfoRepository;
import cn.sc.app.core.domain.repository.SysOperLogRepository;
import cn.sc.app.core.domain.system.SysLoginInfo;
import cn.sc.app.core.domain.system.SysOperLog;
import cn.sc.app.utils.LogUtils;
import cn.sc.app.utils.ServletUtils;
import cn.sc.app.utils.ip.AddressUtils;
import cn.sc.app.utils.ip.IpUtils;
import cn.sc.app.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class SystemTaskFactory {

    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    private static final String USER_AGENT = "User-Agent";

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息
     * @param args     列表
     * @return 任务task
     */
    public static TimerTask recordLoginInfo(final String username, final String status, final String message, final Object... args) {
        final UserAgent userAgent = UserAgentUtil.parse(ServletUtils.getRequest().getHeader(USER_AGENT));
        final String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        return new TimerTask() {
            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOs().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLoginInfo sysLoginInfo = new SysLoginInfo();
                sysLoginInfo.setUserName(username);
                sysLoginInfo.setIpaddr(ip);
                sysLoginInfo.setLoginLocation(address);
                sysLoginInfo.setBrowser(browser);
                sysLoginInfo.setOs(os);
                sysLoginInfo.setMsg(message);
                // 日志状态
                if (StrUtil.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
                    sysLoginInfo.setStatus(Constants.SUCCESS);
                } else if (Constants.LOGIN_FAIL.equals(status)) {
                    sysLoginInfo.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(SysLoginInfoRepository.class).save(sysLoginInfo);
            }
        };
    }

    public static TimerTask recordOper(SysOperLog operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(SysOperLogRepository.class).save(operLog);
            }
        };
    }
}
