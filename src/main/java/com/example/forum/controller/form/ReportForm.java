package com.example.forum.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

//@Getter
//@Setter
@Data
public class ReportForm {

    private int id;
    @NotBlank(message = "投稿内容を入力してください。")
    private String content;
    private Date createdDate;
    private Date updatedDate;

}

