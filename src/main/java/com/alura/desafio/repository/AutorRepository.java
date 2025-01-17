package com.alura.desafio.repository;

import com.alura.desafio.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE nombre ILIKE %:texto%")
    Optional<Autor> findByNombre(String texto);

    @Query("SELECT a FROM Autor a WHERE fechaDeNacimiento < :anio AND fechaDeFallecimiento > :anio")
    List<Autor> buscarAutoresPorYear(int anio);

}
