package br.com.aprendeai.service.impl;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.aprendeai.dtos.RedefinirSenhaDto;
import br.com.aprendeai.model.PasswordResetToken;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.PasswordResetTokenRepository;
import br.com.aprendeai.repository.UsuarioRepository;
import br.com.aprendeai.service.EmailSender;
import br.com.aprendeai.service.RedefinicaoSenha;
import br.com.aprendeai.util.OtpGenerator;

@Service
public class PasswordResetServiceImpl implements RedefinicaoSenha {

    private final UsuarioRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailSender emailService;
    private final PasswordEncoder passwordEncoder;

    private static final int EXPIRATION_MINUTES = 15;

    public PasswordResetServiceImpl(UsuarioRepository userRepository,
                                    PasswordResetTokenRepository tokenRepository,
                                    EmailSender emailService,
                                    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void solicitarRedefinicao(String email) {
        Usuario user = encontrarPeloLogin(email);

        String otp = OtpGenerator.generateOtp();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);

        // Remove tokens antigos do usuário
        tokenRepository.deleteByUser(user);

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(otp);
        token.setUser(user);
        token.setExpiryDate(expiryDate);
        tokenRepository.save(token);

        String subject = "Código de Redefinição de Senha";
        String content = "Seu código é: " + otp + ". Ele expira em " + EXPIRATION_MINUTES + " minutos.";

        emailService.sendEmail(user.getLogin(), subject, content);
    }

    @Override
    public boolean validarCodigo(String email, String codigo) {
        Usuario user = encontrarPeloLogin(email);

        PasswordResetToken token = tokenRepository.findByUserAndToken(user, codigo);

        if (token == null || token.isExpired()) {
            return false;
        }

        return true;
    }

    @Override
    public boolean redefinirSenha(RedefinirSenhaDto dto) {
        Usuario user = encontrarPeloLogin(dto.email());

        user.setSenha(passwordEncoder.encode(dto.novaSenha()));
        userRepository.save(user);
        tokenRepository.deleteByUser(user);

        return true;
    }
    
    public Usuario encontrarPeloLogin(String login) {
    	return userRepository.findByLogin(login);
    }
}
