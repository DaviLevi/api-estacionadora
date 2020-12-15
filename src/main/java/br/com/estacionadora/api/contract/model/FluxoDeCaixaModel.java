package br.com.estacionadora.api.contract.model;

import java.math.BigDecimal;

import lombok.Getter;


@Getter
public class FluxoDeCaixaModel {

	private BigDecimal fluxoDeCaixa;
	
	public FluxoDeCaixaModel(BigDecimal fluxoDeCaixa) {
		this.fluxoDeCaixa = fluxoDeCaixa;
	}
	
}
