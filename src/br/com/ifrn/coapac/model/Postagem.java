package br.com.ifrn.coapac.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
public class Postagem implements Serializable{
	private static final long serialVersionUID = 6184950579369387533L;

	@Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    private Usuario usuario;
    
    @Column(nullable=false)
    @NotNull(message="O titulo precisa ser preenchido!")
    private String titulo;
    
    private String descricao;
    
    @Column(nullable=false)
    private Date data_postagem;
    
    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    @NotNull(message="A postagem precisa de um tipo!")
    private TipoPostagem tipo;
    
    @Transient
    private Resposta novaResposta;

    public Postagem() {
        novaResposta = new Resposta();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData_postagem() {
        return data_postagem;
    }

    public void setData_postagem(Date data_postagem) {
        this.data_postagem = data_postagem;
    }

    public TipoPostagem getTipo() {
        return tipo;
    }

    public void setTipo(TipoPostagem tipo) {
        this.tipo = tipo;
    }

    public Resposta getNovaResposta() {
        return novaResposta;
    }

    public void setNovaResposta(Resposta novaResposta) {
        this.novaResposta = novaResposta;
    }
    
}
