package com.example.forum.controller.form;

import lombok.Data;

@Data
public class CommentForm {

    private int id;
    private int reportId;
    private String content;

}
