package com.example.backend.services;

import com.example.backend.domain.User;

import java.util.UUID;

public interface UserService {
    public User loadUserByEmail(String email) throws Exception;
    public UUID getParentFolderID(String email) throws Exception;
    public User createUser(String email, String password) throws Exception;
}
