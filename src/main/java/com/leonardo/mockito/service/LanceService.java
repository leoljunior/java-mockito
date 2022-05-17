package com.leonardo.mockito.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leonardo.mockito.dao.LanceDao;
import com.leonardo.mockito.dao.LeilaoDao;
import com.leonardo.mockito.dao.UsuarioDao;
import com.leonardo.mockito.dto.NovoLanceDto;
import com.leonardo.mockito.model.Lance;
import com.leonardo.mockito.model.Leilao;
import com.leonardo.mockito.model.Usuario;

@Service
public class LanceService {

	@Autowired
	private LanceDao lances;

	@Autowired
	private UsuarioDao usuarios;

	@Autowired
	private LeilaoDao leiloes;

	public boolean propoeLance(NovoLanceDto lanceDto, String nomeUsuario) {

		Usuario usuario = usuarios.buscarPorUsername(nomeUsuario);
		Lance lance = lanceDto.toLance(usuario);

		Leilao leilao = this.getLeilao(lanceDto.getLeilaoId());

		if (leilao.propoe(lance)) {
			lances.salvar(lance);
			return true;
		}

		return false;
	}

	public Leilao getLeilao(Long leilaoId) {
		return leiloes.buscarPorId(leilaoId);
	}

}