package com.josiassantos.algafoodapi.domain.service;


import com.josiassantos.algafoodapi.domain.filter.VendaDiariaFilter;

public interface VendaReportService {

	byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset);
	
}
