package org.task.Service;

import org.task.Model.Question;
import org.task.Repository.QuestionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("questionsService")
public class QuestionsService {
    private final QuestionsRepository questionsRepository;

    public QuestionsService(QuestionsRepository questionsRepository) {
        this.questionsRepository = questionsRepository;
    }

    public List<Question> getAllQuestions (String disease){
        return questionsRepository.getAllQuestions(disease);
    }

}
