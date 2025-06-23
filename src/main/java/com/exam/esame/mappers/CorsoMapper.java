package com.exam.esame.mappers;

import com.exam.esame.dto.CorsoDTO;
import com.exam.esame.entity.Corso;

public class CorsoMapper {

    public static CorsoDTO toDTO(Corso corso) {
        if (corso == null) {
            return null;
        }
        return new CorsoDTO(corso.getId(), corso.getNome(), corso.getDescrizione());
    }

    public static Corso toEntity(CorsoDTO corsoDTO) {
        if (corsoDTO == null) {
            return null;
        }
        Corso corso = new Corso();
        corso.setId(corsoDTO.getId()); // Importante per gli aggiornamenti
        corso.setNome(corsoDTO.getNome());
        corso.setDescrizione(corsoDTO.getDescrizione());
        return corso;
    }
}