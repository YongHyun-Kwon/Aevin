package com.aevin.domain.boards;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Question findBySubject(String subject);
    Question findBySubjectAndContent(String subject, String content);
    List<Question> findBySubjectLike(String subject);
    Page<Question> findAll(Pageable pageable);
    Page<Question> findAll(Specification<Question> specification, Pageable pageable);
    @Query("select "
            + " distinct q "
            + " from Question q "
            + " left outer join Member memberJoin1 on q.author=memberJoin1 "
            + " left outer join Answer answerJoin on answerJoin.question=q "
            + " left outer join Member memberJoin2 on answerJoin.author=memberJoin2 "
            + " where "
            + " q.subject like %:keyword% "
            + " or q.content like %:keyword% "
            + " or memberJoin1.name like %:keyword% "
            + " or answerJoin.content like %:keyword% "
            + " or memberJoin2.name like %:keyword% ")
    Page<Question> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
