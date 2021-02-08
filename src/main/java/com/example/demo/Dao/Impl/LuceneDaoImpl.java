package com.example.demo.Dao.Impl;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.Dao.ILuceneDao;
import com.example.demo.entity.Course;
import org.apache.lucene.document.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository(value = "luceneDao")
public class LuceneDaoImpl implements ILuceneDao{

    @Autowired(required = false)
    private IndexWriter indexWriter;

    @Autowired
    private Analyzer analyzer;

    @Autowired
    private SearcherManager searcherManager;

    @Autowired
    private CourseMapper courseMapper;
    /*
    * 创建索引，有三个字段，id，name，tags
    * */
    @Override
    public void createCourseIndex(List<Course> courseList) throws IOException {
        List<Document> docs = new ArrayList<Document>();
        for(Course c : courseList) {
            Document document = new Document();
            document.add(new Field("name", c.getName() + "", TextField.TYPE_STORED));
            String tags = "";
            tags += c.getContentList();
            tags += c.getTitleList();
            tags += c.getUniversityList();
            document.add(new StringField("id", c.getId() + "", Field.Store.YES));
            document.add(new Field("name", c.getName() + "", TextField.TYPE_STORED));
            document.add(new Field("tags", tags + "", TextField.TYPE_STORED));
            docs.add(document);
        }
        indexWriter.addDocuments(docs);
        indexWriter.commit();
    }

    /*
    * 通过关键字搜索
    * */
    @Override
    public List<Course> searchByKeyWord(String keyWord) throws IOException, ParseException {
        searcherManager.maybeRefresh();
        List<Course> list = new ArrayList<>();
        IndexSearcher indexSearcher = searcherManager.acquire();
        Analyzer analyzer = new IKAnalyzer();
        // 两个搜索域，name 和 tags
        String[] fields = {"name", "tags"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        String line = keyWord != null ? keyWord : in.readLine();
        Query query = parser.parse(line);

        TopDocs topDocs = indexSearcher.search(query, 10);

        for(ScoreDoc scoreDoc : topDocs.scoreDocs) {
            int doc = scoreDoc.doc;
            Document document = indexSearcher.doc(doc);
            Course course = new Course();
            // 这里需要一个通过 id 拿到数据库中 Course 的接口
            course = courseMapper.getCourseById(document.get("id"));
            list.add(course);
        }
        return list;
    }
}
