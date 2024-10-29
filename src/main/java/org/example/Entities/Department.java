package org.example.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    //pokazwa che edin department moje da bude swurzan s nqkolko employeeta
    //mappedBy = "department" pokazwa po koi variable e mapped Department-a w Employee classa
    //trqq da suotwetstwa po ime, zatwa w Employee classa trqq da ima reference kum Department s variable ime "department"
    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Employee> employees;
}
