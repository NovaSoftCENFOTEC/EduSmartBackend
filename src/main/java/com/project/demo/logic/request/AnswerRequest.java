package com.project.demo.logic.request;

public class AnswerRequest {
    private Integer questionId;
    private Integer optionId;

    public Integer getQuestionId() { return questionId; }
    public void setQuestionId(Integer questionId) { this.questionId = questionId; }
    public Integer getOptionId() { return optionId; }
    public void setOptionId(Integer optionId) { this.optionId = optionId; }
}