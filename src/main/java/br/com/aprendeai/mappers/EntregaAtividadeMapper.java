//package br.com.aprendeai.mappers;
//
//import java.util.List;
//
//import org.mapstruct.Mapping;
//
//import br.com.aprendeai.dtos.EntregaAtividadeResponseDto;
//import br.com.aprendeai.model.EntregaAtividade;
//
//public interface EntregaAtividadeMapper {
//	
//	@Mapping(source = "atividade.id", target = "atividadeId")
//    @Mapping(source = "aluno.id", target = "alunoId")
//    EntregaAtividadeResponseDto toResponseDto(EntregaAtividade entregaAtividade);
//    
//    List<EntregaAtividadeResponseDto> toResponseDtoList(List<EntregaAtividade> entregas);
//
//
//}
