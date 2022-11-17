package cn.sc.app.helper;

import cn.hutool.core.lang.Assert;
import cn.sc.app.core.page.PageDomain;
import cn.sc.app.template.ErrorMsgTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页助手
 */
public class PageHelper {

    public static Pageable getPageable(PageDomain pageDomain) {
        return (Pageable) PageRequest.of(pageDomain.getPage() - 1, pageDomain.getSize(), pageDomain.getDirection(), pageDomain.getOrderByColumn());
    }

    public static Pageable getPageableByUpAndDown(PageDomain pageDomain) {
        checkProperty(pageDomain);
        //正序
        PageRequest of = PageRequest.of(pageDomain.getPage() - 1, pageDomain.getSize(), Sort.Direction.ASC, pageDomain.getToUpColumn());
        //倒序
        of.withSort(Sort.Direction.DESC, pageDomain.getToDownColumn());
        return (Pageable) of;
    }

    public static Sort getSort(PageDomain pageDomain) {
        return Sort.by(pageDomain.getOrderByColumn());
    }

    public static Sort getSortByUpAndDown(PageDomain pageDomain) {
        checkProperty(pageDomain);
        return Sort.by(Sort.Direction.ASC, pageDomain.getToUpColumn()).and(Sort.by(Sort.Direction.DESC, pageDomain.getToDownColumn()));
    }

    private static void checkProperty(PageDomain pageDomain) {
        Assert.notNull(pageDomain.getToUpColumn(), ErrorMsgTemplate.PROPERTY_IS_NULL);
        Assert.notNull(pageDomain.getToDownColumn(), ErrorMsgTemplate.PROPERTY_IS_NULL);
    }

    public static String[] getOrderSortColumn(String... properties) {
        return Arrays.stream(properties).toArray(String[]::new);
    }
}
