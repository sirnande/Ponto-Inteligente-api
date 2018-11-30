package com.para.pontointeligente.api.services;

import java.util.Optional;

import com.para.pontointeligente.api.model.Funcionario;

public interface FuncionarioService {
	
	/**Persiste um fucnionario na base de dados
	 * 
	 * @param funcionario
	 * @return Funcionario
	 */
	
	Funcionario persistir(Funcionario funcionario);

	
	/**
	 * Buscar e retorna um Funcionario dado um CPF
	 * 
	 * @param cpf
	 * @return Funcionario
	 */
	
	Optional<Funcionario> buscarPorCpf(String cpf);
	
	
	/**
	 * Buscar e Retorna um Funcionario por email
	 * 
	 * @param email
	 * @return Funcionario
	 */
	
	Optional<Funcionario> buscarPorEmail(String email);
	
	
	/**
	 * Buscar e retorna um Funcionario por id
	 * 
	 * @param id
	 * @return Funcionario
	 */
	
	Optional<Funcionario> buscarPorId(Long id);
}
