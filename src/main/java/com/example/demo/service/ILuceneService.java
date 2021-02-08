package com.example.demo.service;

import com.example.demo.entity.Course;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
public interface ILuceneService {
    public void synCourseCreateIndex() throws IOException;

    public List<Course> searchByKeyWord(String keyWord) throws IOException, ParseException;
}
