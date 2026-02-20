package by.java_enterprise.employeemanager.Utils;

import jakarta.annotation.Nullable;

import java.util.UUID;

public class Function {
    public UUID convertId(@Nullable String id) {
        if (id == null) {
            return null;
        }

        String withHyphens = String.format(
                "%s-%s-%s-%s-%s",
                id.substring(0, 8),
                id.substring(8, 12),
                id.substring(12, 16),
                id.substring(16, 20),
                id.substring(20, 32)
        );

        return UUID.fromString(withHyphens);
    }
}
