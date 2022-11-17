package cn.sc.app.core.domain.system;

import cn.sc.app.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.ConstraintMode.NO_CONSTRAINT;

@Data
@Entity
@Table(name = "sys_role", indexes = {@Index(name = "uk_role_key", columnList = "role_key", unique = true)})
@EntityListeners(AuditingEntityListener.class)
public class SysRole extends BaseEntity {

    @Override
    public String toString() {
        return "SysRole{" +
                "roleName='" + roleName + '\'' +
                ", roleKey='" + roleKey + '\'' +
                ", roleSort=" + roleSort +
                ", status='" + status + '\'' +
                ", remark='" + remark + '\'' +
                ", menuIds=" + menuIds +
                ", userIds=" + userIds +
                ", flag=" + flag +
                '}';
    }

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    @Column(name = "role_name", length = 16, nullable = false)
    private String roleName;

    /**
     * 角色权限
     */
    @Column(name = "role_key", length = 16, nullable = false)
    private String roleKey;

    /**
     * 角色排序
     */
    @Column(name = "role_sort", columnDefinition = "int(11) not null default 0")
    private Integer roleSort;

    /**
     * 角色状态（0正常 1停用）
     */
    @Column(name = "status", columnDefinition = "varchar(1) not null default '0'")
    private String status;

    @Column(name = "remark", length = 32)
    private String remark;

    @JsonIgnoreProperties(value = {"sysRoles"})
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "sys_user_role",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", foreignKey = @ForeignKey(NO_CONSTRAINT)),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "none")))
    private List<SysUser> sysUsers;

    @Transient
    private List<Long> menuIds;

    @Transient
    private List<Long> userIds;

    @Transient
    private String username;

    @Transient
    private String phone;

    @Transient
    private boolean flag;

    public boolean isAdmin() {
        return isAdmin(this.getId());
    }

    public static boolean isAdmin(Long roleId) {
        return roleId != null && 1L == roleId;
    }

}
