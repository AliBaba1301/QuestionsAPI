package org.task.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.task.Model.Question;
import org.task.Model.QuestionResponse;
import org.task.Model.RejectionResponse;
import org.task.Service.QuestionsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @PostMapping(path = "/responses", produces = "application/json")
    public ResponseEntity validateResponses(@RequestBody List<QuestionResponse> responses) {

        try {
            Map<Boolean, List<RejectionResponse>> validationMap = questionsService.validateResponses(responses);
            Boolean valid = new ArrayList<>(validationMap.keySet()).get(0);
            List<RejectionResponse> rejectionStrings = new ArrayList<>(validationMap.values()).get(0);
            if (valid) {
                return new ResponseEntity<String>("We are able to prescribe to you today", HttpStatus.ACCEPTED);
            }
            return new ResponseEntity<>(rejectionStrings, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not validate responses: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
