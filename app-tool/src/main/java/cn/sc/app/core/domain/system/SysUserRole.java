package cn.sc.app.core.domain.system;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@IdClass(SysUserRole.class)
@Table(name = "sys_user_role")
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Id
    @Column(name = "user_id", length = 20, nullable = false)
    private Long userId;

    /**
     * 角色ID
     */
    @Id
    @Column(name = "role_id", length = 20, nullable = false)
    private Long roleId;
}
