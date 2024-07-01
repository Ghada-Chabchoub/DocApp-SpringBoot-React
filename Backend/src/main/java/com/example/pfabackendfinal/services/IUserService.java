package com.example.pfabackendfinal.services;

import com.example.pfabackendfinal.entity.User;

import java.util.List;

public interface IUserService {
    User registerUser(User user);
    User registerDoctor(User user);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);
    User updateProfile(Long id, String name,String username , String email,String password);
}
