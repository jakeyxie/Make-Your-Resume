package com.example.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Validate {
    private Integer id;

    private Integer userId;

    private String email;

    private String resetToken;

    private String type;

    private Date gmtCreate;

    private Date gmtModified;
}
