package com.para.pontointeligente.api.services.implementation;




import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.para.pontointeligente.api.model.Empresa;
import com.para.pontointeligente.api.repository.EmpresaRepository;
import com.para.pontointeligente.api.services.EmpresaService;

@Service
public class EmpresaServiceImplementation implements EmpresaService{

	private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImplementation.class);
	
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Override
	public Empresa buscarPorCnpj(String cnpj) {
	
		log.info("Bunscando uma empresa pela o CNPJ {}",cnpj);
		
		Optional<Empresa> maybeEmpresa = Optional.ofNullable(empresaRepository.findByCnpj(cnpj));
		
		return maybeEmpresa.orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
	}
	
	
	@Override
	public Empresa persistir(Empresa empresa) {
		log.info("Perssistindo empresa: {}",empresa);
		
		return this.empresaRepository.save(empresa);
	}
	
}
