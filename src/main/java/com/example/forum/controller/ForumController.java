package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

//Validated
@Controller
public class ForumController {
    @Autowired
    ReportService reportService;
    @Autowired
    CommentService commentService;
    @Autowired
    HttpSession session;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top(@RequestParam(name="start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String start,
                            @RequestParam(name="end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String end) throws ParseException {
        ModelAndView mav = new ModelAndView();
        List<ReportForm> contentData = new ArrayList<>();
        //startとendに値が入ってたら日付で絞り込む
        if(!StringUtils.isBlank(start) && !StringUtils.isBlank(end)) {
            contentData = reportService.findByStartToEnd(start, end);
        } else if(!StringUtils.isBlank(start)) {
            contentData = reportService.findByDate(start, end);
        } else if(!StringUtils.isBlank(end)) {
            contentData = reportService.findByDate(start, end);
        } else {
            // 投稿を全件取得
            contentData = reportService.findAllReport();
        }
        //コメントを全件取得
        List<CommentForm> commentData = commentService.findAllComment();
        //errorMessageがコメント登録から渡ってきていればエラーメッセージを表示する
        String errorMessage = null;
        Integer reportId = null;
        if(session.getAttribute("errorMessage") != null) {
            errorMessage = session.getAttribute("errorMessage").toString();
            reportId = (Integer) session.getAttribute("reportId");
            session.invalidate();
        }

        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        mav.addObject("comments", commentData);
        mav.addObject("start", start);
        mav.addObject("end", end);
        mav.addObject("errorMessage", errorMessage);
        mav.addObject("reportId", reportId);

        return mav;
    }

    /*
     * 新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        ReportForm reportForm = new ReportForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        return mav;
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@ModelAttribute("formModel") @Validated ReportForm reportForm,
                                   BindingResult result){
        ModelAndView mav = new ModelAndView();

        if (result.hasErrors()) {
            mav.setViewName("/new");
        } else {
            // 投稿をテーブルに格納
            reportService.saveReport(reportForm);
            // rootへリダイレクト
            return new ModelAndView("redirect:/");
        }

        return mav;
    }

    /*
     * 投稿削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id){
        reportService.deleteReport(id);
        return new ModelAndView("redirect:/");
    }

    /*
     * 編集画面表示処理
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // 編集する投稿を取得
        ReportForm report = reportService.editReport(id);
        // 編集する投稿をセット
        mav.addObject("formModel", report);
        // 画面遷移先を指定
        mav.setViewName("/edit");
        return mav;
    }

    /*
     * 編集処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent (@PathVariable Integer id,
                                       @ModelAttribute("formModel") @Validated ReportForm reportForm,
                                       BindingResult result) {
        ModelAndView mav = new ModelAndView();

        if (result.hasErrors()) {
            mav.setViewName("/edit");
        } else {
            // UrlParameterのidを更新するentityにセット
            reportForm.setId(id);
            // 編集した投稿を更新
            reportService.saveReport(reportForm);
            //reportService.updateReport(reportForm);
            // rootへリダイレクト
            return new ModelAndView("redirect:/");
        }

        return mav;
    }

    /*
     * コメント登録処理
     */
    @PostMapping("/newComment/{id}")
    public ModelAndView commentContent (@PathVariable Integer id,
                                        @ModelAttribute("formModel") @Validated CommentForm comment,
                                        BindingResult result) {
        ModelAndView mav = new ModelAndView();

        if(result.hasErrors()){
            //String errorMessage = result.getAllErrors().toString();
            String errorMessage = "";
            for (ObjectError error : result.getAllErrors()) {
                // ここでメッセージを取得する。
                errorMessage += error.getDefaultMessage();
            }
            session.setAttribute("reportId", id);
            session.setAttribute("errorMessage", errorMessage);
            return new ModelAndView("redirect:/");
        } else {
            comment.setReportId(id);
            commentService.saveComment(comment);
            reportService.saveReportUpdatedDate(id);
            return new ModelAndView("redirect:/");
        }
    }

    /*
     * コメント編集画面表示
     */
    @GetMapping("/editComment/{id}")
    public ModelAndView editComment(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // 編集する投稿を取得
        CommentForm comment = commentService.editComment(id);
        // 編集する投稿をセット
        mav.addObject("formModel", comment);
        // 画面遷移先を指定
        mav.setViewName("/editComment");
        return mav;
    }

    /*
     * コメント編集処理
     */
    @PutMapping("/updateComment/{id}/{reportId}")
    public ModelAndView updateComment (@PathVariable Integer id,
                                       @PathVariable Integer reportId,
                                       @ModelAttribute("formModel") @Validated CommentForm comment,
                                       BindingResult result) {
        ModelAndView mav = new ModelAndView();

        if(result.hasErrors()){
            mav.setViewName("/editComment");
        } else {
            // UrlParameterのidを更新するentityにセット
            comment.setId(id);
            comment.setReportId(reportId);
            // 編集したコメントを更新
            commentService.saveComment(comment);
            // rootへリダイレクト
            return new ModelAndView("redirect:/");
        }

        return mav;
    }

    /*
     * コメント削除処理
     */
    @DeleteMapping("/deleteComment/{id}")
    public ModelAndView deleteComment(@PathVariable Integer id){
        commentService.deleteComment(id);
        return new ModelAndView("redirect:/");
    }




}

