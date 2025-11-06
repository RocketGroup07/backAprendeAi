//package br.com.aprendeai.model;
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "redefinicoes_senha")
//public class RedefinicaoSenha {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String email;
//
//    @Column(nullable = false)
//    private String codigo;
//
//    @Column(nullable = false)
//    private LocalDateTime validade;
//
//    public RedefinicaoSenha() {}
//
//    public RedefinicaoSenha(String email, String codigo) {
//        this.email = email;
//        this.codigo = codigo;
//        this.validade = LocalDateTime.now().plusMinutes(30);
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public String getCodigo() {
//        return codigo;
//    }
//
//    public LocalDateTime getValidade() {
//        return validade;
//    }
//
//    public boolean estaExpirado() {
//        return LocalDateTime.now().isAfter(validade);
//    }
//}
