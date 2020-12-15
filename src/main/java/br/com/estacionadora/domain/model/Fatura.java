package br.com.estacionadora.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "fatura")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Fatura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Embedded
	@AttributeOverrides({
        @AttributeOverride(name = "placa", column = @Column(name = "placa", nullable = false)),
        @AttributeOverride(name = "marca", column = @Column(name = "marca", nullable = false)),
        @AttributeOverride(name = "modelo", column = @Column(name = "modelo", nullable = false))
	})
	private Veiculo veiculo;
	
	@Column(nullable = false)
	private LocalDateTime dataEntrada;
	
	@Column(nullable = false)
	private LocalDateTime dataSaida;

	@Column(nullable = false)
	private BigDecimal valorPago;
	
}
