package com.exam.esame.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatoStudenteInfoDTO {
    private Long studenteId;
    private String nomeCompletoStudente;
    private String matricolaStudente;
    private double mediaVoti;
    private List<String> corsiMancantiNomi;
}