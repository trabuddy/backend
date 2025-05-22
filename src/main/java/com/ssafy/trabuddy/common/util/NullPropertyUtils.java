package com.ssafy.trabuddy.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class NullPropertyUtils {
    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper wrapper = new BeanWrapperImpl(source);
        Set<String> nullProps = new HashSet<>();
        for (var pd : wrapper.getPropertyDescriptors()) {
            Object val = wrapper.getPropertyValue(pd.getName());
            if (val == null) {
                nullProps.add(pd.getName());
            }
        }
        return nullProps.toArray(String[]::new);
    }
}
