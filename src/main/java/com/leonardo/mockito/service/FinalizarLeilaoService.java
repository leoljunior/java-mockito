package com.leonardo.mockito.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leonardo.mockito.dao.LeilaoDao;
import com.leonardo.mockito.model.Lance;
import com.leonardo.mockito.model.Leilao;

@Service
public class FinalizarLeilaoService {

	private LeilaoDao leiloes;

	@Autowired //Ao fazer testes é melhor que a injeção de dependencia seja via contrutor, pois assim já passamos o mock  como parametro
	public FinalizarLeilaoService(LeilaoDao leiloes) {
		this.leiloes = leiloes;
	}

	public void finalizarLeiloesExpirados() {
		List<Leilao> expirados = leiloes.buscarLeiloesExpirados();
		expirados.forEach(leilao -> {
			Lance maiorLance = maiorLanceDadoNoLeilao(leilao);
			leilao.setLanceVencedor(maiorLance);
			leilao.fechar();
			leiloes.salvar(leilao);
		});
	}

	private Lance maiorLanceDadoNoLeilao(Leilao leilao) {
//		List<Lance> lancesDoLeilao = leilao.getLances(); o getLances devolve uma lista unmodifiableList que ao fazer o sort lança uma exception, logo mudamos a implementação para
		List<Lance> lancesDoLeilao = new ArrayList<>(leilao.getLances()); //devolver um arrayList
		lancesDoLeilao.sort((lance1, lance2) -> {
			return lance2.getValor().compareTo(lance1.getValor());
		});
		return lancesDoLeilao.get(0);
	}

}
