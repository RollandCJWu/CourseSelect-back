package com.example.courseapp.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Lectures")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId;

    @Column(nullable = false)
    private String title;

    @JsonBackReference("teachers_lectures")
    @ManyToOne
    @JoinColumn(name = "teacherId", nullable = false)
    @JsonIgnoreProperties("lectures")
    private Teacher teacher;

    @JsonBackReference("students_lectures")
    @ManyToMany
    @JoinTable(
        name = "Students_Lectures",
        joinColumns = @JoinColumn(name = "lectureId"),
        inverseJoinColumns = @JoinColumn(name = "studentId")
    )
    @JsonIgnoreProperties("lectures")
    private List<Student> students = new ArrayList<>();
}
