package com.josiassantos.algafoodapi.domain.repository;

import com.josiassantos.algafoodapi.domain.model.Cozinha;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CozinhaRepository extends CustomJpaRepository<Cozinha, Long> {

	List<Cozinha> findTodasByNomeContaining(String nome);
	
	Optional<Cozinha> findByNome(String nome);
	
	boolean existsByNome(String nome);
	
}
