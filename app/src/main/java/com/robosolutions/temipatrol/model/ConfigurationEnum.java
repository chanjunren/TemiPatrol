package com.robosolutions.temipatrol.model;

import java.util.HashMap;
import java.util.Map;

public enum ConfigurationEnum {
    MASK_DETECTION_MSG(1),
    HUMAN_DIST_MSG(2),
    SERVER_IP_ADD(3),
    ADMIN_PW(4);

    private int value;
    private static Map<Integer, ConfigurationEnum> map = new HashMap<>();

    private ConfigurationEnum(int value) {
        this.value = value;
    }

    static {
        for (ConfigurationEnum confEnum: ConfigurationEnum.values()) {
            map.put(confEnum.value, confEnum);
        }
    }

    public static ConfigurationEnum valueOf(int enumType) {
        return map.get(enumType);
    }

    public int getValue() {
        return value;
    }
}
