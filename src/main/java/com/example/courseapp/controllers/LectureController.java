package com.example.courseapp.controllers;

import com.example.courseapp.entities.Lecture;
import com.example.courseapp.repositories.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lectures")
public class LectureController {

    @Autowired
    private LectureRepository lectureRepository;

    @GetMapping
    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }

    @PostMapping
    public Lecture createLecture(@RequestBody Lecture lecture) {
        return lectureRepository.save(lecture);
    }

    @GetMapping("/{id}")
    public Lecture getLecture(@PathVariable Long id) {
        return lectureRepository.findById(id).orElseThrow(() -> new RuntimeException("找不到課程"));
    }

    @PutMapping("/{id}")
    public Lecture updateLecture(@PathVariable Long id, @RequestBody Lecture lectureDetails) {
        Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new RuntimeException("找不到課程"));
        lecture.setTitle(lectureDetails.getTitle());
        lecture.setTeacher(lectureDetails.getTeacher());
        lecture.setStudents(lectureDetails.getStudents());
        return lectureRepository.save(lecture);
    }

    @DeleteMapping("/{id}")
    public void deleteLecture(@PathVariable Long id) {
        lectureRepository.deleteById(id);
    }
}
