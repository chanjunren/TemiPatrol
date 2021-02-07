package com.ongbengchia.temipatrol.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
}
