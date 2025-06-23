package com.exam.esame.services;

import com.exam.esame.dto.CorsoDTO;
import java.util.List;

public interface CorsoService {
    List<CorsoDTO> getAllCorsi();
    CorsoDTO getCorsoById(Long id);
    CorsoDTO createCorso(CorsoDTO corsoDTO);
    CorsoDTO updateCorso(Long id, CorsoDTO corsoDTO);
    void deleteCorso(Long id);
    List<CorsoDTO> searchCorsiByNome(String nome); // Esempio di metodo aggiuntivo
}