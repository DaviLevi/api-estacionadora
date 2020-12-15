package br.com.estacionadora.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.estacionadora.domain.model.VeiculoEstacionado;

@Repository
public interface VeiculoEstacionadoRepository extends JpaRepository<VeiculoEstacionado, Long>{

	Optional<VeiculoEstacionado> findByVeiculoPlaca(String placa);
	
}
