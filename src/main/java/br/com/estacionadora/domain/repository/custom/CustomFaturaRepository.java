package br.com.estacionadora.domain.repository.custom;

import java.time.LocalDateTime;
import java.util.List;

import br.com.estacionadora.domain.model.Fatura;

public interface CustomFaturaRepository {

	public List<Fatura> buscarEntre(LocalDateTime dataInicial, LocalDateTime dataFinal);
	
}
