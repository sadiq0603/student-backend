package com.sloms.service;

import com.sloms.model.Assessment;
import com.sloms.model.User;
import com.sloms.repository.AssessmentRepository;
import com.sloms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    public Map<String, Object> getOverallAnalytics(String studentId) {
        List<User> students = userRepository.findByRole("student");
        List<Assessment> assessments;
        
        if (studentId != null) {
            assessments = assessmentRepository.findByStudentId(studentId);
        } else {
            assessments = assessmentRepository.findAll();
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", students.size());
        stats.put("totalAssessments", assessments.size());
        
        double avg = assessments.stream()
                .mapToInt(Assessment::getScore)
                .average()
                .orElse(0.0);
        stats.put("averagePerformance", Math.round(avg * 10.0) / 10.0);

        // Subject-wise performance
        Map<String, Double> subjectPerformance = assessments.stream()
                .filter(a -> a.getSubject() != null)
                .collect(Collectors.groupingBy(
                        Assessment::getSubject,
                        Collectors.averagingInt(Assessment::getScore)
                ));
        stats.put("subjectPerformance", subjectPerformance);

        // Recent students
        stats.put("recentStudents", students.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing((User u) -> u.getCreatedAt() != null ? u.getCreatedAt() : LocalDateTime.MIN).reversed())
                .limit(10)
                .collect(Collectors.toList()));

        // Pre-fetch all assessments once and group by student ID to avoid N+1 query problem
        List<Assessment> allAssessments = assessmentRepository.findAll();
        Map<String, List<Assessment>> assessmentsByStudent = allAssessments.stream()
                .collect(Collectors.groupingBy(Assessment::getStudentId));

        // Student Performance Summary
        List<Map<String, Object>> studentsSummary = students.stream()
                .filter(Objects::nonNull)
                .map(student -> {
                    Map<String, Object> summary = new HashMap<>();
                    summary.put("name", student.getName() != null ? student.getName() : "Unknown");
                    summary.put("grade", student.getGrade() != null ? student.getGrade() : "N/A");
                    summary.put("id", student.getId());
                    
                    List<Assessment> studentAssessments = assessmentsByStudent.getOrDefault(student.getId(), new ArrayList<>());
                    double studentAvg = studentAssessments.stream()
                            .mapToInt(Assessment::getScore)
                            .average()
                            .orElse(0.0);
                    summary.put("score", Math.round(studentAvg * 10.0) / 10.0);
                    
                    // Simple status badge logic
                    String status = "Average";
                    if (studentAvg >= 70) status = "Excellent";
                    else if (studentAvg < 50) status = "Needs Improvement";
                    summary.put("status", status);
                    
                    return summary;
                }).collect(Collectors.toList());
        stats.put("studentsSummary", studentsSummary);

        // Additional Stats for Dashboard
        stats.put("subjectsCount", subjectPerformance.size());
        
        // Find strong/weak areas if studentId is provided
        if (studentId != null && !subjectPerformance.isEmpty()) {
            List<Map.Entry<String, Double>> sortedSubjects = new ArrayList<>(subjectPerformance.entrySet());
            sortedSubjects.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
            
            stats.put("strongAreas", sortedSubjects.stream().limit(2).map(Map.Entry::getKey).collect(Collectors.toList()));
            stats.put("areasToImprove", sortedSubjects.size() > 1 ? 
                    sortedSubjects.stream().skip(Math.max(0, sortedSubjects.size() - 2)).map(Map.Entry::getKey).collect(Collectors.toList()) : 
                    new ArrayList<>());
        }

        return stats;
    }
}
