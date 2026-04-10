package com.sloms.service;

import com.sloms.model.Assessment;
import com.sloms.repository.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    public List<Assessment> getAllAssessments(String studentId) {
        if (studentId != null) {
            return assessmentRepository.findByStudentId(studentId);
        }
        return assessmentRepository.findAll();
    }

    public Assessment createAssessment(Assessment assessment) {
        assessment.setCreatedAt(LocalDateTime.now());
        if (assessment.getId() == null) {
            assessment.setId("assessment-" + System.currentTimeMillis());
        }
        return assessmentRepository.save(assessment);
    }

    public void deleteAssessment(String id) {
        assessmentRepository.deleteById(id);
    }
}
