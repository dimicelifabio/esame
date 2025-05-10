package com.exam.esame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exam.esame.model.Corso;

@Repository
public interface CorsoRepository extends JpaRepository<Corso, Long> {
    Corso findByNome(String nome);
}