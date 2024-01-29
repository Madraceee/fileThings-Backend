package com.example.backend.services;

import com.example.backend.domain.FileFolder;

import java.util.List;
import java.util.UUID;

public interface FileFolderService {
    public List<FileFolder> getFolderFilesService(UUID parentFolderID, String owner);
    public List<FileFolder> getAllFolders(String owner);
    public FileFolder addFolder(String folderName, UUID parent  ,String ownerEmail);
    public void deleteFolder(UUID folderID, String owner) throws Exception;
    public FileFolder renameFileOrFolder(UUID id, String newName, String email);
    public FileFolder moveFileFolder(UUID id, UUID parentID);
    public FileFolder addFile(String folderName, UUID parent ,String ownerEmail);
    public void deleteFile(UUID fileID, String owner) throws Exception;
}
