package br.com.estacionadora.api.contract.error;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class Erro {

	private String resumo;
	
	private String descricao;
	
	private int codigo;
	
	private LocalDateTime timestamp;
	
	private List<Problema> problemas;
	
	@Builder
	@Getter
	public static class Problema{
		
		private String propriedade;
		
		private String descricao;
		
	}
	
	
}
