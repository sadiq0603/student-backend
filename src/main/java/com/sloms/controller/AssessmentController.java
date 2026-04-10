package com.sloms.controller;

import com.sloms.model.Assessment;
import com.sloms.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessments")
@CrossOrigin(origins = "*")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping
    public List<Assessment> getAllAssessments(@RequestParam(required = false) String studentId) {
        return assessmentService.getAllAssessments(studentId);
    }

    @PostMapping
    public ResponseEntity<?> createAssessment(@RequestBody Assessment assessment) {
        return ResponseEntity.ok(assessmentService.createAssessment(assessment));
    }
}
