package by.java_enterprise.employeemanager.Controller;

import by.java_enterprise.employeemanager.DTO.employee.*;
import by.java_enterprise.employeemanager.Service.EmployeeService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<GetAllEmployeesResponse> getEmployees(
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String name) {
        GetAllEmployeeRequest request = new GetAllEmployeeRequest(position, name);
        List<EmployeeResponse> employees = employeeService.getAll(request);

        GetAllEmployeesResponse response = new GetAllEmployeesResponse(employees, employees.size());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/single")
    public ResponseEntity<Optional<EmployeeResponse>> getEmployeeById(@RequestHeader("Authorization") String callerId,
                                                                      @RequestBody @Nullable String targetId) {
        GetEmployeeRequest request = new GetEmployeeRequest(callerId, targetId);
        Optional<EmployeeResponse> employee = employeeService.getById(request);

        return employee.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(employee) :
                ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<CreateEmployeeResponse> createEmployee(@RequestBody CreateEmployeeRequest request) {
        Optional<EmployeeResponse> employee = employeeService.createEmployee(request);

        CreateEmployeeResponse response = employee.map(employeeResponse ->
                new CreateEmployeeResponse(Status.SUCCESS, employeeResponse.id())).orElseGet(() ->
                new CreateEmployeeResponse(Status.FAILURE, null));

        return employee.isPresent() ? ResponseEntity.status(HttpStatus.CREATED).body(response) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PatchMapping
    public ResponseEntity<UpdateEmployeeResponse> updateEmployee(@RequestHeader("Authorization") String callerId,
                                                                 @RequestBody UpdateEmployeeRequest request) {
        Optional<EmployeeResponse> employee = employeeService.updateEmployee(callerId, request);

        UpdateEmployeeResponse response = employee.map(employeeResponse ->
                new UpdateEmployeeResponse(Status.SUCCESS, employeeResponse.id())).orElseGet(() ->
                new UpdateEmployeeResponse(Status.FAILURE, null));

        return employee.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
