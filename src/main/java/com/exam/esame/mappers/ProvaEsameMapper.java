package com.exam.esame.mappers;

import com.exam.esame.dto.ProvaEsameDTO;
import com.exam.esame.entity.ProvaEsame;


public class ProvaEsameMapper {

    public static ProvaEsameDTO toDTO(ProvaEsame provaEsame) {
        if (provaEsame == null) {
            return null;
        }
        return new ProvaEsameDTO(
            provaEsame.getId(),
            provaEsame.getStudente() != null ? provaEsame.getStudente().getId() : null,
            provaEsame.getCorso() != null ? provaEsame.getCorso().getId() : null,
            provaEsame.getStudente() != null ? provaEsame.getStudente().getNome() + " " + provaEsame.getStudente().getCognome() : "N/A",
            provaEsame.getCorso() != null ? provaEsame.getCorso().getNome() : "N/A",
            provaEsame.getVoto()
        );
    }

    // Nota: toEntity da ProvaEsameDTO richiederà di recuperare le entità Studente e Corso
    // dai repository usando studenteId e corsoId. Questo di solito avviene nel Service.
    // Quindi, un metodo toEntity completo qui potrebbe non essere l'ideale senza accesso ai repository.
    // Per ora, creiamo un metodo base che imposta solo il voto, assumendo che Studente e Corso
    // vengano impostati separatamente nel service.
    public static ProvaEsame toPartialEntity(ProvaEsameDTO dto) {
        ProvaEsame entity = new ProvaEsame();
        entity.setVoto(dto.getVoto());
        return entity;
    }
}