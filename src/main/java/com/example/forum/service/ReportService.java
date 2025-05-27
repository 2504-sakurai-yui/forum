package com.example.forum.service;

import com.example.forum.controller.form.ReportForm;
import com.example.forum.mapper.TestMapper;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Report;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    ReportRepository reportRepository;
    @Autowired
    TestMapper testMapper;

    /*
     * レコード全件取得処理
     */
    public List<ReportForm> findAllReport() {
        //List<Report> results = reportRepository.findAllByOrderByIdDesc();
        //List<Report> results = reportRepository.findAllByOrderByUpdatedDateDesc();
        List<Report> results = testMapper.findAllByOrderByUpdatedDateDesc();
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<ReportForm> setReportForm(List<Report> results) {
        List<ReportForm> reports = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            ReportForm report = new ReportForm();
            Report result = results.get(i);
            report.setId(result.getId());
            report.setContent(result.getContent());
            //report.setCreatedDate(result.getCreatedDate());
            //report.setUpdatedDate(result.getUpdatedDate());
            reports.add(report);
        }
        return reports;
    }

    /*
     * レコード追加
     */
    public void saveReport(ReportForm reqReport) {
        Report saveReport = setReportEntity(reqReport);
        //reportRepository.save(saveReport);
        testMapper.save(saveReport);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Report setReportEntity(ReportForm reqReport) {
        Report report = new Report();
        report.setId(reqReport.getId());
        report.setContent(reqReport.getContent());
        report.setCreatedDate(reqReport.getCreatedDate());
        report.setUpdatedDate(reqReport.getUpdatedDate());
        return report;
    }

    /*
     *レコード削除
     */
    public void deleteReport(Integer id){
        //reportRepository.deleteById(id);
        testMapper.deleteById(id);
    }

    /*
     * レコード1件取得
     */
    public ReportForm editReport(Integer id) {
        List<Report> results = new ArrayList<>();
        //results.add((Report) reportRepository.findById(id).orElse(null));
        results.add(testMapper.findById(id));
        List<ReportForm> reports = setReportForm(results);
        return reports.get(0);
    }

    /*
     * startからend間のレコード全件取得処理
     */
    public List<ReportForm> findByStartToEnd(String start, String end) throws ParseException {
        String startDate = start + " 00:00:00";
        String endDate = end + " 23:59:59";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateStartDate = sdf.parse(startDate);
        Date dateEndDate = sdf.parse(endDate);

        //List<Report> results = reportRepository.findAllByCreatedDateBetween(dateStartDate, dateEndDate);
        List<Report> results = testMapper.findAllByCreatedDateBetween(dateStartDate, dateEndDate);

        //Formに詰めなおし
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }

    /*
     * startのみ、endのみ入力されていた時
     */
    public List<ReportForm> findByDate(String start, String end) throws ParseException {
        //startが入力されていた時
        String startDate = null;
        if(!StringUtils.isBlank(start)){
            startDate = start + " 00:00:00";
        } else {
            startDate = "2020-01-01 00:00:00";
        }

        //endが入力されていた時
        String endDate = null;
        if(!StringUtils.isBlank(end)){
            endDate = end + " 23:59:59";
        } else {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String now = String.valueOf(sdf.format(date));
            endDate = now;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(startDate);
        Date date2 = sdf.parse(endDate);

        //List<Report> results = reportRepository.findAllByCreatedDateBetween(date,date2);
        List<Report> results = testMapper.findAllByCreatedDateBetween(date, date2);
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }

    /*
     * コメント登録したとき投稿のupdated_date更新
     */
    public void saveReportUpdatedDate(Integer id) {
        //Report saveReport = setReportEntityUpdate(reqReport);
        //int result = reportRepository.saveByUpdatedDate(id);
        int result = testMapper.saveByUpdatedDate(id);
    }

}
