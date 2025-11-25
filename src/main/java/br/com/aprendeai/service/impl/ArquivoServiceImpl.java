package br.com.aprendeai.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.io.source.ByteArrayOutputStream;

import br.com.aprendeai.config.AccessControlService;
import br.com.aprendeai.dtos.FrequenciaDTO;
import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.repository.ArquivoRepository;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.service.ArquivoService;
import br.com.aprendeai.service.ChamadaService;

@Service
public class ArquivoServiceImpl implements ArquivoService {
	
	@Value("${file.upload-dir}")
    private String uploadDir;

    private final ArquivoRepository arquivoRepository;
    private final ChamadaService chamadaService;
    private final TurmaRepository turmaRepository;
    private final AccessControlService accessControl;
    private final TemplateEngine templateEngine;

	public ArquivoServiceImpl(ArquivoRepository arquivoRepository, ChamadaService chamadaService,
			TurmaRepository turmaRepository, AccessControlService accessControl, TemplateEngine templateEngine) {
		this.arquivoRepository = arquivoRepository;
		this.chamadaService = chamadaService;
		this.turmaRepository = turmaRepository;
		this.accessControl = accessControl;
		this.templateEngine = templateEngine;
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
    
    @Override
    public byte[] gerarRelatorioFrequencia(Long turmaId) {
    		Context context = new Context();
    		
            var lista = chamadaService.calcularFrequenciaTurma(turmaId);
            
            Optional<Turma> turma = turmaRepository.findById(turmaId);
            
            Turma turmaExiste = turma.get();
            
            accessControl.verificarAcessoProfessor(turmaExiste);
            
            context.setVariable("turmaNome", turmaExiste.getNome());
            context.setVariable("alunos", lista);

            String html = templateEngine.process("relatorio", context);
            
            ConverterProperties properties = new ConverterProperties();

            properties.setBaseUri("src/main/resources/static/");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            com.itextpdf.html2pdf.HtmlConverter.convertToPdf(html, baos, properties);

            return baos.toByteArray();
    }
    
//    private String gerarHtmlRelatorio(String turmaNome, List<FrequenciaDTO> lista) {
//    	
//        String linhas = gerarLinhas(lista);
//
//        String template = """
//                <!DOCTYPE html>
//                <html>
//                <head>
//                    <meta charset="UTF-8"/>
//                    <style>
//                    @font-face {
//        				font-family: 'Neulis-Sans';
//        				src: url('./fonts/Montserrat/static/Montserrat-Black.ttf') format('truetype');
//        				font-weight: normal;
//        				font-style: normal;
//        				}
//
//						@font-face {
//						    font-family: 'Neulis-Sans';
//						    src: url('./fonts/Montserrat/static/Montserrat-Bold.ttf') format('truetype');
//						    font-weight: bold;
//						    font-style: normal;
//						}
//                        body { font-family: 'Neulis-San', sans-serif; padding: 20px; }
//                        h1 { text-align: center; }
//                        table { width: 100%; border-collapse: collapse; margin-top: 25px; }
//                        table, th, td { border: 1px solid #666; }
//                        th { background: #eee; padding: 8px; text-align: center; }
//                        td { padding: 6px; text-align: center; }
//                    </style>
//                </head>
//                <body>
//                <h1>Relatório de Frequência - Turma {{turmaNome}}</h1>
//                <table>
//                    <thead>
//                    <tr>
//                        <th>Aluno</th>
//                        <th>Horas Presentes</th>
//                        <th>Carga Total</th>
//                        <th>% Presença</th>
//                        <th>% Faltas</th>
//                    </tr>
//                    </thead>
//                    <tbody>
//                        {{linhasTabela}}
//                    </tbody>
//                </table>
//                </body>
//                </html>
//                """;
//
//        return template.replace("{{turmaNome}}", turmaNome)
//                       .replace("{{linhasTabela}}", linhas);
//    }
//
//    private String gerarLinhas(List<FrequenciaDTO> lista) {
//        StringBuilder sb = new StringBuilder();
//        for (FrequenciaDTO f : lista) {
//            sb.append("<tr>")
//              .append("<td>").append(f.nomeAluno()).append("</td>")
//              .append("<td>").append(f.horasPresenteTotal()).append("</td>")
//              .append("<td>").append(f.cargaHorariaTotal()).append("</td>")
//              .append("<td>").append(String.format("%.2f%%", f.percentualPresenca())).append("</td>")
//              .append("<td>").append(String.format("%.2f%%", 100 - f.percentualPresenca())).append("</td>")
//              .append("</tr>");
//        }
//        return sb.toString();
//    }
}