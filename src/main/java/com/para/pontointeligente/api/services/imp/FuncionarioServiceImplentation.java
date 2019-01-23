package com.para.pontointeligente.api.services.imp;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.para.pontointeligente.api.model.Funcionario;
import com.para.pontointeligente.api.repository.FuncionarioRepository;
import com.para.pontointeligente.api.services.FuncionarioService;

@Service
public class FuncionarioServiceImplentation implements FuncionarioService{

	private static final Logger log = LoggerFactory.getLogger(FuncionarioServiceImplentation.class);
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	
	public Funcionario persistir(Funcionario funcionario) {
		log.info("Persistindo funcionario: {}", funcionario);
		
		return this.funcionarioRepository.save(funcionario);
	}
	
	@Override
	public Optional<Funcionario> buscarPorCpf(String cpf) {
		log.info("Busca funcionario pleo cpf: {}", cpf);
		
		return Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
	}
	
	
	@Override
	public Optional<Funcionario> buscarPorEmail(String email) {
		log.info("Buscar funcionario pelo emial: {}", email);
		
		return Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
		
	}
	
	@Override
	public Optional<Funcionario> buscarPorId(Long id) {
		log.info("Buscar um funcionario pelo id: {}", id);
		
			
		return Optional.ofNullable(this.funcionarioRepository.findOne(id));
	}
	
	
}
