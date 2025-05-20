package com.example.forum.controller.form;

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
    private String content;
    private Date createdDate;
    private Date updatedDate;

}

