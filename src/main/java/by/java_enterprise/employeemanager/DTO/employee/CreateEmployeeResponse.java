package by.java_enterprise.employeemanager.DTO.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

public record CreateEmployeeResponse(
        @JsonProperty("status")
        Status status,

        @JsonProperty("id")
        @Nullable
        String id
) {}
