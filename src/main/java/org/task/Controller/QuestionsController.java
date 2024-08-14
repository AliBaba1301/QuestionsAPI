package org.task.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.task.Model.Question;
import org.task.Service.QuestionsService;

import java.util.List;

@RestController
public class QuestionsController {
    private final QuestionsService questionsService;

    public QuestionsController(final QuestionsService questionsService) {
        this.questionsService = questionsService;
    }

    @GetMapping(path = "/questions", produces = "application/json")
    public ResponseEntity getAllQuestions(@RequestParam(name = "disease") String disease) {
        try {
            List<Question> allQuestionsData = questionsService.getAllQuestions(disease);
            return new ResponseEntity<>(allQuestionsData, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not get questions: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
