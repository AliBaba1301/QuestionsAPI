package org.task.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.task.Model.RejectionResponse;
import org.task.Model.Question;
import org.task.Model.QuestionResponse;
import org.task.Repository.QuestionsRepository;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class QuestionsServiceTest {
    private final List<Question> questions = List.of(
            new Question(0,"test question 1",
                    true,"rejection string 1"),
            new Question(1,"test question 2",
                    true,"rejection string 2"),
            new Question(2,"test question 3",
                    true,"rejection string 3"));
    private final String disease = "all";
    private final List<QuestionResponse> validResponses = List.of(
            new QuestionResponse(0,true),
            new QuestionResponse(1,true),
            new QuestionResponse(2,true));
    private final List<QuestionResponse> invalidResponses = List.of(
            new QuestionResponse(0,false),
            new QuestionResponse(1,false),
            new QuestionResponse(3,false));
    private final Map<Integer, Map.Entry<Boolean,String>> expectedResponses = new HashMap<>();
    private final Map<Boolean, List<RejectionResponse>> validResponsesMap = new HashMap<>();

    private QuestionsRepository mockQuestionRepository;
    private QuestionsService questionService;


    @BeforeEach
    public void setup(){
        mockQuestionRepository = mock(QuestionsRepository.class);
        questionService = new QuestionsService(mockQuestionRepository);
        validResponsesMap.put(true, new ArrayList<>());
        for (Question question:questions){
            expectedResponses.put(question.getQuestionId(),
                    new AbstractMap.SimpleEntry<>(question.isExpectedAnswer(), question.getRejectionString()));
        }
    }

    @Test
    public void getAllQuestions_ReturnsFullListOfQuestions(){
        doReturn(questions).when(mockQuestionRepository).getAllQuestions(disease);
        assertEquals(questions,questionService.getAllQuestions(disease));
    }

    @Test
    public void addQuestions_ReturnsCombinedListOfQuestions(){
        List<Question> newQuestions = List.of(
                new Question(3,"new test question 1",
                        false,"rejection string 1"),
                new Question(4,"new test question 2",
                        false,"rejection string 2"),
                new Question(5,"new test question 3",
                        false,"rejection string 3"));
        List<Question> questionsCombined = Stream.concat(questions.stream(), newQuestions.stream()).toList();
        doReturn(questionsCombined).when(mockQuestionRepository).addQuestions(disease,newQuestions);
        assertEquals(questionsCombined,questionService.addQuestions(disease,newQuestions));
    }

    @Test
    public void updateQuestions_ReturnsUpdatedListOfQuestions(){
        List<Question> updatedQuestions = List.of(
                new Question(0,"test question 1",
                        true,"new rejection string 1"),
                new Question(1,"test question 2",
                        true,"new rejection string 2"),
                new Question(2,"test question 3",
                        true,"new rejection string 3"));

        doReturn(updatedQuestions).when(mockQuestionRepository).updateQuestions(disease,updatedQuestions);
        assertEquals(updatedQuestions,questionService.updateQuestions(disease,updatedQuestions));
    }

    @Test
    public void validateResponses_valid() throws Exception {
        doReturn(expectedResponses).when(mockQuestionRepository).getExpectedResponses();
        assertEquals(validResponsesMap,questionService.validateResponses(validResponses));
    }

    @Test
    public void validateResponses_() throws Exception {
        doReturn(expectedResponses).when(mockQuestionRepository).getExpectedResponses();
        Exception e = assertThrows(Exception.class,() ->
                questionService.validateResponses(invalidResponses));

        assertEquals("Sent a question that does not exist",e.getMessage());
    }
}
