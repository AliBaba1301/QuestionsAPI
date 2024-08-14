package org.task.Repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.task.Model.Question;


import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class QuestionsRepositoryTest {
    private final List<Question> questions = List.of(
            new Question(0, "test question 1",
                    true, "rejection string 1"),
            new Question(1, "test question 2",
                    true, "rejection string 2"),
            new Question(2, "test question 3",
                    true, "rejection string 3"));
    private final String diseaseDefault = "all";
    private final String diseaseSpecific = "allergy";

    private QuestionsRepository questionRepository;
    private NamedParameterJdbcTemplate mockJdbcTemplate;


    @BeforeEach
    public void setup() {
        mockJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        questionRepository = new QuestionsRepository(mockJdbcTemplate);
    }

    @Test
    public void getAllQuestions_returnsListOfAllQuestions_Default() {
        doReturn(questions).when(mockJdbcTemplate).query(
                eq("Select ID,Question, Expected_Answer, Rejection_String" +
                        " From Questions"),
                any(SqlParameterSource.class),
                any(RowMapper.class));

        assertEquals(questions, questionRepository.getAllQuestions(diseaseDefault));
    }

    @Test
    public void getAllQuestions_returnsListOfAllQuestions_Specific() {
        doReturn(questions).when(mockJdbcTemplate).query(
                eq("Select ID,Question, Expected_Answer, Rejection_String" +
                        " From Questions Where Disease in ('generic', :disease)"),
                any(SqlParameterSource.class),
                any(RowMapper.class));

        assertEquals(questions, questionRepository.getAllQuestions(diseaseSpecific));
    }

    @Test
    public void addQuestions_uploadsWithNoExceptions() {
        List<Question> newQuestions = List.of(
                new Question(3, "new test question 1",
                        false, "rejection string 1"),
                new Question(4, "new test question 2",
                        false, "rejection string 2"),
                new Question(5, "new test question 3",
                        false, "rejection string 3"));
        List<Question> questionsCombined = Stream.concat(questions.stream(), newQuestions.stream()).toList();

        questionRepository.addQuestions(diseaseDefault, newQuestions);
        verify(mockJdbcTemplate, times(newQuestions.size())).update(
                eq("INSERT INTO QUESTIONS (Disease, Question, " +
                        "Expected_Answer, Rejection_String) " +
                        "VALUES (:disease, :question,:expectedAnswer,:rejectionString)"),
                any(SqlParameterSource.class));
    }

    @Test
    public void updateQuestions_updatesWithNoExceptions() {
        List<Question> updatedQuestions = List.of(
                new Question(0, "test question 1",
                        true, "new rejection string 1"),
                new Question(1, "test question 2",
                        true, "new rejection string 2"),
                new Question(2, "test question 3",
                        true, "new rejection string 3"));

        questionRepository.updateQuestions(diseaseSpecific, updatedQuestions);
        verify(mockJdbcTemplate, times(updatedQuestions.size())).update(
                eq("UPDATE QUESTIONS " +
                        "SET Disease = :disease , Question = :question, " +
                        "Expected_Answer = :expectedAnswer, Rejection_String = :rejectionString " +
                        "WHERE Id = :questionId"),
                any(SqlParameterSource.class));
    }

    @Test
    public void deleteQuestions_deletesWithNoExceptions() {
        questionRepository.deleteQuestions(questions);
        verify(mockJdbcTemplate, times(questions.size())).update(
                eq("DELETE FROM QUESTIONS WHERE Id = :questionsId"),
                any(SqlParameterSource.class));
    }

    @Test
    public void getExpectedResponses_runsWithNoExceptions() {
        questionRepository.getExpectedResponses();
        verify(mockJdbcTemplate, times(1)).query(
                eq("Select ID, Expected_Answer, Rejection_String" +
                        " From Questions"),
                any(Map.class),
                any(RowMapper.class));
    }
}
