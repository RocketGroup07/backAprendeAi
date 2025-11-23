package br.com.aprendeai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.aprendeai.model.EntregaAtividade;

public interface EntregaAtividadeRepository extends JpaRepository<EntregaAtividade, Long>{
	
	List<EntregaAtividade> findByAtividade_TurmaIdAndEntregueTrue(Long turmaId);
    
    Optional<EntregaAtividade> findByAtividadeIdAndAlunoId(Long atividadeId, Long alunoId);
    
    List<EntregaAtividade> findByAtividadeId(Long atividadeId);

    List<EntregaAtividade> findByAtividadeIdAndEntregueTrue(Long atividadeId);

    List<EntregaAtividade> findByAlunoId(Long alunoId);

}
