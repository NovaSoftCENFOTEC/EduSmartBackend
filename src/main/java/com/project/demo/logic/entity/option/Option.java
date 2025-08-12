package com.project.demo.logic.entity.option;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.demo.logic.entity.question.Question;
import jakarta.persistence.*;

/**
 * Representa una opción de respuesta para una pregunta de opción múltiple.
 * Indica el texto de la opción y si es la respuesta correcta.
 */
@JsonIgnoreProperties({"question"})
@Entity
@Table(name = "option")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String text;

    @Column(name = "is_correct")
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    /**
     * Obtiene el identificador único de la opción.
     * @return id de la opción
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único de la opción.
     * @param id identificador de la opción
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el texto de la opción.
     * @return texto de la opción
     */
    public String getText() {
        return text;
    }

    /**
     * Establece el texto de la opción.
     * @param text texto de la opción
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Indica si la opción es la respuesta correcta.
     * @return true si es correcta, false en caso contrario
     */
    public boolean isCorrect() {
        return isCorrect;
    }

    /**
     * Establece si la opción es la respuesta correcta.
     * @param correct true si es correcta, false en caso contrario
     */
    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    /**
     * Obtiene la pregunta asociada a la opción.
     * @return pregunta
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Establece la pregunta asociada a la opción.
     * @param question pregunta
     */
    public void setQuestion(Question question) {
        this.question = question;
    }
}