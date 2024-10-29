package org.example.Mappers;

import org.example.DTOs.ProjectDTO;
import org.example.Entities.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMapper {
    @Mapping(target = "name", source = "dto.name")
    public Project convertDtoToEntity(ProjectDTO dto, Long id);
}
