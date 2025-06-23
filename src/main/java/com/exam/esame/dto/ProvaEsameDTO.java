package com.exam.esame.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvaEsameDTO {
    private Long id;
    private Long studenteId; // O StudenteDTO studente;
    private Long corsoId;   // O CorsoDTO corso;
    private String nomeStudente; // Denormalizzato per la vista
    private String nomeCorso;    // Denormalizzato per la vista
    private Integer voto;
}