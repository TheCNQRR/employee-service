package by.java_enterprise.employeemanager.Service;

import by.java_enterprise.employeemanager.DTO.employee.EmployeeResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IEmployeeService {
    List<EmployeeResponse> getAll(String position, String name);
    Optional<EmployeeResponse> getById(UUID id);
}
