package br.com.aprendeai.config;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import br.com.aprendeai.model.Turma;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.TurmaRepository;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final TurmaRepository turmaRepository;

    public CustomPermissionEvaluator(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }
    
    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if (!(auth.getPrincipal() instanceof Usuario usuario)) {
            return false;
        }

        if (targetDomainObject instanceof Turma turma) {
            return switch (permission.toString()) {
                case "professor" -> turma.getProfessor().getId().equals(usuario.getId());
                case "aluno" -> turma.getAlunos().contains(usuario);
                case "participante" -> turma.getProfessor().getId().equals(usuario.getId())
                        || turma.getAlunos().contains(usuario);
                default -> false;
            };
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if (!(auth.getPrincipal() instanceof Usuario usuario)) {
            return false;
        }

        if ("Turma".equalsIgnoreCase(targetType)) {
            Turma turma = turmaRepository.findById((Long) targetId).orElse(null);
            if (turma == null)
                return false;

            return switch (permission.toString()) {
                case "professor" -> turma.getProfessor().getId().equals(usuario.getId());
                case "aluno" -> turma.getAlunos().contains(usuario);
                case "participante" -> turma.getProfessor().getId().equals(usuario.getId())
                        || turma.getAlunos().contains(usuario);
                default -> false;
            };
        }

        return false;
    }
}