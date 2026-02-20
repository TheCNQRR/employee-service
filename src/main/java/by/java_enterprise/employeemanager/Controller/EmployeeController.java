package by.java_enterprise.employeemanager.Controller;

import by.java_enterprise.employeemanager.DTO.employee.EmployeeResponse;
import by.java_enterprise.employeemanager.DTO.employee.GetAllEmployeesResponse;
import by.java_enterprise.employeemanager.Service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService;

    @GetMapping
    public ResponseEntity<GetAllEmployeesResponse> getEmployees(
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String name) {
        List<EmployeeResponse> employees = employeeService.getAll(position, name);

        GetAllEmployeesResponse response = new GetAllEmployeesResponse(employees, employees.size());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
