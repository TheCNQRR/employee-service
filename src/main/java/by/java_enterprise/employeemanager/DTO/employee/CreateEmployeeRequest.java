package by.java_enterprise.employeemanager.DTO.employee;

import by.java_enterprise.employeemanager.Model.Position;
import jakarta.annotation.Nullable;

import java.util.UUID;

public record CreateEmployeeRequest(
    @Nullable
    String id,
    String name,
    Position position
) {}
