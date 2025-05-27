package com.example.forum.mapper;

import com.example.forum.repository.entity.Report;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface TestMapper {
    //@Select("SELECT id, content FROM report ORDER BY updated_date DESC")
    List<Report> findAllByOrderByUpdatedDateDesc();

    @Insert("INSERT INTO report( content ) VALUES ( #{content} )")
    void save(Report saveReport);

    @Delete("DELETE FROM report WHERE id = #{id}")
    void deleteById(Integer id);

    @Select("SELECT content FROM report WHERE id = #{id}")
    Report findById(Integer id);

    @Select("SELECT id, content FROM report WHERE created_date BETWEEN #{start} AND #{end}")
    List<Report> findAllByCreatedDateBetween(Date start, Date end);

    @Update("UPDATE report SET updated_date = CURRENT_TIMESTAMP WHERE id = #{id}")
    int saveByUpdatedDate(Integer id);
}
