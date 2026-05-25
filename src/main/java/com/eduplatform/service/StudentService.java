package com.eduplatform.service;

import com.eduplatform.dto.StudentRequestDto;
import com.eduplatform.exception.ResourceNotFoundException;
import com.eduplatform.model.Student;
import com.eduplatform.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;

    @Transactional
    public Student createOrGetStudent(StudentRequestDto request) {
        return studentRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseGet(() -> {
                    Student student = Student.builder()
                            .name(request.getName())
                            .email(request.getEmail().toLowerCase())
                            .phone(request.getPhone())
                            .build();
                    return studentRepository.save(student);
                });
    }

    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }
}
