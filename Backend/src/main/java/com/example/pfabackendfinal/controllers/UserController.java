package com.example.pfabackendfinal.controllers;


import com.example.pfabackendfinal.dto.UpdateUserDto;
import com.example.pfabackendfinal.entity.BookedApp;
import com.example.pfabackendfinal.entity.Doctor;
import com.example.pfabackendfinal.entity.User;
import com.example.pfabackendfinal.exception.PhotoRetrievalException;
import com.example.pfabackendfinal.response.BookingResponse;
import com.example.pfabackendfinal.response.DoctorResponse;
import com.example.pfabackendfinal.response.UserResponse;
import com.example.pfabackendfinal.services.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;



@RestController
//@CrossOrigin(origins = "http://localhost:5173")

@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final IUserService userService;

    @GetMapping("/all")
   //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getUsers(){

        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/{email}")
   // @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_DOCTOR')")
    public ResponseEntity<?> getUser(@PathVariable("email") String email){
        try{
            User theUser = userService.getUser(email);
            return ResponseEntity.ok(theUser);
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user");
        }
    }
    @DeleteMapping("/delete/{userId}")
    //@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #email == principal.username)")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String email){
        try{
            userService.deleteUser(email);
            return ResponseEntity.ok("User deleted successfully");
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user: " + e.getMessage());
        }
    }
    /*@PutMapping("/update/{id}")
    public ResponseEntity<User> updateProfile(@PathVariable(value = "id") Long id, @Valid @RequestBody User userDetails) {
        User updatedUser = userService.updateProfile(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }*/
    @PutMapping("/update/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> updateProfile(@PathVariable Long id,
                                                      @RequestBody UpdateUserDto up
                                                      //@RequestBody String username, @RequestBody String email,@RequestBody String password
                                                     ) throws SQLException, IOException {


        User theUser = userService.updateProfile(id, up.getName(), up.getUsername(), up.getEmail(), up.getPassword());
        UserResponse userResponse = getUserResponse(theUser);
        return ResponseEntity.ok(userResponse);
    }

    private UserResponse getUserResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword()

               );
    }



}
