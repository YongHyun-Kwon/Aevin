package com.aevin.domain.boards;

import com.aevin.domain.members.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Member author;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Answer answer;

    public Long getQuestionId() {
        Long result = null;
        if (this.question != null) {
            result = this.question.getId();
        } else if(this.answer != null) {
            result = this.answer.getQuestion().getId();
        }
        return result;
    } // getQuestionId
}
