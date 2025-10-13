package com.example.etemplate.repositories;

import com.example.etemplate.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Usuario, String> {
    // 👇 Podés agregar métodos personalizados si querés, por ejemplo:
    // Optional<Usuario> findByEmail(String email);
    @Query(value = "SELECT u FROM Usuario u WHERE u.email = :email AND u.deleted = false")
    public Usuario findByEmail(@Param("email")String email);
}
