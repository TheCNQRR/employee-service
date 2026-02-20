package by.java_enterprise.employeemanager.DTO.employee;

import jakarta.annotation.Nullable;

public record GetEmployeeRequest(
        @Nullable
        String id
) {
}
