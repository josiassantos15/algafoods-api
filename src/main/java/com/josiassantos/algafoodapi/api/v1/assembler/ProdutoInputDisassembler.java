package com.josiassantos.algafoodapi.api.v1.assembler;

import com.josiassantos.algafoodapi.api.v1.model.input.ProdutoInput;
import com.josiassantos.algafoodapi.domain.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProdutoInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Produto toDomainObject(ProdutoInput produtoInput) {
		return modelMapper.map(produtoInput, Produto.class);
	}
	
	public void copyToDomainObject(ProdutoInput produtoInput, Produto produto) {
		modelMapper.map(produtoInput, produto);
	}
	
}
