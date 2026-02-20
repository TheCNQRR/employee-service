package by.java_enterprise.employeemanager.DTO.employee;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GetAllEmployeesResponse(
        @JsonProperty("members")
        List<EmployeeResponse> employees,

        @JsonProperty("totalCount")
        Integer TotalCount
) {
}
