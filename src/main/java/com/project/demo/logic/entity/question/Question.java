package com.project.demo.logic.entity.question;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.demo.logic.entity.option.Option;
import com.project.demo.logic.entity.quiz.Quiz;
import jakarta.persistence.*;
import java.util.Set;

/**
 * Representa una pregunta de opción múltiple dentro de un cuestionario.
 * Contiene el texto de la pregunta, el cuestionario asociado y sus opciones.
 */
@JsonIgnoreProperties({"quiz", "options"})
@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private java.util.Set<Option> options;

    /**
     * Obtiene el identificador único de la pregunta.
     * @return id de la pregunta
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único de la pregunta.
     * @param id identificador de la pregunta
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el conjunto de opciones de la pregunta.
     * @return conjunto de opciones
     */
    public Set<Option> getOptions() {
        return options;
    }

    /**
     * Establece el conjunto de opciones de la pregunta.
     * @param options conjunto de opciones
     */
    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    /**
     * Obtiene el texto de la pregunta.
     * @return texto de la pregunta
     */
    public String getText() {
        return text;
    }

    /**
     * Establece el texto de la pregunta.
     * @param text texto de la pregunta
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Obtiene el cuestionario asociado a la pregunta.
     * @return cuestionario
     */
    public Quiz getQuiz() {
        return quiz;
    }

    /**
     * Establece el cuestionario asociado a la pregunta.
     * @param quiz cuestionario
     */
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}