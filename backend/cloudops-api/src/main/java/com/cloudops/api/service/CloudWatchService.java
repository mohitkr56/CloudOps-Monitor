package com.cloudops.api.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;
import software.amazon.awssdk.services.cloudwatch.model.GetMetricStatisticsRequest;
import software.amazon.awssdk.services.cloudwatch.model.GetMetricStatisticsResponse;
import software.amazon.awssdk.services.cloudwatch.model.Datapoint;

@Service
public class CloudWatchService {

    private static final String INSTANCE_ID =
            "i-04279b18e8b2e6952";

    private final CloudWatchClient cloudWatchClient;

    public CloudWatchService() {

        this.cloudWatchClient = CloudWatchClient.builder()
                .region(Region.AP_SOUTHEAST_2)
                .build();
    }

    public Map<String, Object> getServerMetrics() {

        double cpu = getMetric(
                "AWS/EC2",
                "CPUUtilization",
                List.of(
                    Dimension.builder()
                        .name("InstanceId")
                        .value(INSTANCE_ID)
                        .build()
                )
        );

        double memory = getMetric(
                "CloudOps/Server",
                "mem_used_percent",
                List.of(
                    Dimension.builder()
                        .name("InstanceId")
                        .value(INSTANCE_ID)
                        .build()
                )
        );

        double disk = getMetric(
                "CloudOps/Server",
                "disk_used_percent",
                List.of(
                    Dimension.builder()
                        .name("InstanceId")
                        .value(INSTANCE_ID)
                        .build(),
                    Dimension.builder()
                        .name("device")
                        .value("nvme0n1p1")
                        .build(),
                    Dimension.builder()
                        .name("fstype")
                        .value("xfs")
                        .build(),
                    Dimension.builder()
                        .name("path")
                        .value("/")
                        .build()
                )
        );

        return Map.of(
                "instanceId", INSTANCE_ID,
                "cpuUsage", cpu,
                "memoryUsage", memory,
                "diskUsage", disk,
                "region", "ap-southeast-2",
                "timestamp", Instant.now().toString()
        );
    }

    private double getMetric(
            String namespace,
            String metricName,
            List<Dimension> dimensions) {

        Instant endTime = Instant.now();
        Instant startTime = endTime.minusSeconds(900);

        GetMetricStatisticsRequest request =
                GetMetricStatisticsRequest.builder()
                        .namespace(namespace)
                        .metricName(metricName)
                        .dimensions(dimensions)
                        .startTime(startTime)
                        .endTime(endTime)
                        .period(300)
                        .statisticsWithStrings("Average")
                        .build();

        GetMetricStatisticsResponse response =
                cloudWatchClient.getMetricStatistics(request);

        return response.datapoints()
                .stream()
                .max((a, b) ->
                        a.timestamp().compareTo(b.timestamp()))
                .map(Datapoint::average)
                .orElse(0.0);
    }
}
