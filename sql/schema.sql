-- SLOMS Database Schema
CREATE DATABASE IF NOT EXISTS sloms_db;
USE sloms_db;

-- Users Table (Admins and Students)
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'student') NOT NULL,
    grade VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Assessments Table
CREATE TABLE IF NOT EXISTS assessments (
    id VARCHAR(50) PRIMARY KEY,
    student_id VARCHAR(50) NOT NULL,
    subject VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL, -- Quiz, Test, Exam, Assignment, Project
    score INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Initial Admin Account
INSERT INTO users (id, name, email, password, role)
VALUES ('admin-1', 'D. Sadiq', 'sadiq123@gmail.com', 'sadiq@2007', 'admin')
ON DUPLICATE KEY UPDATE email=email;

-- Sample Students (Optional for initial state)
-- INSERT INTO users (id, name, email, password, role, grade) VALUES ('student-sample', 'John Doe', 'john@gmail.com', 'pass123', 'student', 'Grade 10');
