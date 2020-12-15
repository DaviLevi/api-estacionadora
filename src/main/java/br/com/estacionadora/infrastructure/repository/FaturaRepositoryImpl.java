package br.com.estacionadora.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import br.com.estacionadora.domain.model.Fatura;
import br.com.estacionadora.domain.repository.custom.CustomFaturaRepository;

@Repository
public class FaturaRepositoryImpl implements CustomFaturaRepository{

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Fatura> buscarEntre(LocalDateTime dataInicial, LocalDateTime dataFinal) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		
		CriteriaQuery<Fatura> criteria = builder.createQuery(Fatura.class);
		
		Root<Fatura> root = criteria.from(Fatura.class); // from Fatura
		
		List<Predicate> predicados = new ArrayList<Predicate>();
		
		if(dataInicial != null)
			predicados.add(builder.greaterThanOrEqualTo(root.get("dataSaida"), dataInicial)); // where dataCriacao
		
		if(dataFinal != null)
			predicados.add(builder.lessThanOrEqualTo(root.get("dataSaida"), dataFinal));
		
		criteria.where(predicados.toArray(new Predicate[0]));
		
		return manager.createQuery(criteria).getResultList();
	}

}
