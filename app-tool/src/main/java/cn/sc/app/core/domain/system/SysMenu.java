package cn.sc.app.core.domain.system;

import cn.sc.app.core.domain.BaseEntity;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统菜单表 -> 权限表
 */
@Data
@Table(name = "sys_menu", indexes = {@Index(name = "uk_menu_name", columnList = "menu_name")})
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单名称
     */
    @Column(name = "menu_name", length = 16, nullable = false)
    private String menuName;

    /**
     * 父菜单ID
     */
    @Column(name = "parent_id", columnDefinition = "bigint(20) not null default 0")
    private Long parentId;

    /**
     * 显示顺序
     */
    @Column(name = "order_num", columnDefinition = "int(11) not null default 0")
    private Integer orderNum;

    /**
     * 组件路径
     */
    @Column(name = "component", length = 32)
    private String component;

    /**
     * 路由地址
     */
    @Column(name = "path", length = 32)
    private String path;

    /**
     * 类型（M目录 C菜单 F按钮）
     */
    @Column(name = "menu_type", length = 1, nullable = false)
    private String menuType;

    /**
     * 显示状态（0显示 1隐藏）
     */
    @Column(name = "visible", columnDefinition = "varchar(1) not null default '0'")
    private String visible;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    @Column(name = "status", columnDefinition = "varchar(1) not null default '0'")
    private String status;

    /**
     * 权限字符串
     */
    @Column(name = "perms", length = 32)
    private String perms;

    /**
     * 菜单图标
     */
    @Column(name = "icon", length = 32)
    private String icon;

    /**
     * 备注
     */
    @Column(name = "remark", length = 32)
    private String remark;

    /**
     * 子菜单
     */
    @Transient
    private List<SysMenu> children = new ArrayList<SysMenu>();
}
