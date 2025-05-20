package com.example.forum.repository;

import com.example.forum.repository.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    public List<Report> findAllByOrderByIdDesc();
    public List<Report> findAllByCreatedDateBetween(Date start, Date end);
    public List<Report> saveByUpdatedDate(Integer id);
}
