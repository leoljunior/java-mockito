package com.leonardo.mockito.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leonardo.mockito.model.Lance;
import com.leonardo.mockito.model.Leilao;

@Repository
public class LanceDao {

	private EntityManager em;

	@Autowired
	public LanceDao(EntityManager em) {
		this.em = em;
	}
	public void salvar(Lance lance) {
		em.persist(lance);
	}

	public Lance buscarMaiorLanceDoLeilao(Leilao leilao) {
		return em.createQuery("SELECT l FROM Lance l WHERE l.valor = (SELECT MAX(lance.valor) FROM Lance lance)", Lance.class)
				.setParameter("leilao", leilao)
				.getSingleResult();
	}
	
}
