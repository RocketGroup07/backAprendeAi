package br.com.aprendeai.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.aprendeai.enums.PapelEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "usuarios")
public class Usuario implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nome", nullable = false)
	private String nome;
	
	@Column(name = "login", nullable = false, unique = true)
	private String login;
	
	@Column(name = "senha", nullable = false)
	private String senha;
	
	@Column(name = "papel", nullable = false)
	@Enumerated(EnumType.STRING)
	private PapelEnum papel;
	 
	@Column(name = "criado_em")
	private LocalDateTime criadoEm;
	
	public Usuario(String nome, String login, String senha, PapelEnum papel) {
		this.nome = nome;
		this.login = login;
		this.senha = senha;
		this.papel = papel;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(this.papel == PapelEnum.ADMIN) {
			return List.of(
					new SimpleGrantedAuthority("ROLE_ADMIN"),
					new SimpleGrantedAuthority("ROLE_USER")
					);
					
		}
		return List.of(
				new SimpleGrantedAuthority("ROLE_USER")
				);
	}

	@Override
	public String getPassword() {
		return this.senha;
	}

	@Override
	public String getUsername() {
		return this.login;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
