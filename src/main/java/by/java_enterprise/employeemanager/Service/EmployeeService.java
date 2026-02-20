package by.java_enterprise.employeemanager.Service;

import by.java_enterprise.employeemanager.DTO.employee.*;
import by.java_enterprise.employeemanager.Model.Employee;
import by.java_enterprise.employeemanager.Model.Position;
import by.java_enterprise.employeemanager.Repository.EmployeeRepository;
import by.java_enterprise.employeemanager.Utils.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings({"LoggingSimilarMessage", "DuplicatedCode"})
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final Function function = new Function();

    public List<EmployeeResponse> getAll(GetAllEmployeeRequest request) {
        log.info("getAll called: parameters: name={}, position={}", request.name(), request.position());

        List<Employee> employees = employeeRepository.findAll(request.position(), request.name());

        return employees.stream()
                .map(employee -> new EmployeeResponse(
                        employee.getId().toString().replace("-", ""),
                        employee.getName(),
                        employee.getPosition()
                )).toList();
    }

    public Optional<EmployeeResponse> getById(GetEmployeeRequest request) {
        log.info("getById called: callerId={}, targetId={}", request.callerId(), request.targetId());

        UUID callerUuid = function.convertId(request.callerId());
        UUID targetUuid = function.convertId(request.targetId());

        Optional<Employee> caller = employeeRepository.findById(callerUuid);
        Optional<Employee> target = employeeRepository.findById(targetUuid);

        if (caller.isEmpty()) {
            log.warn("Caller not found: {}", callerUuid);
            return Optional.empty();
        }

        if (target.isEmpty()) {
            log.debug("Target not found, returning caller");
            return caller.map(it -> new EmployeeResponse(
                    it.getId().toString().replace("-", ""),
                    it.getName(),
                    it.getPosition()
            ));
        }

        Employee callerEmployee = caller.get();
        Employee targetEmployee = target.get();

        boolean access = callerEmployee.equals(targetEmployee) || (callerEmployee.getPosition() == Position.Administrator);
        log.debug("access={}", access);

        var employee = access ? targetEmployee : callerEmployee;

        return Optional.of(new EmployeeResponse(
                employee.getId().toString().replace("-", ""),
                employee.getName(),
                employee.getPosition()
        ));
    }

    public Optional<EmployeeResponse> createEmployee(CreateEmployeeRequest request) {
        log.info("create called: id={}, name={}, position={}", request.id(), request.name(), request.position());
        UUID uuid = request.id() == null ? UUID.randomUUID() : function.convertId(request.id());

        Employee employee = Employee.builder()
                .id(uuid)
                .name(request.name())
                .position(request.position())
                .build();

        Optional<Employee> result = employeeRepository.createEmployee(employee);

        return result.isPresent() ? result.map(it -> new EmployeeResponse(
                it.getId().toString().replace("-", ""),
                it.getName(),
                it.getPosition()
        )) : Optional.empty();
    }

    public Optional<EmployeeResponse> updateEmployee(String callerId, UpdateEmployeeRequest request) {
        log.info("getById called: callerId={}, targetId={}, name={}, position={}", callerId, request.targetId(),
                request.name(), request.position());

        UUID callerUuid = function.convertId(callerId);
        UUID targetUuid = function.convertId(request.targetId());

        Optional<Employee> caller = employeeRepository.findById(callerUuid);
        Optional<Employee> target = employeeRepository.findById(targetUuid);

        if (caller.isEmpty()) {
            log.warn("Caller not found: {}", callerUuid);
            return Optional.empty();
        }

        Employee employee;
        if (target.isEmpty()) {
            log.debug("Target not found");
            employee = Employee.builder()
                    .id(caller.get().getId())
                    .name(caller.get().getName())
                    .position(caller.get().getPosition())
                    .build();
        } else {
            Employee callerEmployee = caller.get();
            Employee targetEmployee = target.get();

            boolean access = callerEmployee.equals(targetEmployee) || (callerEmployee.getPosition() == Position.Administrator);
            log.debug("access={}", access);

            employee = access ? targetEmployee : callerEmployee;
        }

        if (request.name() != null) {
            employee.setName(request.name());
        }

        if (request.position() != null) {
            employee.setPosition(request.position());
        }

        Optional<Employee> result = employeeRepository.updateEmployee(employee);

        return result.isPresent() ? result.map(it -> new EmployeeResponse(
                it.getId().toString().replace("-", ""),
                it.getName(),
                it.getPosition())) : Optional.empty();
    }

    public Optional<EmployeeResponse> deleteEmployee(DeleteEmployeeRequest request) {
        log.info("delete called: callerId={}, targetId={}", request.callerId(), request.targetId());

        UUID callerUuid = function.convertId(request.callerId());
        UUID targetUuid = function.convertId(request.targetId());

        Optional<Employee> caller = employeeRepository.findById(callerUuid);
        Optional<Employee> target = employeeRepository.findById(targetUuid);

        if (caller.isEmpty()) {
            log.warn("Caller not found: {}", callerUuid);
            return Optional.empty();
        }

        if (target.isEmpty()) {
            log.debug("Target not found");
            Optional<Employee> result = employeeRepository.deleteEmployee(caller.get());
            return result.isPresent() ? result.map(it -> new EmployeeResponse(
                    it.getId().toString().replace("-", ""),
                    it.getName(),
                    it.getPosition())) : Optional.empty();
        }

        Employee callerEmployee = caller.get();
        Employee targetEmployee = target.get();

        boolean access = callerEmployee.equals(targetEmployee) || (callerEmployee.getPosition() == Position.Administrator);
        log.debug("access={}", access);

        Employee employee = access ? targetEmployee : callerEmployee;

        employee.setPosition(Position.Deleted);

        Optional<Employee> result = employeeRepository.deleteEmployee(employee);

        return result.isPresent() ? result.map(it -> new EmployeeResponse(
                it.getId().toString().replace("-", ""),
                it.getName(),
                it.getPosition())) : Optional.empty();
    }
}
