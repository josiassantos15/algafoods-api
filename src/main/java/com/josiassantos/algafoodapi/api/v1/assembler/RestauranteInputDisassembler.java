package com.josiassantos.algafoodapi.api.v1.assembler;

import com.josiassantos.algafoodapi.api.v1.model.input.RestauranteInput;
import com.josiassantos.algafoodapi.domain.model.Cidade;
import com.josiassantos.algafoodapi.domain.model.Cozinha;
import com.josiassantos.algafoodapi.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestauranteInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Restaurante toDomainObject(RestauranteInput restauranteInput) {
		return modelMapper.map(restauranteInput, Restaurante.class);
	}
	
	public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante) {
		restaurante.setCozinha(new Cozinha());
		
		if (restaurante.getEndereco() != null) {
			restaurante.getEndereco().setCidade(new Cidade());
		}
		
		modelMapper.map(restauranteInput, restaurante);
	}
	
}
