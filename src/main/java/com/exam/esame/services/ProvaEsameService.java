package com.exam.esame.services;

import com.exam.esame.dto.ProvaEsameDTO;
import com.exam.esame.dto.StatoStudenteInfoDTO;
import java.util.List;

public interface ProvaEsameService {
    List<ProvaEsameDTO> getAllProveEsami();
    ProvaEsameDTO getProvaEsameById(Long id);
    ProvaEsameDTO registerOrUpdateProvaEsame(ProvaEsameDTO provaEsameDTO);
    void deleteProvaEsame(Long id); // Potrebbe non essere necessario in base ai requisiti
    List<StatoStudenteInfoDTO> getReportStatoStudenti();
}