package com.josiassantos.algafoodapi.domain.service;

import com.josiassantos.algafoodapi.domain.exception.PermissaoNaoEncontradaException;
import com.josiassantos.algafoodapi.domain.model.Permissao;
import com.josiassantos.algafoodapi.domain.repository.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastroPermissaoService {

	@Autowired
	private PermissaoRepository permissaoRepository;
	
	public Permissao buscarOuFalhar(Long permissaoId) {
		return permissaoRepository.findById(permissaoId)
			.orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
	}
	
}
