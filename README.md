# ExamTrack - Sistema di Gestione Esami

## Panoramica

ExamTrack è un'applicazione web per la gestione delle prove d'esame degli studenti universitari. Permette di gestire l'anagrafica degli studenti, dei corsi e delle prove d'esame, offrendo anche funzionalità di reportistica sullo stato degli studenti.

## Architettura del Progetto

L'applicazione è strutturata secondo un'architettura a strati che garantisce una chiara separazione delle responsabilità:

### 1. Strato di Presentazione (Controllers)

I controller JSF gestiscono l'interazione con l'utente e la presentazione dei dati. Sono annotati con `@Named` e `@SessionScoped` per mantenere lo stato durante la sessione dell'utente.

@Named
@SessionScoped
public class StudenteController {
    // Gestisce le operazioni relative agli studenti nell'interfaccia utente
}

### 2. Strato di Servizio (Services)
I servizi implementano la logica di business dell'applicazione. Sono annotati con @Service e utilizzano @Transactional per garantire l'integrità dei dati.

@Service
public class StudenteServiceImpl implements StudenteService {
    // Implementa la logica di business per gli studenti
}

### Strato di Accesso ai Dati (Repositories)
I repository estendono JpaRepository per fornire operazioni CRUD sulle entità e query personalizzate.

@Repository
public interface StudenteRepository extends JpaRepository<Studente, Long> {
    Studente findByMatricola(String matricola);
}

### 4. Strato di Trasferimento Dati (DTOs)
I DTO (Data Transfer Objects) sono utilizzati per trasferire dati tra i vari strati dell'applicazione, evitando l'esposizione diretta delle entità.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudenteDTO {
    private Long id;
    private String nome;
    private String cognome;
    private String matricola;
}

### 5. Strato di Entità (Entities)
Le entità JPA rappresentano le tabelle del database e sono annotate con @Entity.

@Entity
@Table(name = "studenti")
@Data
public class Studente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Altri campi
}

### 6. Mapper
I mapper convertono le entità in DTO e viceversa, facilitando il trasferimento dei dati tra i vari strati.

public class StudenteMapper {
    public static StudenteDTO toDTO(Studente studente) {
        // Converte un'entità Studente in un DTO
    }
    
    public static Studente toEntity(StudenteDTO dto) {
        // Converte un DTO in un'entità Studente
    }
}

### 7. Gestione delle Eccezioni
Le eccezioni personalizzate gestiscono gli errori in modo centralizzato e forniscono messaggi chiari all'utente.

public class ResourceNotFoundException extends RuntimeException {
    // Gestisce il caso in cui una risorsa non viene trovata
}

public class DuplicateResourceException extends RuntimeException {
    // Gestisce il caso in cui si tenta di creare una risorsa duplicata
}

public class ValidationException extends RuntimeException {
    // Gestisce errori di validazione dei dati
}

## Funzionalità dell'Applicazione Web
L'applicazione offre le seguenti funzionalità principali:

1. Gestione Studenti :
   
   - Visualizzazione della lista degli studenti
   - Inserimento di nuovi studenti
   - Modifica dei dati degli studenti esistenti
   - Eliminazione degli studenti
2. Gestione Corsi :
   
   - Visualizzazione della lista dei corsi
   - Inserimento di nuovi corsi
   - Modifica dei dati dei corsi esistenti
   - Eliminazione dei corsi
3. Gestione Prove d'Esame :
   
   - Registrazione di nuove prove d'esame (studente, corso, voto)
   - Visualizzazione delle prove d'esame registrate
   - Aggiornamento automatico delle prove esistenti
4. Reportistica :
   
   - Visualizzazione dello stato degli studenti
   - Calcolo della media dei voti per ogni studente
   - Elenco dei corsi ancora da superare per ogni studente