/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Luan
 */
@Entity
public class Copia implements Serializable, EntidadePersistivel{
	private static final long serialVersionUID = 4675364990448500469L;

	@Id
    @GeneratedValue
    private Integer id;
    
    @Column(nullable = false)
    @NotNull
    private int quantidade;
    
    @ManyToOne
    @NotNull
    private Usuario usuario;
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    private Date data_copia;
    
    @Override
    public Integer getId() {
        return id;
    }
    
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getData_copia() {
        return data_copia;
    }

    public void setData_copia(Date data_copia) {
        this.data_copia = data_copia;
    }
    
}
