package com.exam.esame.services.impl;

import com.exam.esame.dto.StudenteDTO;
import com.exam.esame.entity.Studente;
import com.exam.esame.entity.ProvaEsame;
import com.exam.esame.exceptions.DuplicateResourceException;
import com.exam.esame.exceptions.ResourceNotFoundException;
import com.exam.esame.mappers.StudenteMapper;
import com.exam.esame.repository.StudenteRepository;
import com.exam.esame.services.StudenteService;
import com.exam.esame.repository.ProvaEsameRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudenteServiceImpl implements StudenteService {

    private final StudenteRepository studenteRepository;
    private final ProvaEsameRepository provaEsameRepository;

    @Autowired
    public StudenteServiceImpl(StudenteRepository studenteRepository, ProvaEsameRepository provaEsameRepository) {
        this.studenteRepository = studenteRepository;
        this.provaEsameRepository = provaEsameRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudenteDTO> getAllStudenti() {
        return studenteRepository.findAll().stream()
                .map(StudenteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudenteDTO getStudenteById(Long id) {
        Studente studente = studenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato con ID: " + id));
        return StudenteMapper.toDTO(studente);
    }

    @Override
    @Transactional
    public StudenteDTO createStudente(StudenteDTO studenteDTO) {
        if (studenteRepository.findByMatricola(studenteDTO.getMatricola()) != null) {
            throw new DuplicateResourceException("Uno studente con matricola '" + studenteDTO.getMatricola() + "' esiste già.");
        }
        Studente studente = StudenteMapper.toEntity(studenteDTO);
        studente.setId(null); // Assicura che sia una nuova entità
        return StudenteMapper.toDTO(studenteRepository.save(studente));
    }

    @Override
    @Transactional
    public StudenteDTO updateStudente(Long id, StudenteDTO studenteDTO) {
        Studente existingStudente = studenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato con ID: " + id));

        Studente studenteConStessaMatricola = studenteRepository.findByMatricola(studenteDTO.getMatricola());
        if (studenteConStessaMatricola != null && !studenteConStessaMatricola.getId().equals(id)) {
            throw new DuplicateResourceException("Un altro studente con matricola '" + studenteDTO.getMatricola() + "' esiste già.");
        }

        existingStudente.setNome(studenteDTO.getNome());
        existingStudente.setCognome(studenteDTO.getCognome());
        existingStudente.setMatricola(studenteDTO.getMatricola());
        return StudenteMapper.toDTO(studenteRepository.save(existingStudente));
    }

    @Override
    @Transactional
    public void deleteStudente(Long id) {
        Studente studente = studenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato con ID: " + id));
        List<ProvaEsame> proveAssociate = provaEsameRepository.findByStudente(studente);
        provaEsameRepository.deleteAll(proveAssociate);
        studenteRepository.delete(studente);
    }
}