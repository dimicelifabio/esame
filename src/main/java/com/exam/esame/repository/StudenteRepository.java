package com.exam.esame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exam.esame.entity.Studente;

@Repository
public interface StudenteRepository extends JpaRepository<Studente, Long> {
    Studente findByMatricola(String matricola);
}