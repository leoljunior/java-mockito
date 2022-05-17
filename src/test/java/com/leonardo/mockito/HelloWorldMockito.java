package com.leonardo.mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.leonardo.mockito.dao.LeilaoDao;
import com.leonardo.mockito.model.Leilao;

public class HelloWorldMockito {

	@Test
	void hello() {
		LeilaoDao mock = Mockito.mock(LeilaoDao.class);
		List<Leilao> lista =  mock.buscarTodos(); //mockito simula o metodo buscar todos que devolve uma lista de leilões, neste caso retornará uma lista vazia
		assertTrue(lista.isEmpty()); //o teste passa, confirmando que a lista esta vazia
	}
	
}
