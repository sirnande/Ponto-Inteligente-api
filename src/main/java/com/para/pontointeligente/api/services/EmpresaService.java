package com.para.pontointeligente.api.services;

import java.util.Optional;

import com.para.pontointeligente.api.model.Empresa;

public interface EmpresaService {
	
	/**
	 *Retorna uma empresa por um cnpj
	 *
	 * 
	 * @param cnpj
	 * @return Optional<Empresa>
	 */
	
	Optional<Empresa> buscarPorCnpj(String cnpj);

	
	/**
	 * Cadastra uma nova empresa na base de dados
	 * 
	 * 
	 * 
	 * @param empresa
	 * @return Empresa
	 */
	
	Empresa persistir(Empresa empresa);
	
}
