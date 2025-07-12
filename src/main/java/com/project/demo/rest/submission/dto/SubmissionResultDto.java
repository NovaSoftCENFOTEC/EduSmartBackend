package com.project.demo.rest.submission.dto;

import com.project.demo.rest.answer.dto.AnswerResultDto;

import java.util.List;

public class SubmissionResultDto {
    private int totalQuestions;
    private int correctAnswers;
    private double score;
    private List<AnswerResultDto> results;

    public SubmissionResultDto() {}

    public SubmissionResultDto(int totalQuestions, int correctAnswers, double score, List<AnswerResultDto> results) {
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.score = score;
        this.results = results;
    }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public List<AnswerResultDto> getResults() { return results; }
    public void setResults(List<AnswerResultDto> results) { this.results = results; }
}