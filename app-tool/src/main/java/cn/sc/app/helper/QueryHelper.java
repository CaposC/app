package cn.sc.app.helper;

import cn.hutool.core.util.ObjectUtil;
import cn.sc.app.annotation.Perception;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.Column;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryHelper<T> {

    private static final String BFH = "%";

    public Specification<T> getSpecification(T t) {
        Specification<T> specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //条件集合
                Predicate predicate = criteriaBuilder.conjunction();
                Class<?> aClass = t.getClass();
                List<Field> allFields = getAllFields(aClass);
                for (Field field : allFields) {
                    if (field.getAnnotation(Column.class) == null && field.getAnnotation(Perception.class) == null) {
                        continue;
                    }
                    // 获取指定对象的当前字段的值
                    try {
                        //开启私有变量访问
                        field.setAccessible(true);
                        Object fieldVal = field.get(t);
                        String fieldName = field.getName();
                        if (ObjectUtil.isNotEmpty(fieldVal)) {
                            predicate.getExpressions().add(criteriaBuilder.like(root.get(fieldName), BFH + (String) fieldVal + BFH));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
                return predicate;
            }
        };
        return specification;
    }

    //返回该类所有字段
    private static List<Field> getAllFields(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        return new ArrayList<>(Arrays.asList(declaredFields));
    }

}
