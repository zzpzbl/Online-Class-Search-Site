package com.example.demo.Dao;

import com.example.demo.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.List;

@Mapper
public interface ILuceneDao {
    /*创建索引*/
    public void createCourseIndex(List<Course> courseList) throws IOException;
    /*关键字搜索*/
    public List<Course> searchByKeyWord(String keyWord) throws IOException, ParseException;
    /*其他 API 另加，暂时就写了这两*/
}
