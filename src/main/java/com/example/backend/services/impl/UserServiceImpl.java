package com.example.backend.services.impl;

import com.example.backend.domain.FileFolder;
import com.example.backend.domain.User;
import com.example.backend.enums.ContentType;
import com.example.backend.repositories.FileFolderRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final FileFolderRepository fileFolderRepository;

    public UserServiceImpl(UserRepository userRepository, FileFolderRepository fileFolderRepository) {
        this.userRepository = userRepository;
        this.fileFolderRepository = fileFolderRepository;
    }

    @Override
    public User loadUserByEmail(String email) throws Exception {
        Optional<User> result = userRepository.findById(email);
        if(result.isEmpty()){
            throw new Exception("No User Found");
        }

        return result.get();
    }

    @Override
    public UUID getParentFolderID(String email) throws Exception {
        Optional<User> result = userRepository.findById(email);
        if (result.isEmpty()){
            throw new Exception("No User Found");
        }

        return result.get().getParentFolder();
    }

    @Override
    public User createUser(String email, String password) throws Exception{
        User newUser = User.builder()
                .email(email)
                .hashedPassword(password)
                .build();

        Optional<User> result = userRepository.findById(email);
        if(result.isPresent()){
            throw new Exception("User present");
        }

        newUser = userRepository.save(newUser);
        FileFolder newFolder = FileFolder.builder()
                .name("/")
                .type(ContentType.FOLDER)
                .owner(newUser)
                .build();

        newFolder = fileFolderRepository.save(newFolder);
        newUser.setParentFolder(newFolder.getId());
        return userRepository.save(newUser);
    }
}
