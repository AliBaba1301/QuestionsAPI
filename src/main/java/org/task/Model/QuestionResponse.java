package org.task.Model;


public class QuestionResponse {
    private int questionId;
    private boolean answer;

    public QuestionResponse( int questionId, boolean answer ) {
        this.questionId = questionId;
        this.answer = answer;
    }

    public QuestionResponse() {
    }

    public int getQuestionId() {
        return questionId;
    }

    public boolean getAnswer() {
        return answer;
    }
}
