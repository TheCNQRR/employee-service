package by.java_enterprise.employeemanager.DTO.employee;

import jakarta.annotation.Nullable;

public record DeleteEmployeeRequest(
        String callerId,
        @Nullable String targetId
) {
}
