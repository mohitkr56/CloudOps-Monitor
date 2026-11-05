package com.cloudops.api.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cloudops.api.repository.MetricsRepository;

@Component
public class MetricsScheduler {

    private static final Logger log =
            LoggerFactory.getLogger(MetricsScheduler.class);

    private final CloudWatchService cloudWatchService;
    private final MetricsRepository metricsRepository;

    public MetricsScheduler(
            CloudWatchService cloudWatchService,
            MetricsRepository metricsRepository) {

        this.cloudWatchService = cloudWatchService;
        this.metricsRepository = metricsRepository;
    }

    @Scheduled(fixedRate = 60000, initialDelay = 10000)
    public void collectMetrics() {

        try {

            Map<String, Object> metrics =
                    cloudWatchService.getServerMetrics();

            metricsRepository.saveMetrics(
                    (String) metrics.get("instanceId"),
                    ((Number) metrics.get("cpuUsage")).doubleValue(),
                    ((Number) metrics.get("memoryUsage")).doubleValue(),
                    ((Number) metrics.get("diskUsage")).doubleValue(),
                    (String) metrics.get("region")
            );

            log.info(
                    "Metrics successfully saved to Oracle. CPU: {}%, Memory: {}%, Disk: {}%",
                    metrics.get("cpuUsage"),
                    metrics.get("memoryUsage"),
                    metrics.get("diskUsage")
            );

        } catch (DataAccessException exception) {

            log.error("Unable to save scheduled metrics to Oracle", exception);

        } catch (Exception exception) {

            log.error("Scheduled metrics collection failed", exception);
        }
    }
}
