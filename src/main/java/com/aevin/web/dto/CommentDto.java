package com.aevin.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

    @NotEmpty(message = "내용은 필수 입력 값 입니다.")
    private String content;
}
