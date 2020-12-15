package br.com.estacionadora.api.contract.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.estacionadora.domain.model.VeiculoEstacionado;
import lombok.Getter;

@Getter
public class VeiculoBuscadoModel {

	private String placa;
	
	private String modelo;
	
	private String marca;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime dataEntrada;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime dataAtual;
	
	private BigDecimal custoTotal;
	
	public VeiculoBuscadoModel(VeiculoEstacionado veiculo) {
		this.placa = veiculo.getVeiculo().getPlaca();
		this.modelo = veiculo.getVeiculo().getModelo();
		this.marca = veiculo.getVeiculo().getMarca();
		
		this.dataEntrada = veiculo.getDataEntrada();
		this.dataAtual = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}
	
}
