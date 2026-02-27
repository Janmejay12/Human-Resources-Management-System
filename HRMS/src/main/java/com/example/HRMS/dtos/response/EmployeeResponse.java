package com.example.HRMS.dtos.response;

import com.example.HRMS.enums.Roles;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
