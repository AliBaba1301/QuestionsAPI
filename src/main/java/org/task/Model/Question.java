package org.task.Model;

public class Question {
    private int questionId;
    private String question;
    private boolean expectedAnswer;
    private String rejectionString;

    public Question(){
    }

    public Question( int questionId, String question, boolean expectedAnswer, String rejectionString ) {
        this.rejectionString = rejectionString;
        this.expectedAnswer = expectedAnswer;
        this.question = question;
        this.questionId = questionId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getQuestion() {
        return question;
    }

    public boolean isExpectedAnswer() {
        return expectedAnswer;
    }

    public String getRejectionString() {
        return rejectionString;
    }
}
