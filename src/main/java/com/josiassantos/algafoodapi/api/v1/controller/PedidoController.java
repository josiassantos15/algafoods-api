package com.josiassantos.algafoodapi.api.v1.controller;


import com.josiassantos.algafoodapi.api.v1.assembler.PedidoInputDisassembler;
import com.josiassantos.algafoodapi.api.v1.assembler.PedidoModelAssembler;
import com.josiassantos.algafoodapi.api.v1.assembler.PedidoResumoModelAssembler;
import com.josiassantos.algafoodapi.api.v1.model.PedidoModel;
import com.josiassantos.algafoodapi.api.v1.model.PedidoResumoModel;
import com.josiassantos.algafoodapi.api.v1.model.input.PedidoInput;
import com.josiassantos.algafoodapi.api.v1.openapi.controller.PedidoControllerOpenApi;
import com.josiassantos.algafoodapi.core.data.PageWrapper;
import com.josiassantos.algafoodapi.core.data.PageableTranslator;
import com.josiassantos.algafoodapi.core.security.AlgaSecurity;
import com.josiassantos.algafoodapi.core.security.CheckSecurity;
import com.josiassantos.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.josiassantos.algafoodapi.domain.exception.NegocioException;
import com.josiassantos.algafoodapi.domain.filter.PedidoFilter;
import com.josiassantos.algafoodapi.domain.model.Pedido;
import com.josiassantos.algafoodapi.domain.model.Usuario;
import com.josiassantos.algafoodapi.domain.repository.PedidoRepository;
import com.josiassantos.algafoodapi.domain.service.EmissaoPedidoService;
import com.josiassantos.algafoodapi.infrastructure.repository.spec.PedidoSpecs;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/v1/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController implements PedidoControllerOpenApi {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private EmissaoPedidoService emissaoPedido;
	
	@Autowired
	private PedidoModelAssembler pedidoModelAssembler;
	
	@Autowired
	private PedidoResumoModelAssembler pedidoResumoModelAssembler;
	
	@Autowired
	private PedidoInputDisassembler pedidoInputDisassembler;
	
	@Autowired
	private PagedResourcesAssembler<Pedido> pagedResourcesAssembler;
	
	@Autowired
	private AlgaSecurity algaSecurity;
	
	@CheckSecurity.Pedidos.PodePesquisar
	@Override
	@GetMapping
	public PagedModel<PedidoResumoModel> pesquisar(PedidoFilter filtro,
												   @PageableDefault(size = 10) Pageable pageable) {
		Pageable pageableTraduzido = traduzirPageable(pageable);
		
		Page<Pedido> pedidosPage = pedidoRepository.findAll(
				PedidoSpecs.usandoFiltro(filtro), pageableTraduzido);
		
		pedidosPage = new PageWrapper<>(pedidosPage, pageable);
		
		return pagedResourcesAssembler.toModel(pedidosPage, pedidoResumoModelAssembler);
	}
	
	@CheckSecurity.Pedidos.PodeCriar
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
		try {
			Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

			novoPedido.setCliente(new Usuario());
			novoPedido.getCliente().setId(algaSecurity.getUsuarioId());

			novoPedido = emissaoPedido.emitir(novoPedido);

			return pedidoModelAssembler.toModel(novoPedido);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@CheckSecurity.Pedidos.PodeBuscar
	@Override
	@GetMapping("/{codigoPedido}")
	public PedidoModel buscar(@PathVariable String codigoPedido) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
		
		return pedidoModelAssembler.toModel(pedido);
	}
	
	private Pageable traduzirPageable(Pageable apiPageable) {
		var mapeamento = Map.of(
				"codigo", "codigo",
				"subtotal", "subtotal",
				"taxaFrete", "taxaFrete",
				"valorTotal", "valorTotal",
				"dataCriacao", "dataCriacao",
				"restaurante.nome", "restaurante.nome",
				"restaurante.id", "restaurante.id",
				"cliente.id", "cliente.id",
				"cliente.nome", "cliente.nome"
			);
		
		return PageableTranslator.translate(apiPageable, mapeamento);
	}
	
}
