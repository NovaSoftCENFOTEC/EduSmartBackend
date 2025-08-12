package com.project.demo.logic.entity.quiz;

import com.project.demo.logic.entity.story.Story;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Representa un cuestionario dentro de la plataforma.
 * Contiene información como título, descripción, fecha de entrega y la historia asociada.
 */
@Entity
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;

    @Transient
    private boolean generateWithAI = false;

    @Transient
    private int numberOfQuestions = 5;

    /**
     * Constructor por defecto.
     */
    public Quiz() {
    }

    /**
     * Constructor con título y descripción.
     *
     * @param title       título del cuestionario
     * @param description descripción del cuestionario
     */
    public Quiz(String title, String description) {
        this.title = title;
        this.description = description;
    }

    /**
     * Obtiene el identificador único del cuestionario.
     *
     * @return id del cuestionario
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador único del cuestionario.
     *
     * @param id identificador del cuestionario
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el título del cuestionario.
     *
     * @return título
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece el título del cuestionario.
     *
     * @param title título
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtiene la descripción del cuestionario.
     *
     * @return descripción
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción del cuestionario.
     *
     * @param description descripción
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene la fecha de entrega del cuestionario.
     *
     * @return fecha de entrega
     */
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    /**
     * Establece la fecha de entrega del cuestionario.
     *
     * @param dueDate fecha de entrega
     */
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Obtiene la historia asociada al cuestionario.
     *
     * @return historia
     */
    public Story getStory() {
        return story;
    }

    /**
     * Establece la historia asociada al cuestionario.
     *
     * @param story historia
     */
    public void setStory(Story story) {
        this.story = story;
    }

    /**
     * Indica si el cuestionario debe generarse automáticamente con IA.
     *
     * @return true si se genera con IA, false en caso contrario
     */
    public boolean isGenerateWithAI() {
        return generateWithAI;
    }

    /**
     * Establece si el cuestionario debe generarse automáticamente con IA.
     *
     * @param generateWithAI true para generar con IA
     */
    public void setGenerateWithAI(boolean generateWithAI) {
        this.generateWithAI = generateWithAI;
    }

    /**
     * Obtiene el número de preguntas a generar automáticamente.
     *
     * @return número de preguntas
     */
    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    /**
     * Establece el número de preguntas a generar automáticamente.
     *
     * @param numberOfQuestions número de preguntas
     */
    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
}