package com.aevin.service;

import com.aevin.domain.boards.Comment;
import com.aevin.domain.boards.CommentRepository;
import com.aevin.domain.boards.Question;
import com.aevin.domain.members.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment createComment(Question question, Member author, String content) {

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setQuestion(question);
        comment.setAuthor(author);
        comment = this.commentRepository.save(comment);
        return comment;

    } // create

    public Optional<Comment> getComment(Integer id) { return this.commentRepository.findById(id); } // getComment

    public Comment modifyComment(Comment comment, String content) {
        comment.setContent(content);
        comment.setModifyDate(LocalDateTime.now());
        comment = this.commentRepository.save(comment);
        return comment;
    } // modifyComment

    public void deleteComment(Comment comment) { this.commentRepository.delete(comment); }
} // class
