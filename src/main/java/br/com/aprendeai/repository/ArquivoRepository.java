package br.com.aprendeai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.aprendeai.model.Arquivo;

@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Long>{
	
//	List<Arquivo> findByPost_TurmaIdOrAtividade_TurmaId(Long turmaIdPost, Long turmaIdAtividade);


}
