package by.java_enterprise.employeemanager.DTO.employee;

import by.java_enterprise.employeemanager.Model.Position;
import jakarta.annotation.Nullable;

public record UpdateEmployeeRequest(
        @Nullable String targetId,
        @Nullable String name,
        @Nullable Position position
) {
}
