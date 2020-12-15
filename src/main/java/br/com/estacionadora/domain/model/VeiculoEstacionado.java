package br.com.estacionadora.domain.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "veiculo_estacionado")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VeiculoEstacionado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Embedded
	@AttributeOverrides({
        @AttributeOverride(name = "placa", column = @Column(name = "placa", unique = true, nullable = false)),
        @AttributeOverride(name = "marca", column = @Column(name = "marca", nullable = false)),
        @AttributeOverride(name = "modelo", column = @Column(name = "modelo", nullable = false))
	})
	private Veiculo veiculo;
	
	@Column(nullable = false)
	private LocalDateTime dataEntrada;
	
	@Column(nullable = true)
	private LocalDateTime dataSaida;
	
	@Transient
	private BigDecimal custoPorHoraEstacionada = BigDecimal.valueOf(3.0);
	
	@Transient
	private BigDecimal custoPrimeiraHoraEstacionada = BigDecimal.valueOf(5.0);
	
	
	public Fatura geraFatura() { 
		Fatura fatura = new Fatura();
		fatura.setVeiculo(veiculo);
		fatura.setDataEntrada(dataEntrada);
		fatura.setDataSaida(dataSaida);
		fatura.setValorPago(getCustoTotal());
		return fatura;
	}
	
	public BigDecimal getCustoTotal() {
		LocalDateTime dataAtual = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		Duration tempoEstacionado = Duration.between(dataEntrada, dataSaida != null ? dataSaida : dataAtual);
		long horasEstacionadas = (long)(Math.ceil(tempoEstacionado.toMinutes()/60.0));
		Function<Long, BigDecimal> calculaValor = hrs -> custoPorHoraEstacionada.multiply(BigDecimal.valueOf(hrs-1)).add(custoPrimeiraHoraEstacionada);
		return calculaValor.apply(horasEstacionadas);
	}
	
}
