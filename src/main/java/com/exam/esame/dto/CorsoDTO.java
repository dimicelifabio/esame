package com.exam.esame.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorsoDTO {
    private Long id;
    private String nome;
    private String descrizione;
}