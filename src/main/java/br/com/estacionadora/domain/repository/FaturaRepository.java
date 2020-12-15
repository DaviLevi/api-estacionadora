package br.com.estacionadora.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.estacionadora.domain.model.Fatura;
import br.com.estacionadora.domain.repository.custom.CustomFaturaRepository;

@Repository
public interface FaturaRepository extends JpaRepository<Fatura, Long>, CustomFaturaRepository{

	@Override
	List<Fatura> buscarEntre(LocalDateTime dataInicial, LocalDateTime dataFinal);
	
}
