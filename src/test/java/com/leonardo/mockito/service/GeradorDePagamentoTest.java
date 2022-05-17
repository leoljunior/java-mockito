package com.leonardo.mockito.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.leonardo.mockito.dao.PagamentoDao;
import com.leonardo.mockito.model.Lance;
import com.leonardo.mockito.model.Leilao;
import com.leonardo.mockito.model.Pagamento;
import com.leonardo.mockito.model.Usuario;

class GeradorDePagamentoTest {

	private GeradorDePagamento geradorDePagamento;
	
	@Mock
	private PagamentoDao pagamentoDaoMock; 
	
	@Captor //capturar objeto com Mockito
	private ArgumentCaptor<Pagamento> pagamentoCaptor;
	
	@BeforeEach
	private void iniciar() {
		MockitoAnnotations.openMocks(this);
		this.geradorDePagamento = new GeradorDePagamento(pagamentoDaoMock);
	}
	
	
	@Test
	void deveriaGerarPagamentoParaLeilaoVencedor() {
		Leilao leilao = leilao();
		Lance lanceVencedor = leilao.getLances().get(0);
		geradorDePagamento.gerarPagamento(lanceVencedor);
		
		
		//Aqui temos que passar um pagamento para o metodo salvar, porem o pagamento é instanciado dentro da classe pagamentoDao
		//então para passar esse parametro nós podemos captura-lo com o @Captor do Mockito.
		Mockito.verify(pagamentoDaoMock).salvar(pagamentoCaptor.capture());//aqui qdo o metodo é chamado ja capturamos o parametro passado
		
		Pagamento pagamento = pagamentoCaptor.getValue(); //aqui pegamos o valor do parametro
		
		assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento()); //aqui testamos se a data de vencimento do pagto está correta, 1 dia apos vencimento
		
		assertEquals(lanceVencedor.getValor(), pagamento.getValor()); //aqui testamos se o valor do pagamento gerado é igual ao do lançe dado
		
		assertEquals(lanceVencedor.getUsuario(), pagamento.getUsuario()); //aqui testamos se o usuario é o mesmo
		
		assertEquals(leilao, pagamento.getLeilao());//aqui testamos se o leilao é o mesmo
		
	}

	
	private Leilao leilao() {
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Alessa"));
		
		Lance segundo = new Lance(new Usuario("Danny"), new BigDecimal("900"));
		
		leilao.propoe(segundo);
		
		return leilao;
	}
}
