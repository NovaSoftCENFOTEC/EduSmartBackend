package com.project.demo.logic.entity.user;
import com.project.demo.logic.entity.badge.Badge;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.school.School;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Representa un usuario dentro de la plataforma.
 * Implementa UserDetails para la autenticación y autorización.
 * Incluye información como nombre, correo, rol, escuela y fechas de creación/actualización.
 */
@Table(name = "user")
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastname;
    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "profile_picture")
    private String profilePic;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName().toString());
        return List.of(authority);
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "school_id", referencedColumnName = "id", nullable = false)
    private School school;

    @Column(name = "needs_password_change", nullable = false)
    boolean needsPasswordChange = false;

    // Constructors
    /**
     * Constructor por defecto.
     */
    public User() {}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Obtiene el identificador único del usuario.
     * @return id del usuario
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     * @param id identificador del usuario
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del usuario.
     * @return nombre
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del usuario.
     * @param name nombre
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el apellido del usuario.
     * @return apellido
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Establece el apellido del usuario.
     * @param lastname apellido
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * @return correo electrónico
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     * @param email correo electrónico
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return contraseña
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     * @param password contraseña
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene la fecha de creación del usuario.
     * @return fecha de creación
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Establece la fecha de creación del usuario.
     * @param createdAt fecha de creación
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Obtiene la fecha de actualización del usuario.
     * @return fecha de actualización
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Establece la fecha de actualización del usuario.
     * @param updatedAt fecha de actualización
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Obtiene la URL de la foto de perfil del usuario.
     * @return URL de la foto de perfil
     */
    public String getProfilePic() {
        return profilePic;
    }

    /**
     * Establece la URL de la foto de perfil del usuario.
     * @param profilePic URL de la foto de perfil
     */
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    /**
     * Obtiene el rol del usuario.
     * @return rol
     */
    public Role getRole() {
        return role;
    }

    /**
     * Establece el rol del usuario.
     * @param role rol
     * @return instancia actual de User
     */
    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    /**
     * Obtiene la escuela asociada al usuario.
     * @return escuela
     */
    public School getSchool() {
        return school;
    }

    /**
     * Establece la escuela asociada al usuario.
     * @param school escuela
     */
    public void setSchool(School school) {
        this.school = school;
    }

    /**
     * Indica si el usuario necesita cambiar la contraseña.
     * @return true si necesita cambiarla, false en caso contrario
     */
    public boolean isNeedsPasswordChange() {
        return needsPasswordChange;
    }

    /**
     * Establece si el usuario necesita cambiar la contraseña.
     * @param needsPasswordChange true si necesita cambiarla
     */
    public void setNeedsPasswordChange(boolean needsPasswordChange) {
        this.needsPasswordChange = needsPasswordChange;
    }
}
