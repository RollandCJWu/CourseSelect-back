package com.example.courseapp.controllers;

import org.json.JSONObject;
import com.example.courseapp.entities.Teacher;
import com.example.courseapp.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherRepository teacherRepository;

    @PostMapping("/login")
    public String login(@RequestBody String entity) {
        JSONObject obj = new JSONObject(entity);
        String email = obj.optString("email", null);
        String password = obj.optString("password", null);
        
        if (email == null || password == null) {
            return new JSONObject().put("success", false).put("message", "請輸入電子郵件或密碼").toString();
        }

        Optional<Teacher> teacher = teacherRepository.findByEmail(email);
        if (teacher.isEmpty()) {
            return new JSONObject().put("success", false).put("message", "信箱或密碼錯誤").toString();
        }

        JSONObject responseJson = new JSONObject()
                .put("success", true)
                .put("message", "登入成功")
                .put("teacherId", teacher.get().getTeacherId())
                .put("firstName", teacher.get().getFirstName())
                .put("lastName", teacher.get().getLastName())
                .put("email", teacher.get().getEmail());
        return responseJson.toString();
    }

    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @PostMapping
    public Teacher createTeacher(@RequestBody Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @GetMapping("/{id}")
    public Teacher getTeacher(@PathVariable Long id) {
        return teacherRepository.findById(id).orElseThrow(() -> new RuntimeException("找不到老師"));
    }

    @PutMapping("/{id}")
    public Teacher updateTeacher(@PathVariable Long id, @RequestBody Teacher teacherDetails) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new RuntimeException("找不到"));
        teacher.setFirstName(teacherDetails.getFirstName());
        teacher.setLastName(teacherDetails.getLastName());
        teacher.setEmail(teacherDetails.getEmail());
        return teacherRepository.save(teacher);
    }

    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable Long id) {
        teacherRepository.deleteById(id);
    }
}

