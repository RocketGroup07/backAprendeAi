package br.com.aprendeai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.repository.ArquivoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ArquivoService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final ArquivoRepository arquivoRepository;

    public ArquivoService(ArquivoRepository arquivoRepository) {
        this.arquivoRepository = arquivoRepository;
    }

    public Arquivo uploadArquivo(MultipartFile arquivo) throws IOException {
        String nomeOriginal = arquivo.getOriginalFilename();
        String extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
        String nomeUnico = UUID.randomUUID().toString() + extensao; // Evita conflitos de nome
        Path caminhoDestino = Paths.get(uploadDir).resolve(nomeUnico);

        Files.createDirectories(caminhoDestino.getParent());
        Files.copy(arquivo.getInputStream(), caminhoDestino);

        Arquivo novoArquivo = new Arquivo();
        novoArquivo.setNomeArquivo(nomeOriginal);
        novoArquivo.setTipoArquivo(arquivo.getContentType());
        novoArquivo.setCaminhoArquivo(caminhoDestino.toString());

        return arquivoRepository.save(novoArquivo);
    }

    public Arquivo getArquivo(Long id) {
        return arquivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));
    }

    public Path getCaminhoArquivo(Arquivo arquivo) {
        return Paths.get(arquivo.getCaminhoArquivo());
    }
}
