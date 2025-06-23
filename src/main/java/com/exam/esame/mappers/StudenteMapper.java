package com.exam.esame.mappers;

import com.exam.esame.dto.StudenteDTO;
import com.exam.esame.entity.Studente;

public class StudenteMapper {

    public static StudenteDTO toDTO(Studente studente) {
        if (studente == null) {
            return null;
        }
        return new StudenteDTO(studente.getId(), studente.getNome(), studente.getCognome(), studente.getMatricola());
    }

    public static Studente toEntity(StudenteDTO studenteDTO) {
        if (studenteDTO == null) {
            return null;
        }
        Studente studente = new Studente();
        studente.setId(studenteDTO.getId()); // Importante per gli aggiornamenti
        studente.setNome(studenteDTO.getNome());
        studente.setCognome(studenteDTO.getCognome());
        studente.setMatricola(studenteDTO.getMatricola());
        return studente;
    }
}