package br.com.aprendeai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.aprendeai.model.Arquivo;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long>{

}
