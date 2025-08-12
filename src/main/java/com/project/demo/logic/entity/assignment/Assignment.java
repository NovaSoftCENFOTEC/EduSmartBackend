package com.project.demo.logic.entity.assignment;

import com.project.demo.logic.entity.group.Group;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

/**
 * Representa una tarea asignada a un grupo.
 * Incluye información como título, descripción, tipo, fecha de entrega y grupo asociado.
 */
@Table(name = "assignment")
@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String description;
    private String type;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private Group group;

    /**
     * Constructor por defecto.
     */
    public Assignment() {
    }

    /**
     * Constructor con todos los parámetros.
     *
     * @param id          identificador de la tarea
     * @param title       título de la tarea
     * @param description descripción de la tarea
     * @param type        tipo de tarea
     * @param dueDate     fecha de entrega
     * @param createdAt   fecha de creación
     * @param group       grupo asociado
     */
    public Assignment(long id, String title, String description, String type, LocalDate dueDate, LocalDate createdAt, Group group) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.group = group;
    }

    /**
     * Obtiene el identificador único de la tarea.
     *
     * @return id de la tarea
     */
    public long getId() {
        return id;
    }

    /**
     * Establece el identificador único de la tarea.
     *
     * @param id identificador de la tarea
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Obtiene el título de la tarea.
     *
     * @return título
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece el título de la tarea.
     *
     * @param title título
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtiene la descripción de la tarea.
     *
     * @return descripción
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción de la tarea.
     *
     * @param description descripción
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene el tipo de tarea.
     *
     * @return tipo
     */
    public String getType() {
        return type;
    }

    /**
     * Establece el tipo de tarea.
     *
     * @param type tipo
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Obtiene la fecha de entrega de la tarea.
     *
     * @return fecha de entrega
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Establece la fecha de entrega de la tarea.
     *
     * @param dueDate fecha de entrega
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Obtiene la fecha de creación de la tarea.
     *
     * @return fecha de creación
     */
    public LocalDate getCreatedAt() {
        return createdAt;
    }

    /**
     * Establece la fecha de creación de la tarea.
     *
     * @param createdAt fecha de creación
     */
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Obtiene el grupo asociado a la tarea.
     *
     * @return grupo
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Establece el grupo asociado a la tarea.
     *
     * @param group grupo
     */
    public void setGroup(Group group) {
        this.group = group;
    }
}
