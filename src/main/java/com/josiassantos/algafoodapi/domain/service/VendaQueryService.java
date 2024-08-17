package com.josiassantos.algafoodapi.domain.service;


import com.josiassantos.algafoodapi.domain.filter.VendaDiariaFilter;
import com.josiassantos.algafoodapi.domain.model.dto.VendaDiaria;

import java.util.List;

public interface VendaQueryService {

	List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset);
	
}
