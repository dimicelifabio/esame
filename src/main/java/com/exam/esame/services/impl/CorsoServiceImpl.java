package com.exam.esame.services.impl;

import com.exam.esame.dto.CorsoDTO;
import com.exam.esame.entity.Corso;
import com.exam.esame.entity.ProvaEsame;
import com.exam.esame.exceptions.DuplicateResourceException;
import com.exam.esame.exceptions.ResourceNotFoundException;
import com.exam.esame.mappers.CorsoMapper;
import com.exam.esame.repository.CorsoRepository;
import com.exam.esame.repository.ProvaEsameRepository;
import com.exam.esame.services.CorsoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CorsoServiceImpl implements CorsoService {

    private final CorsoRepository corsoRepository;
    private final ProvaEsameRepository provaEsameRepository;

    @Autowired
    public CorsoServiceImpl(CorsoRepository corsoRepository, ProvaEsameRepository provaEsameRepository) {
        this.corsoRepository = corsoRepository;
        this.provaEsameRepository = provaEsameRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CorsoDTO> getAllCorsi() {
        return corsoRepository.findAll().stream()
                .map(CorsoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CorsoDTO getCorsoById(Long id) {
        Corso corso = corsoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato con ID: " + id));
        return CorsoMapper.toDTO(corso);
    }

    @Override
    @Transactional
    public CorsoDTO createCorso(CorsoDTO corsoDTO) {
        if (corsoRepository.findByNome(corsoDTO.getNome()) != null) {
            throw new DuplicateResourceException("Un corso con nome '" + corsoDTO.getNome() + "' esiste già.");
        }
        Corso corso = CorsoMapper.toEntity(corsoDTO);
        corso.setId(null); // Assicura che sia una nuova entità
        return CorsoMapper.toDTO(corsoRepository.save(corso));
    }

    @Override
    @Transactional
    public CorsoDTO updateCorso(Long id, CorsoDTO corsoDTO) {
        Corso existingCorso = corsoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato con ID: " + id));

        Corso corsoConStessoNome = corsoRepository.findByNome(corsoDTO.getNome());
        if (corsoConStessoNome != null && !corsoConStessoNome.getId().equals(id)) {
            throw new DuplicateResourceException("Un altro corso con nome '" + corsoDTO.getNome() + "' esiste già.");
        }

        existingCorso.setNome(corsoDTO.getNome());
        existingCorso.setDescrizione(corsoDTO.getDescrizione());
        return CorsoMapper.toDTO(corsoRepository.save(existingCorso));
    }

    @Override
    @Transactional
    public void deleteCorso(Long id) {
        Corso corso = corsoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato con ID: " + id));
        // Prima elimina le prove d'esame associate
        List<ProvaEsame> proveAssociate = provaEsameRepository.findByCorso(corso);
        provaEsameRepository.deleteAll(proveAssociate);
        corsoRepository.delete(corso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CorsoDTO> searchCorsiByNome(String nome) {
        // Implementazione di esempio, potrebbe essere necessario un metodo custom nel repository
        return corsoRepository.findAll().stream()
                .filter(c -> c.getNome().toLowerCase().contains(nome.toLowerCase()))
                .map(CorsoMapper::toDTO)
                .collect(Collectors.toList());
    }
}