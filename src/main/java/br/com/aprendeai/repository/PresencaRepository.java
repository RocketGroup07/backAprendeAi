package br.com.aprendeai.repository;

import br.com.aprendeai.model.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PresencaRepository extends JpaRepository<Presenca, Long> {

    List<Presenca> findByDiaAulaId(Long diaAulaId);

    @Query("SELECT p FROM Presenca p WHERE p.aluno.id = :alunoId AND p.diaAula.turma.id = :turmaId")
    List<Presenca> findByAlunoIdAndTurmaId(@Param("alunoId") Long alunoId, @Param("turmaId") Long turmaId);
}
