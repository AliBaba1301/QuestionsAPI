package org.task.Service;

import org.task.Model.Question;
import org.task.Model.QuestionResponse;
import org.task.Model.RejectionResponse;
import org.task.Repository.QuestionsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("questionsService")
public class QuestionsService {
    private final QuestionsRepository questionsRepository;

    public QuestionsService(QuestionsRepository questionsRepository) {
        this.questionsRepository = questionsRepository;
    }

    public List<Question> getAllQuestions (String disease){
        return questionsRepository.getAllQuestions(disease);
    }

    public Map<Boolean, List<RejectionResponse>> validateResponses (List<QuestionResponse> responses){
        boolean allValid = true;
        Map<Integer, Map.Entry<Boolean,String>> expectedResponses = questionsRepository.getExpectedResponses();
        Map<Boolean,List<RejectionResponse>> map = new HashMap<>();
        List<RejectionResponse> invalidResponses = new ArrayList<>();
        for (QuestionResponse response : responses) {
            if (!(response.getAnswer() == expectedResponses.get(response.getQuestionId()).getKey())){
                allValid = false;
                invalidResponses.add(new RejectionResponse(response.getQuestionId(),
                        expectedResponses.get(response.getQuestionId()).getValue()));
            }
        }
        map.put(allValid,invalidResponses);

        return map;
    }
}
