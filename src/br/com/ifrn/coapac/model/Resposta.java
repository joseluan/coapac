package br.com.ifrn.coapac.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Luan
 */

@Entity
public class Resposta implements Serializable {
	private static final long serialVersionUID = 3846112981795091048L;

	@Id
    @GeneratedValue
    private Integer id;
    
    @Column(length=1405, nullable=false)
    @NotNull(message="A resposta não pode ser vazia")
    private String texto;
    
    @ManyToOne
    private Usuario usuario_comentario;
    
    @ManyToOne
    private Usuario usuario_concluida;
    
    @ManyToOne
    private Postagem postagem;
    
    @Column(nullable=false)
    @Temporal(value=TemporalType.DATE)
    private Date data_resposta;
    
    @Column(nullable=false)
    @Temporal(value=TemporalType.DATE)
    private Date data_conclusao;
    
    private boolean isConcluida;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Usuario getUsuario_comentario() {
        return usuario_comentario;
    }

    public void setUsuario_comentario(Usuario usuario_comentario) {
        this.usuario_comentario = usuario_comentario;
    }

    public Usuario getUsuario_concluida() {
        return usuario_concluida;
    }

    public void setUsuario_concluida(Usuario usuario_concluida) {
        this.usuario_concluida = usuario_concluida;
    }

    public Postagem getPostagem() {
        return postagem;
    }

    public void setPostagem(Postagem postagem) {
        this.postagem = postagem;
    }

    public Date getData_resposta() {
        return data_resposta;
    }

    public void setData_resposta(Date data_resposta) {
        this.data_resposta = data_resposta;
    }

    public Date getData_conclusao() {
        return data_conclusao;
    }

    public void setData_conclusao(Date data_conclusao) {
        this.data_conclusao = data_conclusao;
    }

    public boolean isIsConcluida() {
        return isConcluida;
    }

    public void setIsConcluida(boolean isConcluida) {
        this.isConcluida = isConcluida;
    }
    
}
