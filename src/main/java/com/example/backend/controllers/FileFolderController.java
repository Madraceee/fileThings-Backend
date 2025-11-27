package com.example.backend.controllers;

import com.example.backend.domain.FileFolder;
import com.example.backend.domain.dto.FileFolderDto;
import com.example.backend.mappers.Mapper;
import com.example.backend.services.FileFolderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class FileFolderController {

    private final FileFolderService fileFolderService;

    private final Mapper<FileFolder, FileFolderDto> fileFolderDtoMapper;

    private Authentication authentication;
    private UserDetails principal;

    public FileFolderController(FileFolderService fileFolderService,Mapper<FileFolder, FileFolderDto> fileFolderDtoMapper) {
        this.fileFolderService = fileFolderService;
        this.fileFolderDtoMapper = fileFolderDtoMapper;
    }

    private String getOwner(){
        authentication  =  SecurityContextHolder.getContext().getAuthentication();
        principal = (UserDetails) this.authentication.getPrincipal();
        return principal.getUsername();
    }

    @GetMapping(path = "/folder/{id}")
    public List<FileFolderDto> getFoldersAndFilesOfAParent(@PathVariable("id") int id){
        List<FileFolder> result = fileFolderService.getFolderFilesService(id,getOwner());
        return  result.stream().map(fileFolderDtoMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/allFolders")
    public List<FileFolderDto> getAllFoldersOfOwner(){
        List<FileFolder> result = fileFolderService.getAllFolders(getOwner());
        return  result.stream().map(fileFolderDtoMapper::mapTo).collect(Collectors.toList());
    }

    @PostMapping(path = "/folder")
    public ResponseEntity<FileFolderDto> addFolderToOwner(@RequestBody FileFolderDto fileFolderObj){
        FileFolder result = fileFolderService.addFolder(fileFolderObj.getName(),fileFolderObj.getParent(),getOwner());
        return new ResponseEntity<>(fileFolderDtoMapper.mapTo(result), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/folder/{id}")
    public ResponseEntity deleteFolder(@PathVariable("id") UUID id){
        try{
            fileFolderService.deleteFolder(id,getOwner());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/renameFileFolder/{id}/{name}")
    public ResponseEntity<FileFolderDto> renameFileOrFolder(@PathVariable("id") UUID id, @PathVariable("name") String name){
        FileFolder result = fileFolderService.renameFileOrFolder(id,name,getOwner());
        return new ResponseEntity<>(fileFolderDtoMapper.mapTo(result),HttpStatus.OK);
    }

    @PatchMapping(path = "/moveFileFolder/{id}/{newParent}")
    public ResponseEntity<FileFolderDto> moveFileOrFolder(@PathVariable("id") UUID id, @PathVariable("newParent") UUID newParent){
        FileFolder result = fileFolderService.moveFileFolder(id,newParent);
        return new ResponseEntity<>(fileFolderDtoMapper.mapTo(result),HttpStatus.OK);
    }

    @PostMapping(path = "/file")
    public ResponseEntity<FileFolderDto> addFileToOwner(@RequestBody FileFolderDto fileFolderObj){
        FileFolder result = fileFolderService.addFile(fileFolderObj.getName(),fileFolderObj.getParent(),getOwner());
        if(result == null){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(fileFolderDtoMapper.mapTo(result), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/file/{id}")
    public ResponseEntity deleteFile(@PathVariable("id") UUID id){
        try{
            fileFolderService.deleteFile(id,getOwner());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }
}
