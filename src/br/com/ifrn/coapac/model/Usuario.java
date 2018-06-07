package br.com.ifrn.coapac.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.Transient;

@Entity
public class Usuario implements Serializable{
	private static final long serialVersionUID = -7758997358119649578L;

	@Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String senha;
    
    @Column(nullable = false)
    private String matricula;
    
    @Column(nullable = false)
    private String vinculo;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoUsuario acesso;
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expiracao_copia;
    
    @Column(nullable = false)
    private int quantidade_copia;
    
    @Transient
    private boolean isSession;

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getVinculo() {
        return vinculo;
    }

    public void setVinculo(String vinculo) {
        this.vinculo = vinculo;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public TipoUsuario getAcesso() {
        return acesso;
    }

    public void setAcesso(TipoUsuario acesso) {
        this.acesso = acesso;
    }

    public Date getExpiracao_copia() {
        return expiracao_copia;
    }

    public void setExpiracao_copia(Date expiracao_copia) {
        this.expiracao_copia = expiracao_copia;
    }

    public int getQuantidade_copia() {
        return quantidade_copia;
    }

    public void setQuantidade_copia(int quantidade_copia) {
        this.quantidade_copia = quantidade_copia;
    }

    public boolean isIsSession() {
        return isSession;
    }

    public void setIsSession(boolean isSession) {
        this.isSession = isSession;
    }
    
}
