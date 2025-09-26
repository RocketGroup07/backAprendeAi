package br.com.aprendeai.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.repository.ArquivoRepository;
import br.com.aprendeai.service.ArquivoService;

@Service
public class ArquivoServiceImpl implements ArquivoService {
	
	@Value("${file.upload-dir}")
    private String uploadDir;

    private final ArquivoRepository arquivoRepository;

    public ArquivoServiceImpl(ArquivoRepository arquivoRepository) {
        this.arquivoRepository = arquivoRepository;
    }

    @Override
    public Arquivo uploadArquivo(MultipartFile arquivo) {
        try {
            String nomeOriginal = arquivo.getOriginalFilename();
            String extensao = "";
            if (nomeOriginal.contains(".")) {
                extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
            }
            String nomeUnico = UUID.randomUUID().toString() + extensao;
            Path caminhoDestino = Paths.get(uploadDir).resolve(nomeUnico);

            Files.createDirectories(caminhoDestino.getParent());
            Files.copy(arquivo.getInputStream(), caminhoDestino);

            Arquivo novoArquivo = new Arquivo();
            novoArquivo.setNomeArquivo(nomeOriginal);
            novoArquivo.setTipoArquivo(arquivo.getContentType());
            novoArquivo.setCaminhoArquivo(caminhoDestino.toString());

            return arquivoRepository.save(novoArquivo);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar o arquivo: " + e.getMessage());
        }
    }

    @Override
    public Arquivo getArquivo(Long id) {
        return arquivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado com o ID: " + id));
    }

    @Override
    public List<Arquivo> listarArquivos() {
        return arquivoRepository.findAll();
    }

    @Override
    public void deletarArquivo(Long id) {
        try {
            Arquivo arquivoParaDeletar = arquivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado com o ID: " + id));

            Path caminhoArquivo = Paths.get(arquivoParaDeletar.getCaminhoArquivo());
            Files.deleteIfExists(caminhoArquivo);

            arquivoRepository.delete(arquivoParaDeletar);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao deletar o arquivo: " + e.getMessage());
        }
    }

    @Override
    public Resource downloadArquivo(Long id) {
        try {
            Arquivo arquivo = getArquivo(id);
            Path caminhoArquivo = Paths.get(arquivo.getCaminhoArquivo());
            Resource recurso = new UrlResource(caminhoArquivo.toUri());

            if (recurso.exists() && recurso.isReadable()) {
                return recurso;
            } else {
                throw new RuntimeException("Arquivo não pode ser lido!");
            }
        } catch (IOException e) {
            throw new RuntimeException("Falha ao obter o arquivo para download: " + e.getMessage());
        }
    }
}