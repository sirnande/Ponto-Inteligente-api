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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.para.pontointeligente.api.dtos.CadastroPFDTO;
import com.para.pontointeligente.api.enums.PerfilEnum;
import com.para.pontointeligente.api.model.Empresa;
import com.para.pontointeligente.api.model.Funcionario;
import com.para.pontointeligente.api.response.Response;
import com.para.pontointeligente.api.services.EmpresaService;
import com.para.pontointeligente.api.services.FuncionarioService;
import com.para.pontointeligente.api.utils.PasswordUtils;


@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class CadastroPFController {

	private static final Logger log = LoggerFactory.getLogger(CadastroPFController.class);
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private FuncionarioService funcionarioService;

	public CadastroPFController() {
	}

	
	
	@PostMapping
	public ResponseEntity<Response<CadastroPFDTO>> cadastrar(@Valid @RequestBody CadastroPFDTO cadastroPFDTO,
			BindingResult result) throws NoSuchAlgorithmException {
		log.info("Cadastrando PF: {}", cadastroPFDTO.toString());
		Response<CadastroPFDTO> response = new Response<CadastroPFDTO>();

		validarDadosExistentes(cadastroPFDTO, result);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPFDTO, result);

		if (result.hasErrors()) {
			log.error("Erro validando dados de cadastro PF: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDTO.getCnpj());
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		this.funcionarioService.persistir(funcionario);

		response.setData(this.converterCadastroPFDto(funcionario));
		return ResponseEntity.ok(response);
	}

	
	private void validarDadosExistentes(CadastroPFDTO cadastroPFDTO, BindingResult result) {
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDTO.getCnpj());
		if (!empresa.isPresent()) {
			result.addError(new ObjectError("empresa", "Empresa não cadastrada."));
		}
		
		this.funcionarioService.buscarPorCpf(cadastroPFDTO.getCpf())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente.")));

		this.funcionarioService.buscarPorEmail(cadastroPFDTO.getEmail())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente.")));
	}

	
	private Funcionario converterDtoParaFuncionario(CadastroPFDTO cadastroPFDTO, BindingResult result)
			throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPFDTO.getNome());
		funcionario.setEmail(cadastroPFDTO.getEmail());
		funcionario.setCpf(cadastroPFDTO.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPFDTO.getSenha()));
		cadastroPFDTO.getQtdHorasAlmoco()
				.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		cadastroPFDTO.getQtdHorasTrabalhoDia()
				.ifPresent(qtdHorasTrabDia -> funcionario.setQtdHorasTrabalhadoDia(Float.valueOf(qtdHorasTrabDia)));
		cadastroPFDTO.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

		return funcionario;
	}

	
	
	private CadastroPFDTO converterCadastroPFDto(Funcionario funcionario) {
		CadastroPFDTO cadastroPFDTO = new CadastroPFDTO();
		cadastroPFDTO.setId(funcionario.getId());
		cadastroPFDTO.setNome(funcionario.getNome());
		cadastroPFDTO.setEmail(funcionario.getEmail());
		cadastroPFDTO.setCpf(funcionario.getCpf());
		cadastroPFDTO.setCnpj(funcionario.getEmpresa().getCnpj());
		funcionario.getQtdHorasAlmocoOpt().ifPresent(qtdHorasAlmoco -> cadastroPFDTO
				.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(
				qtdHorasTrabDia -> cadastroPFDTO.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
		funcionario.getValorHoraOpt()
				.ifPresent(valorHora -> cadastroPFDTO.setValorHora(Optional.of(valorHora.toString())));

		return cadastroPFDTO;
	}

}
