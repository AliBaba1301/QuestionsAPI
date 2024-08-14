package org.task.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.task.Model.RejectionResponse;
import org.task.Model.Question;
import org.task.Model.QuestionResponse;
import org.task.QuestionsApiMainApplication;
import org.task.Service.QuestionsService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = QuestionsApiMainApplication.class)
public class QuestionsControllerTest {
    private final List<Question> questions = List.of(
            new Question(0,"test question 1",
                    true,"rejection string 1"),
            new Question(1,"test question 2",
                    true,"rejection string 2"),
            new Question(2,"test question 3",
                    true,"rejection string 3"));
    private final String disease = "all";
    private final List<QuestionResponse> responses = List.of(
            new QuestionResponse(0,true),
            new QuestionResponse(1,true),
            new QuestionResponse(2,true));
    private final List<RejectionResponse> emptyInvalidResponseList = new ArrayList<>();
    private final List<RejectionResponse> invalidResponseList = List.of(
            new RejectionResponse(0,"rejection string 1"),
            new RejectionResponse(1,"rejection string 2"),
            new RejectionResponse(2,"rejection string 3"));
    private final Map<Boolean, List<RejectionResponse>> validResponsesMap = new HashMap<>();
    private final Map<Boolean, List<RejectionResponse>> invalidResponsesMap = new HashMap<>();
    private final BadSqlGrammarException exception = new
            BadSqlGrammarException(null,"Select* FROM QUESTIONS",new SQLException());


    private QuestionsService mockQuestionService;
    private QuestionsController questionsController;

    @BeforeEach
    public void setup(){
        mockQuestionService = mock(QuestionsService.class);
        questionsController = new QuestionsController(mockQuestionService);
        validResponsesMap.put(true, emptyInvalidResponseList);
        invalidResponsesMap.put(false,invalidResponseList);
    }

    @Test
    public void getAllQuestions_Returns200andFullListOfQuestions(){
        doReturn(questions).when(mockQuestionService).getAllQuestions(disease);

        ResponseEntity responseEntity = questionsController.getAllQuestions(disease);
        assertAll(
                () -> assertEquals(HttpStatus.OK,responseEntity.getStatusCode(),"Wrong HTTP status code"),
                () -> assertEquals(questions,responseEntity.getBody(),"Object not returned correctly"));
    }

    @Test
    public void getAllQuestions_Returns500OnException(){
        doThrow(exception)
                .when(mockQuestionService).getAllQuestions(disease);

        ResponseEntity responseEntity = questionsController.getAllQuestions(disease);
        assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,responseEntity.getStatusCode(),
                        "Wrong HTTP status code"),
                () -> assertEquals("Could not get questions: " + exception.getMessage(),
                        responseEntity.getBody(),
                        "Object not returned correctly"));
    }

    @Test
    public void addQuestions_Returns201andFullListOfQuestions(){
        doReturn(questions).when(mockQuestionService).addQuestions(disease,questions);

        ResponseEntity responseEntity = questionsController.addQuestions(disease,questions);
        assertAll(
                () -> assertEquals(responseEntity.getStatusCode(),HttpStatus.CREATED,"Wrong HTTP status code"),
                () -> assertEquals(responseEntity.getBody(),questions,"Object not returned correctly"));
    }

    @Test
    public void addQuestions_Returns500OnException(){
        doThrow(exception)
                .when(mockQuestionService).addQuestions(disease,questions);

        ResponseEntity responseEntity = questionsController.addQuestions(disease,questions);
        assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,responseEntity.getStatusCode(),
                        "Wrong HTTP status code"),
                () -> assertEquals("Could not add new questions: " + exception.getMessage(),
                        responseEntity.getBody(),
                        "Object not returned correctly"));
    }

    @Test
    public void updateQuestions_Returns200andFullListOfQuestions(){
        doReturn(questions).when(mockQuestionService).updateQuestions(disease,questions);

        ResponseEntity responseEntity = questionsController.updateQuestions(disease,questions);
        assertAll(
                () -> assertEquals(responseEntity.getStatusCode(),HttpStatus.OK,"Wrong HTTP status code"),
                () -> assertEquals(responseEntity.getBody(),questions,"Object not returned correctly"));
    }

    @Test
    public void updateQuestions_Returns500OnException(){
        doThrow(exception)
                .when(mockQuestionService).updateQuestions(disease,questions);

        ResponseEntity responseEntity = questionsController.updateQuestions(disease,questions);
        assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,responseEntity.getStatusCode(),
                        "Wrong HTTP status code"),
                () -> assertEquals("Could not update questions: " + exception.getMessage(),
                        responseEntity.getBody(),
                        "Object not returned correctly"));
    }

    @Test
    public void deleteQuestions_Returns200(){
        ResponseEntity responseEntity = questionsController.deleteQuestions(questions);
        assertEquals(responseEntity.getStatusCode(),HttpStatus.OK,"Wrong HTTP status code");
    }

    @Test
    public void deleteQuestions_Returns500OnException(){
        doThrow(exception)
                .when(mockQuestionService).deleteQuestions(questions);

        ResponseEntity responseEntity = questionsController.deleteQuestions(questions);
        assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,responseEntity.getStatusCode(),
                        "Wrong HTTP status code"),
                () -> assertEquals("Could not delete questions: " + exception.getMessage(),
                        responseEntity.getBody(),
                        "Object not returned correctly"));
    }

    @Test
    public void validateResponses_Returns200andSuccessMessage() throws Exception {
        doReturn(validResponsesMap).when(mockQuestionService).validateResponses(responses);

        ResponseEntity responseEntity = questionsController.validateResponses(responses);
        assertAll(
                () -> assertEquals(responseEntity.getStatusCode(),HttpStatus.ACCEPTED,"Wrong HTTP status code"),
                () -> assertEquals(responseEntity.getBody(),"We are able to prescribe to you today"
                        ,"Object not returned correctly"));
    }

    @Test
    public void validateResponses_Returns200andListOfRejectionMessages() throws Exception {
        doReturn(invalidResponsesMap).when(mockQuestionService).validateResponses(responses);

        ResponseEntity responseEntity = questionsController.validateResponses(responses);
        assertAll(
                () -> assertEquals(responseEntity.getStatusCode(),HttpStatus.NOT_ACCEPTABLE,"Wrong HTTP status code"),
                () -> assertEquals(responseEntity.getBody(),invalidResponseList
                        ,"Object not returned correctly"));
    }

    @Test
    public void validateResponses_Returns500() throws Exception {
        doThrow(exception)
                .when(mockQuestionService).validateResponses(responses);

        ResponseEntity responseEntity = questionsController.validateResponses(responses);
        assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,responseEntity.getStatusCode(),
                        "Wrong HTTP status code"),
                () -> assertEquals("Could not validate responses: " + exception.getMessage(),
                        responseEntity.getBody(),
                        "Object not returned correctly"));
    }

}
