package com.exam.esame.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;

import com.exam.esame.model.Corso;
import com.exam.esame.model.ProvaEsame;
import com.exam.esame.model.Studente;
import com.exam.esame.repository.CorsoRepository;
import com.exam.esame.repository.ProvaEsameRepository;
import com.exam.esame.repository.StudenteRepository;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScope
@Getter
@Setter
public class ProvaEsameController {

    @Autowired
    private ProvaEsameRepository provaEsameRepository;
    
    @Autowired
    private StudenteRepository studenteRepository;
    
    @Autowired
    private CorsoRepository corsoRepository;
    
    private List<ProvaEsame> proveEsami;
    private ProvaEsame nuovaProva = new ProvaEsame();
    private Long studenteId;
    private Long corsoId;
    
    @PostConstruct
    public void init() {
        caricaProveEsami();
    }
    
    public void caricaProveEsami() {
        proveEsami = provaEsameRepository.findAll();
    }
    
    public void salvaProvaEsame() {
        Studente studente = studenteRepository.findById(studenteId).orElse(null);
        Corso corso = corsoRepository.findById(corsoId).orElse(null);
        
        if (studente != null && corso != null) {
            nuovaProva.setStudente(studente);
            nuovaProva.setCorso(corso);
            provaEsameRepository.save(nuovaProva);
            nuovaProva = new ProvaEsame();
            caricaProveEsami();
        }
    }
    
    public List<Studente> getStudenti() {
        return studenteRepository.findAll();
    }
    
    public List<Corso> getCorsi() {
        return corsoRepository.findAll();
    }
    
    public List<Map<String, Object>> getStatoStudenti() {
        List<Studente> studenti = studenteRepository.findAll();
        List<Corso> corsi = corsoRepository.findAll();
        List<Map<String, Object>> risultati = new ArrayList<>();
        
        for (Studente studente : studenti) {
            List<ProvaEsame> proveStudente = provaEsameRepository.findByStudente(studente);
            
            // Calcola media voti
            double mediaVoti = proveStudente.stream()
                    .mapToInt(ProvaEsame::getVoto)
                    .average()
                    .orElse(0);
            
            // Trova corsi non superati
            List<Corso> corsiNonSuperati = new ArrayList<>(corsi);
            
            Map<Corso, Integer> corsiSuperati = proveStudente.stream()
                    .filter(p -> p.getVoto() >= 18)
                    .collect(Collectors.toMap(ProvaEsame::getCorso, ProvaEsame::getVoto, (v1, v2) -> v1));
            
            corsiNonSuperati.removeAll(corsiSuperati.keySet());
            
            Map<String, Object> statoStudente = new HashMap<>();
            statoStudente.put("studente", studente);
            statoStudente.put("mediaVoti", mediaVoti);
            statoStudente.put("corsiNonSuperati", corsiNonSuperati);
            
            risultati.add(statoStudente);
        }
        
        return risultati;
    }
}