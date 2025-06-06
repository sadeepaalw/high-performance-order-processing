package com.upside.orderprocessing.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong totalProcessingTime = new AtomicLong(0);
    private final Map<String, AtomicLong> statusCounts = new HashMap<>();

    @GetMapping("/throughput")
    public ResponseEntity<Map<String, Object>> getThroughputMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalRequests", totalRequests.get());
        metrics.put("averageProcessingTime", calculateAverageProcessingTime());
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/latency")
    public ResponseEntity<Map<String, Object>> getLatencyStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("averageLatency", calculateAverageProcessingTime());
        stats.put("totalProcessingTime", totalProcessingTime.get());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/bottlenecks")
    public ResponseEntity<Map<String, Object>> getBottleneckAnalysis() {
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("statusDistribution", getStatusDistribution());
        analysis.put("memoryUsage", getMemoryUsage());
        return ResponseEntity.ok(analysis);
    }

    private double calculateAverageProcessingTime() {
        long requests = totalRequests.get();
        if (requests == 0) return 0;
        return (double) totalProcessingTime.get() / requests;
    }

    private Map<String, Long> getStatusDistribution() {
        Map<String, Long> distribution = new HashMap<>();
        statusCounts.forEach((status, count) -> distribution.put(status, count.get()));
        return distribution;
    }

    private Map<String, Object> getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> memory = new HashMap<>();
        memory.put("usedMemory", runtime.totalMemory() - runtime.freeMemory());
        memory.put("freeMemory", runtime.freeMemory());
        memory.put("maxMemory", runtime.maxMemory());
        return memory;
    }
} 