package com.alura.desafio.repository;

import com.alura.desafio.model.Idioma;
import com.alura.desafio.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface LibroRepository extends JpaRepository<Libro,Long> {

    Optional<Libro> findByTituloContainingIgnoreCase(String titulo);

    @Query("SELECT l FROM Libro l WHERE l.autor.id = :autorId")
    List<Libro> findLibrosByAutorId(@Param("autorId") Long autorId);


    List<Libro> findByIdiomas(Idioma idiomas);



}
