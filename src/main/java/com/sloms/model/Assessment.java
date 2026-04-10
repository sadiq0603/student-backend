package com.sloms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessments")
public class Assessment {
    @Id
    private String id;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String type; // Quiz, Test, Exam, Assignment, Project

    @Column(nullable = false)
    private Integer score;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Assessment() {
    }

    public Assessment(String id, String studentId, String subject, String type, Integer score, LocalDateTime createdAt) {
        this.id = id;
        this.studentId = studentId;
        this.subject = subject;
        this.type = type;
        this.score = score;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

