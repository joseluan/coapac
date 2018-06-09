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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Luan
 */
@Entity
public class Emprestimo implements Serializable, EntidadePersistivel{
	private static final long serialVersionUID = 1657109190048536160L;

	@Id
    @GeneratedValue
    private Integer id;
    
    @ManyToOne
    private Usuario usuario_emp;
    
    @ManyToOne
    private Usuario usuario_sol;
    
    @ManyToOne
    private Usuario usuario_ent;
    
    @ManyToOne
    private Material material;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data_expiracao;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data_entrega;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoSituacao situacao;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoSolicitacao solicitacao;
    
    @Override
    public Integer getId() {
        return id;
    }
    
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario_emp() {
        return usuario_emp;
    }

    public void setUsuario_emp(Usuario usuario_emp) {
        this.usuario_emp = usuario_emp;
    }

    public Usuario getUsuario_ent() {
        return usuario_ent;
    }

    public void setUsuario_ent(Usuario usuario_ent) {
        this.usuario_ent = usuario_ent;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Date getData_expiracao() {
        return data_expiracao;
    }

    public void setData_expiracao(Date data_expiracao) {
        this.data_expiracao = data_expiracao;
    }
    
    public TipoSituacao getSituacao() {
        return situacao;
    }

    public void setSituacao(TipoSituacao situacao) {
        this.situacao = situacao;
    }

    public TipoSolicitacao getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(TipoSolicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }

    public Usuario getUsuario_sol() {
        return usuario_sol;
    }

    public void setUsuario_sol(Usuario usuario_sol) {
        this.usuario_sol = usuario_sol;
    }

    public Date getData_entrega() {
        return data_entrega;
    }

    public void setData_entrega(Date data_entrega) {
        this.data_entrega = data_entrega;
    }
    
}
