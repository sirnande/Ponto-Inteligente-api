package com.para.pontointeligente.api.controllers;

import java.security.NoSuchAlgorithmException;

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

import com.para.pontointeligente.api.dtos.CadastroPJDTO;
import com.para.pontointeligente.api.enums.PerfilEnum;
import com.para.pontointeligente.api.model.Empresa;
import com.para.pontointeligente.api.model.Funcionario;
import com.para.pontointeligente.api.response.Response;
import com.para.pontointeligente.api.services.EmpresaService;
import com.para.pontointeligente.api.services.FuncionarioService;
import com.para.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastar-pj")
@CrossOrigin(origins = "*")
public class CadastroPJController {

	private static final Logger log = LoggerFactory.getLogger(CadastroPJController.class);

	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	@PostMapping
	public ResponseEntity<Response<CadastroPJDTO>> cadastrar(@Valid @RequestBody CadastroPJDTO cadastroPJDTO, BindingResult result) throws NoSuchAlgorithmException{
		log.info("Cadastrando PJ: {}", cadastroPJDTO.toString());
		Response<CadastroPJDTO> response = new Response<>();
		
		validarDadosExistentes(cadastroPJDTO, result);
		Empresa empresa = this.converterDtoParaEmpresa(cadastroPJDTO);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPJDTO, result);

		if (result.hasErrors()) {
			log.error("Erro validando dados de cadastro PJ: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
		this.funcionarioService.persistir(funcionario);

		response.setData(this.converterCadastroPJDto(funcionario));
		return ResponseEntity.ok(response);
	}
	
	private void validarDadosExistentes(CadastroPJDTO cadastroPJDto, BindingResult result) {
		this.empresaService.buscarPorCnpj(cadastroPJDto.getCnpj())
				.ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existente.")));

		this.funcionarioService.buscarPorCpf(cadastroPJDto.getCpf())
				.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente.")));

		this.funcionarioService.buscarPorEmail(cadastroPJDto.getEmail())
				.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente.")));
	}

	
	private Empresa converterDtoParaEmpresa(CadastroPJDTO cadastroPJDto) {
		Empresa empresa = new Empresa();
		empresa.setCnpj(cadastroPJDto.getCnpj());
		empresa.setRazaoSocial(cadastroPJDto.getRazaoSocial());

		return empresa;
	}

	private Funcionario converterDtoParaFuncionario(CadastroPJDTO cadastroPJDto, BindingResult result)
			throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPJDto.getNome());
		funcionario.setEmail(cadastroPJDto.getEmail());
		funcionario.setCpf(cadastroPJDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPJDto.getSenha()));

		return funcionario;
	}

	private CadastroPJDTO converterCadastroPJDto(Funcionario funcionario) {
		CadastroPJDTO cadastroPJDto = new CadastroPJDTO();
		cadastroPJDto.setId(funcionario.getId());
		cadastroPJDto.setNome(funcionario.getNome());
		cadastroPJDto.setEmail(funcionario.getEmail());
		cadastroPJDto.setCpf(funcionario.getCpf());
		cadastroPJDto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		cadastroPJDto.setCnpj(funcionario.getEmpresa().getCnpj());

		return cadastroPJDto;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
