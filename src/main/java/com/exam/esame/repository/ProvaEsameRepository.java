package com.exam.esame.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exam.esame.entity.Corso;
import com.exam.esame.entity.ProvaEsame;
import com.exam.esame.entity.Studente;

@Repository
public interface ProvaEsameRepository extends JpaRepository<ProvaEsame, Long> {
    List<ProvaEsame> findByStudente(Studente studente);
    List<ProvaEsame> findByCorso(Corso corso);
    ProvaEsame findByStudenteAndCorso(Studente studente, Corso corso);
}