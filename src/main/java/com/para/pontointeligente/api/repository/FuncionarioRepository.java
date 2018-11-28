package com.para.pontointeligente.api.repository;

import com.para.pontointeligente.api.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true) // ja que todos vão ser de consuta e em vez de colocar essa notação em cada um
//aqui será colocado aqui em cima que vale para todo
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Funcionario findByCpf(String cpf);

    Funcionario findByEmail(String email);

    Funcionario findByCpfOrEmail(String cpf, String email);

}
