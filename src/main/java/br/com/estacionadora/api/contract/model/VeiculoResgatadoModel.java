package br.com.estacionadora.api.contract.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.estacionadora.domain.model.VeiculoEstacionado;
import lombok.Getter;

@Getter
public class VeiculoResgatadoModel {

	private String placa;
	
	private String modelo;
	
	private String marca;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime dataResgate;
	
	private BigDecimal valorTotal;
	
	public VeiculoResgatadoModel(VeiculoEstacionado veiculoEstacionado) {
		this.placa = veiculoEstacionado.getVeiculo().getPlaca();
		this.modelo = veiculoEstacionado.getVeiculo().getModelo();
		this.marca = veiculoEstacionado.getVeiculo().getMarca();
		
		this.dataResgate = veiculoEstacionado.getDataSaida();
		this.valorTotal = veiculoEstacionado.getCustoTotal();
	}
}
