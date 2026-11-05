
package com.cloudops.api.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudops.api.service.CloudWatchService;

@RestController
@RequestMapping("/api")
public class MetricsController {

    private final CloudWatchService cloudWatchService;

    public MetricsController(CloudWatchService cloudWatchService) {
        this.cloudWatchService = cloudWatchService;
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        return ResponseEntity.ok(
                cloudWatchService.getServerMetrics()
        );
    }
}
