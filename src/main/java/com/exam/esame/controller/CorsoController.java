package com.exam.esame.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;

import com.exam.esame.model.Corso;
import com.exam.esame.repository.CorsoRepository;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScope
@Getter
@Setter
public class CorsoController {

    @Autowired
    private CorsoRepository corsoRepository;
    
    private List<Corso> corsi;
    private Corso nuovoCorso = new Corso();
    
    @PostConstruct
    public void init() {
        caricaCorsi();
    }
    
    public void caricaCorsi() {
        corsi = corsoRepository.findAll();
    }
    
    public void salvaCorso() {
        corsoRepository.save(nuovoCorso);
        nuovoCorso = new Corso();
        caricaCorsi();
    }
}