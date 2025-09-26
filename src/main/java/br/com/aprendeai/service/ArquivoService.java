package br.com.aprendeai.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import br.com.aprendeai.model.Arquivo;

public interface ArquivoService {
	
	public Arquivo uploadArquivo(MultipartFile arquivo);
	
	public Arquivo getArquivo(Long id);
	
	public List<Arquivo> listarArquivos();
	
	public void deletarArquivo(Long id);

	public Resource downloadArquivo(Long id);
}