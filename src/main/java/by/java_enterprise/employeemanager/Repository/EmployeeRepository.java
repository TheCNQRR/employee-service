package by.java_enterprise.employeemanager.Repository;

import by.java_enterprise.employeemanager.Model.Employee;
import by.java_enterprise.employeemanager.Model.Position;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
@SuppressWarnings({"SqlSourceToSinkFlow", "LoggingSimilarMessage"})
public class EmployeeRepository {
    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> findAll(String position, String name) {
        StringBuilder sql = new StringBuilder("SELECT * FROM employee");
        List<Object> parameters = new ArrayList<>();
        List<String> conditions = new ArrayList<>();

        if (position != null && !position.isBlank()) {
            conditions.add("position = ?");
            parameters.add(position);
        }

        if (name != null && !name.isBlank()) {
            conditions.add("name = ?");
            parameters.add(name);
        }

        if (!conditions.isEmpty()) {
            sql.append(" WHERE ");
            sql.append(String.join(" AND ", conditions));
        }

        log.debug("Executing SQL: {}", sql);

        return jdbcTemplate.query(sql.toString(), parameters.toArray(), (result, rowNumber) -> {
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
            log.debug("Exception {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Employee> createEmployee(Employee employee) {
        String sql = """
                INSERT INTO employee (id, name, position) VALUES
                (?, ?, ?)
                """;
        try {
            jdbcTemplate.update(sql, employee.getId(), employee.getName(), employee.getPosition().name());
            return Optional.of(employee);
        } catch (Exception e) {
            log.debug("Exception {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Employee> updateEmployee(Employee employee) {
        String sql = """
                UPDATE employee
                SET name = ?,
                    position = ?
                WHERE id = ?
                """;
        try {
            jdbcTemplate.update(sql, employee.getName(), employee.getPosition().name(), employee.getId());
            return Optional.of(employee);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Exception {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Employee> deleteEmployee(Employee employee) {
        String sql = """
                UPDATE employee
                SET position = ?
                WHERE id = ?
                """;
        try {
            jdbcTemplate.update(sql, employee.getPosition().name(), employee.getId());
            return Optional.of(employee);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Exception {}", e.getMessage());
            return Optional.empty();
        }
    }
}
