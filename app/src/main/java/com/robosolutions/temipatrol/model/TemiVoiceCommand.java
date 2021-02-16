package com.robosolutions.temipatrol.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "voiceCmdTable")
public class TemiVoiceCommand {
    @PrimaryKey
    @ColumnInfo(name="keyValue")
    private int key;
    @ColumnInfo(name="command")
    private String command;

    public TemiVoiceCommand(String command, int key) {
        this.command = command;
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "TemiVoiceCommand{" +
                "key=" + key +
                ", command='" + command + '\'' +
                '}';
    }
}

