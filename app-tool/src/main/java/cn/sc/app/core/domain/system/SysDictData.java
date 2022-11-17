package cn.sc.app.core.domain.system;

import cn.sc.app.core.domain.BaseEntity;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * 字典数据表
 */
@Data
@Table(name = "sys_dict_data", indexes = {@Index(name = "k_dict_type", columnList = "dict_type")})
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SysDictData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 字典排序
     */
    @Column(name = "dict_sort", columnDefinition = "int(11) not null default 0")
    private Integer dictSort;

    /**
     * 字典标签
     */
    @Column(name = "dict_label", length = 8, nullable = false)
    private String dictLabel;

    /**
     * 字典键值
     */
    @Column(name = "dict_value", length = 16, nullable = false)
    private String dictValue;

    /**
     * 字典类型
     */
    @Column(name = "dict_type", length = 32, nullable = false)
    private String dictType;

    /**
     * 是否默认（Y是 N否）
     */
    @Column(name = "is_default", columnDefinition = "varchar(8) not null default 'N'")
    private String isDefault;

    /**
     * 状态（0正常 1停用）
     */
    @Column(name = "status", columnDefinition = "varchar(1) not null default '0'")
    private String status;

    /**
     * 备注
     */
    @Column(name = "remark", length = 32)
    private String remark;

    /**
     * 样式属性（其他样式扩展）
     */
    @Column(name = "css_class", length = 32)
    private String cssClass;

    /**
     * 表格字典样式
     */
    @Column(name = "list_class", length = 32)
    private String listClass;
}
