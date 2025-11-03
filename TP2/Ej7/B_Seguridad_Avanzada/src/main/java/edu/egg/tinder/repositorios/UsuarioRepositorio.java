package edu.egg.tinder.repositorios;

import edu.egg.tinder.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    @Query(value = "SELECT * FROM usuario u WHERE u.mail = :mail", nativeQuery = true)
    public Usuario findByMail(@Param("mail") String mail);

    @Query(value = "SELECT * FROM usuario u WHERE u.mail = :mail AND u.password = :password", nativeQuery = true)
    public Usuario findByMailAndPassword(@Param("mail") String mail, @Param("password") String password);

    @Query(value = "SELECT * FROM usuario u WHERE u.mail = :mail", nativeQuery = true)
    public Usuario findByEmail(@Param("mail") String mail);
}
