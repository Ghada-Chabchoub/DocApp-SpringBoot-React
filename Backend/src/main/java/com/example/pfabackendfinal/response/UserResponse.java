package com.example.pfabackendfinal.response;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



@Data

@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private List<String> roles;
    private String name;
    private String username;
    private String password;

    public UserResponse(Long id, String email, List<String> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }


    public UserResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserResponse(long id, String name, String username, String email, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email=email;
        this.password=password;
    }
}
