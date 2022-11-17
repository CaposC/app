package cn.sc.app.core.domain.system;

import cn.sc.app.core.domain.BaseEntity;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * 字典类型表 sys_dict_type
 */
@Data
@Table(name = "sys_dict_type", indexes = {@Index(name = "k_dict_type", columnList = "dict_type")})
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SysDictType extends BaseEntity {
    
    private static final long serialVersionUID = 1L;

    /**
     * 字典名称
     */
    @Column(name = "dict_name", length = 16, nullable = false)
    private String dictName;

    /**
     * 字典类型
     */
    @Column(name = "dict_type", length = 32, nullable = false)
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    @Column(name = "status", columnDefinition = "varchar(1) not null default '0'")
    private String status;

    @Column(name = "remark", length = 16)
    private String remark;
    
}
