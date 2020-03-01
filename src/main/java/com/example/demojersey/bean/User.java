package com.example.demojersey.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;

public class User {
    public String name;
    public int age;
    @SerializedName(value = "emailAddress", alternate = {"email", "email_address"})
    public String emailAddress;

    public User() {}

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return name +", " + age + ", " + emailAddress;
    }

    public String[] getClassFields() {
        int len = getClass().getDeclaredFields().length;
        String[] fields = new String[len];
        int i =0;
        for(Field f: getClass().getDeclaredFields()) {
            fields[i++] = f.getName();
        }
        return fields;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(this);
    }
}
