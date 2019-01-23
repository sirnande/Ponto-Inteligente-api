package com.para.pontointeligente.api.services.imp;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.para.pontointeligente.api.model.Lancamento;
import com.para.pontointeligente.api.repository.LancamentoRepository;
import com.para.pontointeligente.api.services.LancamentoService;

@Service
public class LancamentoServiceImplimentation implements LancamentoService{

	private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImplimentation.class);
	
	@Autowired
	private LancamentoRepository lacamentoRepository;
	

	public Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest) {
		log.info("Buscando laçamento para o funcionario ID: {}", funcionarioId);
		
		return this.lacamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
	}

	@Cacheable("lancamentoPorId")
	public Optional<Lancamento> buscarPorId(Long id) {
		log.info("Buacando por id: {}", id);		
		return Optional.ofNullable(this.lacamentoRepository.findOne(id));
	}

	@CachePut("lancamentoPorId")
	public Lancamento persistir(Lancamento lancamento) {
		log.info("Persistindo o laçamento: {}", lancamento);
		return this.lacamentoRepository.save(lancamento);
	}
	
	
	public void remover(Long id) {
		log.info("Removendo o lacamento ID: {}", id);
		this.lacamentoRepository.delete(id);
	}

	
}
