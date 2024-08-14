package org.task.Model;

public class RejectionResponse {
    private int questionId;
    private String rejectionString;

    public RejectionResponse(int questionId, String rejectionString) {
        this.questionId = questionId;
        this.rejectionString = rejectionString;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getRejectionString() {
        return rejectionString;
    }
}
