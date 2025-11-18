//package br.com.aprendeai.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import br.com.aprendeai.config.AccessControlService;
//import br.com.aprendeai.dtos.AtividadeResponseDto;
//import br.com.aprendeai.dtos.EntregaAtividadeRequestDto;
//import br.com.aprendeai.dtos.RequestNotaDto;
//import br.com.aprendeai.enums.ArquivoTipo;
//import br.com.aprendeai.enums.StatusAtividade;
//import br.com.aprendeai.mappers.AtividadeMapper;
//import br.com.aprendeai.model.Arquivo;
//import br.com.aprendeai.model.Atividade;
//import br.com.aprendeai.model.EntregaAtividade;
//import br.com.aprendeai.model.Turma;
//import br.com.aprendeai.model.Usuario;
//import br.com.aprendeai.repository.AtividadeRepository;
//import br.com.aprendeai.repository.EntregaAtividadeRepository;
//import br.com.aprendeai.repository.TurmaRepository;
//import br.com.aprendeai.service.ArquivoService;
//import br.com.aprendeai.service.EntregaAtividadeService;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.transaction.Transactional;
//
//public class EntregaAtividadeServiceImpl implements EntregaAtividadeService{
//	
//	
//	private final EntregaAtividadeRepository entregaAtividadeRepository;
//	private final AtividadeMapper atividadeMapper;
//	private final ObjectMapper objectMapper;
//	private final ArquivoService arquivoServie;
//	private final AccessControlService accessControlService;
//	private final AtividadeRepository atividadeRepository;
//	private final TurmaRepository turmaRepository;
//	
//
//	public EntregaAtividadeServiceImpl(EntregaAtividadeRepository entregaAtividadeRepository,
//			AtividadeMapper atividadeMapper, ObjectMapper objectMapper, ArquivoService arquivoServie,
//			AccessControlService accessControlService, AtividadeRepository atividadeRepository, 
//			TurmaRepository turmaRepository) {
//		this.entregaAtividadeRepository = entregaAtividadeRepository;
//		this.atividadeMapper = atividadeMapper;
//		this.objectMapper = objectMapper;
//		this.arquivoServie = arquivoServie;
//		this.accessControlService = accessControlService;
//		this.atividadeRepository = atividadeRepository;
//		this.turmaRepository = turmaRepository;
//	}
//
//	@Override
//    @Transactional
//    public List<AtividadeResponseDto> listarAtividadesEntregues(Long turmaId) {
//        
//        List<EntregaAtividade> atividadesEntregues = entregaAtividadeRepository.findByAtividade_TurmaIdAndEntregueTrue(turmaId);
//        
//        Usuario usuario = accessControlService.getUsuarioLogado();
//        Turma turma = buscarTurmaId(turmaId); 
//        
//        if (turma.getProfessor() == null || !turma.getProfessor().equals(usuario)) {
//            throw new AccessDeniedException("Acesso negado. Você não é o professor desta turma.");
//        }
//
//        return atividadesEntregues.stream()
//                .map(entrega -> atividadeMapper.toResponseDTO(entrega.getAtividade(), entrega))
//                .collect(Collectors.toList());
//    }
//
//	
//	@Override
//    @Transactional
//    public AtividadeResponseDto entregarAtividade(Long atividadeId, String resposta, MultipartFile arquivo) {
//        try {
//            Atividade atividade = buscarAtividadeId(atividadeId);
//            Turma turma = atividade.getTurma();
//            accessControlService.isAluno(turma); 
//            Usuario aluno = accessControlService.getUsuarioLogado();
//
//            EntregaAtividade entregaExistente = entregaAtividadeRepository
//                    .findByAtividadeIdAndAlunoId(atividadeId, aluno.getId())
//                    .orElse(null);
//
//            if (entregaExistente != null) {
//                throw new IllegalStateException("Entrega já existe. Use o método de edição.");
//            }
//
//            EntregaAtividade novaEntrega = new EntregaAtividade();
//            novaEntrega.setAtividade(atividade);
//            novaEntrega.setAluno(aluno);
//            novaEntrega.entregar();
//
//            EntregaAtividadeRequestDto dto = objectMapper.readValue(resposta, EntregaAtividadeRequestDto.class);
//            if (dto.resposta() != null) {
//                novaEntrega.setRespostaTexto(dto.resposta());
//            }
//
//            List<Arquivo> arquivosEntrega = new ArrayList<>();
//            if (arquivo != null && !arquivo.isEmpty()) {
//                Arquivo arquivoEntrega = arquivoServie.uploadArquivo(arquivo);
//                arquivoEntrega.setEntregaAtividade(novaEntrega); 
//                arquivoEntrega.setTipo(ArquivoTipo.ENTREGA);
//                arquivosEntrega.add(arquivoEntrega);
//            }
//            novaEntrega.setArquivosEntrega(arquivosEntrega);
//
//            EntregaAtividade entregaSalva = entregaAtividadeRepository.save(novaEntrega);
//            return atividadeMapper.toResponseDTO(atividade, entregaSalva); 
//
//        } catch (EntityNotFoundException e) {
//            throw e;
//        } catch (Exception e) {
//            e.printStackTrace(); 
//            throw new RuntimeException("Erro ao entregar atividade: " + e.getMessage(), e);
//        }
//    }
//    
//	@Override
//    @Transactional
//    public Arquivo baixarEntregaDeAluno(Long atividadeId, Long alunoId) {
//        
//        EntregaAtividade entrega = entregaAtividadeRepository
//                .findByAtividadeIdAndAlunoId(atividadeId, alunoId)
//                .orElseThrow(() -> new EntityNotFoundException("Entrega não encontrada para esta atividade e aluno."));
//
//        Usuario usuarioLogado = accessControlService.getUsuarioLogado();
//        Turma turma = entrega.getAtividade().getTurma();
//
//        boolean isProfessor = false;
//        try {
//            accessControlService.verificarAcessoProfessor(turma); 
//            isProfessor = true;
//        } catch (AccessDeniedException ignored) {
//
//        }
//        
//        boolean eOProprioAluno = usuarioLogado.getId().equals(alunoId);
//        
//        if (!isProfessor && !eOProprioAluno) {
//            throw new AccessDeniedException("Você não tem permissão para baixar esta entrega.");
//        }
//        
//        Arquivo arquivoEntrega = entrega.getArquivosEntrega().stream()
//                .filter(a -> a.getTipo() == ArquivoTipo.ENTREGA)
//                .findFirst()
//                .orElseThrow(() -> new EntityNotFoundException("Arquivo de entrega não encontrado."));
//        
//        return arquivoEntrega;
//    }
//    
//	@Override
//    @Transactional
//    public AtividadeResponseDto editarEntrega(Long atividadeId, String novaResposta, MultipartFile novoArquivo) {
//        try {
//            Atividade atividade = buscarAtividadeId(atividadeId);
//            Turma turma = atividade.getTurma();
//            accessControlService.isAluno(turma);
//            Usuario aluno = accessControlService.getUsuarioLogado();
//
//            EntregaAtividade entregaExistente = entregaAtividadeRepository
//                    .findByAtividadeIdAndAlunoId(atividadeId, aluno.getId())
//                    .orElseThrow(() -> new EntityNotFoundException("Entrega não encontrada para edição."));
//
//            if (novoArquivo != null && !novoArquivo.isEmpty()) {
//                for (Arquivo arquivo : entregaExistente.getArquivosEntrega()) {
//                    arquivoServie.deletarArquivo(arquivo.getId());
//                }
//                entregaExistente.setArquivosEntrega(new ArrayList<>()); 
//
//                Arquivo arquivoEntrega = arquivoServie.uploadArquivo(novoArquivo);
//                arquivoEntrega.setEntregaAtividade(entregaExistente); 
//                arquivoEntrega.setTipo(ArquivoTipo.ENTREGA);
//                
//                entregaExistente.getArquivosEntrega().add(arquivoEntrega); 
//            }
//
//            if (novaResposta != null) {
//                EntregaAtividadeRequestDto dto = objectMapper.readValue(novaResposta, EntregaAtividadeRequestDto.class);
//                if (dto.resposta() != null) {
//                    entregaExistente.setRespostaTexto(dto.resposta());
//                }
//            }
//
//            EntregaAtividade entregaSalva = entregaAtividadeRepository.save(entregaExistente);
//            entregaSalva.entregar();
//            return atividadeMapper.toResponseDTO(atividade, entregaSalva);
//
//        } catch (Exception e) {
//            throw new RuntimeException("Erro ao editar entrega: " + e.getMessage(), e);
//        }
//    }
//
//	@Override
//    @Transactional
//    public void excluirEntrega(Long atividadeId) {
//        
//        Atividade atividade = buscarAtividadeId(atividadeId);
//        Turma turma = atividade.getTurma();
//        accessControlService.isAluno(turma);
//        Usuario aluno = accessControlService.getUsuarioLogado();
//
//        EntregaAtividade entregaExistente = entregaAtividadeRepository
//                .findByAtividadeIdAndAlunoId(atividadeId, aluno.getId())
//                .orElseThrow(() -> new EntityNotFoundException("Entrega não encontrada para exclusão."));
//
//        for (Arquivo arquivo : entregaExistente.getArquivosEntrega()) {
//            arquivoServie.deletarArquivo(arquivo.getId());
//        }
//        
//        entregaAtividadeRepository.delete(entregaExistente);
//    }
//
//
//    @Override
//    @Transactional
//    public AtividadeResponseDto corrigirAtividade(Long atividadeId, Long alunoId, RequestNotaDto dto) {
//    	
//    	Atividade atividade = buscarAtividadeId(atividadeId);
//    	
//		EntregaAtividade entrega = entregaAtividadeRepository
//		             .findByAtividadeIdAndAlunoId(atividadeId, alunoId)
//		             .orElseThrow(() -> new EntityNotFoundException("Entrega não encontrada"));
//		
//		 Turma turma = atividade.getTurma();
//		        accessControlService.verificarAcessoProfessor(turma);
//		
//         if (entrega.getStatus() != StatusAtividade.ENTREGUE) {
//             	throw new IllegalStateException("A atividade só pode ser corrigida após ser entregue.");
//         }
//		
//         entrega.corrigir(dto.nota(), dto.feedback());
//         entregaAtividadeRepository.save(entrega);
//         return atividadeMapper.toResponseDTO(atividade, entrega);
//    }
//    
//    private Atividade buscarAtividadeId(Long id) {
//        return atividadeRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada com id: " + id));
//    }
//    
//    private Turma buscarTurmaId(Long id) {
//        return turmaRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com id: " + id));
//    }
//
//}
