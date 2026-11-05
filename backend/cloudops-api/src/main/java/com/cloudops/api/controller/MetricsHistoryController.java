
package com.cloudops.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics")
public class MetricsHistoryController {

    private final JdbcTemplate jdbcTemplate;

    public MetricsHistoryController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/history")
    public List<Map<String, Object>> getMetricsHistory(
            @RequestParam(defaultValue = "50") int limit) {

        int safeLimit = Math.max(1, Math.min(limit, 500));

        String sql = """
                SELECT *
                FROM (
                    SELECT
                        METRIC_ID,
                        INSTANCE_ID,
                        CPU_USAGE,
                        MEMORY_USAGE,
                        DISK_USAGE,
                        AWS_REGION,
                        RECORDED_AT
                    FROM SERVER_METRICS
                    ORDER BY METRIC_ID DESC
                )
                WHERE ROWNUM <= ?
                ORDER BY METRIC_ID ASC
                """;

        return jdbcTemplate.queryForList(sql, safeLimit);
    }
}
