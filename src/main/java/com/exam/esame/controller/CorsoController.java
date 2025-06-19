package com.exam.esame.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.exam.esame.entity.ProvaEsame;
import com.exam.esame.entity.Corso;
import com.exam.esame.repository.ProvaEsameRepository;
import com.exam.esame.repository.CorsoRepository;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped; // Importato scope CDI


@Named
@SessionScoped // Modificato in scope CDI
public class CorsoController {

    @Autowired
    private CorsoRepository corsoRepository;

    @Autowired
    private ProvaEsameRepository provaEsameRepository;
    
    private List<Corso> corsi;
    private Corso nuovoCorso = new Corso();
    private Corso corsoSelezionato; 
    
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

    public void selezionaCorsoPerModifica(Corso corso) {
        // Per evitare problemi con entità detached, è buona pratica ricaricare l'entità
        // o clonarla se non si vuole modificare direttamente l'oggetto nella lista.
        // In questo caso, per semplicità, usiamo l'oggetto passato.
        // Considera findById se riscontri problemi di stato dell'entità.
        this.corsoSelezionato = corsoRepository.findById(corso.getId()).orElse(null);
    }
    
    public void aggiornaCorso() {
        if (corsoSelezionato != null) {
            corsoRepository.save(corsoSelezionato);
            caricaCorsi(); // Ricarica la lista per aggiornare la tabella
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successo", "Corso aggiornato."));
            // corsoSelezionato = null; // Opzionale: resetta dopo l'aggiornamento
                                     // Utile se la dialog non viene resettata correttamente dall'update
        }
    }

    public void eliminaCorso(Long id) {
        corsoRepository.findById(id).ifPresent(corsoDaEliminare -> {
            List<ProvaEsame> proveAssociate = provaEsameRepository.findByCorso(corsoDaEliminare);
            provaEsameRepository.deleteAll(proveAssociate);

            corsoRepository.delete(corsoDaEliminare);
            caricaCorsi();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successo", "Corso eliminato."));
        });
    }

    // Getter e Setter espliciti
    public List<Corso> getCorsi() {
        return corsi;
    }

    public void setCorsi(List<Corso> corsi) {
        this.corsi = corsi;
    }

    public Corso getNuovoCorso() {
        return nuovoCorso;
    }

    public void setNuovoCorso(Corso nuovoCorso) {
        this.nuovoCorso = nuovoCorso;
    }

    // Getter e Setter per corsoSelezionato
    public Corso getCorsoSelezionato() {
        return corsoSelezionato;
    }

    public void setCorsoSelezionato(Corso corsoSelezionato) {
        this.corsoSelezionato = corsoSelezionato;
    }
}