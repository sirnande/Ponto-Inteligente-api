package com.para.pontointeligente.api.model;

import javax.persistence.*;

import com.para.pontointeligente.api.enums.PerfilEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "funcionario")
public class Funcionario implements Serializable {


    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private BigDecimal valorHora;
    private float qtdHorasTrabalhadoDia;
    private float qtdHorasAlmoco;
    private PerfilEnum perfil;
    private Date dataCriacao;
    private Date dataAtualizacao;
    private Empresa empresa;
    private List<Lancamento> lancamento;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "nome", nullable = false)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "email", nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "senha", nullable = false)
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Column(name = "cpf", nullable = false)
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Column(name = "valor_hora", nullable = false)
    public BigDecimal getValorHora() {
        return valorHora;
    }

    public void setValorHora(BigDecimal valorHora) {
        this.valorHora = valorHora;
    }


    @Transient
    // Significa que o JPA deve ignorar este método, pois ele não estar relacionado com implementação o mapeamento com o banco de dado
    //pois ele deve estar aqui por algum motivo
    public Optional<BigDecimal> getValorHoraOpt() {
        return Optional.ofNullable(valorHora);
    }


    @Column(name = "qtd-horas_trabalho_dia", nullable = false)
    public float getQtdHorasTrabalhadoDia() {
        return qtdHorasTrabalhadoDia;
    }

    public void setQtdHorasTrabalhadoDia(float qtdHorasTrabalhadoDia) {
        this.qtdHorasTrabalhadoDia = qtdHorasTrabalhadoDia;
    }


    @Transient
    public Optional<Float> getQtdHorasTrabalhoDiaOpt() {
        return Optional.ofNullable(qtdHorasTrabalhadoDia);
    }


    @Column(name = "qtd_hora_almoco", nullable = false)
    public float getQtdHorasAlmoco() {
        return qtdHorasAlmoco;
    }

    public void setQtdHorasAlmoco(float qtdHorasAlmoco) {
        this.qtdHorasAlmoco = qtdHorasAlmoco;
    }


    @Transient
    public Optional<Float> getQtdHorasAlmocoOpt() {
        return Optional.ofNullable(qtdHorasAlmoco);
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false)
    public PerfilEnum getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilEnum perfil) {
        this.perfil = perfil;
    }

    @Column(name = "data_criacao", nullable = false)
    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }


    @Column(name = "data_atualizacao", nullable = false)
    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    //Mapeamneto de muito fuincionario para uma empresa do tipo EAGER pois apos carregar o funcionário eu ccarrego junto os dados da emrpesa
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }


    @OneToMany(mappedBy = "funcionario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Lancamento> getLancamento() {
        return lancamento;
    }

    public void setLancamento(List<Lancamento> lancamento) {
        this.lancamento = lancamento;
    }

    @PreUpdate
    public void preUpdate() {
        dataAtualizacao = new Date();
    }

    @PrePersist
    public void prePerist() {
        final Date atual = new Date();
        dataCriacao = atual;
        dataAtualizacao = atual;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }


}
