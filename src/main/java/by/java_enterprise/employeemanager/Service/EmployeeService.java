package by.java_enterprise.employeemanager.Service;

import by.java_enterprise.employeemanager.DTO.employee.EmployeeResponse;
import by.java_enterprise.employeemanager.Model.Employee;
import by.java_enterprise.employeemanager.Repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService implements IEmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeResponse> getAll() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .map(employee -> new EmployeeResponse(
                        employee.getId().toString().replace("-", ""),
                        employee.getName(),
                        employee.getPosition()
                )).toList();
    }

    public Optional<EmployeeResponse> getById(UUID id) {
        Optional<Employee> employee = employeeRepository.findById(id);

        return employee.isPresent() ? employee.map(it -> new EmployeeResponse(
                it.getId().toString().replace("-", ""),
                it.getName(),
                it.getPosition()
        )) : Optional.empty();
    }


}
