package com.sloms.controller;

import com.sloms.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public Map<String, Object> getOverallAnalytics(@RequestParam(required = false) String studentId) {
        return reportService.getOverallAnalytics(studentId);
    }
}
