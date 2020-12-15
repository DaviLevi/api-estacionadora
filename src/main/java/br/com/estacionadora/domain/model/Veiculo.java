package br.com.estacionadora.domain.model;

import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class Veiculo {

	private String marca;
	
	private String placa;
	
	private String modelo;
	
}
