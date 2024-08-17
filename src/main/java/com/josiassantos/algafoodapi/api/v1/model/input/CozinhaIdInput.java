package com.josiassantos.algafoodapi.api.v1.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CozinhaIdInput {

	@Schema(example = "1")
	@NotNull
	private Long id;

}
