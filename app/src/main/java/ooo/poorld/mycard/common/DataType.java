package ooo.poorld.mycard.common;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ooo.poorld.mycard.utils.Constans;

/**
 * author: teenyda
 * date: 2020/11/21
 * description:
 */
public enum  DataType {
    TYPE_WORD(1),
    TYPE_IMAGE(2),
    TYPE_AUDIO(3),
    TYPE_VIDEO(4),;
    private Integer type;

    private static final Map<String, DataType> MAP = new HashMap<>();

    /*static {
        for (DataType type : values()) {
            MAP.put(type.name, type);
        }
    }*/

    DataType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
/*
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }*/

    //获取枚举实例
/*    public static DataType fromValue(Integer value) {
        for (DataType val : values()) {
            if (val.getType().equals(value)) {
                return val;
            }
        }
        throw new IllegalArgumentException();
    }

    public static DataType valueOfName(String name) {
        return MAP.get(name);
    }

    public String toLocale() {
        if (Locale.CHINA.equals(Locale.getDefault())) {
            return name;
        }
        return toString();
    }*/

}
