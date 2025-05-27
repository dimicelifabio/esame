package com.exam.esame.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;

import com.exam.esame.model.Studente;
import com.exam.esame.repository.StudenteRepository;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScope
@Getter
@Setter
public class StudenteController {

    @Autowired
    private StudenteRepository studenteRepository;
    
    private List<Studente> studenti;
    private Studente nuovoStudente = new Studente();
    
    @PostConstruct
    public void init() {
        caricaStudenti();
    }
    
    public void caricaStudenti() {
        studenti = studenteRepository.findAll();
    }
    
    public void salvaStudente() {
        studenteRepository.save(nuovoStudente);
        nuovoStudente = new Studente();
        caricaStudenti();
    }
}