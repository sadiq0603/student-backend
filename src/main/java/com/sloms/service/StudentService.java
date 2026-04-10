package com.sloms.service;

import com.sloms.model.User;
import com.sloms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllStudents() {
        return userRepository.findByRole("student");
    }

    public User createStudent(User student) {
        if (userRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        student.setRole("student");
        student.setCreatedAt(LocalDateTime.now());
        if (student.getId() == null) {
            student.setId("student-" + System.currentTimeMillis());
        }
        return userRepository.save(student);
    }

    public User updateStudent(String id, User studentDetails) {
        User student = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        Optional<User> existing = userRepository.findByEmail(studentDetails.getEmail());
        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            throw new RuntimeException("Email already exists");
        }

        student.setName(studentDetails.getName());
        student.setEmail(studentDetails.getEmail());
        student.setGrade(studentDetails.getGrade());
        if (studentDetails.getPassword() != null && !studentDetails.getPassword().isEmpty()) {
            student.setPassword(studentDetails.getPassword());
        }
        
        return userRepository.save(student);
    }

    public void deleteStudent(String id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getStudentById(String id) {
        return userRepository.findById(id);
    }
}
