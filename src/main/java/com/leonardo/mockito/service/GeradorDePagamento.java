package com.leonardo.mockito.service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leonardo.mockito.dao.PagamentoDao;
import com.leonardo.mockito.model.Lance;
import com.leonardo.mockito.model.Pagamento;

@Service
public class GeradorDePagamento {
	
	private PagamentoDao pagamentos;
	private Clock clock; //Clock abstração para horario para conseguir testar o metodo com a classe static LocalDate.now
	
	@Autowired
	public GeradorDePagamento(PagamentoDao pagamentos, Clock clock) {
		this.pagamentos = pagamentos;
		this.clock = clock;
	}

	public void gerarPagamento(Lance lanceVencedor) {
		LocalDate vencimento = LocalDate.now(clock).plusDays(1);
		Pagamento pagamento = new Pagamento(lanceVencedor, proximoDiaUtil(vencimento));
		this.pagamentos.salvar(pagamento);
	}

	private LocalDate proximoDiaUtil(LocalDate dataBase) {
		DayOfWeek diaDaSemana = dataBase.getDayOfWeek();
		if (diaDaSemana == DayOfWeek.SATURDAY) {
			return dataBase.plusDays(2);
		} else if (diaDaSemana == DayOfWeek.SUNDAY) {
			return dataBase.plusDays(1);
		}
		return dataBase;
	}

}
