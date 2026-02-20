package by.java_enterprise.employeemanager.Repository;

import by.java_enterprise.employeemanager.Model.Employee;
import by.java_enterprise.employeemanager.Model.Position;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EmployeeRepository {
    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> findAll(String position, String name) {
        StringBuilder sql = new StringBuilder("SELECT * FROM employee");
        List<String> parameters = new ArrayList<>();

        if (position != null && !position.isBlank()) {
            parameters.add("position = ?");
        }

        if (name != null && !name.isBlank()) {
            parameters.add("name = ?");
        }

        if (!parameters.isEmpty()) {
            sql.append(" WHERE ");
            sql.append(String.join(" AND ", parameters));
        }

        return jdbcTemplate.query(sql.toString(), (result, rowNumber) -> {
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
