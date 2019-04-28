package com.swordfall.model;

/**
 * @Author: Yang JianQiu
 * @Date: 2019/4/28 14:30
 */
public class User {
    private String username;
    private int age;

    public User(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
