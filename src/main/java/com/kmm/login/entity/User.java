package com.kmm.login.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String userName;
    private String password;
    private String salt;
}
