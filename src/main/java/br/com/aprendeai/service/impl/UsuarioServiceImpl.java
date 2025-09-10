//package br.com.aprendeai.service.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import br.com.aprendeai.dtos.UsuarioDto;
//import br.com.aprendeai.model.Usuario;
//import br.com.aprendeai.repository.UsuarioRepository;
//import br.com.aprendeai.service.UsuarioService;
//
//@Service
//public class UsuarioServiceImpl implements UsuarioService{
//	
//	@Autowired
//	private UsuarioRepository usuarioRepository;
//	
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//	
//	@Override
//	public UsuarioDto salvar(UsuarioDto usuarioDto) {
//		
//		Usuario usuarioJaExiste = usuarioRepository.findByLogin(usuarioDto.login());
//				
//		if(usuarioJaExiste != null) {
//			throw new RuntimeException("Usuário já existe!");
//		}
//		
//		var passwordHash = passwordEncoder.encode(usuarioDto.senha());
//		
//		Usuario entity = new Usuario(usuarioDto.nome(), usuarioDto.login(), passwordHash, usuarioDto.papel());
//		
//		Usuario novoUsuario = usuarioRepository.save(entity);
//		
//		return new UsuarioDto(novoUsuario.getNome(), novoUsuario.getLogin(),novoUsuario.getSenha(), novoUsuario.getPapel());
//	}
//
//}
