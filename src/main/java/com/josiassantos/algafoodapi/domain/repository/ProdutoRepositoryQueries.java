package com.josiassantos.algafoodapi.domain.repository;


import com.josiassantos.algafoodapi.domain.model.FotoProduto;

public interface ProdutoRepositoryQueries {

	FotoProduto save(FotoProduto foto);
	
	void delete(FotoProduto foto);
	
}
