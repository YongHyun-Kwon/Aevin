package com.aevin.service;

import com.aevin.domain.boards.Answer;
import com.aevin.domain.boards.AnswerRepository;
import com.aevin.domain.boards.Question;
import com.aevin.domain.members.Member;
import com.aevin.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer saveAnswer(Question question, String content, Member author) {

        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);

        return answer;
    } // saveAnswer

    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        } // end else
    } // getAnswer

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    } // modify

    public void delete(Answer answer) { this.answerRepository.delete(answer); } // delete

    public void vote(Answer answer, Member member) {
        answer.getVoter().add(member);
        this.answerRepository.save(answer);
    }


} // class

