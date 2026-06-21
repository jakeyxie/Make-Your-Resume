package com.example.entity;

import lombok.Data;

@Data
public class UserTemplate {
    private Integer id;
    private String img;     //图片
    private String content; //内容
    private String time;    //创建时间
    private Integer resumeId;//分类id
    private Integer userId; //所属于用户id

    /**
     * 不是数据库里面的字段，而是用来存储数据库里category_id关联category表查询出来的title字段
     */
    private String username;
    private String resumeSort;

    private String userAvatar;

    /**
     * 不是数据库里面的字段，而是用来存储数据库里user_id关联user表查询出来的name字段
     */
}
