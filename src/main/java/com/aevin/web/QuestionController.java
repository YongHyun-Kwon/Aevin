package com.aevin.web;

import com.aevin.domain.boards.Question;
import com.aevin.domain.members.Member;
import com.aevin.service.MemberService;
import com.aevin.service.QuestionService;
import com.aevin.web.dto.AnswerDto;
import com.aevin.web.dto.QuestionDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String getQuestionList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        Page<Question> paging = this.questionService.getList(page, keyword);
        model.addAttribute("paging", paging);
        model.addAttribute("keyword", keyword);
        return "question_list";
    } // getQuestionList

    @GetMapping(value = "/detail/{id}")
    public String questionDetail(Model model, @PathVariable("id") Integer id, AnswerDto answerDto) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    } // questionDetail

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionDto questionDto) { return "question_form"; }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionDto questionDto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        } // end if
        Member member =this.memberService.getMember(principal.getName());
        this.questionService.createQuestion(questionDto.getSubject(), questionDto.getContent(), member);
        return "redirect:/question/list"; // 질문 저장 후 질문 목록으로 이동
    } // questionDetail

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionDto questionDto, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        } // end if
        this.questionService.modifyQuestion(question, questionDto.getSubject(), questionDto.getContent());
        return String.format("redirect:/question/detail/%s", id);
    } // questionModify

}
