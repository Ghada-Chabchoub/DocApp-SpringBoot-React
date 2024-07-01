package com.example.pfabackendfinal.dto;

import lombok.Data;

@Data
public class UpdateUserDto
{
    private String name ;
    private String username;
    private String email ;
    private String password;
}
