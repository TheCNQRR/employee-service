package by.java_enterprise.employeemanager.DTO.employee;

import by.java_enterprise.employeemanager.Model.Position;
import com.fasterxml.jackson.annotation.JsonProperty;

public record EmployeeResponse (
        @JsonProperty("id")
        String id,

        @JsonProperty("name")
        String Name,

        @JsonProperty("position")
        Position position
) {}
