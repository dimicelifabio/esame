package com.exam.esame.services.impl;

import com.exam.esame.dto.ProvaEsameDTO;
import com.exam.esame.dto.StatoStudenteInfoDTO;
import com.exam.esame.entity.Corso;
import com.exam.esame.entity.ProvaEsame;
import com.exam.esame.entity.Studente;
import com.exam.esame.exceptions.ResourceNotFoundException;
import com.exam.esame.exceptions.ValidationException;
import com.exam.esame.mappers.ProvaEsameMapper;
import com.exam.esame.repository.CorsoRepository;
import com.exam.esame.repository.ProvaEsameRepository;
import com.exam.esame.repository.StudenteRepository;
import com.exam.esame.services.ProvaEsameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProvaEsameServiceImpl implements ProvaEsameService {

    private final ProvaEsameRepository provaEsameRepository;
    private final StudenteRepository studenteRepository;
    private final CorsoRepository corsoRepository;

    @Autowired
    public ProvaEsameServiceImpl(ProvaEsameRepository provaEsameRepository,
                                 StudenteRepository studenteRepository,
                                 CorsoRepository corsoRepository) {
        this.provaEsameRepository = provaEsameRepository;
        this.studenteRepository = studenteRepository;
        this.corsoRepository = corsoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProvaEsameDTO> getAllProveEsami() {
        return provaEsameRepository.findAll().stream()
                .map(ProvaEsameMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProvaEsameDTO getProvaEsameById(Long id) {
        ProvaEsame provaEsame = provaEsameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prova Esame non trovata con ID: " + id));
        return ProvaEsameMapper.toDTO(provaEsame);
    }

    @Override
    @Transactional
    public ProvaEsameDTO registerOrUpdateProvaEsame(ProvaEsameDTO provaEsameDTO) {
        if (provaEsameDTO.getStudenteId() == null || provaEsameDTO.getCorsoId() == null) {
            throw new ValidationException("ID Studente e ID Corso non possono essere nulli.");
        }
        if (provaEsameDTO.getVoto() == null || provaEsameDTO.getVoto() < 0 || provaEsameDTO.getVoto() > 30) {
            throw new ValidationException("Il voto deve essere compreso tra 0 e 30.");
        }

        Studente studente = studenteRepository.findById(provaEsameDTO.getStudenteId())
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato con ID: " + provaEsameDTO.getStudenteId()));
        Corso corso = corsoRepository.findById(provaEsameDTO.getCorsoId())
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato con ID: " + provaEsameDTO.getCorsoId()));

        ProvaEsame provaEsame = provaEsameRepository.findByStudenteAndCorso(studente, corso);
        if (provaEsame == null) {
            provaEsame = new ProvaEsame();
            provaEsame.setStudente(studente);
            provaEsame.setCorso(corso);
        }
        provaEsame.setVoto(provaEsameDTO.getVoto());

        return ProvaEsameMapper.toDTO(provaEsameRepository.save(provaEsame));
    }

    @Override
    @Transactional
    public void deleteProvaEsame(Long id) {
        if (!provaEsameRepository.existsById(id)) {
            throw new ResourceNotFoundException("Prova Esame non trovata con ID: " + id);
        }
        provaEsameRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatoStudenteInfoDTO> getReportStatoStudenti() {
        List<Studente> studenti = studenteRepository.findAll();
        List<Corso> tuttiICorsi = corsoRepository.findAll();
        List<StatoStudenteInfoDTO> report = new ArrayList<>();

        for (Studente studente : studenti) {
            List<ProvaEsame> proveStudente = provaEsameRepository.findByStudente(studente);
            double mediaVoti = proveStudente.stream()
                    .filter(p -> p.getVoto() != null && p.getVoto() >= 18)
                    .mapToInt(ProvaEsame::getVoto)
                    .average().orElse(0.0);

            List<String> corsiSuperatiNomi = proveStudente.stream()
                    .filter(p -> p.getVoto() != null && p.getVoto() >= 18)
                    .map(p -> p.getCorso().getNome())
                    .distinct().collect(Collectors.toList());

            List<String> corsiMancantiNomi = tuttiICorsi.stream()
                    .map(Corso::getNome)
                    .filter(nomeCorso -> !corsiSuperatiNomi.contains(nomeCorso))
                    .collect(Collectors.toList());

            report.add(new StatoStudenteInfoDTO(
                    studente.getId(),
                    studente.getNome() + " " + studente.getCognome(),
                    studente.getMatricola(),
                    mediaVoti,
                    corsiMancantiNomi
            ));
        }
        return report;
    }
}