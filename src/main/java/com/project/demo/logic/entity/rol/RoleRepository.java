package com.project.demo.logic.entity.rol;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Role.
 * Permite consultar roles por nombre.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    /**
     * Busca un rol por su nombre.
     * @param name nombre del rol
     * @return rol encontrado (opcional)
     */
    Optional<Role> findByName(RoleEnum name);
}
