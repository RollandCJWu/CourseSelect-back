package com.example.courseapp.controllers;

import com.example.courseapp.entities.Lecture;
import com.example.courseapp.entities.Student;
import com.example.courseapp.repositories.LectureRepository;
import com.example.courseapp.repositories.StudentRepository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LectureRepository lectureRepository;

    @PostMapping("/login")
    public String login(@RequestBody String entity) {
        JSONObject obj = new JSONObject(entity);
        String email = obj.optString("email", null);
        String password = obj.optString("password", null);
        
        if (email == null || password == null) {
            return new JSONObject().put("success", false).put("message", "請輸入電子郵件或密碼").toString();
        }

        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isEmpty()) {
            return new JSONObject().put("success", false).put("message", "信箱或密碼錯誤").toString();
        }

        JSONObject responseJson = new JSONObject()
                .put("success", true)
                .put("message", "登入成功")
                .put("studentId", student.get().getStudentId())
                .put("firstName", student.get().getFirstName())
                .put("lastName", student.get().getLastName())
                .put("email", student.get().getEmail());
        return responseJson.toString();
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new RuntimeException("找不到學生"));
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("找不到"));
        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setEmail(studentDetails.getEmail());
        return studentRepository.save(student);
    }

    @PutMapping("/{id}/enroll/{lectureId}")
    public String enrollLecture(@PathVariable Long id, @PathVariable Long lectureId) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("找不到學生"));
        Optional<Lecture> lecture = Optional.ofNullable(lectureRepository.findById(lectureId).orElseThrow(() -> new RuntimeException("找不到課程")));

        lecture.ifPresent(l -> student.getLectures().add(l));
        return new JSONObject().put("success", true).put("message", "成功報名課程").toString();
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
    }
}
