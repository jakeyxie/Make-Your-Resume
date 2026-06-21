package com.example.mapper;

import com.example.entity.Validate;

import java.util.List;

public interface ValidateMapper {
    int insert(Validate validate);
    Validate selectByToken(String token);
    List<Validate> selectByEmail(String email);
}
