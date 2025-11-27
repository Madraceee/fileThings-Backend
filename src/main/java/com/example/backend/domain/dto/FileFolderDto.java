package com.example.backend.domain.dto;
import com.example.backend.enums.ContentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileFolderDto {
    private UUID id;
    private String name;
    private ContentType type;
    private UUID parent;
    private UserDto owner;
    private LocalDate createdAt;
}
