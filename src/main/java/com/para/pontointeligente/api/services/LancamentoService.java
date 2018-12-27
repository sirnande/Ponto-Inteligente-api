package com.para.pontointeligente.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.para.pontointeligente.api.model.Lancamento;

@Service
public interface LancamentoService {

	/**
	 * Persisti um lancamento na base de dados
	 * 
	 * @param lancamento
	 * @return Lancamento
 	 */
	
	Lancamento persistir(Lancamento lancamento);
	
	
	/**
	 * Retorna uma lisat paginada de um determinado funcionario
	 * 
	 * @param funcionarioId
	 * @param pageRequest
	 * @return Page<Lancamento>
	 */
	
	Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest);
	
	
	/**
	 * Retornar um lancamento por id
	 *  
	 * @param id
	 * @return Optional<Lancamento>
	 */
	
	Optional<Lancamento> buscarPorId(Long id);
	
	
	/**
	 * Remove um lancamento na base de dados
	 * 
	 * @param id
	 */
	
	void remover(Long id);
	
}
