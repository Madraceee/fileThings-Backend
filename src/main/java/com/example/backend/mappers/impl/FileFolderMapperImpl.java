package com.example.backend.mappers.impl;

import com.example.backend.domain.FileFolder;
import com.example.backend.domain.dto.FileFolderDto;
import com.example.backend.mappers.Mapper;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FileFolderMapperImpl implements Mapper<FileFolder, FileFolderDto> {

    private final ModelMapper modelMapper;

    private final Converter<FileFolder, UUID> fileFolderToUuidConverter = new Converter<FileFolder, UUID>() {
        @Override
        public UUID convert(MappingContext<FileFolder, UUID> context) {
            FileFolder source = context.getSource();
            return (source != null) ? source.getId() : null;
        }
    };

    public FileFolderMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        modelMapper.addConverter(fileFolderToUuidConverter);
    }

    @Override
    public FileFolderDto mapTo(FileFolder fileFolder) {
        return modelMapper.map(fileFolder,FileFolderDto.class);
    }

    @Override
    public FileFolder mapFrom(FileFolderDto fileFolderDto) {
        return modelMapper.map(fileFolderDto,FileFolder.class);
    }


}
