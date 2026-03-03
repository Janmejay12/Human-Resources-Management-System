package com.example.HRMS.dtos.response;

import com.example.HRMS.enums.Roles;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    @NotBlank(message = "Employee name is required")
    private String employeeName;
    private Long employeeId;
    private String designation;
    private Date birthDate;
    private Date joiningDate;
    private String email;
    private String userName;
    private String departmentName;
    private Long departmentId;
    private Long managerId;
    private String managerEmployeeName;
    private Long roleId;
    private Roles roleName;
}
