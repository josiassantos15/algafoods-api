package com.josiassantos.algafoodapi.api.v1.assembler;

import com.josiassantos.algafoodapi.api.v1.AlgaLinks;
import com.josiassantos.algafoodapi.api.v1.model.PermissaoModel;
import com.josiassantos.algafoodapi.core.security.AlgaSecurity;
import com.josiassantos.algafoodapi.domain.model.Permissao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PermissaoModelAssembler 
		implements RepresentationModelAssembler<Permissao, PermissaoModel> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@Autowired
	private AlgaSecurity algaSecurity;

	@Override
	public PermissaoModel toModel(Permissao permissao) {
		PermissaoModel permissaoModel = modelMapper.map(permissao, PermissaoModel.class);
		return permissaoModel;
	}
	
	@Override
	public CollectionModel<PermissaoModel> toCollectionModel(Iterable<? extends Permissao> entities) {
		CollectionModel<PermissaoModel> collectionModel 
			= RepresentationModelAssembler.super.toCollectionModel(entities);

		if (algaSecurity.podeConsultarUsuariosGruposPermissoes()) {
			collectionModel.add(algaLinks.linkToPermissoes());
		}
		
		return collectionModel;
	}
	
}
