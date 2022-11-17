package cn.sc.app.core.domain.system;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@IdClass(SysRoleMenu.class)
@Table(name = "sys_role_menu")
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @Id
    @Column(name = "role_id", length = 20, nullable = false)
    private Long roleId;

    /**
     * 菜单ID
     */
    @Id
    @Column(name = "menu_id", length = 20, nullable = false)
    private Long menuId;
}
