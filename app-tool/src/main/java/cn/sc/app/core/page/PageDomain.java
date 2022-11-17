package cn.sc.app.core.page;

import lombok.Data;
import org.springframework.data.domain.Sort;

import javax.persistence.Transient;

@Data
public abstract class PageDomain {

    private static final long serialVersionUID = 1L;

    private static final String ID = "id";

    @Transient
    private Integer page;
    @Transient
    private Integer size;
    @Transient
    private String[] orderByColumn;
    @Transient
    private Sort.Direction direction;
    //正序字段
    @Transient
    private String[] toUpColumn;
    //倒序字段
    @Transient
    private String[] toDownColumn;

    public PageDomain() {
        this.page = 1;
        this.size = 10;
        this.orderByColumn = new String[]{ID};
        this.direction = Sort.Direction.DESC;
    }

    public PageDomain(String[] orderByColumn) {
        this.page = 1;
        this.size = 10;
        this.orderByColumn = orderByColumn;
        this.direction = Sort.Direction.DESC;
    }

    public PageDomain(String[] orderByColumn, Sort.Direction direction) {
        this.page = 1;
        this.size = 10;
        this.orderByColumn = orderByColumn;
        this.direction = direction;
    }

    public PageDomain(String[] toUpColumn, String[] toDownColumn) {
        this.page = 1;
        this.size = 10;
        this.toUpColumn = toUpColumn;
        this.toDownColumn = toDownColumn;
    }

}
