/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.dao;

import java.io.Serializable; 
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ifrn.coapac.model.Usuario;

/**
 *
 * @author Luan
 */
public class UsuarioDAO extends GenericoDAO implements Serializable {
	private static final long serialVersionUID = -1361560997720957548L;
	private EntityManager em;

	public UsuarioDAO(EntityManager em) {
		this.em = em;
	}

	// Recuperando dados do banco
	public List<Usuario> gerarLista(Query query) {
		@SuppressWarnings("unchecked")
		List<Usuario> lista = query.getResultList();
		return lista;
	}

	public Usuario getById(int id) {
		Usuario ep = em.find(Usuario.class, id);
		return ep;
	}
	
	public Usuario getByQuery(Query query) {
		Usuario u = (Usuario) query.getSingleResult();
		return u;
	}

}
