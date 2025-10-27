package br.com.aprendeai.config;

import org.springframework.stereotype.Service;

import br.com.aprendeai.model.Turma;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.util.AuthenticatedUser;

@Service
public class AccessControlService {
	
	private final AuthenticatedUser authenticatedUser;

    public AccessControlService(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public Usuario getUsuarioLogado() {
        return authenticatedUser.getCurrentUser();
    }

    public void verificarParticipacao(Turma turma) {
        Usuario usuario = getUsuarioLogado();
        if (!usuarioParticipaDaTurma(usuario, turma)) {
            throw new SecurityException("Usuário não pertence a esta turma.");
        }
    }

    public void verificarAcessoProfessor(Turma turma) {
        Usuario usuario = getUsuarioLogado();
        if (!turma.getProfessor().getId().equals(usuario.getId())) {
            throw new SecurityException("Apenas o professor da turma pode executar esta ação.");
        }
    }

    public boolean usuarioParticipaDaTurma(Usuario usuario, Turma turma) {
        return turma.getProfessor().getId().equals(usuario.getId())
                || turma.getAlunos().contains(usuario);
    }

    public boolean isProfessor(Turma turma) {
        Usuario usuario = getUsuarioLogado();
        return turma.getProfessor().getId().equals(usuario.getId());
    }

    public boolean isAluno(Turma turma) {
        Usuario usuario = getUsuarioLogado();
        return turma.getAlunos().contains(usuario);
    }

}
