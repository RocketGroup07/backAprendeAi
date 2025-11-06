//package br.com.aprendeai.repository;
//
//import br.com.aprendeai.model.RedefinicaoSenha;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Repository
//public class RedefinicaoSenhaRepository {
//
//    private final ConcurrentHashMap<String, RedefinicaoSenha> codigos = new ConcurrentHashMap<>();
//
//    public void salvar(String email, RedefinicaoSenha redefinicao) {
//        codigos.put(email, redefinicao);
//    }
//
//    public Optional<RedefinicaoSenha> buscarPorEmail(String email) {
//        return Optional.ofNullable(codigos.get(email));
//    }
//
//    public void remover(String email) {
//        codigos.remove(email);
//    }
//}
