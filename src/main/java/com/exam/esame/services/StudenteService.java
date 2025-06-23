package com.exam.esame.services;

import com.exam.esame.dto.StudenteDTO;
import java.util.List;

public interface StudenteService {
    List<StudenteDTO> getAllStudenti();
    StudenteDTO getStudenteById(Long id);
    StudenteDTO createStudente(StudenteDTO studenteDTO);
    StudenteDTO updateStudente(Long id, StudenteDTO studenteDTO);
    void deleteStudente(Long id);
}