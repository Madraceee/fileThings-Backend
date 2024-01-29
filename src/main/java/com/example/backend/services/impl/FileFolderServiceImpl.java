package com.example.backend.services.impl;

import com.example.backend.domain.FileFolder;
import com.example.backend.domain.User;
import com.example.backend.enums.ContentType;
import com.example.backend.repositories.FileFolderRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.FileFolderService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FileFolderServiceImpl implements FileFolderService {

    private final FileFolderRepository fileFolderRepository;
    private final UserRepository userRepository;


    public FileFolderServiceImpl(FileFolderRepository fileFolderRepository, UserRepository userRepository) {
        this.fileFolderRepository = fileFolderRepository;
        this.userRepository = userRepository;
    }

    // Get all the folders and files of the specified user in the given parent folder
    @Override
    public List<FileFolder> getFolderFilesService(UUID parentFolderID, String ownerName){
        Optional<User> ownerObj = userRepository.findById(ownerName);
        if (ownerObj.isPresent()) {
            User owner = ownerObj.get();
            Iterable<FileFolder> results = fileFolderRepository.findFileFolderOfParent(parentFolderID, owner);
            return StreamSupport.stream(results.spliterator(), false)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    // Get all the folders and their ids
    @Override
    public List<FileFolder> getAllFolders(String owner){
        Optional<User> ownerObj = userRepository.findById(owner);
        if(ownerObj.isPresent()){
            Iterable<FileFolder> result =  fileFolderRepository.findByOwner(ownerObj.get());
            return StreamSupport.stream(result.spliterator(), false)
                    .filter(fileFolder -> fileFolder.getType() == ContentType.FOLDER)
                    .collect(Collectors.toList());
        }else{
            return null;
        }
    }

    // Add folder
    @Override
    public FileFolder addFolder(String folderName, UUID parent ,String owner){
        Optional<User> ownerObj = userRepository.findById(owner);
        FileFolder fileFolderObj = null;

        if(parent != null) {
            Optional<FileFolder> parentObj =fileFolderRepository.findById(parent);
            fileFolderObj = parentObj.get();
        }
        if(ownerObj.isPresent()){
            UUID id = UUID.randomUUID();
            FileFolder newFolder = FileFolder.builder()
                    .id(id)
                    .name(folderName)
                    .parent(fileFolderObj)
                    .owner(ownerObj.get())
                    .type(ContentType.FOLDER)
                    .build();
            return fileFolderRepository.save(newFolder);
        }
        return null;
    }

    // Add File
    @Override
    public FileFolder addFile(String folderName, UUID parent ,String ownerEmail){
        Optional<User> ownerObj = userRepository.findById(ownerEmail);
        Optional<FileFolder> parentObj =fileFolderRepository.findById(parent);

        if(ownerObj.isPresent() && parentObj.isPresent()){
            UUID id = UUID.randomUUID();
            FileFolder newFolder = FileFolder.builder()
                    .id(id)
                    .name(folderName)
                    .parent(parentObj.get())
                    .owner(ownerObj.get())
                    .type(ContentType.FILE)
                    .build();
            return fileFolderRepository.save(newFolder);
        }
        return null;
    }

    // Delete Folder
    @Override
    public void deleteFile(UUID fileID, String owner) throws Exception{
        Optional<User> ownerObj = userRepository.findById(owner);
        Optional<FileFolder> file =fileFolderRepository.findById(fileID);

        if(ownerObj.isPresent() && file.isPresent() && file.get().getOwner().equals(ownerObj.get())){
            fileFolderRepository.deleteById(fileID);
        }else{
            throw new Exception("Could not Delete");
        }

    }

    @Override
    public void deleteFolder(UUID folderID, String owner) throws Exception{
        Optional<User> ownerObj = userRepository.findById(owner);
        Optional<FileFolder> file =fileFolderRepository.findById(folderID);

        if(ownerObj.isPresent() && file.isPresent() && file.get().getOwner().equals(ownerObj.get())){
            fileFolderRepository.deleteById(folderID);
        }else{
            throw new Exception("Could not Delete");
        }
    }

    // Rename File or Folder
    @Override
    public FileFolder renameFileOrFolder(UUID id, String newName, String email){
        return fileFolderRepository.findById(id).map( fileFolder -> {
            if(Objects.equals(fileFolder.getOwner().getEmail(), email)){
                fileFolder.setName(newName);
                return fileFolderRepository.save(fileFolder);
            }
            throw new RuntimeException("Not the owner");
        }).orElseThrow(() -> new RuntimeException("File/Folder does not exist"));
    }

    // Move a File or Folder
    @Override
    public FileFolder moveFileFolder(UUID id, UUID parentID){
        return fileFolderRepository.findById(id).map( fileFolder -> {
            Optional<FileFolder> parentFolder = fileFolderRepository.findById(parentID);
            parentFolder.ifPresent(fileFolder::setParent);
            return fileFolderRepository.save(fileFolder);
        }).orElseThrow(() -> new RuntimeException("File/Folder does not exist"));
    }

}
