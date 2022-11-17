package cn.sc.app.utils;

import cn.hutool.core.util.StrUtil;

import java.util.*;

/**
 * 截取工具类
 */
public class Knife {

    private final static String BLANK = "";

    /**
     * 规律字符串通过符号删除元素
     *
     * @param original 初始规律字符串
     * @param target   目标删除字符串
     * @param symbol   符号
     * @return 根据符号拼接的字符串
     */
    public static String delString(String original, String target, String symbol) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> list = Arrays.asList(original.split(symbol));
        if (list.isEmpty()) {
            return BLANK;
        }
        list.stream().filter(l -> !l.equals(target)).forEach((String l) -> stringBuilder.append(l).append(symbol));
        return stringBuilder.toString();
    }

    /**
     * 多线程切割列表
     *
     * @param target 需要切除的目标数组
     * @param count  切成多少份
     * @param <T>    自定义类
     * @return <T> List<List<T>>
     */
    private <T> List<List<T>> splitList(List<T> target, int count) {
        List<List<T>> listArr = new ArrayList<List<T>>();
        int arrSize = target.size() % count == 0 ? target.size() / count : target.size() / count + 1;
        for (int i = 0; i < arrSize; i++) {
            List<T> sub = new ArrayList<>();
            //把指定索引数据放入到list中
            for (int j = i * count; j <= count * (i + 1) - 1; j++) {
                if (j <= target.size() - 1) {
                    sub.add(target.get(j));
                }
            }
            listArr.add(sub);
        }
        return listArr;
    }

    /**
     * 去除驼峰变成数据库普通字段
     */
    public static String wipeHump(String parameter) {
        if (parameter.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(parameter);
        Map<Integer, String> map = new HashMap<>();
        Stack<Integer> indexs = new Stack<>();
        for (int i = 0; i < parameter.length(); i++) {
            char ch = parameter.charAt(i);
            if (Character.isUpperCase(ch)) {
                indexs.push(i);
                map.put(i, (("_") + ch).toLowerCase());
            }
        }
        for (int i = 0; i <= indexs.size(); i++) {
            Integer pop = indexs.pop();
            sb.replace(pop, pop + 1, map.get(pop));
        }
        return sb.toString();
    }

    /**
     * 用户身份证号码的打码隐藏加星号加*
     * <p>18位和非18位身份证处理均可成功处理</p>
     * <p>参数异常直接返回null</p>
     *
     * @param idCardNum 身份证号码
     * @param front     需要显示前几位
     * @param end       需要显示末几位
     * @return 处理完成的身份证
     */
    public static String idMask(String idCardNum, int front, int end) {
        //身份证不能为空
        if (StrUtil.isEmpty(idCardNum)) {
            return null;
        }
        //需要截取的长度不能大于身份证号长度
        if ((front + end) > idCardNum.length()) {
            return null;
        }
        //需要截取的不能小于0
        if (front < 0 || end < 0) {
            return null;
        }
        //计算*的数量
        int asteriskCount = idCardNum.length() - (front + end);
        StringBuffer asteriskStr = new StringBuffer();
        for (int i = 0; i < asteriskCount; i++) {
            asteriskStr.append("*");
        }
        String regex = "(\\w{" + String.valueOf(front) + "})(\\w+)(\\w{" + String.valueOf(end) + "})";
        return idCardNum.replaceAll(regex, "$1" + asteriskStr + "$3");
    }
}
