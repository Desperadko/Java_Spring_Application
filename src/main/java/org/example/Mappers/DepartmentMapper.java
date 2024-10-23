package org.example.Mappers;

import org.example.DTOs.DepartmentDTO;
import org.example.Entities.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DepartmentMapper {
    @Mapping(target = "name", source = "dto.name")
    public Department convertDtoToEntity(DepartmentDTO dto, Long id);
}
