package br.com.estacionadora.api.contract.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.estacionadora.domain.model.VeiculoEstacionado;
import lombok.Getter;

@Getter
public class VeiculoEstacionadoModel {

	private String placa;
	
	private String modelo;
	
	private String marca;
	
	private LocalDateTime dataEntrada;
	
	private BigDecimal valorPorHora;
	
	private BigDecimal valorPrimeiraHora;
	
	public VeiculoEstacionadoModel(VeiculoEstacionado veiculo) {
		this.placa = veiculo.getVeiculo().getPlaca();
		this.modelo = veiculo.getVeiculo().getModelo();
		this.marca = veiculo.getVeiculo().getMarca();
		
		this.dataEntrada = veiculo.getDataEntrada();
		this.valorPorHora = veiculo.getCustoPorHoraEstacionada();
		this.valorPrimeiraHora = veiculo.getCustoPrimeiraHoraEstacionada();
	}
}
