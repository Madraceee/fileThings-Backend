package com.example.backend.domain;

import com.example.backend.enums.ContentType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="file_folders" ,indexes = {
        @Index(name = "owner_index" , columnList = "parent_id")
})
public class FileFolder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "file_folder_id")
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private ContentType type;

    @ManyToOne
    @Nullable
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FileFolder parent;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;
}
