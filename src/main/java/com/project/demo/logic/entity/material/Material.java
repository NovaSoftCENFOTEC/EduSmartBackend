package com.project.demo.logic.entity.material;

import com.project.demo.logic.entity.course.Course;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

/**
 * Representa un material educativo subido por un profesor.
 * Incluye información como nombre, URL del archivo, fecha de subida, curso y profesor.
 */
@Table(name = "material")
@Entity
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(name = "file_url")
    private String fileUrl;

    @CreationTimestamp
    @Column(updatable = false, name = "uploaded_at")
    private Date uploadedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", referencedColumnName = "id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id", nullable = false)
    private User teacher;

    /**
     * Constructor por defecto.
     */
    public Material() {
    }

    /**
     * Constructor con todos los parámetros.
     *
     * @param id         identificador del material
     * @param name       nombre del material
     * @param fileUrl    URL del archivo
     * @param uploadedAt fecha de subida
     * @param course     curso asociado
     * @param teacher    profesor que subió el material
     */
    public Material(long id, String name, String fileUrl, Date uploadedAt, Course course, User teacher) {
        this.id = id;
        this.name = name;
        this.fileUrl = fileUrl;
        this.uploadedAt = uploadedAt;
        this.course = course;
        this.teacher = teacher;
    }

    /**
     * Obtiene el identificador único del material.
     *
     * @return id del material
     */
    public long getId() {
        return id;
    }

    /**
     * Establece el identificador único del material.
     *
     * @param id identificador del material
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del material.
     *
     * @return nombre
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del material.
     *
     * @param name nombre
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene la URL del archivo del material.
     *
     * @return URL del archivo
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * Establece la URL del archivo del material.
     *
     * @param fileUrl URL del archivo
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * Obtiene la fecha de subida del material.
     *
     * @return fecha de subida
     */
    public Date getUploadedAt() {
        return uploadedAt;
    }

    /**
     * Establece la fecha de subida del material.
     *
     * @param uploadedAt fecha de subida
     */
    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    /**
     * Obtiene el curso asociado al material.
     *
     * @return curso
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Establece el curso asociado al material.
     *
     * @param course curso
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Obtiene el profesor que subió el material.
     *
     * @return profesor
     */
    public User getTeacher() {
        return teacher;
    }

    /**
     * Establece el profesor que subió el material.
     *
     * @param teacher profesor
     */
    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
}
