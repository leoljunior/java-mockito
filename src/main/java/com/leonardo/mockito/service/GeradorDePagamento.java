package com.leonardo.mockito.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leonardo.mockito.dao.PagamentoDao;
import com.leonardo.mockito.model.Lance;
import com.leonardo.mockito.model.Pagamento;

@Service
public class GeradorDePagamento {
	
	private PagamentoDao pagamentos;
	
	@Autowired
	public GeradorDePagamento(PagamentoDao pagamentos) {
		this.pagamentos = pagamentos;
	}

	public void gerarPagamento(Lance lanceVencedor) {
		LocalDate vencimento = LocalDate.now().plusDays(1);
		Pagamento pagamento = new Pagamento(lanceVencedor, vencimento);
		this.pagamentos.salvar(pagamento);
	}

}
