package com.project.demo.logic.entity.group;

import com.project.demo.logic.entity.course.Course;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Representa un grupo de estudiantes dentro de un curso.
 * Contiene información como nombre, curso, estudiantes y profesor asignado.
 */
@Table(name = "group_table")
@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", referencedColumnName = "id", nullable = false)
    private Course course;

    @ManyToMany
    @JoinTable(
        name = "group_student",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> students = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id", nullable = false)
    private User teacher;

    /**
     * Constructor por defecto.
     */
    public Group() {
    }

    /**
     * Constructor con todos los parámetros.
     * @param id identificador del grupo
     * @param name nombre del grupo
     * @param course curso asociado
     * @param students conjunto de estudiantes
     * @param teacher profesor asignado
     */
    public Group(long id, String name, Course course, Set<User> students, User teacher) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.students = students;
        this.teacher = teacher;
    }

    /**
     * Obtiene el identificador único del grupo.
     * @return id del grupo
     */
    public long getId() {
        return id;
    }

    /**
     * Establece el identificador único del grupo.
     * @param id identificador del grupo
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del grupo.
     * @return nombre
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del grupo.
     * @param name nombre
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el curso asociado al grupo.
     * @return curso
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Establece el curso asociado al grupo.
     * @param course curso
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Obtiene el conjunto de estudiantes del grupo.
     * @return conjunto de estudiantes
     */
    public Set<User> getStudents() {
        return students;
    }

    /**
     * Establece el conjunto de estudiantes del grupo.
     * @param students conjunto de estudiantes
     */
    public void setStudents(Set<User> students) {
        this.students = students;
    }

    /**
     * Obtiene el profesor asignado al grupo.
     * @return profesor
     */
    public User getTeacher() {
        return teacher;
    }

    /**
     * Establece el profesor asignado al grupo.
     * @param teacher profesor
     */
    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
}
