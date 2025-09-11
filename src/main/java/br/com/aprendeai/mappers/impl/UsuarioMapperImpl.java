package br.com.aprendeai.mappers.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.aprendeai.dtos.UsuarioCreateDto;
import br.com.aprendeai.dtos.UsuarioResponseDto;
import br.com.aprendeai.dtos.UsuarioUpdateDto;
import br.com.aprendeai.enums.PapelEnum;
import br.com.aprendeai.mappers.UsuarioMapper;
import br.com.aprendeai.model.Usuario;

@Component
public class UsuarioMapperImpl implements UsuarioMapper{

	private final PasswordEncoder passwordEncoder;
    
    public UsuarioMapperImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    public UsuarioResponseDto toResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        return new UsuarioResponseDto(
            usuario.getId(),
            usuario.getNome(),
            usuario.getLogin(),
            usuario.getCriadoEm()
        );
    }
    
    public Usuario toEntityFromCreateDto(UsuarioCreateDto usuarioCreateDto) {
        if (usuarioCreateDto == null) {
            return null;
        }
        
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioCreateDto.nome());
        usuario.setLogin(usuarioCreateDto.login());
        
        if (usuarioCreateDto.senha() != null && !usuarioCreateDto.senha().isBlank()) {
            String senhaCriptografada = passwordEncoder.encode(usuarioCreateDto.senha());
            usuario.setSenha(senhaCriptografada);
        }
        
        usuario.setPapel(PapelEnum.USER);
        
        return usuario;
    }
    
    public Usuario updateEntityFromCreateDto(Usuario usuario, UsuarioCreateDto usuarioCreateDto) {
        if (usuario == null || usuarioCreateDto == null) {
            return usuario;
        }
        
        usuario.setNome(usuarioCreateDto.nome());
        usuario.setLogin(usuarioCreateDto.login());
        
        if (usuarioCreateDto.senha() != null && !usuarioCreateDto.senha().isBlank()) {
            String senhaCriptografada = passwordEncoder.encode(usuarioCreateDto.senha());
            usuario.setSenha(senhaCriptografada);
        }
        
        return usuario;
    }
    
    public Usuario toEntityFromResponseDto(UsuarioResponseDto usuarioResponseDto) {
        if (usuarioResponseDto == null) {
            return null;
        }
        
        Usuario usuario = new Usuario();
        usuario.setId(usuarioResponseDto.id());
        usuario.setNome(usuarioResponseDto.nome());
        usuario.setLogin(usuarioResponseDto.login());
        
        return usuario;
    }
    
    public Usuario updateEntityFromUpdateDto(Usuario usuario, UsuarioUpdateDto usuarioUpdateDto) {
        if (usuario == null || usuarioUpdateDto == null) {
            return usuario;
        }
        
        usuario.setNome(usuarioUpdateDto.nome());
        usuario.setLogin(usuarioUpdateDto.login());
        
        if (usuarioUpdateDto.senha() != null && !usuarioUpdateDto.senha().isBlank()) {
            String senhaCriptografada = passwordEncoder.encode(usuarioUpdateDto.senha());
            usuario.setSenha(senhaCriptografada);
        }
        
        return usuario;
    }
}
