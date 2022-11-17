package cn.sc.app.core.domain.system;

import cn.sc.app.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 系统访问记录表
 */
@Data
@Entity
@Table(name = "sys_login_info", indexes = {@Index(name = "k_username", columnList = "username")})
@EntityListeners(AuditingEntityListener.class)
public class SysLoginInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户账号
     */
    @Column(name = "username", length = 16, nullable = false)
    private String userName;

    /**
     * 登录状态 0成功 1失败
     */
    @Column(name = "status", length = 1, nullable = false)
    private String status;

    /**
     * 登录IP地址
     */
    @Column(name = "ipaddr", length = 64)
    private String ipaddr;

    /**
     * 登录地点
     */
    @Column(name = "loginLocation", length = 64)
    private String loginLocation;

    /**
     * 浏览器类型
     */
    @Column(name = "browser", length = 16)
    private String browser;

    /**
     * 操作系统
     */
    @Column(name = "os", length = 64)
    private String os;

    /**
     * 提示消息
     */
    @Column(name = "msg", length = 32)
    private String msg;

    /**
     * 访问时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "login_time")
    private Date loginTime;

}
