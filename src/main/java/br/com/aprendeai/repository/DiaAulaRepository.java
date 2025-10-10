package br.com.aprendeai.repository;

import br.com.aprendeai.model.DiaAula;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface DiaAulaRepository extends JpaRepository<DiaAula, Long> {
    Optional<DiaAula> findByTurmaIdAndDataAula(Long turmaId, LocalDate dataAula);
}
