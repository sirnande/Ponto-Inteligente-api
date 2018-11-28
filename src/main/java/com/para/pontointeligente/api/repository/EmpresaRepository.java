package com.para.pontointeligente.api.repository;

import com.para.pontointeligente.api.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    @Transactional(readOnly = true)
        //por se tratar de um método só de consulta ele faz um select no banco
        //ele nao precisa fazer trasanção e nem bloquear o banco de dados
    Empresa findByCnpj(String cnpj);

}
