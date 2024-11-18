package com.aevin.web;

import com.aevin.domain.boards.Comment;
import com.aevin.domain.boards.Question;
import com.aevin.domain.members.Member;
import com.aevin.service.CommentService;
import com.aevin.service.MemberService;
import com.aevin.service.QuestionService;
import com.aevin.web.dto.CommentDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final QuestionService questionService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/question/{id}")
    public String createComment(CommentDto commentDto) {return "comment_form"; } // createComment

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/question/{id}")
    public String createQuestionComment(@PathVariable("id") Integer id, @Valid CommentDto commentDto, BindingResult bindingResult, Principal principal) {
        Optional<Question> question = Optional.ofNullable(this.questionService.getQuestion(id));
        Optional<Member> member = Optional.ofNullable(this.memberService.getMember(principal.getName()));
        if (question.isPresent() && member.isPresent()) {
            if (bindingResult.hasErrors()) {
                return "comment_form";
            } // end if
            Comment comment = this.commentService.createComment(question.get(), member.get(), commentDto.getContent());
            return String.format("redirect:/question/detail/%s", comment.getQuestionId());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        } // end else
    } // createQuestionComment

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyComment(CommentDto commentDto, @PathVariable("id") Integer id, Principal principal) {
        Optional<Comment> _comment = this.commentService.getComment(id);
        if (_comment.isPresent()) {
            Comment comment = _comment.get();
            if (!comment.getAuthor().getEmail().equals(principal.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            } // end if
            commentDto.setContent(comment.getContent());
        } // end if
        return "comment_form";
    } // modifyComment

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyComment(@Valid CommentDto commentDto, BindingResult bindingResult, Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }
        Optional<Comment> _comment = this.commentService.getComment(id);
        if (_comment.isPresent()) {
            Comment comment = _comment.get();
            if (!comment.getAuthor().getEmail().equals(principal.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            } // end if
            comment = this.commentService.modifyComment(comment, commentDto.getContent());
            return String.format("redirect:/question/detail/%s", comment.getQuestionId());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        } // end else
    } // modifyComment

    @PreAuthorize("isAuthenticated")
    @GetMapping("delete/{id}")
    public String deleteComment(Principal principal, @PathVariable("id") Integer id) {
        Optional<Comment> _comment = this.commentService.getComment(id);
        if (_comment.isPresent()) {
            Comment comment = _comment.get();
            if (!comment.getAuthor().getEmail().equals(principal.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
            } // end if
            this.commentService.deleteComment(comment);
            return String.format("redirect:/question/detail/%s", comment.getQuestionId());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "entity not found");
        } // ens else
    } // deleteComment

} // class
