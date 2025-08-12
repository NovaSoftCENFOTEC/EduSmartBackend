package com.project.demo.logic.entity.submission;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.demo.logic.entity.answer.Answer;
import com.project.demo.logic.entity.quiz.Quiz;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

/**
 * Representa una entrega de cuestionario realizada por un estudiante.
 * Contiene información sobre la fecha de entrega, puntaje, cuestionario, estudiante y respuestas asociadas.
 */
@JsonIgnoreProperties({"answers"})
@Entity
@Table(name = "submission")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime submittedAt;
    private double score;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL)
    private Set<Answer> answers;

    /**
     * Obtiene el identificador único de la entrega.
     * @return id de la entrega
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único de la entrega.
     * @param id identificador de la entrega
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene la fecha en que se realizó la entrega.
     * @return fecha de entrega
     */
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    /**
     * Establece la fecha en que se realizó la entrega.
     * @param submittedAt fecha de entrega
     */
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    /**
     * Obtiene el puntaje obtenido en la entrega.
     * @return puntaje
     */
    public double getScore() {
        return score;
    }

    /**
     * Establece el puntaje obtenido en la entrega.
     * @param score puntaje
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Obtiene el cuestionario asociado a la entrega.
     * @return cuestionario
     */
    public Quiz getQuiz() {
        return quiz;
    }

    /**
     * Establece el cuestionario asociado a la entrega.
     * @param quiz cuestionario
     */
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    /**
     * Obtiene el estudiante que realizó la entrega.
     * @return estudiante
     */
    public User getStudent() {
        return student;
    }

    /**
     * Establece el estudiante que realizó la entrega.
     * @param student estudiante
     */
    public void setStudent(User student) {
        this.student = student;
    }

    /**
     * Obtiene el conjunto de respuestas asociadas a la entrega.
     * @return respuestas
     */
    public Set<Answer> getAnswers() {
        return answers;
    }

    /**
     * Establece el conjunto de respuestas asociadas a la entrega.
     * @param answers respuestas
     */
    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }
}