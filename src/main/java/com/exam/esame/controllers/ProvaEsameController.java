package com.exam.esame.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.exam.esame.entity.Corso;
import com.exam.esame.entity.ProvaEsame;
import com.exam.esame.entity.Studente;
import com.exam.esame.repository.CorsoRepository;
import com.exam.esame.repository.ProvaEsameRepository;
import com.exam.esame.repository.StudenteRepository;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
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
            ProvaEsame provaEsistente = provaEsameRepository.findByStudenteAndCorso(studente, corso);
            if (provaEsistente != null) {
                provaEsistente.setVoto(nuovaProva.getVoto());
                provaEsameRepository.save(provaEsistente);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Successo", "Prova d'esame aggiornata."));
            } else {
                nuovaProva.setStudente(studente);
                nuovaProva.setCorso(corso);
                provaEsameRepository.save(nuovaProva);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Successo", "Prova d'esame registrata."));
            }
            nuovaProva = new ProvaEsame(); // Resetta per nuovo inserimento
            studenteId = null; // Resetta selezione studente
            corsoId = null; // Resetta selezione corso
            caricaProveEsami(); // Ricarica la lista delle prove
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", "Studente o corso non validi."));
        }
    }

    public void aggiornaDatiProve() {
        caricaProveEsami();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aggiornamento",
                "Dati della pagina Prove d'Esame aggiornati."));
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

            double mediaVoti = proveStudente.stream()
                    .filter(p -> p.getVoto() != null && p.getVoto() >= 18)
                    .mapToInt(ProvaEsame::getVoto)
                    .average()
                    .orElse(0);

            List<Corso> corsiSuperatiConVotoValido = proveStudente.stream()
                    .filter(p -> p.getVoto() != null && p.getVoto() >= 18)
                    .map(ProvaEsame::getCorso)
                    .distinct()
                    .collect(Collectors.toList());

            List<Corso> corsiNonSuperati = new ArrayList<>(corsi);
            corsiNonSuperati.removeAll(corsiSuperatiConVotoValido);

            Map<String, Object> statoStudente = new HashMap<>();
            statoStudente.put("studente", studente);
            statoStudente.put("mediaVoti", mediaVoti);
            statoStudente.put("corsiNonSuperati", corsiNonSuperati);

            risultati.add(statoStudente);
        }

        return risultati;
    }
}