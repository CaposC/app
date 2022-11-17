package cn.sc.app.core.domain.system;

import cn.sc.app.core.domain.BaseEntity;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

//系统操作日志
@Data
@Entity
@Table(name = "sys_oper_log")
@EntityListeners(AuditingEntityListener.class)
public class SysOperLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 操作模块
     */
    @Column(name = "title", length = 50)
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除 4授权 5导出 6导入 7强退）
     */
    @Column(name = "business_type")
    private Integer businessType;

    /**
     * 请求方法
     */
    @Column(name = "method")
    private String method;

    /**
     * 请求方式
     */
    @Column(name = "request_method", length = 10)
    private String requestMethod;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    @Column(name = "operator_type")
    private Integer operatorType;

    /**
     * 操作人员
     */
    @Column(name = "oper_name", length = 50)
    private String operName;

    /**
     * 请求url
     */
    @Column(name = "oper_url", length = 300)
    private String operUrl;

    /**
     * 操作地址
     */
    @Column(name = "oper_ip", length = 128)
    private String operIp;

    /**
     * 操作地点
     */
    @Column(name = "oper_location")
    private String operLocation;

    /**
     * 请求参数
     */
    @Column(name = "oper_param", length = 2000)
    private String operParam;

    /**
     * 返回参数
     */
    @Column(name = "json_result", length = 2000)
    private String jsonResult;

    /**
     * 操作状态（0正常 1异常）
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 错误消息
     */
    @Column(name = "error_msg", length = 2000)
    private String errorMsg;

}
