package com.exam.esame.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.exam.esame.dto.CorsoDTO;
import com.exam.esame.exceptions.DuplicateResourceException;
import com.exam.esame.exceptions.ResourceNotFoundException;
import com.exam.esame.services.CorsoService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped; 


@Named
@SessionScoped 
public class CorsoController {

    @Autowired
    private CorsoService corsoService;
    
    private List<CorsoDTO> corsi;
    private CorsoDTO nuovoCorso = new CorsoDTO();
    private CorsoDTO corsoSelezionato = new CorsoDTO(); 
    
    @PostConstruct
    public void init() {
        caricaCorsi();
    }
    
    private void caricaCorsi() {
        corsi = corsoService.getAllCorsi();
    }
    
    public void salvaCorso() {
        try {
            corsoService.createCorso(nuovoCorso);
            nuovoCorso = new CorsoDTO(); // Resetta il form
            caricaCorsi();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successo", "Corso creato."));
        } catch (DuplicateResourceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", e.getMessage()));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", "Impossibile salvare il corso."));
        }
    }

    public void selezionaCorsoPerModifica(CorsoDTO corsoDTO) {
        // Clona il DTO per evitare modifiche riflesse nella tabella prima del salvataggio
        // e per assicurarsi che stiamo lavorando con una copia pulita per la dialog.
        this.corsoSelezionato = new CorsoDTO(corsoDTO.getId(), corsoDTO.getNome(), corsoDTO.getDescrizione());
    }
    
    public void aggiornaCorso() {
        if (corsoSelezionato != null && corsoSelezionato.getId() != null) {
            try {
                corsoService.updateCorso(corsoSelezionato.getId(), corsoSelezionato);
                caricaCorsi();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successo", "Corso aggiornato."));
                corsoSelezionato = new CorsoDTO(); // Resetta l'oggetto per la prossima selezione/modifica
            } catch (ResourceNotFoundException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", e.getMessage()));
            } catch (DuplicateResourceException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore di Duplicazione", e.getMessage()));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", "Impossibile aggiornare il corso."));
            }
        }
    }

    public void eliminaCorso(Long id) {
        try {
            corsoService.deleteCorso(id);
            caricaCorsi();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successo", "Corso eliminato."));
        } catch (ResourceNotFoundException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", e.getMessage()));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", "Impossibile eliminare il corso."));
        }
    }

    /**
     * Metodo helper per fornire una nuova istanza di CorsoDTO per il form di creazione.
     * Utile per resettare il form nella dialog.
     * @return Un nuovo CorsoDTO vuoto.
     */
    public CorsoDTO newCorsoDTO() {
        return new CorsoDTO();
    }

    // Getters e Setters per i campi usati nella vista JSF
    public List<CorsoDTO> getCorsi() {
        return corsi;
    }

    public void setCorsi(List<CorsoDTO> corsi) {
        this.corsi = corsi;
    }

    public CorsoDTO getNuovoCorso() {
        return nuovoCorso;
    }

    public void setNuovoCorso(CorsoDTO nuovoCorso) {
        this.nuovoCorso = nuovoCorso;
    }

    public CorsoDTO getCorsoSelezionato() {
        return corsoSelezionato;
    }

    public void setCorsoSelezionato(CorsoDTO corsoSelezionato) {
        this.corsoSelezionato = corsoSelezionato;
    }
}