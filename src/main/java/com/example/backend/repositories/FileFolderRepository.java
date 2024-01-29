package com.example.backend.repositories;

import com.example.backend.domain.FileFolder;
import com.example.backend.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileFolderRepository extends CrudRepository<FileFolder, UUID> {

    Iterable<FileFolder> findByOwner(User owner);

    @Query("SELECT a FROM FileFolder a WHERE a.parent.id = :parentID AND a.owner = :owner")
    Iterable<FileFolder> findFileFolderOfParent(@Param("parentID") UUID parentID, @Param("owner") User owner);
}
