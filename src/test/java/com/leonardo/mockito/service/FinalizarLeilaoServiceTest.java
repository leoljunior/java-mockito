package com.leonardo.mockito.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.leonardo.mockito.dao.LeilaoDao;
import com.leonardo.mockito.model.Lance;
import com.leonardo.mockito.model.Leilao;
import com.leonardo.mockito.model.Usuario;

class FinalizarLeilaoServiceTest {

	private FinalizarLeilaoService service; //classe a ser testada
	
	@Mock //indica qual atributo são mocks, o mesmo que fazer LeilaoDao leilaoDaoMock =  Mockito.mock(LeilaoDao.class); - porem deve ser iniciado
	private LeilaoDao leilaoDaoMock; //usaremos beforeEach do junit para iniciar
	
	@Mock
	private EnviadorDeEmails enviadorDeEmails;
	
	@BeforeEach //usando o beferoEach do junit, antes de cada testeminiciamos o mock e o service
	public void iniciar() {
		MockitoAnnotations.openMocks(this);
		this.service = new FinalizarLeilaoService(leilaoDaoMock, enviadorDeEmails);
	}
	
	
	
	@Test
	void deveriaFinalizarUmLeilao() {
		List<Leilao> leiloes = leiloes();
		
		//Aqui mostramos para o mockito que quando(when) o metodo X for chamado então retornamos(thenReturn) a lista de leilões, isso para que o mock não retorne
		Mockito.when(leilaoDaoMock.buscarLeiloesExpirados()).thenReturn(leiloes);//uma lista vazia. ou seja simulaa o comportamento
		
		service.finalizarLeiloesExpirados();//chamando o metodo a ser testado, finalizarLeiloesExpirados
		
		
		//Agora fazemos os asserts
		Leilao leilao = leiloes.get(0); //como na lista temos somente um leilão, guardamos ele numa variavel
		
		assertTrue(leilao.isFechado()); //na classe de serviço o metodo finalizarLeiloesExpirados() chama o metodo leilao.fechar(); para setar o atributo fechado para true, aqui verificamos isso
 		assertEquals(leilao.getLanceVencedor().getValor(), new BigDecimal("900")); //a classe de serviço tambem seta o lançe vencedor, aqui testamos se o lance vencedor foi setado corretamente
 		
 		//por fim a classe de serviço chama o metodo salvar, que é um metodo que esta dentro do nosso mock, leilaoDao, logo tem um metodo especifico para testar se uma
 		//classe dentro de um mock foi chamada, metodo verify
 		Mockito.verify(leilaoDaoMock).salvar(leilao);
	}

	
	@Test
	void deveriaEnviarEmailParaLeilaoVencedor() {
		List<Leilao> leiloes = leiloes();		
		Mockito.when(leilaoDaoMock.buscarLeiloesExpirados()).thenReturn(leiloes); 		
		service.finalizarLeiloesExpirados();		
		Mockito.verify(enviadorDeEmails).enviarEmailVencedorLeilao(leiloes.get(0).getLanceVencedor()); 		
	}
	
	@Test
	void naoDeveriaEnviarEmailAoVencedorEmCasoDeErroAoEncerrarOLeilao() {
		List<Leilao> leiloes = leiloes();		
		Mockito.when(leilaoDaoMock.buscarLeiloesExpirados()).thenReturn(leiloes); 
		
		//Aqui quando o leilaoDaoMock.salvar(Mockito.any()) for executado com qualquer parametro(Mockito.any)
		//lançamos uma RuntimeException
		Mockito.when(leilaoDaoMock.salvar(Mockito.any())).thenThrow(RuntimeException.class);	
		
		//Tem que ser colocado no try catch por causa da exception que sera lançada, se não tratada aqui o service nem executa o finalizarLeiloesExpirados abaixo.
		try {			
			service.finalizarLeiloesExpirados();
			Mockito.verifyNoInteractions(enviadorDeEmails);//verificamos se houve interação com o mock enviadorDeEmails			
		} catch (Exception e) {}
		
			
	}
	
	
	private List<Leilao> leiloes() {
		List<Leilao> lista = new ArrayList<>();		
		
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Alessa"));
		
		Lance primeiro = new Lance(new Usuario("Anne"), new BigDecimal("600"));
		Lance segundo = new Lance(new Usuario("Danny"), new BigDecimal("900"));
		
		leilao.propoe(primeiro);
		leilao.propoe(segundo);
		
		lista.add(leilao);
		
		return lista;
	}
	
}
