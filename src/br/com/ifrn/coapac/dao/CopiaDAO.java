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

import br.com.ifrn.coapac.model.Copia;
import br.com.ifrn.coapac.model.Limite;

/**
 *
 * @author Luan
 */
public class CopiaDAO extends GenericoDAO implements Serializable {
	private static final long serialVersionUID = 3997788520854948772L;

	public CopiaDAO(EntityManager gerenciador) {
		this.em = gerenciador;
	}
	
	// Recuperando dados do banco
	public Copia getById(int id) {
		Copia c = em.find(Copia.class, id);
		return c;
	}
	
	public Limite getByIdLimite(int id) {
		Limite l = em.find(Limite.class, id);
		return l;
	}

	public List<Copia> gerarLista(Query query) {
		@SuppressWarnings("unchecked")
		List<Copia> lista = query.getResultList();
		return lista;
	}
	
	public List<Limite> gerarListaLimite(Query query) {
		@SuppressWarnings("unchecked")
		List<Limite> lista = query.getResultList();
		return lista;
	}

}
