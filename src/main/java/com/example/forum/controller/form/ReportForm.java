package com.example.forum.controller.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

//@Getter
//@Setter
@Data
public class ReportForm {

    private int id;
    private String content;
    private Date createdDate;

}

