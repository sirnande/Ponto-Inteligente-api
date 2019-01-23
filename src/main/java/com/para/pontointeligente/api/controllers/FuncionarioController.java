package com.para.pontointeligente.api.controllers;



import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.para.pontointeligente.api.dtos.FuncionarioDTO;
import com.para.pontointeligente.api.model.Funcionario;
import com.para.pontointeligente.api.response.Response;
import com.para.pontointeligente.api.services.FuncionarioService;
import com.para.pontointeligente.api.utils.PasswordUtils;


@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

	private static final Logger log = LoggerFactory.getLogger(FuncionarioController.class);

	@Autowired
	private FuncionarioService funcionarioService;

	public FuncionarioController() {
	}

	/**
	 * Atualiza os dados de um funcionário.
	 * 
	 * @param id
	 * @param funcionarioDto
	 * @param result
	 * @return ResponseEntity<Response<FuncionarioDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<FuncionarioDTO>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody FuncionarioDTO funcionarioDTO, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Atualizando funcionário: {}", funcionarioDTO.toString());
		Response<FuncionarioDTO> response = new Response<FuncionarioDTO>();

		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(id);
		if (!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionário não encontrado."));
		}

		this.atualizarDadosFuncionario(funcionario.get(), funcionarioDTO, result);

		if (result.hasErrors()) {
			log.error("Erro validando funcionário: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.funcionarioService.persistir(funcionario.get());
		response.setData(this.converterFuncionarioDTO(funcionario.get()));

		return ResponseEntity.ok(response);
	}

	
	private void atualizarDadosFuncionario(Funcionario funcionario, FuncionarioDTO funcionarioDTO, BindingResult result)
			throws NoSuchAlgorithmException {
		funcionario.setNome(funcionarioDTO.getNome());

		if (!funcionario.getEmail().equals(funcionarioDTO.getEmail())) {
			this.funcionarioService.buscarPorEmail(funcionarioDTO.getEmail())
					.ifPresent(func -> result.addError(new ObjectError("email", "Email já existente.")));
			funcionario.setEmail(funcionarioDTO.getEmail());
		}
		
		funcionario.setQtdHorasAlmoco(0);
		funcionarioDTO.getQtdHorasAlmoco()
				.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));

		funcionario.setQtdHorasTrabalhadoDia(0);
		funcionarioDTO.getQtdHorasTrabalhoDia()
				.ifPresent(qtdHorasTrabDia -> funcionario.setQtdHorasTrabalhadoDia(Float.valueOf(qtdHorasTrabDia)));

		funcionario.setValorHora(null);
		funcionarioDTO.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

		if (funcionarioDTO.getSenha().isPresent()) {
			funcionario.setSenha(PasswordUtils.gerarBCrypt(funcionarioDTO.getSenha().get()));
		}
	}

	/**
	 * Retorna um DTO com os dados de um funcionário.
	 * 
	 * @param funcionario
	 * @return FuncionarioDto
	 */
	private FuncionarioDTO converterFuncionarioDTO(Funcionario funcionario 	) {
		FuncionarioDTO funcionarioDTO = new FuncionarioDTO();
		funcionarioDTO.setId(funcionario.getId());
		funcionarioDTO.setEmail(funcionario.getEmail());
		funcionarioDTO.setNome(funcionario.getNome());
		funcionario.getQtdHorasAlmocoOpt().ifPresent(
				qtdHorasAlmoco -> funcionarioDTO.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(
				qtdHorasTrabDia -> funcionarioDTO.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
		funcionario.getValorHoraOpt()
				.ifPresent(valorHora -> funcionarioDTO.setValorHora(Optional.of(valorHora.toString())));

		return funcionarioDTO;
	}

}
