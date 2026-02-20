package by.java_enterprise.employeemanager.Service;

import by.java_enterprise.employeemanager.DTO.employee.EmployeeResponse;
import by.java_enterprise.employeemanager.Model.Employee;
import by.java_enterprise.employeemanager.Model.Position;
import by.java_enterprise.employeemanager.Repository.EmployeeRepository;
import by.java_enterprise.employeemanager.Utils.Function;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService implements IEmployeeService {
    private final EmployeeRepository employeeRepository;
    private final Function function = new Function();

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeResponse> getAll(String position, String name) {
        List<Employee> employees = employeeRepository.findAll(position, name);

        return employees.stream()
                .map(employee -> new EmployeeResponse(
                        employee.getId().toString().replace("-", ""),
                        employee.getName(),
                        employee.getPosition()
                )).toList();
    }

    public Optional<EmployeeResponse> getById(String targetId, @Nullable String callerId) {
        UUID callerUuid = function.convertId(callerId);
        UUID targetUuid = function.convertId(targetId);

        Optional<Employee> caller = employeeRepository.findById(callerUuid);
        Optional<Employee> target = employeeRepository.findById(targetUuid);

        if (caller.isEmpty()) {
            return Optional.empty();
        }

        if (target.isEmpty()) {
            return caller.map(it -> new EmployeeResponse(
                    it.getId().toString().replace("-", ""),
                    it.getName(),
                    it.getPosition()
            ));
        }

        Employee callerEmployee = caller.get();
        Employee targetEmployee = target.get();

        boolean access = callerEmployee.equals(targetEmployee) || (callerEmployee.getPosition() == Position.Administrator);

        var employee = access ? targetEmployee : callerEmployee;

        return  Optional.of(new EmployeeResponse(
                employee.getId().toString().replace("-", ""),
                employee.getName(),
                employee.getPosition()
        ));
    }

    public Optional<EmployeeResponse> getMe(String id) {
        UUID uuid = function.convertId(id);
        Optional<Employee> employee = employeeRepository.findById(uuid);

        return employee.isPresent() ? employee.map(it -> new EmployeeResponse(
                it.getId().toString().replace("-", ""),
                it.getName(),
                it.getPosition()
        )) : Optional.empty();
    }

    public Optional<EmployeeResponse> createEmployee(String name, Position position, @Nullable String id) {
        UUID uuid = id == null ? UUID.randomUUID() : function.convertId(id);

        Employee employee = new Employee(uuid, name, position);

        Optional<Employee> result = employeeRepository.createEmployee(employee);

        return result.isPresent() ? result.map(it -> new EmployeeResponse(
                it.getId().toString().replace("-", ""),
                it.getName(),
                it.getPosition()
        )) : Optional.empty();
    }
}
