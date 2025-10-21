package br.com.aprendeai.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.aprendeai.enums.PapelEnum;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.UsuarioRepository;

@Component
public class AdminInitializer implements CommandLineRunner{

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	
	public AdminInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	 @Override
	    public void run(String... args) {
	        if (!usuarioRepository.existsByLogin("admin.bytes@bytes.com")) {
	            usuarioRepository.save(new Usuario("ADMIN", "admin.bytes@bytes.com", passwordEncoder.encode("@dmin20251"), PapelEnum.ADMIN));
	            System.out.println("Usuário administrador criado com sucesso!");
	        } else {
	            System.out.println("Usuário administrador já existe.");
	        }
	    }
	
}
