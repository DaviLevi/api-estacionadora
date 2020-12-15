package br.com.estacionadora.api.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.estacionadora.api.contract.model.FluxoDeCaixaModel;
import br.com.estacionadora.domain.service.FluxoDeCaixaService;


@RestController
@RequestMapping("/fluxo-de-caixa")
public class FluxoDeCaixaController {

	@Autowired
	private FluxoDeCaixaService fluxoDeCaixaService;
	
	@GetMapping
	public FluxoDeCaixaModel getFluxoDeCaixa(@DateTimeFormat(pattern = "ddMMyyyy") LocalDate dataInicial,
			@DateTimeFormat(pattern = "ddMMyyyy") LocalDate dataFinal) {
		return fluxoDeCaixaService.buscarFluxoDeCaixaEntre(dataInicial, dataFinal);
	}
	
}
