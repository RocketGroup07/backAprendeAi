package br.com.aprendeai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.aprendeai.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

}
