package com.para.pontointeligente.api.services;

import org.springframework.stereotype.Service;

import com.para.pontointeligente.api.model.Lancamento;

@Service
public interface LancamentoService {

	/**
	 * Persisti um lancamento na base de dados
	 * 
	 * @param lancamento
	 * @return
	 */
	
	Lancamento persistir(Lancamento lancamento);
	
	
	
	
	
}
