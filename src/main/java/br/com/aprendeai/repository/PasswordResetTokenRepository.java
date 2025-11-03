package br.com.aprendeai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.aprendeai.model.PasswordResetToken;
import br.com.aprendeai.model.Usuario;
import jakarta.transaction.Transactional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
	
	public PasswordResetToken findByToken(String token);
	
	@Transactional
	public void deleteByUser(Usuario user);

	PasswordResetToken findByUserAndToken(Usuario user, String codigo);

}
