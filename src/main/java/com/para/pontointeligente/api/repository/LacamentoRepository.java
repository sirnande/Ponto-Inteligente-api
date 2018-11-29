package com.para.pontointeligente.api.repository;

import com.para.pontointeligente.api.model.Lancamento;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;

//@NoRepositoryBean
@Repository
@Transactional(readOnly = true)
@NamedQueries({
        @NamedQuery(name = "LacamentoRepository.findByFuncionarioId",
                query = "SELECT lanc "
                        + "FROM Lancamento lanc "
                        + "WHERE lanc.funcionario.id = :funcionarioId")})
public interface LacamentoRepository extends JpaRepository<Lancamento, Long> {

    List<Lancamento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId);

    Page<Lancamento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId, Pageable pageable);

}
