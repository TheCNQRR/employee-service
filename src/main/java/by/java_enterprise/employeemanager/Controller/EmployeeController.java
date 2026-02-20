package by.java_enterprise.employeemanager.Controller;

import by.java_enterprise.employeemanager.DTO.employee.*;
import by.java_enterprise.employeemanager.Service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService;

    @GetMapping()
    public ResponseEntity<GetAllEmployeesResponse> getEmployees(
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String name) {
        List<EmployeeResponse> employees = employeeService.getAll(position, name);

        GetAllEmployeesResponse response = new GetAllEmployeesResponse(employees, employees.size());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/single")
    public ResponseEntity<Optional<EmployeeResponse>> getEmployeeById(@RequestHeader("Authorization") String callerId,
                                                            @RequestBody GetEmployeeRequest request) {
        Optional<EmployeeResponse> employee = employeeService.getById(callerId, request.id());

        return employee.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(employee) :
                ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping()
    public ResponseEntity<CreateEmployeeResponse> createEmployee(@RequestBody CreateEmployeeRequest request) {
        Optional<EmployeeResponse> employee = employeeService.createEmployee(request.name(), request.position(), request.id());

        CreateEmployeeResponse response = employee.map(employeeResponse ->
                new CreateEmployeeResponse(Status.SUCCESS, employeeResponse.id())).orElseGet(() ->
                new CreateEmployeeResponse(Status.FAILURE, null));

        return employee.isPresent() ? ResponseEntity.status(HttpStatus.CREATED).body(response) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
