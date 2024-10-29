package org.example.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity //kazwa che she e entity prawe6to tablica i wsqka instanciq na twa entity 6e e record w samata tablica
@Table(name = "employees") //kazwa kak da sa kazwa tablicata i/ili s koq tablica da se suotwetstwa samoto entity
public class Employee {
    @Id //kazwa 4e dadenata promenliwa 6te sluji kat ID w tablicata
    @GeneratedValue(strategy = GenerationType.IDENTITY) //kazwa kak da sa prai ID-to .. w slu4aq da e unikalno
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "employee_number")
    private Integer employeeNumber;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany
    @JoinTable(
            name = "employee_project",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects;
}
