package com.cloudops.api.repository;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MetricsRepository {

    private final JdbcTemplate jdbcTemplate;

    public MetricsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveMetrics(
            String instanceId,
            double cpuUsage,
            double memoryUsage,
            double diskUsage,
            String region) {

        String sql = """
                INSERT INTO SERVER_METRICS (
                    INSTANCE_ID,
                    CPU_USAGE,
                    MEMORY_USAGE,
                    DISK_USAGE,
                    AWS_REGION,
                    RECORDED_AT
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                instanceId,
                cpuUsage,
                memoryUsage,
                diskUsage,
                region,
                Timestamp.from(Instant.now())
        );
    }
}
