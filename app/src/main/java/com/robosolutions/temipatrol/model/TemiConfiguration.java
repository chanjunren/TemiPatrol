package com.robosolutions.temipatrol.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.robosolutions.temipatrol.db.TypeConverter;

@Entity(tableName = "configurationTable")
// Used to store IP Address / Voice Commands / Password
public class TemiConfiguration {
    @PrimaryKey
    @ColumnInfo(name="keyValue")
    @TypeConverters(TypeConverter.class)
    private ConfigurationEnum key;
    @ColumnInfo(name="value")
    private String value;

    public TemiConfiguration(String value, ConfigurationEnum key) {
        this.value = value;
        this.key = key;
    }

    public ConfigurationEnum getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(ConfigurationEnum key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TemiConfiguration{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

