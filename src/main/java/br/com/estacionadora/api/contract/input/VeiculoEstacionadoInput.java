package br.com.estacionadora.api.contract.input;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.estacionadora.api.validation.annotation.PlacaMercosul;
import br.com.estacionadora.domain.model.Veiculo;
import br.com.estacionadora.domain.model.VeiculoEstacionado;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VeiculoEstacionadoInput {

	@NotBlank
	private String marca;
	
	@PlacaMercosul
	@NotBlank
	private String placa;
	
	@NotBlank
	private String modelo;
	
	@NotNull
	@JsonFormat(
			  shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime dataEntrada;
	
	public VeiculoEstacionado toDomain() {
		Veiculo veiculo = new Veiculo();
		veiculo.setMarca(marca);
		veiculo.setModelo(modelo);
		veiculo.setPlaca(placa);
		
		VeiculoEstacionado veiculoEstacionado = new VeiculoEstacionado();
		veiculoEstacionado.setDataEntrada(dataEntrada);
		veiculoEstacionado.setVeiculo(veiculo);
		
		return veiculoEstacionado;
	}
	
}
