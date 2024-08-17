package com.josiassantos.algafoodapi.api.v1.controller;

import com.josiassantos.algafoodapi.api.v1.assembler.GrupoInputDisassembler;
import com.josiassantos.algafoodapi.api.v1.assembler.GrupoModelAssembler;
import com.josiassantos.algafoodapi.api.v1.model.GrupoModel;
import com.josiassantos.algafoodapi.api.v1.model.input.GrupoInput;
import com.josiassantos.algafoodapi.api.v1.openapi.controller.GrupoControllerOpenApi;
import com.josiassantos.algafoodapi.core.security.CheckSecurity;
import com.josiassantos.algafoodapi.domain.model.Grupo;
import com.josiassantos.algafoodapi.domain.repository.GrupoRepository;
import com.josiassantos.algafoodapi.domain.service.CadastroGrupoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoController implements GrupoControllerOpenApi {

	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private CadastroGrupoService cadastroGrupo;
	
	@Autowired
	private GrupoModelAssembler grupoModelAssembler;
	
	@Autowired
	private GrupoInputDisassembler grupoInputDisassembler;
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@Override
	@GetMapping
	public CollectionModel<GrupoModel> listar() {
		List<Grupo> todosGrupos = grupoRepository.findAll();
		
		return grupoModelAssembler.toCollectionModel(todosGrupos);
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@Override
	@GetMapping("/{grupoId}")
	public GrupoModel buscar(@PathVariable Long grupoId) {
		Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);
		
		return grupoModelAssembler.toModel(grupo);
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GrupoModel adicionar(@RequestBody @Valid GrupoInput grupoInput) {
		Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput);
		
		grupo = cadastroGrupo.salvar(grupo);
		
		return grupoModelAssembler.toModel(grupo);
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@PutMapping("/{grupoId}")
	public GrupoModel atualizar(@PathVariable Long grupoId,
			@RequestBody @Valid GrupoInput grupoInput) {
		Grupo grupoAtual = cadastroGrupo.buscarOuFalhar(grupoId);
		
		grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);
		
		grupoAtual = cadastroGrupo.salvar(grupoAtual);
		
		return grupoModelAssembler.toModel(grupoAtual);
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@DeleteMapping("/{grupoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> remover(@PathVariable Long grupoId) {
		cadastroGrupo.excluir(grupoId);
		return ResponseEntity.noContent().build();
	}
	
}
