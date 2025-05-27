package com.exam.esame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exam.esame.model.Studente;

@Repository
public interface StudenteRepository extends JpaRepository<Studente, Long> {
    Studente findByMatricola(String matricola);
}