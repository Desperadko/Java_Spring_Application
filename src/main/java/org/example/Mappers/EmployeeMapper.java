package org.example.Mappers;

import org.example.DTOs.EmployeeDTO;
import org.example.Entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {
    //mapping annotationa kazwa na compilera in run-time da naprawi suotwetnite mapping funkcii
    //mapping funckiq moje da bude ne6 kato employee.setFirstName(dto.firstName);
    //target argumentite trqbwa da suotwetstwat w sluchaq na Employee fields
    //source argumentite trqbwa da wzimat dto argumenta ot actual funkciqta i da izpolzwa neinite fields
    @Mapping(target = "firstName", source = "dto.firstName")
    @Mapping(target = "lastName", source = "dto.lastName")
    @Mapping(target = "employeeNumber", source = "dto.employeeNumber")
    Employee convertDtoToEntity(EmployeeDTO dto, Long id);
}
