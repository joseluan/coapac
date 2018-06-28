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
public class Resposta implements Serializable, EntidadePersistivel {
	private static final long serialVersionUID = 7055609881465778179L;

	@Id
    @GeneratedValue
    private Integer id;
    
    @Column(length=1405, nullable=false)
    @NotNull(message="A resposta não pode ser vazia")
    private String texto;
    
    @ManyToOne
    private Usuario usuario;
    
    @ManyToOne
    private Postagem postagem;
    
    @Column(nullable=false)
    @Temporal(value=TemporalType.DATE)
    private Date data_resposta;
    
    @Override
    public Integer getId() {
        return id;
    }
    
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
    
}
