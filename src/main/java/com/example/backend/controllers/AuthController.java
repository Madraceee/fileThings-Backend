package com.example.backend.controllers;

import com.example.backend.domain.User;
import com.example.backend.domain.dto.LoginDto;
import com.example.backend.domain.dto.UserDto;
import com.example.backend.security.JWTUtil;
import com.example.backend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private JWTUtil jwtUtil;

    private UserService userService;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @ResponseBody
    @PostMapping(path = "/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto){
        try{
            Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));

            User userObj = userService.loadUserByEmail(loginDto.getEmail());
            String token = jwtUtil.generateToken(userObj.getEmail());
            UUID parentFolder = userService.getParentFolderID(userObj.getEmail());
            UserDto user = UserDto.builder()
                        .email(userObj.getEmail())
                        .parentFolder(parentFolder)
                        .token(token)
                        .build();
            return ResponseEntity.ok(user);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Username or Password");
        }
    }

    @PostMapping(path = "/create")
    public ResponseEntity create(@RequestBody LoginDto loginDto){
        try{
            User user = userService.createUser(loginDto.getEmail(),loginDto.getPassword());
            return new ResponseEntity<>(user,HttpStatus.CREATED);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Exists");
        }
    }
}
