//package br.com.aprendeai.service.impl;
//
//import java.time.LocalDateTime;
//
//import br.com.aprendeai.model.PasswordResetToken;
//import br.com.aprendeai.model.Usuario;
//import br.com.aprendeai.repository.PasswordResetTokenRepository;
//import br.com.aprendeai.repository.UsuarioRepository;
//import br.com.aprendeai.util.OtpGenerator;
//
//public class PasswordResetServiceImpl {
//	
//    private UsuarioRepository userRepository;
//    
//    private PasswordResetTokenRepository tokenRepository;
//  
//    private EmailService emailService; 
//
//    public PasswordResetServiceImpl(UsuarioRepository userRepository, PasswordResetTokenRepository tokenRepository,
//			EmailService emailService) {
//		this.userRepository = userRepository;
//		this.tokenRepository = tokenRepository;
//		this.emailService = emailService;
//	}
//
//	private final int EXPIRATION_MINUTES = 15; 
//
//    public void createPasswordResetToken(String login) throws Exception {
//        Usuario user = userRepository.findByLogin(login);
//        
//        if()
//            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));
//        
//        String otp = OtpGenerator.generateOtp(); 
//        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);
//
//        // 2. Armazenamento/Atualização
//        // Você pode deletar tokens antigos ou apenas sobrescrever:
//        tokenRepository.deleteByUser(user); // Limpa tokens anteriores
//        
//        PasswordResetToken resetToken = new PasswordResetToken();
//        resetToken.setToken(otp);
//        resetToken.setUser(user);
//        resetToken.setExpiryDate(expiryDate);
//        tokenRepository.save(resetToken);
//
//        // 3. Envio do Token (Isto é crucial)
//        String subject = "Código de Redefinição de Senha";
//        String content = "Seu código de redefinição é: " + otp + ". Ele expira em " 
//                         + EXPIRATION_MINUTES + " minutos.";
//        emailService.sendEmail(user.getEmail(), subject, content); 
//    }
//
//    public boolean validateTokenAndResetPassword(String token, String newPassword) {
//        PasswordResetToken resetToken = tokenRepository.findByToken(token)
//            .orElse(null);
//
//        if (resetToken == null || resetToken.isExpired()) {
//            // Token não encontrado ou expirado
//            return false;
//        }
//
//        // 4. Redefinição da Senha
//        Usuario user = resetToken.getUser();
//        // **NÃO ESQUEÇA DE CRIPTOGRAFAR A NOVA SENHA ANTES DE SALVAR**
//        user.setPassword(passwordEncoder.encode(newPassword)); 
//        userRepository.save(user);
//
//        // Deleta o token após o uso (boa prática)
//        tokenRepository.delete(resetToken); 
//        
//        return true;
//    }
//
//}
