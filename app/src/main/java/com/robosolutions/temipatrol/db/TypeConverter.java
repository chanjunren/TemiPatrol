package com.robosolutions.temipatrol.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.robosolutions.temipatrol.model.ConfigurationEnum;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TypeConverter {

    @androidx.room.TypeConverter
    public static ArrayList<String> stringToInputList(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return new ArrayList<>();
        }

        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @androidx.room.TypeConverter
    public static String inputListToString(ArrayList<String> inputs) {
        Gson gson = new Gson();
        return gson.toJson(inputs);
    }

    @androidx.room.TypeConverter
    public static int configurationEnumToInt(ConfigurationEnum confEnum) {
        return confEnum.getValue();
    }

    @androidx.room.TypeConverter
    public static ConfigurationEnum stringToConfigurationEnum(int enumValue) {
        return ConfigurationEnum.valueOf(enumValue);
    }
}
