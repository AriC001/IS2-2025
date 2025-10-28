package com.is.biblioteca.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.is.biblioteca.business.domain.entity.Libro;

public interface LibroRepository extends JpaRepository<Libro, String>{

	@Query("SELECT l From Libro l WHERE l.titulo = :titulo AND l.eliminado = FALSE")
    public Libro buscarLibroPorTitulo(@Param("titulo") String titulo);

    @Query("SELECT l FROM Libro l WHERE l.autor.id = :id AND l.eliminado = FALSE")
    public List<Libro> listarLibroPorAutor(@Param("id") String id);

    @Query("SELECT l FROM Libro l WHERE l.editorial.nombre = :nombre AND l.eliminado = FALSE")
    public List<Libro> listarLibroPorEditorial(@Param("nombre") String nombre);

    @Query("SELECT l FROM Libro l WHERE l.isbn= :isbn AND l.eliminado = FALSE")
    public Libro buscarLibroPorIsbn(@Param("isbn") Long isbn);


    public List<Libro> findAllByAnio(@Param ("anio") Integer anio);

    @Query("SELECT l From Libro l WHERE l.autor.id= :idAutor AND l.editorial.id = :idEditorial AND l.anio= :anio AND l.eliminado = FALSE")
    public List<Libro> listarLibroPorAutorYEditorialYAnio(@Param("idAutor") String idAutor, @Param("idEditorial") String idEditorial, @Param("anio") Integer anio);

    @Query("SELECT l From Libro l WHERE l.titulo= :titulo AND l.autor.id= :idAutor AND l.editorial.id = :idEditorial AND l.eliminado = FALSE")
    public Libro buscarLibroPorTituloAutorEditorial(@Param("titulo") String titulo, @Param("idAutor") String idAutor, @Param("idEditorial") String idEditorial);

    @Query("SELECT l From Libro l WHERE (l.titulo LIKE %:filtro% OR l.autor.nombre LIKE %:filtro% OR l.editorial.nombre LIKE %:filtro%) AND l.eliminado = FALSE")
    public List<Libro> listarLibroPorFiltro(@Param("filtro") String filtro);

    @Query("SELECT l FROM Libro l WHERE l.eliminado = false ")
    public List<Libro> findAllActive();
}
