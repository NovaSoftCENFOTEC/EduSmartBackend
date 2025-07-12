package com.project.demo.rest.answer.dto;

public class AnswerResultDto {
    private String question;
    private String selectedAnswer;
    private boolean isCorrect;

    public AnswerResultDto() {}

    public AnswerResultDto(String question, String selectedAnswer, boolean isCorrect) {
        this.question = question;
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = isCorrect;
    }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(String selectedAnswer) { this.selectedAnswer = selectedAnswer; }

    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean correct) { isCorrect = correct; }
}