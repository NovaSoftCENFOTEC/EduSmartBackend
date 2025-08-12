package com.project.demo.logic.entity.answer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.demo.logic.entity.option.Option;
import com.project.demo.logic.entity.question.Question;
import com.project.demo.logic.entity.submission.Submission;
import jakarta.persistence.*;

/**
 * Representa una respuesta dada por un usuario en una entrega.
 * Contiene la relación con la entrega, la pregunta y la opción seleccionada.
 */
@JsonIgnoreProperties({"submission", "question", "selectedOption"})
@Entity
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    @ManyToOne
    @JoinColumn(name = "selected_option_id", nullable = false)
    private Option selectedOption;

    /**
     * Obtiene el identificador único de la respuesta.
     * @return id de la respuesta
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único de la respuesta.
     * @param id identificador de la respuesta
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene la entrega asociada a la respuesta.
     * @return entrega
     */
    public Submission getSubmission() {
        return submission;
    }

    /**
     * Establece la entrega asociada a la respuesta.
     * @param submission entrega
     */
    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    /**
     * Obtiene la pregunta asociada a la respuesta.
     * @return pregunta
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Establece la pregunta asociada a la respuesta.
     * @param question pregunta
     */
    public void setQuestion(Question question) {
        this.question = question;
    }

    /**
     * Obtiene la opción seleccionada en la respuesta.
     * @return opción seleccionada
     */
    public Option getSelectedOption() {
        return selectedOption;
    }

    /**
     * Establece la opción seleccionada en la respuesta.
     * @param selectedOption opción seleccionada
     */
    public void setSelectedOption(Option selectedOption) {
        this.selectedOption = selectedOption;
    }
}