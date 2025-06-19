package com.exam.esame.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.context.annotation.SessionScope; // Rimuovere scope Spring
import com.exam.esame.entity.ProvaEsame;

import com.exam.esame.entity.Studente;
import com.exam.esame.repository.ProvaEsameRepository;
import com.exam.esame.repository.StudenteRepository;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.enterprise.context.SessionScoped; // Importare scope CDI
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped // Utilizzare scope CDI
@Getter
@Setter
public class StudenteController {

    @Autowired
    private StudenteRepository studenteRepository;

    @Autowired
    private ProvaEsameRepository provaEsameRepository;
    
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
        studenti = studenteRepository.findAll();
        nuovoStudente = new Studente();
        caricaStudenti();
    }
    
    @Getter @Setter
    private Studente studenteSelezionato; 
    
    public void selezionaStudentePerModifica(Studente studente) {
        this.studenteSelezionato = studente; // O una copia per evitare modifiche dirette prima del salvataggio
    }
    
    public void aggiornaStudente() {
        if (studenteSelezionato != null) {
            studenteRepository.save(studenteSelezionato); // JPA gestisce l'update se l'ID esiste
            caricaStudenti(); // Ricarica la lista
            // Aggiungi messaggio di successo con FacesContext
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successo", "Studente aggiornato."));
        }
    }
    
    public void eliminaStudente(Long id) {
        studenteRepository.findById(id).ifPresent(studenteDaEliminare -> {
            // Elimina prima tutte le prove d'esame associate a questo studente
            List<ProvaEsame> proveAssociate = provaEsameRepository.findByStudente(studenteDaEliminare);
            provaEsameRepository.deleteAll(proveAssociate);

            studenteRepository.delete(studenteDaEliminare);
            caricaStudenti(); // Ricarica la lista
            // Aggiungi messaggio di successo
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successo", "Studente eliminato."));
        });
    }

    public Studente getNuovoStudente() {
    return nuovoStudente;
}

public void setNuovoStudente(Studente nuovoStudente) {
    this.nuovoStudente = nuovoStudente;
}
}