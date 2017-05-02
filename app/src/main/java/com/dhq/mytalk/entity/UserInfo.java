package com.dhq.mytalk.entity;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class UserInfo extends BaseEntity {

    private String name;
    private String gender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
