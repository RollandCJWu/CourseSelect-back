package com.example.courseapp.repositories;

import com.example.courseapp.entities.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}