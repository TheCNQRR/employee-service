package by.java_enterprise.employeemanager.Service;

import by.java_enterprise.employeemanager.DTO.employee.EmployeeResponse;
import by.java_enterprise.employeemanager.Model.Position;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {
    List<EmployeeResponse> getAll(String position, String name);
    Optional<EmployeeResponse> getById(String callerId, String id);
    Optional<EmployeeResponse> createEmployee(String name, Position position, @Nullable String id);
}
