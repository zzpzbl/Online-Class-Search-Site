package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
public class JpaController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/hello")
    public String hello() {
        return "<div>Hello World!</div><div>This is jpa on project!</div>";
    }

    @RequestMapping("/Course/searchByCourseId")
    public List<Map<String, Object>> courseSearchByCourseId(@RequestBody String jsonObject) {
        JSONObject object = JSONObject.parseObject(jsonObject);
        int course_id = object.getIntValue("course_id");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT * FROM Mycourse WHERE course_id = ?", course_id);
        return list;
//        return userservice.courseSearchByCourseId(jsonObject);
    }

    @RequestMapping("/Course/insertByFile")
    public void courseInsertByFile(@RequestBody String file) throws Exception {
        System.out.println(file);
    }

    @RequestMapping("/User/insert")
    public void userInsert(int user_id, String name, String school,
                           String telephone, String password, String portrait_url) {
        jdbcTemplate.update("INSERT INTO User VALUES (?, ?, ?, ?, ?, ?)",
                user_id, name, school, telephone, password, portrait_url);
//        userservice.userInsert(user_id, name, school, telephone, password, portrait_url);
    }

    @RequestMapping("/User/searchByUserId")
    public List<Map<String, Object>> userSearchByUserId(@RequestBody String jsonObject) {
        JSONObject object = JSONObject.parseObject(jsonObject);
        int user_id = object.getIntValue("user_id");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT * FROM User WHERE user_id = ?", user_id);
        return list;
//        return userservice.userSearchByUserId(jsonObject);
    }

    @RequestMapping("/Posting/insert")
    public void postingInsert(int post_id, String type, String title,
                              String content, int counter, String post_time) {
        jdbcTemplate.update("INSERT INTO Posting VALUES (?, ?, ?, ?, ?, ?)",
                post_id, type, title, content, counter, post_time);
//        userservice.postingInsert(post_id, type, title, content, counter, post_time);
    }
}
