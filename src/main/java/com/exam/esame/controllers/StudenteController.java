package com.exam.esame.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.exam.esame.dto.StudenteDTO;
import com.exam.esame.exceptions.DuplicateResourceException;
import com.exam.esame.exceptions.ResourceNotFoundException;
import com.exam.esame.services.StudenteService;

// import com.exam.esame.repository.ProvaEsameRepository; // Se non più usato direttamente
// import com.exam.esame.repository.StudenteRepository; // Se non più usato direttamente
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import org.primefaces.PrimeFaces; // Import aggiunto
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
public class StudenteController {

    @Autowired
    private StudenteService studenteService;
    
    private List<StudenteDTO> studenti;
    private StudenteDTO nuovoStudente = new StudenteDTO();
    private StudenteDTO studenteSelezionato = new StudenteDTO();
    
    @PostConstruct
    public void init() {
        caricaStudenti();
    }
    
    private void caricaStudenti() {
        studenti = studenteService.getAllStudenti();
    }
    
    public void salvaStudente() {
        boolean saved = false; // Flag per indicare se il salvataggio ha avuto successo
        try {
            studenteService.createStudente(nuovoStudente);
            nuovoStudente = new StudenteDTO(); // Resetta il form
            caricaStudenti();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successo", "Studente creato."));
            saved = true;
        } catch (DuplicateResourceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", e.getMessage()));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Errore Grave", "Impossibile salvare lo studente."));
            e.printStackTrace(); // Stampa lo stack trace per debug
        }
        // Imposta il parametro di callback per la chiusura della dialog
        PrimeFaces.current().ajax().addCallbackParam("saved", saved);
    }
    
    public void selezionaStudentePerModifica(StudenteDTO studenteDTO) {
        // Clona il DTO per evitare modifiche riflesse nella tabella prima del salvataggio
        // Questo assicura che la dialog lavori su una copia e non sull'oggetto della lista.
        this.studenteSelezionato = new StudenteDTO(studenteDTO.getId(), studenteDTO.getNome(), studenteDTO.getCognome(), studenteDTO.getMatricola());
    }
    
    public void aggiornaStudente() {
        boolean updated = false; // Flag per indicare se l'aggiornamento ha avuto successo
        if (studenteSelezionato != null && studenteSelezionato.getId() != null) {
            try {
                studenteService.updateStudente(studenteSelezionato.getId(), studenteSelezionato);
                caricaStudenti();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successo", "Studente aggiornato."));
                studenteSelezionato = new StudenteDTO(); // Resetta
                updated = true;
            } catch (ResourceNotFoundException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", e.getMessage()));
            } catch (DuplicateResourceException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore di Duplicazione", e.getMessage()));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Errore Grave", "Impossibile aggiornare lo studente."));
                e.printStackTrace(); // Stampa lo stack trace per debug
            }
        }
        // Imposta il parametro di callback per la chiusura della dialog
        PrimeFaces.current().ajax().addCallbackParam("updated", updated);
    }
    
    public void eliminaStudente(Long id) {
        try {
            studenteService.deleteStudente(id);
            caricaStudenti();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successo", "Studente eliminato."));
        } catch (ResourceNotFoundException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", e.getMessage()));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Errore Grave", "Impossibile eliminare lo studente."));
            e.printStackTrace(); // Stampa lo stack trace per debug
        }
    }

    /**
     * Prepara un nuovo oggetto StudenteDTO per la dialog di creazione.
     * Chiamato dal pulsante "Nuovo Studente".
     */
    public void prepareNewStudente() {
        this.nuovoStudente = new StudenteDTO();
    }
}