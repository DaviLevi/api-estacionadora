package br.com.estacionadora.domain.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.estacionadora.domain.exception.EntidadeNaoEncontradaException;
import br.com.estacionadora.domain.exception.VeiculoJaEstacionadoException;
import br.com.estacionadora.domain.model.Fatura;
import br.com.estacionadora.domain.model.VeiculoEstacionado;
import br.com.estacionadora.domain.repository.FaturaRepository;
import br.com.estacionadora.domain.repository.VeiculoEstacionadoRepository;

@Service
public class VeiculoEstacionadoService {

	@Autowired
	private VeiculoEstacionadoRepository veiculoEstacionadoRepository;
	
	@Autowired
	private FaturaRepository faturaRepository;
	
	public List<VeiculoEstacionado> listarTodos(){
		return veiculoEstacionadoRepository.findAll();
	}
	
	public VeiculoEstacionado buscar(String placa) {
		return veiculoEstacionadoRepository.findByVeiculoPlaca(placa).orElseThrow(() -> new EntidadeNaoEncontradaException("Nao existe nenhum veiculo estacionado com a placa " + placa));
	}
	
	public VeiculoEstacionado resgatar(String placa) {
		VeiculoEstacionado veiculoResgatado = veiculoEstacionadoRepository.findByVeiculoPlaca(placa).orElseThrow(() -> new EntidadeNaoEncontradaException("Nao existe nenhum veiculo estacionado com a placa " + placa));
		veiculoResgatado.setDataSaida(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
		Fatura faturaGerada = veiculoResgatado.geraFatura();
		faturaRepository.save(faturaGerada);
		veiculoEstacionadoRepository.delete(veiculoResgatado);
		return veiculoResgatado;
	}
	
	public VeiculoEstacionado estacionar(VeiculoEstacionado veiculoEstacionado) {
		Optional<VeiculoEstacionado> veiculoDuplicado = veiculoEstacionadoRepository.findByVeiculoPlaca(veiculoEstacionado.getVeiculo().getPlaca());
		if(veiculoDuplicado.isPresent())
			throw new VeiculoJaEstacionadoException("Um veiculo com a mesma placa ja foi estacionado");
		return veiculoEstacionadoRepository.save(veiculoEstacionado);
	}
	
}
