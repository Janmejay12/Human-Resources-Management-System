package com.example.HRMS.entities;

import com.example.HRMS.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(name = "role_name", nullable = false)
    private Roles roleName;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>();

}
