package by.java_enterprise.employeemanager.Repository;

import by.java_enterprise.employeemanager.Model.Employee;
import by.java_enterprise.employeemanager.Model.Position;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EmployeeRepository {
    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> findAll() {
        String sql = "SELECT * FROM employee";
        return jdbcTemplate.query(sql, (result, rowNumber) -> {
            Employee employee = new Employee();
            employee.setId(result.getObject("id", UUID.class));
            employee.setName(result.getString("name"));
            employee.setPosition(Position.valueOf(result.getString("position")));

            return employee;
        });
    }

    public Optional<Employee> findById(UUID id) {
        String sql = """
                     SELECT * FROM employee
                     WHERE id = ?
                     """;
        try {
            var employee = jdbcTemplate.queryForObject(sql, (result, rowNumber) -> {
                Employee e = new Employee();
                e.setId(result.getObject("id", UUID.class));
                e.setName(result.getString("name"));
                e.setPosition(Position.valueOf(result.getString("position")));

                return e;
            }, id);

            return Optional.ofNullable(employee);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
