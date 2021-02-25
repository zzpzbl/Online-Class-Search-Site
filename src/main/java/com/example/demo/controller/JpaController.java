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

    static int user_id = 1;

    @RequestMapping("/hello")
    public String hello() {
        return "<div>Hello World!</div><div>This is jpa on project!</div>";
    }

//    @RequestMapping("/Course/searchByCourseId")
//    public List<Map<String, Object>> courseSearchByCourseId(@RequestBody String jsonObject) {
//        JSONObject object = JSONObject.parseObject(jsonObject);
//        int course_id = object.getIntValue("course_id");
//        List<Map<String, Object>> list = jdbcTemplate.queryForList(
//                "SELECT * FROM Mycourse WHERE course_id = ?", course_id);
//        return list;
//    }
//
//    @RequestMapping("/Course/insertByFile")
//    public void courseInsertByFile(@RequestBody String file) throws Exception {
//        System.out.println(file);
//    }

    @RequestMapping("/User/insert")
    public String userInsert(@RequestBody String jsonObject) {
        JSONObject object = JSONObject.parseObject(jsonObject);
        user_id++;
        String name = object.getString("name");
        String school = object.getString("school");
        String telephone = object.getString("telephone");
        String password = object.getString("password");
        String portrait_url = "http://47.100.79.77/img/portrait.f98bd381.svg";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT * FROM User WHERE name = ?", name);
        if (list.size() == 1) {
            return "注册失败";
        }
        else {
            jdbcTemplate.update("INSERT INTO User VALUES (?, ?, ?, ?, ?, ?)",
                    user_id, name, school, telephone, password, portrait_url);
            return "注册成功";
        }
    }

    @RequestMapping("/User/login")
    public String userLogin(@RequestBody String jsonObject) {
        JSONObject object = JSONObject.parseObject(jsonObject);
        String name = object.getString("name");
        String password = object.getString("password");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT * FROM User WHERE name = ? and password = ?",
                name, password);
        if (list.size() == 1)
            return "登录成功";
        else
            return "登录失败";
    }

    @RequestMapping("/Posting/hot")
    public List<Map<String, Object>> postingHot() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT post_id, type, title, content, counter, post_time, user_id, name, portrait_url " +
                        "FROM Posting NATURAL JOIN Post NATURAL JOIN User " +
                        "ORDER BY counter DESC " +
                        "LIMIT 3");
        return list;
    }

    @RequestMapping("/Posting/searchByCourseId")
    public List<Map<String, Object>> postingSearchByCourseId(@RequestBody String jsonObject) {
        JSONObject object = JSONObject.parseObject(jsonObject);
        int course_id = object.getIntValue("course_id");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT post_id, type, title, content, counter, post_time, user_id, name, portrait_url " +
                        "FROM Posting NATURAL JOIN Post NATURAL JOIN User NATURAL JOIN relative " +
                        "WHERE course_id = ?", course_id);
        return list;
    }

    @RequestMapping("/Posting/search")
    public List<Map<String, Object>> postingSearch() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT post_id, type, title, content, counter, post_time, user_id, name, portrait_url " +
                        "FROM Posting NATURAL JOIN Post NATURAL JOIN User " +
                        "ORDER BY counter");
        return list;
    }

//    @RequestMapping("/User/searchByUserId")
//    public List<Map<String, Object>> userSearchByUserId(@RequestBody String jsonObject) {
//        JSONObject object = JSONObject.parseObject(jsonObject);
//        int user_id = object.getIntValue("user_id");
//        List<Map<String, Object>> list = jdbcTemplate.queryForList(
//                "SELECT * FROM User WHERE user_id = ?", user_id);
//        return list;
//    }
//
//    @RequestMapping("/Posting/insert")
//    public void postingInsert(int post_id, String type, String title,
//                              String content, int counter, String post_time) {
//        jdbcTemplate.update("INSERT INTO Posting VALUES (?, ?, ?, ?, ?, ?)",
//                post_id, type, title, content, counter, post_time);
//    }
}
