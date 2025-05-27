package com.exam.esame.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.exam.esame.model.Corso;
import com.exam.esame.model.ProvaEsame;
import com.exam.esame.model.Studente;
import com.exam.esame.repository.CorsoRepository;
import com.exam.esame.repository.ProvaEsameRepository;
import com.exam.esame.repository.StudenteRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(StudenteRepository studenteRepository, 
                                     CorsoRepository corsoRepository,
                                     ProvaEsameRepository provaEsameRepository) {
        return args -> {
            // Creazione studenti di esempio
            Studente studente1 = new Studente(null, "Mario", "Rossi", "MAT001");
            Studente studente2 = new Studente(null, "Laura", "Bianchi", "MAT002");
            studenteRepository.save(studente1);
            studenteRepository.save(studente2);
            
            // Creazione corsi di esempio
            Corso corso1 = new Corso(null, "Programmazione Java", "Corso base di Java");
            Corso corso2 = new Corso(null, "Database", "Introduzione ai database relazionali");
            Corso corso3 = new Corso(null, "Web Development", "Sviluppo di applicazioni web");
            corsoRepository.save(corso1);
            corsoRepository.save(corso2);
            corsoRepository.save(corso3);
            
            // Creazione prove d'esame di esempio
            ProvaEsame prova1 = new ProvaEsame(null, studente1, corso1, 28);
            ProvaEsame prova2 = new ProvaEsame(null, studente1, corso2, 25);
            ProvaEsame prova3 = new ProvaEsame(null, studente2, corso1, 30);
            ProvaEsame prova4 = new ProvaEsame(null, studente2, corso3, 15); // Non superato
            provaEsameRepository.save(prova1);
            provaEsameRepository.save(prova2);
            provaEsameRepository.save(prova3);
            provaEsameRepository.save(prova4);
        };
    }
}