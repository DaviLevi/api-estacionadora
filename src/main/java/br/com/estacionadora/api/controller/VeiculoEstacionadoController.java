package br.com.estacionadora.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.estacionadora.api.contract.input.VeiculoEstacionadoInput;
import br.com.estacionadora.api.contract.model.VeiculoBuscadoModel;
import br.com.estacionadora.api.contract.model.VeiculoEstacionadoModel;
import br.com.estacionadora.api.contract.model.VeiculoResgatadoModel;
import br.com.estacionadora.domain.model.VeiculoEstacionado;
import br.com.estacionadora.domain.service.VeiculoEstacionadoService;

@RestController
@RequestMapping("/veiculos-estacionados")
public class VeiculoEstacionadoController {

	@Autowired
	private VeiculoEstacionadoService veiculoEstacionadoService;
	
	@GetMapping
	public List<VeiculoEstacionadoModel> listar(){
		return veiculoEstacionadoService
				.listarTodos()
					.stream()
						.map(v -> new VeiculoEstacionadoModel(v))
							.collect(Collectors.toList());
	}
	
	@PostMapping
	public ResponseEntity<VeiculoEstacionadoModel> estacionar(@RequestBody @Valid VeiculoEstacionadoInput input){
		VeiculoEstacionado domain = input.toDomain();
		VeiculoEstacionadoModel model = new VeiculoEstacionadoModel(veiculoEstacionadoService.estacionar(domain));
		return ResponseEntity.status(HttpStatus.CREATED).body(model);
	}
	
	@GetMapping("/{placa}")
	public VeiculoBuscadoModel buscar(@PathVariable String placa){
		return new VeiculoBuscadoModel(veiculoEstacionadoService.buscar(placa));
	}
	
	@PutMapping("/{placa}/resgate")
	public VeiculoResgatadoModel resgatar(@PathVariable String placa){
		return new VeiculoResgatadoModel(veiculoEstacionadoService.resgatar(placa));
	}
	
}
