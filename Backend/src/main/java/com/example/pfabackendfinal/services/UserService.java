package com.example.pfabackendfinal.services;


import com.example.pfabackendfinal.entity.Doctor;
import com.example.pfabackendfinal.entity.Role;
import com.example.pfabackendfinal.entity.User;
import com.example.pfabackendfinal.exception.InternalServerException;
import com.example.pfabackendfinal.exception.ResourceNotFoundException;
import com.example.pfabackendfinal.exception.UserAlreadyExistsException;
import com.example.pfabackendfinal.repository.RoleRepository;
import com.example.pfabackendfinal.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;



@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singletonList(userRole));
        return userRepository.save(user);
    }
    @Override
    public User registerDoctor(User user) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        Role userRole = roleRepository.findByName("ROLE_DOCTOR").get();
        user.setRoles(Collections.singletonList(userRole));
        return userRepository.save(user);
    }


    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User theUser = getUser(email);
        if (theUser != null){
            userRepository.deleteByEmail(email);
        }

    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }



    @Override
    public User updateProfile(Long id, String name,String username , String email,String password) {
        User user = userRepository.findById(id).get();
        if (name != null) user.setName(name);
        if (username != null) user.setUsername(username);
        if (email != null) user.setEmail(email);
        if (password != null) user.setPassword(password);
        return userRepository.save(user);

    }


}


