package com.example.courseapp.repositories;

import com.example.courseapp.entities.Teacher;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
     Optional<Teacher> findByEmail(String email);
}