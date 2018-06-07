package br.com.ifrn.coapac.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
public class Material implements Serializable{
	private static final long serialVersionUID = -741578570814305217L;

	@Id
    @GeneratedValue
    @Column(nullable = false)
    private int id;
    
    @Column(nullable = false)
    @NotNull(message = "Informe o nome do material")
    private String nome;
    
    private String codigo;
    
    @NotNull
    @Column(nullable = false)
    private String descricao;
    
    @NotNull(message = "Informe a quantidade do material")
    @Column(nullable = false)
    private long quantidade;
    
    @NotNull(message = "Escolha o tipo do material")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoMaterial tipo;
    
    @Enumerated(EnumType.STRING)
    private Ativo isAtivo;
    
    @Transient
    private Integer qtdEmprestimos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(long quantidade) {
        this.quantidade = quantidade;
    }

    public TipoMaterial getTipo() {
        return tipo;
    }

    public void setTipo(TipoMaterial tipo) {
        this.tipo = tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getQtdEmprestimos() {
        return qtdEmprestimos;
    }

    public void setQtdEmprestimos(Integer qtdEmprestimos) {
        this.qtdEmprestimos = qtdEmprestimos;
    }

    public Ativo getIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(Ativo isAtivo) {
        this.isAtivo = isAtivo;
    }
    
}
