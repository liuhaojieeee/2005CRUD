package com.wd.Utils;

import org.springframework.data.elasticsearch.core.SearchHit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName ESHighLightUtil
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/4
 * @Version V1.0
 **/
public class ESHighLightUtil {



    public static <T> List<T> getHighlightList(List<SearchHit<T>> search){
        List<T> list = search.stream().map(hit -> {
            T content = hit.getContent();
            Map<String, List<String>> highlightFields = hit.getHighlightFields();
            highlightFields.forEach((key, value) -> {
                try {
                    Method method = content.getClass().getMethod("set" + firstCharUpper(key), String.class);
                    method.invoke(content, value.get(0));

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            });

            return content;
        }).collect(Collectors.toList());


        return list;
    }


    public static String firstCharUpper(String str){
        char[] chars = str.toCharArray();
        chars[0] -= 32;
        return String.valueOf(chars);
    }

}
