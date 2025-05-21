package com.example.forum.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentForm {

    private int id;
    private int reportId;
    @NotBlank(message = "コメントを入力してください。")
    private String content;

}
