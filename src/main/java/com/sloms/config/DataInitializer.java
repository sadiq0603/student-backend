package com.sloms.config;

import com.sloms.model.User;
import com.sloms.model.Student;
import com.sloms.model.Assessment;
import com.sloms.repository.UserRepository;
import com.sloms.repository.StudentRepository;
import com.sloms.repository.AssessmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AssessmentRepository assessmentRepository;

    public DataInitializer(UserRepository userRepository, AssessmentRepository assessmentRepository) {
        this.userRepository = userRepository;
        this.assessmentRepository = assessmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            System.out.println("Initializing demo data...");

            // Create Admin
            User admin = new User(
                UUID.randomUUID().toString(),
                "Sadiq Admin",
                "sadiq123@gmail.com",
                "sadiq@2007",
                "admin",
                null,
                LocalDateTime.now()
            );
            userRepository.save(admin);

            // Create Students
            User s1 = new User(UUID.randomUUID().toString(), "John Doe", "john@test.com", "pass123", "student", "Grade 10", LocalDateTime.now());
            User s2 = new User(UUID.randomUUID().toString(), "Jane Smith", "jane@test.com", "pass123", "student", "Grade 10", LocalDateTime.now());
            User s3 = new User(UUID.randomUUID().toString(), "Alice Brown", "alice@test.com", "pass123", "student", "Grade 11", LocalDateTime.now());
            
            userRepository.saveAll(Arrays.asList(s1, s2, s3));

            // Create some demo assessments
            Assessment a1 = new Assessment(UUID.randomUUID().toString(), s1.getId(), "Mathematics", "Quiz", 85, LocalDateTime.now());
            Assessment a2 = new Assessment(UUID.randomUUID().toString(), s1.getId(), "Science", "Test", 72, LocalDateTime.now().minusDays(2));
            Assessment a3 = new Assessment(UUID.randomUUID().toString(), s2.getId(), "Mathematics", "Quiz", 90, LocalDateTime.now());
            Assessment a4 = new Assessment(UUID.randomUUID().toString(), s3.getId(), "History", "Project", 65, LocalDateTime.now().minusDays(5));

            assessmentRepository.saveAll(Arrays.asList(a1, a2, a3, a4));

            System.out.println("Demo data initialized successfully!");
        }
    }
}
