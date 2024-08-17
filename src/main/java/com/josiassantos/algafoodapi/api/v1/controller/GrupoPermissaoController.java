package com.josiassantos.algafoodapi.api.v1.controller;

import com.josiassantos.algafoodapi.api.v1.AlgaLinks;
import com.josiassantos.algafoodapi.api.v1.assembler.PermissaoModelAssembler;
import com.josiassantos.algafoodapi.api.v1.model.PermissaoModel;
import com.josiassantos.algafoodapi.api.v1.openapi.controller.GrupoPermissaoControllerOpenApi;
import com.josiassantos.algafoodapi.core.security.AlgaSecurity;
import com.josiassantos.algafoodapi.core.security.CheckSecurity;
import com.josiassantos.algafoodapi.domain.model.Grupo;
import com.josiassantos.algafoodapi.domain.service.CadastroGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v1/grupos/{grupoId}/permissoes",
		produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoPermissaoController implements GrupoPermissaoControllerOpenApi {

	@Autowired
	private CadastroGrupoService cadastroGrupo;
	
	@Autowired
	private PermissaoModelAssembler permissaoModelAssembler;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@Autowired
	private AlgaSecurity algaSecurity;
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@Override
	@GetMapping
	public CollectionModel<PermissaoModel> listar(@PathVariable Long grupoId) {
		Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);
		
		CollectionModel<PermissaoModel> permissoesModel 
			= permissaoModelAssembler.toCollectionModel(grupo.getPermissoes())
				.removeLinks();
		
		permissoesModel.add(algaLinks.linkToGrupoPermissoes(grupoId));
		
		if (algaSecurity.podeEditarUsuariosGruposPermissoes()) {
			permissoesModel.add(algaLinks.linkToGrupoPermissaoAssociacao(grupoId, "associar"));
		
			permissoesModel.getContent().forEach(permissaoModel -> {
				permissaoModel.add(algaLinks.linkToGrupoPermissaoDesassociacao(
						grupoId, permissaoModel.getId(), "desassociar"));
			});
		}
		
		return permissoesModel;
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@DeleteMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		cadastroGrupo.desassociarPermissao(grupoId, permissaoId);
		
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@PutMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> associar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		cadastroGrupo.associarPermissao(grupoId, permissaoId);
		
		return ResponseEntity.noContent().build();
	}

}
