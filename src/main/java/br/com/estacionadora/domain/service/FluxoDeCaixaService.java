package br.com.estacionadora.domain.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.estacionadora.api.contract.model.FluxoDeCaixaModel;
import br.com.estacionadora.domain.repository.FaturaRepository;

@Service
public class FluxoDeCaixaService {

	@Autowired
	private FaturaRepository faturaRepository;
	
	public FluxoDeCaixaModel buscarFluxoDeCaixaEntre(LocalDate dataInicial, LocalDate dataFinal) {
		
		DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm");
		LocalDateTime dateTimeInicial = null;
		LocalDateTime dateTimeFinal = null;
		
		if(dataInicial != null) {
			String partStrIni = dataInicial.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
			dateTimeInicial = LocalDateTime.parse(partStrIni + " 00:00", formatterDateTime);
		}
		
		if(dataFinal != null) {
			String partStrFin = dataFinal.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
			dateTimeFinal = LocalDateTime.parse(partStrFin + " 23:59", formatterDateTime);
		}
		
		BigDecimal fluxoDeCaixa = faturaRepository.buscarEntre(dateTimeInicial, dateTimeFinal)
				.stream()
				.map(fatura -> fatura.getValorPago())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		return new FluxoDeCaixaModel(fluxoDeCaixa);
	}
}
