package com.project.demo.logic.entity.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad User.
 * Permite consultar usuarios por nombre, apellido, correo, escuela y rol.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>  {
    /**
     * Busca usuarios cuyo nombre contiene un carácter específico (ignorando mayúsculas/minúsculas).
     * @param character carácter a buscar
     * @return lista de usuarios
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE %?1%")
    List<User> findUsersWithCharacterInName(String character);

    /**
     * Busca un usuario por nombre exacto.
     * @param name nombre del usuario
     * @return usuario encontrado (opcional)
     */
    @Query("SELECT u FROM User u WHERE u.name = ?1")
    Optional<User> findByName(String name);

    /**
     * Busca un usuario por apellido.
     * @param lastname apellido del usuario
     * @return usuario encontrado (opcional)
     */
    Optional<User> findByLastname(String lastname);

    /**
     * Busca un usuario por correo electrónico.
     * @param email correo electrónico
     * @return usuario encontrado (opcional)
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca usuarios por el identificador de la escuela.
     * @param schoolId identificador de la escuela
     * @param pageable paginación
     * @return página de usuarios
     */
    Page<User> getUsersBySchoolId(Long schoolId, Pageable pageable);

    /**
     * Busca usuarios por el identificador de la escuela y el identificador del rol.
     * @param schoolId identificador de la escuela
     * @param roleId identificador del rol
     * @param pageable paginación
     * @return página de usuarios
     */
    Page<User> findBySchoolIdAndRoleId(Long schoolId, int roleId, Pageable pageable);
}
