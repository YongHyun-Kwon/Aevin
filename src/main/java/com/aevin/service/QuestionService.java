package com.aevin.service;

import com.aevin.domain.boards.Answer;
import com.aevin.domain.boards.Question;
import com.aevin.domain.boards.QuestionRepository;
import com.aevin.domain.members.Member;
import com.aevin.exception.DataNotFoundException;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    private Specification<Question> search(String keyword) {
        return new Specification<Question>() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Question> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.distinct(true); // 중복 제거
                Join<Question, Member> memberJoin1 = root.join("author", JoinType.LEFT);
                Join<Question, Answer> answerJoin = root.join("answerList", JoinType.LEFT);
                Join<Answer, Member> memberJoin2 = answerJoin.join("author", JoinType.LEFT);
                return criteriaBuilder.or(criteriaBuilder.like(root.get("subject"), "%" + keyword + "%"), // 제목
                       criteriaBuilder.like(root.get("content"), "%" + keyword + "%"), // 내용
                       criteriaBuilder.like(memberJoin1.get("name"), "%" + keyword + "%"), // 질문 작성자
                       criteriaBuilder.like(answerJoin.get("content"), "%" + keyword + "%"), // 답변 내용
                       criteriaBuilder.like(memberJoin2.get("name"), "%" + keyword + "%")); // 답변 작성자
            } // toPredicate
        };
    } // search

    public Page<Question> getList(int page, String keyword) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> specification = search(keyword);
        return this.questionRepository.findByKeyword(keyword, pageable);
    }

    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        } // end else
    } // getQuestion

    public void createQuestion(String subject, String content, Member member) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setAuthor(member);
        q.setCreatedDate(LocalDateTime.now());
        this.questionRepository.save(q);
    } // createQuestion

    public void modifyQuestion(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    } // modifyQuestion

    public void delete(Question question) { this.questionRepository.delete(question); } // delete

    public void vote(Question question, Member member) {
        question.getVoter().add(member);
        this.questionRepository.save(question);
    } // vote

}
