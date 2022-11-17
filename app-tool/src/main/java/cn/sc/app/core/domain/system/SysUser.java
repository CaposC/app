package cn.sc.app.core.domain.system;

import cn.sc.app.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static javax.persistence.ConstraintMode.NO_CONSTRAINT;

@Data
@Entity
@Table(name = "sys_user", indexes = {@Index(name = "uk_username", columnList = "username", unique = true)})
@EntityListeners(AuditingEntityListener.class)
public class SysUser extends BaseEntity {
    @Override
    public String toString() {
        return "SysUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", status='" + status + '\'' +
                ", remark='" + remark + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", loginIp='" + loginIp + '\'' +
                ", loginDate=" + loginDate +
                ", roleIds=" + roleIds +
                ", roleId=" + roleId +
                ", ids=" + ids +
                ", uuid='" + uuid + '\'' +
                ", code='" + code + '\'' +
                ", roleGroup=" + roleGroup +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }

    private static final long serialVersionUID = 1L;

    @Column(name = "username", length = 16, nullable = false)
    private String username;

    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @Column(name = "nickname", length = 8)
    private String nickname;

    //0-正常，1-停用
    @Column(name = "status", columnDefinition = "varchar(1) not null default '0'")
    private String status;

    @Column(name = "remark", length = 16)
    private String remark;

    @Column(name = "avatar", length = 128)
    private String avatar;

    @Column(name = "sex", length = 1)
    private String sex;

    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "login_ip", length = 128)
    private String loginIp;

    @Column(name = "login_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginDate;

    @JsonIgnoreProperties(value = {"sysUsers"})
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "sys_user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(NO_CONSTRAINT)),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "none")))
    private List<SysRole> sysRoles;

    @Transient
    private List<Long> roleIds;

    @Transient
    private Long roleId;

    @Transient
    private List<Long> ids;

    //唯一标识
    @Transient
    private String uuid;

    //验证码
    @Transient
    private String code;

    //角色名字
    @Transient
    private List<String> roleGroup;

    //新密码
    @Transient
    private String oldPassword;

    //旧密码
    @Transient
    private String newPassword;

    public boolean isAdmin() {
        return isAdmin(getId());
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
}
